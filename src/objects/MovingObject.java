package objects;

import engine.State;
import engine.WorldHandle;
import util.Vector;

public abstract class MovingObject extends GameObject implements ReadableMover
{
	protected double dx, dy;
	protected double mass = 1;
	
	protected MovingObject(double x, double y, double dx, double dy, Faction faction)
	{
		super(x, y, faction);
		this.dx = dx;
		this.dy = dy;
	}
	
	protected MovingObject(double x, double y)
	{
		super(x, y);
		dx = 0;
		dy = 0;
	}

	protected MovingObject(double x, double y, Faction faction)
	{
		this(x, y);
		this.faction = faction;
	}

	public MovingObject(double x, double y, double dx, double dy, Faction faction, double mass)
	{
		this(x, y, dx, dy, faction);
		this.mass = mass;
	}

	
	@Override
	public void update(double dt, WorldHandle h)
	{
		super.update(dt, h);
		x += dt * getDX();
		y += dt * getDY();
		if(!h.currentState().gameBounds.contains(bounds()))
		{
			onWallHit(h.currentState(), dt);
		}
	}
	
	
	
	protected void onWallHit(State s, double dt)
	{
		x -= dt * getDX();
		y -= dt * getDY();
		dx = 0;
		dy = 0;
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
	
	public static final int ACCELERATE = 3;
	public static final int SETVELOCITY = 4;
	public static final int FORCE = 8;
	
	@Override
	public boolean supportsOperation(int code)
	{
		if(super.supportsOperation(code)) return true;
		switch(code)
		{
		case ACCELERATE:
		case SETVELOCITY:
		case FORCE:
			return true;
		}
		return false;
	}
	
	@Override
	protected void handleChange(Change change, GameObject source)
	{
		switch(change.code)
		{
		case ACCELERATE:
			Vector v = (Vector) change.data;
			dx += v.x;
			dy += v.y;
			break;
		case SETVELOCITY:
			Vector vec = (Vector) change.data;
			dx = vec.x;
			dy = vec.y;
			break;
		case FORCE:
			Vector vc = (Vector) change.data;
			dx += vc.x / mass;
			dy += vc.y / mass;
			
		
		default:
			super.handleChange(change, source);
		}
	}
	
	@Override
	public double mass()
	{
		return mass;
	}
}
