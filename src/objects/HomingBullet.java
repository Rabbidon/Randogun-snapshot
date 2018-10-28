package objects;

import engine.ObjectView;
import engine.WorldHandle;
import javafx.scene.paint.Color;
import util.Util;

public class HomingBullet extends Bullet
{
	protected double speed;
	protected ObjectView target;
	
	public HomingBullet(ReadableObject source, ObjectView target, double speed, double radius, Color color, double damage)
	{
		super(source, target, speed, radius, color);	
		this.speed = speed;
		this.target = target;
		this.damage = damage;
	}
	
	@Override
	public void update(double dt, WorldHandle h)
	{
		if(target != null)
		{
			double dist = Util.distance(this, target);
			dist = Util.clamp(dist, 0.01, Double.POSITIVE_INFINITY);
			dx += dt * speed * (target.getX() - getX()) / dist;
			dy += dt * speed * (target.getY() - getY()) / dist;
			double slowDown = Math.pow(0.5, dt);
			dx *= slowDown;
			dy *= slowDown;
			
			target = h.track(target);
		}
		
		super.update(dt, h);
	}
}
