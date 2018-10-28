package fxcore;

import java.util.BitSet;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Keeps track of all keys currently pressed at any given time.
 * Should be added as a top - level event filter to a JavaFX Application.
 * Does not ever consume KeyEvents.
 * @author Rajan
 *
 */
public class KeyTracker implements EventHandler<KeyEvent>
{
	private BitSet keys = new BitSet(256);
	
	public boolean isKeyPressed(KeyCode code)
	{
		return keys.get(code.ordinal());
	}

	@Override
	public void handle(KeyEvent event)
	{
		if(event.getEventType() == KeyEvent.KEY_PRESSED)
		{
			keys.set(event.getCode().ordinal());
		}
		
		if(event.getEventType() == KeyEvent.KEY_RELEASED)
		{
			keys.set(event.getCode().ordinal(), false);
		}
		
	}

}
