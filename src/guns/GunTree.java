package guns;

import static guns.DataType.*;

import engine.WorldHandle;
import objects.Locatable;
import objects.ReadableObject;

/**Takes a fire node and implements it to an actual firing mechanism**/
public class GunTree implements Gun {

	protected double shotTime = 0.66666;
	boolean autoFire;
	double recoil;
	Node rootNode;
	double lastShot;

	Node onShoot;

	public GunTree(Node shoot) {
		onShoot = shoot;
	}

	@Override
	public void fire(ReadableObject source, Locatable target, WorldHandle h) {
		lastShot = h.currentState().time;
		DataPacket d = new DataPacket();
		d.set(SOURCE, source);
		d.set(TARGET, target);
		onShoot.effects(d, h);
	}

	@Override
	public boolean isReady(WorldHandle h) {
		return h.currentState().time - lastShot > shotTime;
	}

	public void export()
	{

	}
	
}
