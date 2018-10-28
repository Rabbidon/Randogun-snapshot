package util;

/**
 * Interface for objects with public, non throwing clone.
 * Provides a convenience non throwing cloning method which automatically casts to the base class.
 * @author Rajan Troll
 *
 */
public interface Cloner extends Cloneable
{
	public Object clone();
	

	
}
