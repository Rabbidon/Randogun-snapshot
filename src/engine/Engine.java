package engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.Predicate;

import com.koloboke.collect.map.IntObjMap;
import com.koloboke.collect.map.hash.HashIntObjMaps;
import com.koloboke.collect.set.LongSet;
import com.koloboke.collect.set.hash.HashLongSets;

import bounds.Bounds;
import bounds.CollisionGrid;
import bounds.Rectangle;
import objects.Cursor;
import objects.Locatable;
import player.Player;
import util.IntList;
import util.Util;

/**
 * Main game engine. Performs all non graphical internal calculation.
 * Keeps track of Player, all bullets, enemies, and events.
 * Provides an immutable State object representing 
 * one snapshot of engine state.
 * 
 * @author Rajan Troll
 *
 */
public final class Engine 
{
	/**
	 * Dimensions of the game world
	 */
	public final double width, height;

	/**
	 * Bounds of the game world
	 */
	public Bounds bounds; 

	/**
	 * Amount of time this Engine has executed
	 */
	private double time;

	/**Initialises list of engine objects**/
	private List<EngineObject> objects = new ArrayList<>();

	/**Initialses list of controllers used by the engine**/
	private List<Controller> control = new ArrayList<>();

	/**Initialises global death listeners**/
	private List<Listener<ObjectView>> globalDeathListeners = new ArrayList<>();

	/**Spawner for this engine, creates enemies and probably other stuff.**/
	public Handler handler = new Handler();

	/**
	 * Representation of engine state. Replaced each update, 
	 * is immutable and can be passed around safely.
	 * Passed to the Renderer each frame. 
	 */
	private State state;

	//Collision detection grid parameters
	private double cellWidth, cellHeight;
	private int gridWidth, gridHeight;



	/**
	 * Creates a new engine with the specified dimensions.
	 * These dimensions cannot be changed after creation.
	 * Initializes the player to the center of the game.
	 * @param width
	 * @param height
	 */

	public final int numCollisionClasses = 5;
	boolean[][] classTable = new boolean[numCollisionClasses][numCollisionClasses];
	{
		regClass(0, "0"); //Objects that don't collide
		regClass(1, "00"); //Player faction entities
		regClass(2, "000"); // Player faction bullets
		regClass(3, "0110"); //Enemies
		regClass(4, "01000"); //Enemy bullets
	}
	boolean convert(char c)
	{
		switch(c)
		{
		case '0': return false;
		case '1': return true;
		default: throw new IllegalArgumentException();
		}
	}

	/**Initialises reference table indicating which classes allow collision with which other classes**/
	void regClass(int cc, String dat)
	{
		for(int i = 0; i < dat.length(); i++)
		{
			boolean allow = convert(dat.charAt(i));
			classTable[cc][i] = allow;
			classTable[i][cc] = allow;
		}
	}


	public IntList permutationTable = new IntList(1024);

	/**Generates a new gamestate by taking a census of whether all game objects are alive or not
	 * and removing inactive controllers and listeners**/
	private void generateState()
	{
		int index = 0;
		permutationTable.clear();
		for(int i = 0; i < objects.size(); i++)
		{
			EngineObject o = objects.get(i);
			if(o.alive())
			{
				objects.set(index, o);
				o.index = index;
				permutationTable.add(index);
				index++;
			}
			else 
			{
				handleDeath(o);
				//o.index = -1;
				permutationTable.add(-1);
			}
		}
		objects.subList(index, objects.size()).clear();
		control.removeIf(c -> !c.active());
		globalDeathListeners.removeIf(l -> !l.active());

		state = new State(objects, width, height, bounds, time);
	}

	/**Registers death**/
	private void handleDeath(EngineObject o)
	{
		o.onDeath(handler);
		for(Listener<ObjectView> l : globalDeathListeners)
			l.onEvent(o, handler);
	}

	/**
	 * Creates engine based on data contained in state. State is immutable; all data is copied into the new engine.
	 * Exactly recreates the state represented by the State object in the engine.
	 * @param state
	 */
	public Engine(State state)
	{
		width = state.width;		
		height = state.height;
		time = state.time;
		gridWidth = gridHeight = 100;
		calcGridDim();
		bounds = Rectangle.of(0, 0, width, height);
		for (ObjectView o : state.objects)
		{
			EngineObject obj = (EngineObject) o.clone();
			add(obj);
		}
		collisionGrid = new CollisionGrid<>(100, width, height, IntList::new);
		generateState();
		Bounds.initTables();
	}

	/**
	 * Returns a State object representing the internal state of all game elements.
	 * State is immutable, engine state cannot be changed. This method does not generate 
	 * a new State, but returns the one calculated after each engine update to all callers 
	 * until the next update.
	 * @return
	 */
	public State getState() 
	{
		return state;
	}

