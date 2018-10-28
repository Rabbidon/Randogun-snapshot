package engine;

import java.util.List;

import bounds.Bounds;
import fxcore.Renderable;
import objects.Locatable;
import util.Cloner;
import util.IntList;

/**A general game object. Has a position, instructions for drawing, and **/
public abstract class ObjectView implements Renderable, Cloner, Locatable
{
	
	int index;
	List<ObjectView> collidingWith;
	IntList messageCodes;
	List<Object> messages;
	
	public abstract boolean isDead();
	
	public abstract Bounds bounds();
	
	boolean alive()
	{
		return !isDead();
	}
	
	@Override
	public Object clone()
	{
		try
		{
			return super.clone();
		}
		catch (CloneNotSupportedException e)
		{
			throw new InternalError(e);
		}
	}
	
	boolean collidesWith(ObjectView other)
	{
		return Bounds.intersect(bounds(), other.bounds());
	}
	
	public int collisionClass()
	{
		return 0;
	}
	
	
	
	
}
