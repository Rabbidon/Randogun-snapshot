package guns;

import static guns.DataType.*;

import java.util.EnumSet;
import java.util.Set;

import objects.Locatable;
import objects.ReadableObject;
import util.Vector;

/**Takes a targeted effect and replicates it in a cone pattern
 * @author Edwin Fennell
 */
public enum ConeNode implements NodeType
{
	FACTORY;
	@Override
	public Node generate(NodeOptions o)
	{
		int number = o.pickCount();
		double angle = Math.PI / 6;
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
			double increment = angle / (number - 1);
			double baseAngle = Math.atan2(displacement.getY(), displacement.getX()) - (angle/2);
			for(int i = 0; i < number; i++)
			{
				Vector targetLoc = Vector.fromPolar(displacement.length(), baseAngle + i*increment).add(source);
				d.set(TARGET, targetLoc);
				effect.effects(d, h);
			}
			d.set(TARGET, target);
		};
	}
	
	@Override
	public Set<DataType> required()
	{
		return EnumSet.of(SOURCE, TARGET);
	}
	




}