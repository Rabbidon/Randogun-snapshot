package objects;

import fxcore.Renderable;
import util.Cloner;

public interface ReadableObject extends Cloner, Locatable, Renderable, Factioned
{
	/**
	 * If the object is dead, return true. 
	 * @return is the object dead ? true : false
	 */
	public boolean isDead();
	
	public default boolean alive()
	{
		return !isDead();
	}
	
	/**
	 * Checks if this object supports the specified type of modification.
	 * The modification is specified by providing its identity code, defined by whichever class has created that modification type.
	 * @param code
	 * @return 
	 */
	public boolean supportsOperation(int code);
}
