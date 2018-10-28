package guns;

import java.util.ArrayList;
import java.util.List;

/**Combines the effects of two nodes
 * @author Edwin Fennell
 **/
public class MergeNode implements NodeType
{
	
	@Override
	public Node generate(NodeOptions o) 
	{
		List<Node> effectSet = new ArrayList<Node>();
		return (d, h) ->
		{
			for (Node effect : effectSet)
			{
				effect.effects(d, h);
			}
		};
	}

}
