package objects.entities;

import objects.Faction;
import objects.GameObject;
import objects.MovingObject;

public abstract class Entity extends MovingObject implements ReadableEntity
{	
	protected Entity(double x, double y) 
	{
		super(x, y);
	}
	
	protected Entity(double x, double y, Faction faction)
	{
		super(x,y,faction);
	}
	
	protected double health;
	protected double maxHealth;
	
	
	@Override
	public boolean isDead()
	{
		return health <= 0;
	}
	
	@Override
	public double health()
	{
		return health;
	}
	
	@Override
	public double maxHealth()
	{
		return maxHealth;
	}
	
	public void damage(double damage)
	{
		health -= damage;
		correctHealth();
	}
	
	public void heal(double amount)
	{
		health += amount;
		correctHealth();
	}
	
	protected void correctHealth()
	{
		health = Math.max(0, Math.min(health, maxHealth));
	}
	
	public static final int DAMAGE = 5;
	public static final int SETHEALTH = 6;
	
	@Override
	public boolean supportsOperation(int code)
	{
		switch(code)
		{
		case DAMAGE:
		case SETHEALTH:
			return true;
		}
		return super.supportsOperation(code);
	}
	
	@Override
	protected void handleChange(Change change, GameObject source)
	{
		switch(change.code)
		{
		case DAMAGE:
			damage((Double) change.data);
			break;
		case SETHEALTH:
			health = (Double) change.data;
			correctHealth();
			break;
		default:
			super.handleChange(change, source);
		}
	}
	
	
	
	
	
	
	
}
