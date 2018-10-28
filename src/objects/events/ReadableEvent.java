package objects.events;

import engine.WorldHandle;
import objects.ReadableObject;
/**
 * Root interface for all events. An event represents something which has effects on engine objects,
 * such as the player, bullets, enemies, and other events. An event can therefore be used to modify the game state
 * in almost any way. In addition, events have graphical effects on the screen, and so can be used for visuals.
 * @author Rajan
 *
 */
public interface ReadableEvent extends ReadableObject
{
	public void effects(WorldHandle handler);
	
	@Override
	default boolean supportsOperation(int op)
	{
		return false;
	}
	
	
}
