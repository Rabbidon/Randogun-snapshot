package objects;

import engine.WorldHandle;
import javafx.scene.paint.Color;

public class ResourcePickup extends Pickup 
{
	int income;
	
	public ResourcePickup(int income,double x, double y, double radius, Color color,double attraction,double attractionRadius)
	{
		super(x,y,radius,color,attraction,attractionRadius);
		this.income = income;
	}

	@Override
	public Impact collideWith(GameObject other, WorldHandle h)
	{
		h.message(other,2,income);
		isDead = true;
		return Impact.NONE;
	}

}