package fxcore;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import objects.ReadableObject;

/**
 * Keeps track of mouse position, and current state of all mouse buttons, 
 * including the time since they were pressed in nanoseconds.
 * Should be added as a top - level event filter in a JavaFX Application.
 * Never consumes events.
 * @author Rajan
 *
 */
public class MouseTracker implements EventHandler<MouseEvent>
{
	private volatile boolean[] clicked = new boolean[MouseButton.values().length];
	private transient volatile long[] timeClicked = new long[MouseButton.values().length];
	private volatile double x, y;
	@Override
	public void handle(MouseEvent event)
	{
		x = event.getX();
		y = event.getY();
		if(event.getEventType() == MouseEvent.MOUSE_PRESSED)
		{
			clicked[event.getButton().ordinal()] = true;
			timeClicked[event.getButton().ordinal()] = System.nanoTime();
		}
		if(event.getEventType() == MouseEvent.MOUSE_RELEASED)
		{
			clicked[event.getButton().ordinal()] = false;
		}
	}
	
	public double x()
	{
		return x;
	}
	
	public double gameX(ReadableObject center)
	{
		return x + center.getX() - MainGame.getScreenWidth() / 2;
	}
	
	public double gameY(ReadableObject center)
	{
		return y + center.getY() - MainGame.getScreenHeight() / 2;
	}
	
	public double y()
	{
		return y;
	}
	
	public boolean isPressed(MouseButton button)
	{
		return clicked[button.ordinal()];
	}
	
	public long nanoTimeSincePress(MouseButton button)
	{
		return System.nanoTime() - timeClicked[button.ordinal()];
	}
	
	public void addMouseListener(EventHandler<? extends MouseEvent> handler)
	{
		throw new UnsupportedOperationException();
	}
	
	

}
