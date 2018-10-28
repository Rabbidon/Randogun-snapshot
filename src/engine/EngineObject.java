package engine;

public abstract class EngineObject extends ObjectView
{
	
	
	/**Updates the object**/
	public abstract void update(double dt, WorldHandle h);
	

	/**Does something when the object spawns**/
	public void onEntry(WorldHandle h)
	{
		
	}

	/**Does something when the object dies**/
	public void onDeath(WorldHandle h)
	{
		
	}

	/**Message recieving functionality**/
	public void message(int code, Object data)
	{
		
	}
	
	/**
	 * Collides the two given game objects. 
	 * @param obj
	 * @param other
	 */
	
	
	
	
	
}
