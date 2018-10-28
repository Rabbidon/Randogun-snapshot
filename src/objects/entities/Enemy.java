package objects.entities;

import engine.ObjectView;
import engine.WorldHandle;
import objects.Faction;
import player.Player;
import util.Vector;


public abstract class Enemy extends Entity implements ReadableEnemy
{

    WorldHandle h;
	ObjectView target;

	protected Enemy(double x, double y, WorldHandle h)
	{
		super(x, y);
		this.target = h.nearestObject( new Vector(x,y),(o -> o instanceof Player));
		faction = Faction.Enemy;
	}
	
	@Override
	public int collisionClass()
	{
		return 3;
	}
	
	
	
}
