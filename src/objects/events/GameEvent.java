package objects.events;

import javafx.scene.canvas.GraphicsContext;
import objects.Faction;
import objects.GameObject;
/**
 * Basic abstract base class for all events. See EventDataHolder. 
 * Stores x, y, faction, and starting time of events. Provides
 * useful methods such as clone.
 * @author Rajan
 *
 */
public abstract class GameEvent extends GameObject implements ReadableEvent
{
	
	
	
	
	
	protected GameEvent(double x, double y)
	{
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	
	protected GameEvent(double x, double y, Faction f)
	{
		super(x, y, f);
	}

	@Override

	public void renderHUD(GraphicsContext g)
	{
		//No effects on HUD by default
	}
	
	
	
	
	
}
