package objects.entities;

import bounds.Bounds;
import bounds.Circle;
import engine.ObjectView;
import engine.WorldHandle;
import objects.GameObject;
import fxcore.MainGame;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import objects.Bullet;
import util.Util;

public class Charger extends Enemy
{
	
	private volatile double hits = 0;
	public static final Color COLOR = Color.BLUE;
	public static final double radius = 50;
	
	
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
	
	
	
	public Charger(double x, double y, WorldHandle h)
	{
		super(x, y, h);
		maxHealth = 300;
		health = maxHealth;
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
	
	@Override
	public void handleChange(Change c, GameObject source)
	{
		super.handleChange(c, source);
		if(c.code == DAMAGE)
		{
			hits += (Double) c.data;
		}
	}

	
	private void lunge(WorldHandle h)
	{
		double speed = 1200;
		double distance = Util.distance(this, target);
		double x = target.getX() - this.x;
		double y = target.getY() - this.y;
		x = speed * x / distance;
		y = speed * y / distance;
		dx = x;
		dy = y;
		
		
	}
	
	@Override
	public void update(double dt, WorldHandle h)
	{
		super.update(dt, h);
		double decayFactor = Math.pow(0.16, dt);
		dx *= decayFactor;
		dy *= decayFactor;
		double lungeThreshold = Util.sqr(240/ (1 + (health / maxHealth)));
		if(Util.hypotSquared(dx, dy) < lungeThreshold) lunge(h);
		//nextEvents.clear();
		for(; hits > 0; hits--)
		{
			double bulletSpeed = 300;
			//if(health / maxHealth < .5) bulletSpeed = 10;
			Bullet b = new Bullet(this, target, bulletSpeed, 5, Color.BLUE);
			b.damage = .5;
			b.spread(Math.toRadians(20));
			h.add(b);
			//nextEvents.add(GameEvent.spawnerOf(b));
		}
		if(!isDead()) heal(.1);
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
