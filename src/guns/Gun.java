package guns;



import engine.WorldHandle;
import objects.Locatable;
import objects.ReadableObject;

public interface Gun
{
	void fire(ReadableObject source, Locatable target, WorldHandle h);
	
	boolean isReady(WorldHandle h);
}
