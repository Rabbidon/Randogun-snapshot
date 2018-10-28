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

public class Greg extends Enemy
{
	static final double radius = 50;
	
	public Greg(double x, double y, WorldHandle h)
	{
		super(x, y, h);
		health = maxHealth = 500;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(GraphicsContext g)
	{
		// TODO Auto-generated method stub
		g.setFill(Color.AQUAMARINE);
		g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);

	}
	

	static final double relocTime = 3;
	static final double bulletSpeed = 200;
	static final double jumpDist = 500;
	
	static final double fireTime = 0.5;
	
	double lastShot = MainGame.getTime();
	double lastReloc = MainGame.getTime();
	
	
	public void update(double dt, WorldHandle h)
	{
		super.update(dt, h);
		double dist = Util.distance(this, target);
		double speed = 150;
		dx = speed * (target.getX() - x) / dist;
		dy = speed * (target.getY() - y) / dist;
		double time = h.currentState().time;
		if(time >= lastShot + fireTime)
		{
			
			
			Bullet shot = new Bullet.Builder()
					.setColor(Color.AQUAMARINE)
					.setDamage(1)
					.setFaction(getFaction())
					.setTarget(target)
					.setLocation(this)
					.setSpeed(200)
					.setRadius(10).build();
			
			h.add(shot);
			lastShot = time;
		}
		if(time >= lastReloc + relocTime)
		{
			double angle = Math.random() * Math.PI * 2;
			x += Math.cos(angle) * jumpDist;
			y += Math.sin(angle) * jumpDist;
			lastReloc = time;
		}

		target = h.track(target);
	}

	@Override
	public Bounds bounds()
	{
		return Circle.centeredCircle(this, radius);
	}

}
