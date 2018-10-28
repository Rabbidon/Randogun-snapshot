package guns;

import static guns.DataType.*;

import bounds.Bounds;
import bounds.Circle;
import engine.WorldHandle;
import fxcore.MainGame;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import objects.GameObject;
import objects.Locatable;
import objects.ReadableObject;
import objects.entities.Entity;
public class SpawnAOE implements Node
{
	double damage;
	double duration;
	double radius;
	Color color;
	Node update;
	Node onHit;
	
	public SpawnAOE(double duration, double damage, double radius)
	{
		this.duration = duration;
		this.damage = damage;
		this.radius = radius;
	}
	public static SpawnAOE generate(NodeOptions o)
	{
		SpawnAOE result = new SpawnAOE(o.pickDuration(), o.pickDamage(), o.pickSize());
		result.color = o.pickColor();
		result.onHit = o.pickNode();
		return result;
	}
	
	
	@Override
	public void effects(DataPacket d, WorldHandle h)
	{
		Locatable target = d.get(TARGET);
		ReadableObject source = d.get(SOURCE);
		//System.out.println("Hi");
		h.add(new GameObject(target.getX(), target.getY(), source.getFaction())
		{
			double spawnTime = MainGame.getTime();
			boolean isDead = false;
			@Override
			public boolean isDead()
			{
				return isDead;
			}
			@Override
			public void draw(GraphicsContext g)
			{
				g.setFill(color);
				b.fill(g);
				
			}
			Bounds b = new Circle()
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
			@Override
			public Bounds bounds()
			{
				return b;
			}
			
			@Override
			public void update(double dt, WorldHandle h)
			{
				super.update(dt, h);
				if (MainGame.getTime() >= duration + spawnTime)
				{
					isDead = true;
				}
				//update.effects()
			}
			boolean s = false;
			
			@Override
			public Impact collideWith(GameObject other, WorldHandle h)
			{
				//isDead = true;
				if(other.getFaction() == faction) return Impact.NONE;
				if(!s) onHit.effects(d, h);
				
				s = true;
				
				return new Impact(this, new Change(Entity.DAMAGE, damage));
			}
		});
	}
}
		
	


