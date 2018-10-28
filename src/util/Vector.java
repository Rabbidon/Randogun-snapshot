package util;


import static java.lang.Math.*;

import objects.Locatable;

/**
 * An immutable two dimensional vector using double as the distance type. Access x and y components of vector using the final fields x and y;
 * to change them a new Vector must be created. Most method of this class operate by returning a new vector with the specified modification, 
 * rather than changing the existing one. 
 * @author Rajan
 *
 */
public final class Vector implements Locatable
{
	/**
	 * The zero vector: (0, 0).
	 * Both x and y are 0.
	 */
	public static final Vector ZERO = new Vector(0, 0);
	
	public final double x, y;
	
	/**
	 * Creates a new vector with the specified x and y components
	 * @param x
	 * @param y
	 */
	public Vector(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Creates a new vector with components equal to their counterparts in the location;
	 * the resulting vector points from the origin to the location of the Locatable 
	 * provided at the time it is provided.
	 * @param location
	 */
	public Vector(Locatable location)
	{
		this(location.getX(), location.getY());
	}

	@Override
	public double getX()
	{
		return x;
	}
	
	@Override
	public double getY()
	{
		return y;
	}
	
	/**
	 * The length of this vector
	 * @return
	 */
	public double length()
	{
		return Math.sqrt(x * x + y * y);
	}
	
	/**
	 * The length of this vector squared, faster to compute than length(), can be used as a faster way to check if the length satisfies some criteria. 
	 * @return
	 */
	public double lengthSquared()
	{
		return x * x + y * y;
	}
	
	/**
	 * Provides a normalized version of this vector, pointing in the same direction, but with length 1.
	 * @return vector normalized to length 1
	 */
	public Vector normalized()
	{
		return normalized(1);
	}
	
	/**
	 * Returns the sum of this vector and the provided vector.
	 * @param v
	 * @return this + v
	 */
	public Vector add(Locatable v)
	{
		return new Vector(x + v.getX(), y + v.getY());
	}
	
	/**
	 * Returns the provided vector subtracted from this vector.
	 * @param v
	 * @return this - v
	 */
	public Vector sub(Vector v)
	{
		return new Vector(x - v.x, y - v.y);
	}
	
	/**
	 * Provides a vector equal to this one scaled by r, its length multiplied by the factor but the direction the same.
	 * @param r
	 * @return this * r
	 */
	public Vector scale(double r)
	{
		return new Vector(x * r, y * r);
	}
	
	/**
	 * Returns a vector perpendicular to this one, with the same length:
	 * (x, y) -> (-y, x)
	 * the dot product of this and the returned vector will be 0.
	 * @return perpendicular vector to this one.
	 */
	public Vector perpendicular()
	{
		return new Vector(-y, x);
	}
	
	/**
	 * Returns a vector equal to this vector scaled by the provided factor plus the provided vector:
	 * scale * this + other
	 * Equivalent to scale(scale).add(other), but avoids creating unnecessary copies, providing better performance.
	 * @param scale
	 * @param other
	 * @return scale * this + other
	 */
	public Vector scaleAdd(double scale, Vector other)
	{
		return new Vector(x * scale + other.x, y * scale + other.y);
	}
	
	/**
	 * Returns a vector equal to this vector plus the provided vector scaled by the provided factor:
	 * this + (other * scale)
	 * Equivalent to add(other.scale(scale)), but avoids creating unnecessary copies, providing better performance.
	 * @param scale
	 * @param other
	 * @return this + scale * other
	 */
	public Vector addScaled(Vector other, double scale)
	{
		return new Vector(x + scale * other.x, y + scale * other.y);
	}
	
	public Vector reflect(Vector normal)
	{
		return normal.scaleAdd(-2 * dotProduct(this, normal) / normal.lengthSquared(), this);
	}
	
	/**
	 * Returns the inverse of this vector: (-x, -y).
	 * this + inverse is equal to the zero vector
	 * @return
	 */
	public Vector negate()
	{
		return new Vector(-x, -y);
	}
	
	public Vector normalized(double d)
	{
		return scale(d / length());
	}
	
	@Override
	public String toString()
	{
		return String.format("(%f, %f)", x, y);
	}
	
	public Vector rotate(double rad)
	{
		double angle = Math.atan2(y,  x) + rad;
		double r = length();
		return fromPolar(r, angle);
	}
	
	/**
	 * Calculates the dot product of the two provided vectors.
	 * This is equal to vec1.x * vec2.x + vec1.y + vec2.y, 
	 * which is also the length of vec1 multiplied by the length of vec2, multiplied by the cosine of the angle between them.
	 * If and only if dotProduct(vec1, vec2) == 0 are vec1 and vec2 perpendicular.
	 * @param vec1
	 * @param vec2
	 * @return vec1 * vec2
	 */
	public static double dotProduct(Vector vec1, Vector vec2)
	{
		return vec1.x * vec2.x + vec1.y * vec2.y;
	}
	
	/**
	 * Generates a vector matching the polar coordinates provided.
	 * The length will equal length, and the angle from horizontal (to the right) will be equal to radians.
	 * @param length of vector
	 * @param direction of vector
	 * @return Vector matching polar coordinates
	 */
	public static Vector fromPolar(double length, double radians)
	{
		return new Vector(cos(radians) * length, sin(radians) * length);
	}
	

	

}
