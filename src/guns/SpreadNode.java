package guns;

/**Introduces inaccuracy by randomly adjusting angle
 * @author Edwin Fennell
 */

import static guns.DataType.*;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import objects.Locatable;
import objects.ReadableObject;
import util.Vector;

public class SpreadNode implements NodeType
{

	@Override
	public Node generate(NodeOptions o) 
	{
		Node effect = o.pickNode();
		double spread = o.pickAngle();
		return (d,h) ->
		{
			ReadableObject source = d.get(SOURCE);
			Locatable target = d.get(TARGET);
			double sourceX = source.getX();
			double sourceY = source.getY();
			double targetX = target.getX();
			double targetY = target.getY();
			Random randomInstance = new Random();
			double localGaussian = randomInstance.nextGaussian();
			double scatter = spread*(localGaussian);
			Vector displacement = new Vector(targetX - sourceX, targetY - sourceY);
			double baseAngle = Math.atan2(displacement.getY(), displacement.getX());
			Vector targetLoc = Vector.fromPolar(displacement.length(), baseAngle + scatter).add(source);
			d.set(TARGET, targetLoc);
			effect.effects(d, h);
		};
	}
	
	@Override
	public Set<DataType> required()
	{
		return EnumSet.of(SOURCE,TARGET);
	}
}
