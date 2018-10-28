package guns;
/**Replicates the effect into a ring pattern
 * @author Edwin Fennell
 */
import static guns.DataType.*;

import java.util.EnumSet;
import java.util.Set;

import objects.Locatable;
import objects.ReadableObject;
import util.Vector;

public enum RingNode implements NodeType
{
	FACTORY;
	@Override
	public Set<DataType> required()
	{
		return EnumSet.of(SOURCE, TARGET);
	}

	@Override
	public Node generate(NodeOptions o)
	{
		int number = o.pickCount();
		Node effect = o.pickNode();
		return (d, h) ->
		{
			ReadableObject source = d.get(SOURCE);
			Locatable target = d.get(TARGET);
			double sourceX = source.getX();
			double sourceY = source.getY();
			double targetX = target.getX();
			double targetY = target.getY();
			Vector displacement = new Vector(targetX - sourceX, targetY - sourceY);
			double baseAngle = Math.atan2(displacement.getY(), displacement.getX());
			for(int i = 0; i < number; i++)
			{
				double angle = i * 2 * Math.PI / number;
				Vector targetLoc = Vector.fromPolar(displacement.length(), baseAngle + angle).add(source);
				d.set(TARGET, targetLoc);
				effect.effects(d, h);
			}
			d.set(TARGET, target);
		};
	}
}
