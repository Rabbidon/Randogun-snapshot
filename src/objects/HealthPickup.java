package objects;

import engine.WorldHandle;
import javafx.scene.paint.Color;

public class HealthPickup extends Pickup 
{


	private double heal;
	
	public HealthPickup(double heal, Locatable loc)
	{
		this(heal,loc.getX(),loc.getY(),10,Color.RED,1,1);
	}
	
	public HealthPickup(double heal, double x, double y, double radius, Color color, double attraction, double attractionRadius)
	{
		super(x, y,radius,color,attraction,attractionRadius);
		this.heal = heal;
	}


	@Override
	public Impact collideWith(GameObject other, WorldHandle h)
	{
		h.message(other,1,heal);
		isDead = true;
		return Impact.NONE;
	}

}
