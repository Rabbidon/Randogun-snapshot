package guns;

/**The node class for constructing gun trees**/
import engine.WorldHandle;

public interface Node 
{
	public static final Node NO_EFFECT = (d, h) -> {};

	void effects(DataPacket d, WorldHandle h);
}
