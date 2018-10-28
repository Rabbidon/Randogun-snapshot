package objects;

import bounds.Bounds;
import bounds.Circle;
import engine.WorldHandle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Pickup extends MovingObject {
	
	public Color color;
	protected boolean isDead = false;
	protected double radius;
	protected double attraction;
	protected double attractionRadius;
	protected Circle bounds = new Circle()
	{
		public void draw(GraphicsContext g)
		{
			g.setFill(color);
			g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);

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

		@Override
		public double radius()
		{
			return radius;
		}
	};
	
	public Pickup(double x, double y, double radius, Color color,double attraction,double attractionRadius)
	{
		super(x,y);
		this.radius = radius;
		this.color = color;
		this.attraction = attraction;
		this.attractionRadius = attractionRadius;
	}

	public double getRadius()
	{
		return bounds.radius();
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public abstract Impact collideWith(GameObject other, WorldHandle h);
	
	@Override
	public void draw(GraphicsContext g) 
	{
		g.setFill(color);
		bounds().fill(g);
	}

	@Override
	public Bounds bounds() 
	{
		return bounds;
	}
	
	@Override
	public boolean isDead() 
	{
		return isDead;
	}
	
	@Override
	public double getDX()
	{
		return dx;
	}
	
	@Override
	public double getDY()
	{
		return dy;
	}
	
	@Override
	public int collisionClass()
	{
		return 4;
	}

}
