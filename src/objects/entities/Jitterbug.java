package objects.entities;

import static fxcore.MainGame.*;

import bounds.Bounds;
import bounds.Circle;
import engine.ObjectView;
import engine.WorldHandle;
import fxcore.MainGame;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import objects.GameObject;
import objects.HomingBullet;
import util.Util;

/**
 * @author Edwin Fennell
 */

public class Jitterbug extends Enemy
{
	public static final Color COLOR = Color.RED;
	public static final double radius = 30;
	public static final double startHealth = 50;
	public static double dashTime = 2;
	public static final double fireTime = 1;


	private double shotTime;
	private double travelTime;
	
	
	private Circle bounds = new Circle()
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
	
	public Jitterbug(double x, double y, WorldHandle h)
	{
		super(x, y, h);
		health = startHealth;
		maxHealth = startHealth;
		shotTime = h.currentState().time;
		travelTime = h.currentState().time;

		
	}

	@Override
	public void draw(GraphicsContext g)
	{
		g.setFill(COLOR);
		g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
	}
	
	@Override
	public Impact collideWith(GameObject other, WorldHandle h)
	{
		if(other.getFaction() != faction)
		{
			return new Impact(this, new Change(DAMAGE, .1));
		}
		return Impact.NONE;
	}
	private void advance(WorldHandle r)
	{
		double speed =20;
		double distance = Util.distance(this, target);
		double x = target.getX() - this.x;
		double y = target.getY() - this.y;
		x = speed * x / distance;
		y = speed * y / distance;
		dx +=x;
		dy += y;
	}
	
	private void retreat(WorldHandle r)
	{
		double speed =5000;
		double distance2 = Util.distanceSquared(this, target);
		double x = target.getX() - this.x;
		double y = target.getY() - this.y;
		x = speed * x / distance2;
		y = speed * y / distance2;
		dx -= x;
		dy -= y;
	}
	
	private void dash (WorldHandle r)
	{
		double speed = 1000;
		//double distance = Util.distance(this, target);
		double x = target.getX() - this.x;
		double y = target.getY() - this.y;
		double angle = Math.atan2(y, x);
		double randomAngle = (Math.PI/2)+((Math.PI)*Math.round(rand.nextDouble()));
		angle += randomAngle;
		dx += speed*Math.cos(angle);
		dy += speed*Math.sin(angle);
	}
	
	@Override
	public void update(double dt, WorldHandle h) 
	{

		super.update(dt, h);
		double speedDecay = Math.pow(0.0000002, dt);
		dx *= speedDecay;
		dy *= speedDecay;
		advance(h);
		retreat(h);
		if(MainGame.getTime() >= travelTime + dashTime)
		{
			travelTime = h.currentState().time;
		    dash(h);
			dashTime = 0.5 + Math.random();
		}
		if(MainGame.getTime() >= shotTime + fireTime)
		{
			shotTime = h.currentState().time;
			h.add(new HomingBullet(this, target, 900, 5, Color.rgb(255,255,255), 0.3));	
		}
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
	public Bounds bounds()
	{
		 return bounds;
	}
}