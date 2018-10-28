package guns;

import static guns.DataType.*;

import java.util.EnumSet;
import java.util.Set;

import engine.ObjectView;
import engine.WorldHandle;
import javafx.scene.paint.Color;
import objects.Bullet;
import objects.GameObject;
import objects.HomingBullet;
import objects.Locatable;
import objects.ReadableObject;
import objects.entities.Entity;
public enum SpawnProjectile implements NodeType
{
	FACTORY;
	
	@Override
	public Set<DataType> required()
	{
		return EnumSet.of(SOURCE, TARGET);
	}
	
	@Override
	public Node generate(NodeOptions o)
	{
		double speed = o.pickSpeed();
		double damage = o.pickDamage();
		double rad = o.pickSize();
		Color color = o.pickColor();
		Node onHit = o.pickNode();
		return (d, h) ->
		{
			Locatable target = d.get(TARGET);
			ReadableObject source = d.get(SOURCE);
			ObjectView t = h.nearestObject(target, obj -> (obj.collisionClass() == 3));
			if(t != null)
			h.add(new Bullet(source, target, speed, 5, color, damage)
			{
				boolean s = true;
				{
					this.radius = rad;
				}
				@Override
				public Impact collideWith(GameObject other, WorldHandle h)
				{
					Impact impact = new Impact(this);
					{
						if(other.supportsOperation(Entity.DAMAGE))
						{
							//isDead = true;
							impact.changes.add(new Change(Entity.DAMAGE, damage));
							//impact.changes.add(new Change(MovingObject.FORCE, new Vector(dx * mass, dy * mass)));
						}
						if(s) onHit.effects(d, h);
						s = false;
					}
					return impact;
				}
			});
		};
	}
	
	
	
	
}
