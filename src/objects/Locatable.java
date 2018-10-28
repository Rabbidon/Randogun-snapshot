package objects;

import util.Util;
import util.Vector;

/**
 * Represents classes which have a location in two dimensional space.
 * @author Rajan
 *
 */

public interface Locatable
{
	/**
	 * Gets the x coordinate of this object.
	 * @return x coordinate of this object
	 */
	public double getX();
	
	/**
	 * Gets the y coordinate of this object.
	 * @return y coordinate of this object
	 */
	public double getY();
	
	/**
	 * Calculates the angle between this object's position and other's position.
	 * @param other
	 * @return angle in radians from the horizontal
	 */
	public default double angleTo(Locatable other)
	{
		return Util.angle(this, other);
	}
	
	public static Locatable of(double x, double y)
	{
		return new Vector(x, y);
	}
	
	public static Locatable of(Vector v)
	{
		return of(v.x, v.y);
	}
}
