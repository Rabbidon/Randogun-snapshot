package engine;

/**A toggleable 'thing' which does something when activated. The foundation for many of the other more complicated mechanisms such as spawners**/
public interface Controller
{
	/**Determines whether the controller is active or not **/
	default boolean active()
	{
		return true;
	}

	/**Implements the active effect of the controller, taking the world**/
	void effects(double dt, WorldHandle h);
	
	default void init(WorldHandle h)
	{
		
	}
}
