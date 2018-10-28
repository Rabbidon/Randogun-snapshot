package guns;

import static guns.DataType.*;
import static util.Util.*;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;

import bounds.Bounds;
import javafx.scene.paint.Color;
/**Currently the standard distributions for node options - pieces together the existing nodes**/
public class RandomNodeOptions implements NodeOptions
{
	
	Collection<NodeType> availableNodes = ImmutableList.of(NoEffect.FACTORY,SpawnProjectile.FACTORY,RepeatNode.FACTORY,ConeNode.FACTORY);
	Deque<Set<DataType>> providedInfo = new ArrayDeque<>();
	{
		providedInfo.push(EnumSet.of(SOURCE, TARGET, HEALTH));
	}
	int depthCap;
	int depth;

	@Override
	public Node pickNode(DataType... provided)
	{
		List<NodeType> validNodes = availableNodes.stream().filter(n -> n.checkRequiredInfo(providedInfo.peek())).collect(Collectors.toList());;
		NodeType chosenNode = validNodes.get(rand.nextInt(validNodes.size()));
		Set<DataType> currentInfo = EnumSet.copyOf(providedInfo.peek());
		currentInfo.addAll(Arrays.asList(provided));
		providedInfo.push(currentInfo);
		Node result = chosenNode.generate(this);
		providedInfo.pop();
		return result;
	}
	
	@Override
	public double pickDamage()
	{
		return 1;
	}

	@Override
	public double pickTime()
	{
		return 0.05d;
	}

	@Override
	public Color pickColor()
	{
		int red = rand.nextInt(256), blue = rand.nextInt(256), green = rand.nextInt(256);
		return Color.rgb(red, green, blue);
	}

	@Override
	public double pickSpeed()
	{
		return rand.nextDouble() * 500 + 500;
	}
	
	

	@Override
	public double pickSize()
	{
		return rand.nextDouble() * 10 + 5;
	}

	@Override
	public int pickCount()
	{
		return 4;
	}

	@Override
	public Bounds pickBounds()
	{
		return null;
	}
	
	@Override
	public double pickDuration()
	{
		return 5;
	}

	@Override
	public double pickAccel()
	{
		return Math.random() * 500 + 300;
	}

	@Override
	public double pickAngle()
	{
		return Math.random();
	}
}
