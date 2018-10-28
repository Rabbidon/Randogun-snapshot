package objects.entities;

import objects.ReadableMover;

public interface ReadableEntity extends ReadableMover
{
	@Override
	public boolean isDead();
	
	public double health();
	
	public double maxHealth();

	
}
