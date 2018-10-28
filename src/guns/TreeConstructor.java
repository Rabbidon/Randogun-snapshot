package guns;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import bounds.Bounds;
import guns.DataTree.DataNode;
import javafx.scene.paint.Color;

public class TreeConstructor implements NodeOptions
{
	
	ImmutableList<NodeType> availableNodes;
	Deque<Set<DataType>> providedInfo = new ArrayDeque<>();
	DataTree data;
	Deque<Traverser> treePath = new ArrayDeque<>();
	Map<String, NodeType> nameMap;

	/**Constructs a gun tree - which is actually just a single node - from the corresponding data tree**/
	public TreeConstructor(Collection<NodeType> available, DataTree source)
	{
		availableNodes = ImmutableList.copyOf(available);
		data = source;
		nameMap = availableNodes.stream()
				.collect(ImmutableMap.toImmutableMap(x -> x.getClass().getName(), x -> x));
	}
	/**Traverses a data tree **/
	class Traverser
	{
		DataNode data;
		int nodeIndex = 0;
		int cindex = 0;
		int dindex = 0;
		
		public Traverser(DataNode start)
		{
			data = start;
		}
		public DataNode nextNode()
		{
			return data.children[nodeIndex++];
		}
		
		double nextC()
		{
			return data.cparams[cindex++];
		}
		
		int nextD()
		{
			return data.dparams[dindex++];
		}
	}

	/**Generates the gun tree**/
	@Override
	public Node pickNode(DataType... provided)
	{
		Set<DataType> currentInfo = EnumSet.copyOf(providedInfo.peek());
		currentInfo.addAll(Arrays.asList(provided));
		providedInfo.push(currentInfo);
		HashSet<String> allowedNodes = new HashSet<>();
		for(NodeType n : availableNodes)
			if(n.checkRequiredInfo(providedInfo.peek()))
				allowedNodes.add(n.getClass().getName());
				
		DataNode next = treePath.peek().nextNode();
		if(!allowedNodes.contains(next.getClass().getName()))
			throw new RuntimeException("Invalid Node!");
		NodeType chosenNode = nameMap.get(next.type);
		treePath.push(new Traverser(next));
		Node result = chosenNode.generate(this);
		providedInfo.pop();
		treePath.pop();
		return result;
	}
	
	@Override
	public double pickDamage()
	{
		return treePath.peek().nextC();
	}

	@Override
	public double pickTime()
	{
		return treePath.peek().nextC();
	}

	@Override
	public Color pickColor()
	{
		Traverser t = treePath.peek();
		return Color.color(t.nextC(), t.nextC(), t.nextC());
	}

	@Override
	public double pickSpeed()
	{
		return treePath.peek().nextC();
	}
	
	

	@Override
	public double pickSize()
	{
		return treePath.peek().nextC();
	}

	@Override
	public int pickCount()
	{
		return treePath.peek().nextD();
	}

	@Override
	public Bounds pickBounds()
	{
		return null;
	}
	
	@Override
	public double pickDuration()
	{
		return treePath.peek().nextC();
	}

	@Override
	public double pickAccel()
	{
		return treePath.peek().nextC();
	}

	@Override
	public double pickAngle()
	{
		return treePath.peek().nextC();
	}
}
