package engine;

import java.util.Collection;
import java.util.function.Predicate;

import bounds.Bounds;
import objects.Locatable;

/**Adds a few more methods to EventHandler**/
public interface WorldHandle extends EventHandler
{
	/**Returns objects satisfying a given condition in a given area**/
	public Collection<ObjectView> search(Bounds b, Predicate<ObjectView> condition);

	public default Collection<ObjectView> search(Bounds b)
	{
		return search(b, o -> true);
	}
	/**Gives nearest object to a given point satisfying a given condition**/
	public ObjectView nearestObject(Locatable loc, Predicate<ObjectView> condition);
	
	public default ObjectView nearestObject(Locatable loc)
	{
		return nearestObject(loc, o -> true);
	}

	/**Set of current collisions**/
	public Iterable<? extends ObjectView> collisions(ObjectView obj);

	/**Current game state**/
	public State currentState();

	/**Enables object tracking from on object in one game state to its successor in the next game state**/
	public ObjectView track(ObjectView old);


	public void addController(Controller controller);

	/**Allows assigning of listeners**/
	public void regGlobalDeathListener(Listener<ObjectView> effect);
	
}
