package bounds;

import java.util.function.Consumer;

import javafx.scene.canvas.GraphicsContext;
import objects.Locatable;
import util.Util;
import util.Vector;

public class Line extends Bounds
{
	static int TYPE;
	static
	{
		TYPE = nextType++;
	}
	{
		type = TYPE;
	}
	public final double x1;
	public final double y1;
	public final double x2;
	public final double y2;

	/**Constructs a line with start and end coordinates**/
	public Line(double x1, double y1, double x2, double y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	/**Constructs a line with start and end position**/
	public Line(Locatable start, Locatable end)
	{
		this(start.getX(), start.getY(), end.getX(), end.getY());
	}
	/**Constructs a line with start position, magnitude, and direction**/
	public Line(Locatable start, double angle, double distance)
	{
		x1 = start.getX();
		y1 = start.getY();
		Vector v = Vector.fromPolar(distance, angle);
		x2 = v.x + x1;
		y2 = v.y + y1;
	}
	
	@Override
	public boolean contains(double x, double y)
	{
		return false;
	}

	/**Drawing stuff**/
	@Override
	public void fill(GraphicsContext g)
	{
		g.strokeLine(x1, y1, x2, y2);
	}

	@Override
	public void stroke(GraphicsContext g)
	{
		g.strokeLine(x1, y1, x2, y2);
	}

	public double length()
	{
		return Math.sqrt(lengthSquared());
	}
	
	private double lengthSquared()
	{
		return Util.distanceSquared(x1, y1, x2, y2);
	}

	public double distanceSquared(double x, double y)
	{
		double lSqrd = lengthSquared();
		if(lSqrd == 0.0) return Util.distanceSquared(x1, y1, x, y);
		double t = ((x - x1) * (x2 - x1) + (y - y1) * (y2 - y1)) / lSqrd;
		if(t < 0.0) return Util.distanceSquared(x, y, x1, y1);
		if(t > 1.0) return Util.distanceSquared(x, y, x2, y2);
		return Util.distanceSquared(x, y, x1 + t * (x2 - x1), y1 + t * (y2 - y1));
	}
	
	public double distance(double x, double y)
	{
		return Math.sqrt(distanceSquared(x, y));
	}

	@Override
	public <T> void gridIntersect(CollisionGrid<T> g, Consumer<T> action)
	{
		// TODO Auto-generated method stub
		
	}

}