	void add(EngineObject obj)
	{
		obj.onEntry(handler);
		obj.index = objects.size();
		objects.add(obj);
	}

	/**
	 * Advances the game represented by this engine one tick.
	 * Updates all game entities, processes all events, and currently can add new enemies.
	 * Also updates the Engine's State object to the latest state.
	 */
	public void update(double dt)
	{
		handleCollisons();
		updateControllers(dt);
		time += dt;
		SortedMap<?, List<Effect>> eventsToExecute = upcomingEvents.headMap(time);
		eventsToExecute.values().forEach(e -> e.forEach(f -> f.effects(handler)));
		eventsToExecute.clear();
		updateObjects(dt);
		handler.mergeChanges();

		generateState();
	}

	/**implements the effects of each controller on update**/
	private void updateControllers(double dt)
	{
		for(Controller c : control)
		{
			c.effects(dt, handler);
		}

	}

	/**resets collision grid**/
	private void resetGrid()
	{
		collisionGrid.forEach(IntList::clear);
	}

	/**Adds a collision to the collision list to be resolved**/
	void regCollision(ObjectView a, ObjectView b)
	{
		if(a.collidesWith(b))
		{
			long x = a.index;
			long y = b.index;
			if(x > y)
			{
				long temp = x;
				x = y;
				y = temp;
			}
			collisionPairs.add(x ^ (y << 32));
		}
	}
	@SuppressWarnings("unchecked")
	private List<ObjectView>[] sortedByClass = new ArrayList[numCollisionClasses];
	{
		Arrays.setAll(sortedByClass, ArrayList::new);
	}
	/**Handles collisions by going through all cells, taking all objects in each cell
	 * and registering collisions between them if their collision classes allow it**/
	private void handleCollisons()
	{
		resetGrid();
		collisionPairs.clear();
		collisionMap.clear();
		for(int i = 0; i < state.objects.size(); i++)
		{
			addToGrid(state.objects.get(i), i);
		}
			for(IntList objs : collisionGrid)
			{
				for(List<ObjectView> l : sortedByClass) l.clear();
				for(int i = 0; i < objs.size(); i++)
				{
					ObjectView a = state.objects.get(objs.get(i));
					sortedByClass[a.collisionClass()].add(a);
				}
				for(int c1 = 0; c1 < numCollisionClasses; c1++)
					for(int c2 = c1 + 1; c2 < numCollisionClasses; c2++)
					{
						if(classTable[c1][c2])
						{
							List<ObjectView> l1 = sortedByClass[c1];
							List<ObjectView> l2 = sortedByClass[c2];
							for(ObjectView a : l1)
								for(ObjectView b : l2)
									regCollision(a, b);
						}
					}
				for(int cc = 0; cc < numCollisionClasses; cc++)
					if(classTable[cc][cc])
					{
						List<ObjectView> list = sortedByClass[cc];
						for(int i = 0; i < list.size(); i++)
							for(int j = i + 1; j < list.size(); j++)
							{
								regCollision(list.get(i), list.get(j));
							}
					}


			}

		collisionPairs.cursor().forEachForward(l ->
		{
			int x = (int) (l & 0xFFFFFFFF);
			int y = (int) (l >>> 32);
			collisionMap.computeIfAbsent(x, k -> new ArrayList<>()).add(objects.get(y));
			collisionMap.computeIfAbsent(y, k -> new ArrayList<>()).add(objects.get(x));
		});


	}
	LongSet collisionPairs = HashLongSets.newUpdatableSet(1024);	
	IntObjMap<List<EngineObject>> collisionMap = HashIntObjMaps.newUpdatableMap(2048);

	/**Calls the update function for each object in the game**/
	private void updateObjects(double dt)
	{
		for(EngineObject o : objects)
		{
			o.update(dt, handler);
		}
	}

	/**
	 * Returns time simulated by this engine.
	 * @return Time in seconds
	 */
	public double getTime()
	{
		return time;
	}

	private void calcGridDim()
	{
		//gridWidth = (int) Math.round(width / cellWidth);
		//gridHeight = (int) Math.round(height / cellHeight);
		cellWidth = width / gridWidth;
		cellHeight = height / gridHeight;
	}
	CollisionGrid<IntList> collisionGrid;

	public SortedMap<Double, List<Effect>> upcomingEvents = new TreeMap<>();


	/**Adds an object to the collision grid by registering which cells it intersects**/
	private void addToGrid(ObjectView objectView, int index)
	{
		objectView.bounds().gridIntersect(collisionGrid, x -> x.add(index));
	}


