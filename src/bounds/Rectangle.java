package bounds;

import java.util.function.Consumer;

import javafx.scene.canvas.GraphicsContext;
import objects.Locatable;
import util.Util;
import util.Vector;

public abstract class Rectangle extends Bounds implements Locatable
{
	static int TYPE;
	static
	{
		TYPE = nextType++;
	}
	{
		type = TYPE;
	}
	public abstract double width();
	
	public abstract double height();
	
	
//	
//	@Override
//	public default boolean containsCircle(Circle c)
//	{
//		return
//		!((c.centerX() + c.radius() > getX() + width()) ||
//		(c.centerX() - c.radius() < getX()) || 
//		(c.centerY() + c.radius() > getY() + height()) ||
//		(c.centerY() - c.radius() < getY()));
//		
//	}
//	

	

	@Override
	public boolean contains(double x, double y)
	{
		return Util.isInRange(x, getX(), getX() + width()) && Util.isInRange(y, getY(), getY() + height());
	}

	
//
//	@Override
//	public default boolean intersectsCircle(Circle circle)
//	{
//		double x = Math.abs(centerX() - circle.centerX());
//		double y = Math.abs(centerY() - circle.centerY());
//		double xapo = width() / 2;
//		double yapo = height() / 2;
//		
//		if(x > (xapo + circle.radius())) return false;
//		if(y > (yapo + circle.radius())) return false;
//		
//		if(x <= xapo || y <= yapo) return true;
//		
//		double cornerDistanceSquared = Math.pow(x - xapo, 2) + Math.pow(y - yapo, 2);
//		return cornerDistanceSquared <= Math.pow(circle.radius(), 2);
//	}

//	@Override
//	public default boolean intersectsRectangle(Rectangle r)
//	{
//		return contains(r.getX(), r.getY()) || r.contains(getX(), getY());
//	}
	
	
	
	/**Constructs rectangle from lower-left corner, width and height**/
	public static Rectangle of(double x, double y, double width, double height)
	{
		return new Rectangle()
		{
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
			
			@Override
			public double width()
			{
				return width;
			}
			
			@Override
			public double height()
			{
				return height;
			}
			
		};
	}

	@Override
	public  void stroke(GraphicsContext g)
	{
		g.strokeRect(getX(), getY(), width(), height());
	}
	
	@Override
	public  void fill(GraphicsContext g)
	{
		g.fillRect(getX(), getX(), width(), height());
	}

	@Override
	public <T> void gridIntersect(CollisionGrid<T> g, Consumer<T> action)
	{
		// TODO Auto-generated method stub
		
	}



}
