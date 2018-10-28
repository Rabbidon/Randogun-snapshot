package objects.entities;

import bounds.Bounds;
import bounds.Circle;
import engine.ObjectView;
import engine.WorldHandle;
import fxcore.MainGame;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import objects.Bullet;
import util.Util;

/**
 * @author Edwin Fennell
 */

public class Clutter extends Enemy
{

	public static final double radius = 20;
	public static final double startHealth = 50;
	public static final long fireTime = 1;
	
	private double shotTime;
	private Bounds bounds = new Circle()
	{
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


	public Clutter(double x, double y, WorldHandle h)
	{
		super(x, y, h);
		health = startHealth;
		maxHealth = startHealth;
		shotTime = h.currentState().time;
		
	}
	private void retreat(WorldHandle r)
	{
		double speed =18000;
		double distance2 = Util.distanceSquared(this, target);
		double x = target.getX() - this.x;
		double y = target.getY() - this.y;
		x = speed * x / distance2;
		y = speed * y / distance2;
		dx -= x;
		dy -= y;
	}
	private void advance(WorldHandle r)
	{
		double speed =30;
		double distance = Util.distance(this, target);
		double x = target.getX() - this.x;
		double y = target.getY() - this.y;
		x = speed * x / distance;
		y = speed * y / distance;
		dx +=x;
		dy += y;
	}
	@Override
	public void update(double dt, WorldHandle h) 
	{	
		super.update(dt, h);
		dx*=0.95;
		dy*=0.95;
		//double distance = Util.distance(this, target);
		advance(h);
		retreat(h);
		if(h.currentState().time < fireTime + shotTime) return;
		shotTime = h.currentState().time;
		h.add(new Bullet(this, target, 900, 10, Color.rgb(164,85,216), 0.7));
		bounds = new Circle()
		{
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
		target = h.track(target);
	}

	@Override
	public void draw(GraphicsContext g) 
	{
		g.setFill(Color.rgb(255, 50, 255));
		g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
	}
	

	@Override
	public Bounds bounds()
	{
		return bounds;
	}

}
