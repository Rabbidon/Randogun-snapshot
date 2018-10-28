package engine;

import java.util.Collection;

import com.google.common.collect.ImmutableList;

import bounds.Bounds;
import util.Util;

/**
 * Class representing an internal engine view.
 * Immutable.
 * Passed from the engine to the renderer for displaying to the screen
 * also passed to all engine objects in update.
 * @author Rajan Troll
 *
 */
public class State
{
	
	public final ImmutableList<ObjectView> objects;
	public final Bounds gameBounds;
	public final double width, height;
	public final double time;

	public State(Iterable<? extends EngineObject> objects, double width, double height, Bounds bounds, double time)
	{
		this.objects = ImmutableList.copyOf(Util.stream(objects).map(e -> (ObjectView) e.clone()).iterator());
		this.gameBounds = bounds;
		this.width = width;
		this.height = height;
		this.time = time;
	}

	/**Stores all information about the game state as a string**/
	@Override
	public String toString()
	{
		return String.format("Objects: %s, Time: %d", objects.toString(), time);
	}

	/**
	 * Convenient Class for building immutable displays, will be obtained from State.builder()
	 * Needs adder methods.
	 * @author Rajan Troll
	 *
	 */
	public static class Builder
	{

		public Collection<? extends EngineObject> objects;
		public double width, height;
		public long time;
		public Bounds bounds;
		
		public State build()
		{
			return new State( objects, width, height, bounds, time);
		}
	}
}
