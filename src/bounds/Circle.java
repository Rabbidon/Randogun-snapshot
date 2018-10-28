package bounds;

import java.util.function.Consumer;

import static util.Util.*;

import javafx.scene.canvas.GraphicsContext;
import objects.Locatable;
import util.Util;

public abstract class Circle extends Bounds implements Locatable
{
	static int TYPE;
	static
	{
		TYPE = nextType++;
	}
	{
		type = TYPE;
	}
	/**Constructs a circle from centre coordinates and radius**/
	public static Circle of(double x, double y, double radius)
	{
		return new Circle()
		{
			@Override
			public double radius()
			{
				return radius;
			}

			@Override
			public double centerX()
			{
				return x;
			}

			@Override
			public double centerY()
			{
				return y;
			}
		};
	}
	/**Constructs a circle at a given point with given radius**/
	public static Circle centeredCircle(Locatable center, double radius)
	{
		return new Circle()
		{
			
			@Override
			public double radius()
			{
				return radius;
			}

			@Override
			public double centerX()
			{
				return center.getX();
			}

			@Override
			public double centerY()
			{
				return center.getY();
			}

		};
	}
	public abstract double centerX();
	
	public abstract double centerY();
	
	public double getX()
	{
		return centerX();
	}
	
	public double getY()
	{
		return centerY();
	}

	@Override
	public boolean contains(double x, double y)
	{
		return Util.distanceSquared(x, y, centerX(), centerY()) <= Math.pow(radius(), 2);
	}
	@Override
	public void fill(GraphicsContext g)
	{
		g.fillOval(centerX() - radius(), centerY() - radius(), 2 * radius(), 2 * radius());
	}
	@Override
	public void stroke(GraphicsContext g)
	{
		g.strokeOval(centerX() - radius(), centerY() - radius(), 2 * radius(), 2 * radius());
	}
	public abstract double radius();


	@Override
	public <T> void gridIntersect(CollisionGrid<T> g, Consumer<T> action)
	{
		//TODO use brehnemens? algorithm to fill in circle exactly
		int beginAcross = g.toXIndex(centerX() - radius());
		int endAcross = g.toXIndex(centerX() + radius()) + 1;
		int beginDown = g.toYIndex(centerY() - radius());
		int endDown = g.toYIndex(centerY() + radius()) + 1;
		beginAcross = relu(beginAcross);
		endAcross = cap(endAcross, g.length());
		beginDown = relu(beginDown);
		endDown = cap(endDown, g.length());
		for(int i = beginAcross; i < endAcross; i++)
			for(int j = beginDown; j < endDown; j++)
			{
				//TODO check if grid cell intersects circle for better effiency
				action.accept(g.atIndex(i, j));
			}
		
		
	}
	


}
