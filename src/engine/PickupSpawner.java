package engine;



import com.koloboke.collect.map.ObjDoubleMap;
import com.koloboke.collect.map.ObjIntMap;
import com.koloboke.collect.map.hash.HashObjDoubleMaps;
import com.koloboke.collect.map.hash.HashObjIntMaps;

import javafx.scene.paint.Color;
import objects.HealthPickup;
import objects.ResourcePickup;
import objects.entities.Charger;
import objects.entities.Clutter;
import objects.entities.Jitterbug;

/**Listens for enemy deaths and spawns pickups when enemies die
 * @author Edwin Fennell
 */
public class PickupSpawner implements Listener<ObjectView> 
{
	public PickupSpawner()
	{
		
	}

	ObjDoubleMap<Class<?>> healthMap = HashObjDoubleMaps.newUpdatableMap();
	
	{
		healthMap.put(Clutter.class,1);
		healthMap.put(Jitterbug.class,1);
		healthMap.put(Charger.class,5);
	}
	
	ObjIntMap<Class<?>> resourceMap = HashObjIntMaps.newUpdatableMap();
	
	{
		resourceMap.put(Clutter.class,10);
		resourceMap.put(Jitterbug.class,15);
		resourceMap.put(Charger.class, 100);
	}

	/**Registers the spawner to global death listeners**/
	public void init(WorldHandle h)
	{
		h.regGlobalDeathListener(this);
	}

	/**Code that adds the pickups to the game world**/
	public void onEvent(ObjectView entity, WorldHandle h) 
	{
		if (healthMap.containsKey(entity.getClass()))
		{
			h.add(new HealthPickup(healthMap.getOrDefault(entity.getClass(),0),entity.getX(),entity.getY(),10.0,Color.RED,1.0,10.0));
		}
		if (resourceMap.containsKey(entity.getClass()))
		{
			h.add(new ResourcePickup(resourceMap.getOrDefault(entity.getClass(),0),entity.getX(),entity.getY(),10.0,Color.GRAY,1.0,10.0));
		}
		
	}

	/**Whether the spawner is active or not**/
	@Override
	public boolean active() 
	{
		return true;
	}

}
