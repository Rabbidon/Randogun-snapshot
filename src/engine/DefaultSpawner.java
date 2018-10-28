package engine;

import static fxcore.MainGame.*;

import java.util.ArrayList;
import java.util.List;

import com.koloboke.collect.map.ObjIntMap;
import com.koloboke.collect.map.hash.HashObjIntMaps;

import player.Player;
import objects.GameObject;
import objects.entities.Charger;
import objects.entities.Clutter;
import objects.entities.Greg;
import objects.entities.Jitterbug;

/**The default spawner for enemies**/
public class DefaultSpawner implements Controller, Listener<ObjectView>
{
	
	public DefaultSpawner()
	{

	}

	/**Information about each enemy type kept by the spawner in order to determine next spawn**/
	public int score;
	private double lastSpawn = 0;
	private double lastBoss = -10000;// Spawn immediately for testing
	private double boss = -10000; // Spawn immediately for testing
	int[] countArray = new int[3];
	//Map<Class<?>, Integer> spawnCaps;
	
	@Override
	public boolean active()
	{
		return true;
	}

	/**Determines how many points each enemy type is worth when killed**/
	ObjIntMap<Class<?>> scoreMap = HashObjIntMaps.newUpdatableMap();
	
	{
		scoreMap.put(Clutter.class, 20);
		scoreMap.put(Jitterbug.class,25);
		scoreMap.put(Charger.class, 100);
	}


	@Override
	public void onEvent(ObjectView entity, WorldHandle h) 
	{
		score = score + scoreMap.getOrDefault(entity.getClass(), 0);
	}

	private ObjIntMap<Class<?>> spawnCap = HashObjIntMaps.newUpdatableMap();

	/**Sets up a listener to register when enemies die**/
	@Override
	public void init(WorldHandle h)
	{
		h.regGlobalDeathListener(this);
	}

	/**Increases spawn cap as score increases**/
	public void updateSpawnCap()
	{
		spawnCap.put(Clutter.class, Math.min(2 + score/200, 10));
		spawnCap.put(Jitterbug.class, Math.min(2 + score/250, 10));
		spawnCap.put(Charger.class, Math.min(1 + score/1000, 3));
	}

	/**Counts existing enemies and spawns new ones if necessary. Also updates score**/
	@Override
	public void effects(double dt, WorldHandle h) 
	{
		State d = h.currentState();
		List<GameObject> spawns = new ArrayList<>();
		double x = rand.nextDouble() * (getGameWidth() - 200) + 100, y = rand.nextDouble() * (getGameHeight() - 200) + 100;
		int count = d.objects.size();
		for (int a=0; a<countArray.length; a++)
		{
			countArray[a] = 0;
		}
		
		updateSpawnCap();
		
		for (int i=0; i<count; i++)
		{
			if (d.objects.get(i) instanceof Jitterbug)
			{
				countArray[0]++;
			}
			if (d.objects.get(i) instanceof Clutter)
			{
				countArray[1]++;
			}
			if (d.objects.get(i) instanceof Charger)
			{
				countArray[2]++;
			}
		}
		if(d.time - lastSpawn >= 0.5)
		{
			if(countArray[0] < spawnCap.getInt(Jitterbug.class))
			{
				if(true)
				{
					System.out.println(h.currentState().time);
				}
				spawns.add(new Jitterbug(x, y , h));
				//spawns.add(new Greg(x, y));
			}
			if(countArray[1] < spawnCap.getInt(Clutter.class))
			{
				spawns.add(new Clutter(x, y, h));
			}
			
			lastSpawn = d.time;
			x = rand.nextDouble() * (getGameWidth() - 200) + 100; 
			y = rand.nextDouble() * (getGameHeight() - 200) + 100;
		}
		if(d.time - lastBoss >= 30)
		{
			if(countArray[2] < spawnCap.getInt(Charger.class))
			{
				//spawns.add(new Charger(x, y));
			}
			//spawns.add(new MannyEnemy(x, y));
			//spawns.add(new SpawningEnemy(x, y));
			lastBoss = d.time;
			x = rand.nextDouble() * (getGameWidth() - 200) + 100; 
			y = rand.nextDouble() * (getGameHeight() - 200) + 100;
		}
		
		if(d.time - boss >= 200)
		{
			//spawns.add(new FirstBoss(x, y));
			boss = d.time;
		}
		for (GameObject obj : spawns)
		{
			h.add(obj);
		}
	}

}
