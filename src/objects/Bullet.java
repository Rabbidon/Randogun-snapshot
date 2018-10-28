package objects;



import static fxcore.MainGame.*;

import bounds.Bounds;
import bounds.Circle;
import engine.State;
import engine.WorldHandle;
import fxcore.MainGame;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import objects.entities.Entity;
import util.Util;
import util.Vector;
/**
 * Represents a bullet in the game. Provides a builder for easy customization.
 * Automatically moves itself by dx and dy each time update() is called.
 * Subclasses may want to override various methods for different behavior.
 * @author Rajan
 *
 */
public class Bullet extends MovingObject implements ReadableBullet
{
	public Color color;
	public double damage = 1;
	protected double startTime;
	protected boolean isDead = false;
	protected double radius;
	protected Circle bounds = new Circle()
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
	
	private Bullet(double x, double y)
	{
		super(x,y);
		startTime = MainGame.getTime();
		mass = .03;
	}
	
	protected Bullet(ReadableObject entity)
	{
		this(entity.getX(), entity.getY());
		faction = entity.getFaction();
		cc = (faction == Faction.Player) ? 2 : 4;
	}
	
	public Bullet(double x, double y, double dx, double dy, double radius, Color color)
	{
		this(x,y);
		this.dx = dx;
		this.dy = dy;
		this.radius = radius;
		this.color = color;
	}
	
	public Bullet(double x, double y, double dx, double dy)
	{
		this(x,y,dx,dy,Color.RED);
	}
	
	public Bullet(double x, double y, double dx, double dy, Color color)
	{
		this(x,y,dx,dy,1,color);
	}
	
	public Bullet(double x, double y, double dx, double dy, double radius)
	{
		this(x,y,dx,dy,radius,Color.RED);
	}
		
	public Bullet(ReadableObject source, Locatable target, double speed, double radius, Color color)
	{
		this(source);
		double distance = Util.distance(source,target);
		dx = speed * (target.getX() - x) / distance;
		dy = speed * (target.getY() - y) / distance;
		this.radius = radius;
		this.color = color;
	}
	
	public Bullet(ReadableObject source, double angle, double speed, double radius, Color color)
	{
		this(source);
		dx = Math.cos(angle) * speed;
		dy = Math.sin(angle) * speed;
		this.radius = radius;
		this.color = color;
	}
	
	public Bullet(ReadableObject source, Locatable target, double speed, double radius, Color color, double damage)
	{
		this(source);
		double distance = Util.distance(source,target);
		dx = speed * (target.getX() - x) / distance;
		dy = speed * (target.getY() - y) / distance;
		this.radius = radius;
		this.color = color;
		this.damage = damage;
	}

	@Override
	public void update(double dt, WorldHandle h)
	{
		super.update(dt,h);
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
	}

	@Override
	protected void onWallHit(State s, double dt)
	{
		isDead = true;
	}

	@Override
	public double getRadius() 
	{
		return bounds.radius();
	}

	@Override
	public Color getColor() 
	{
		return color;
	}
	
	@Override
	public Impact collideWith(GameObject other, WorldHandle h)
	{
		Impact impact = new Impact(this);
		{
			if(other.supportsOperation(Entity.DAMAGE))
			{
				isDead = true;
				impact.changes.add(new Change(Entity.DAMAGE, damage));
				impact.changes.add(new Change(MovingObject.FORCE, new Vector(dx * mass, dy * mass)));
			}
			
			
		}
		return impact;
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

	public void spread(double maxAngle)
	{
		double angle = Math.atan2(dy, dx);
		double speed = Math.hypot(dx, dy);
		angle += maxAngle * (rand.nextDouble() * 2 - 1);
		dx = Math.cos(angle) * speed;
		dy = Math.sin(angle) * speed;
	}
	
	public static class Builder
	{
		protected Locatable start;
		protected Faction faction;
		protected double dy = 0, dx = 0;
		protected Locatable target = null;
		protected Color color = Color.RED;
		protected double radius = 5;
		protected double damage = 1;
		protected double speed = 1;
		
		public Bullet build()
		{
			if(target == null)
			{
				Bullet b = new Bullet(start.getX(), start.getY(), dx, dy, radius, color);
				b.color = color;
				b.faction = faction;
				b.damage = damage;
				return b;
			}
			else
			{
				Bullet b = new Bullet(GameObject.dataOf(start.getX(), start.getY(), faction), target, speed, radius, color);
				b.damage = damage;
				return b;
			}			
		}
		
		public Builder setLocation(Locatable l)
		{
			start = new Vector(l);
			return this;
		}
		
		public Builder setLocation(double x, double y)
		{
			start = new Vector(x, y);
			return this;
		}
		
		public Builder setFaction(Faction f)
		{
			faction = f;
			return this;
		}
		
		public Builder setVelocity(Vector v)
		{
			dy = v.y;
			dx = v.x;
			target = null;
			return this;
		}
		
		public Builder setColor(Color c)
		{
			color = c;
			return this;
		}
		
		public Builder setRadius(double r)
		{
			radius = r;
			return this;
		}
		
		public Builder from(ReadableObject source)
		{
			return setLocation(source).setFaction(source.getFaction());
		}
		
		public Builder setTarget(Locatable l)
		{
			target = new Vector(l);
			return this;
		}
		
		public Builder setDamage(double damage)
		{
			this.damage = damage;
			return this;
		}
		
		public Builder setSpeed(double speed)
		{
			this.speed = speed;
			return this;
		}
		
		
	}
	int cc;
	@Override
	public int collisionClass()
	{
		return cc;
	}
	
	

}