	public class Handler implements WorldHandle
	{
		private List<EngineObject> newObjects = new ArrayList<>(); 
		//private List<>
		private List<Object> messages = new ArrayList<>();
		private List<EngineObject> targets = new ArrayList<>();
		private IntList messageCodes = new IntList();

		/**Adds an object to the list of objects to be added to the game**/
		@Override
		public void add(EngineObject obj) 
		{
			newObjects.add(obj);
		}

		/**Sends messages to their targets**/
		private void mergeChanges()
		{

			for(EngineObject o : newObjects)
				Engine.this.add(o);
			newObjects.clear();
			for(int i = 0; i < messages.size(); i++)
			{
				targets.get(i).message(messageCodes.get(i), messages.get(i));
			}
			messages.clear();
			targets.clear();
			messageCodes.clear();
		}

		/**The message function**/
		@Override
		public void message(ObjectView target, int code, Object data)
		{
			int targetIndex = target.index;
			if(targetIndex != -1)
			{
				targets.add(objects.get(targetIndex));
				messageCodes.add(code);
				messages.add(data);
			}

		}

		/**Returns all objects satisfying a given condition in a given area**/
		@Override
		public Collection<ObjectView> search(Bounds b, Predicate<ObjectView> condition)
		{
			Set<ObjectView> objectList = new HashSet<>();
			b.gridIntersect(collisionGrid, objs -> 
			{
				for(int k = 0; k < objs.size(); k++)
				{
					ObjectView obj = objects.get(objs.get(k));
					if (Bounds.intersect(obj.bounds(), b) & condition.test(obj))
						objectList.add(objects.get(objs.get(k)));
				}
			});
			return objectList;
		}

		/**Given a pair of coordinates, gives the cell containing this point**/
		public int[] getCellCoordinates(double x, double y)
		{
			double cellX = x / cellWidth;
			double cellY = y / cellHeight;
			int[] coordinates = {(int)(cellX), (int)(cellY)};
			return coordinates;

		}

		/**Finds the nearest object satisfying a given condition to a point in space**/
		@Override
		public ObjectView nearestObject(Locatable loc, Predicate<ObjectView> condition)
		{
			int[] coordinates = getCellCoordinates(loc.getX(), loc.getY());
			int x = coordinates[0], y = coordinates[1];
			IntList c = collisionGrid.atIndex(0, 0);
			IntList target = null;
			ObjectView result = null;
			int[] horizontal = {1, 0, -1, 0};
			int[] vertical = {0, 1, 0, -1};
			double closestDistance = Double.POSITIVE_INFINITY;
			int cellsChecked = 0;
			for(int counter = 0; c != target; counter++)
			{
				for(int i = 0; i <= counter / 2; i++)
				{
					if ((0 <= x) & (x < gridWidth) & (0 <= y) & (y < gridHeight))
					{
						c = collisionGrid.atIndex(x, y);
						IntList objs = c;
						for (int j = 0; j < objs.size(); j++)
						{
							int index = objs.get(j);
							ObjectView obj = state.objects.get(index);
							if(condition.test(obj))
							{
								double dist2 = Util.distanceSquared(loc, obj);
								if (dist2 < closestDistance)
								{
									result = obj;
									closestDistance = dist2;

								}
							}
						}
						cellsChecked++;
					}
					x += horizontal[counter & 3]; //counter & 3 faster and same as counter % 4
					y += vertical[counter & 3];

				}

				if(cellsChecked == gridWidth * gridHeight) break;
			}
			return result;
		}

		/**List of all collision for the current gamestate**/
		@Override
		public Iterable<? extends ObjectView> collisions(ObjectView obj)
		{
			return collisionMap.getOrDefault(obj.index, Collections.emptyList());
		}

		/**Calls the current gamestate**/
		@Override
		public State currentState()
		{
			return state;
		}

		/**Allows persistent obect tracking - each object inherits from its predecessor in the previous gamestate**/
		@Override
		public ObjectView track(ObjectView old)
		{
			int index = permutationTable.get(old.index);
			if(index == -1) return null;
			return state.objects.get(index);
		}

		/**Adds event to occur at a given time in the future. Currently only used for delay node**/
		@Override
		public void schedule(double time, Effect event) 
		{
			upcomingEvents.computeIfAbsent(time + Engine.this.time, k -> new ArrayList<>()).add(event);
		}

		/**adds and initialises a new controller**/

		@Override
		public void addController(Controller newController)
		{
			control.add(newController);
			newController.init(handler);
		}

		@Override
		public void regGlobalDeathListener(Listener<ObjectView> l)
		{
			globalDeathListeners.add(l);
		}

	}
}
