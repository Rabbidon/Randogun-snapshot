package guns;

public class DuplicateNode implements NodeType
{
	/**Copies a node
	 * @author Edwin Fennell
	 * */
	@Override
	public Node generate (NodeOptions o)
	{
		int number = o.pickCount();
		Node effect = o.pickNode();
		return (d,h) ->
		{
			for (int i=0; i<number; i++)
			{
				effect.effects(d,h);
			}
		};
	}
}
