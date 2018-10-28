package guns;

/**Repeats the action multiple times with a delay
 * @author Edwin Fennell
 */
public enum RepeatNode implements NodeType
{
	FACTORY;
	@Override
	public Node generate(NodeOptions o)
	{
		int number = o.pickCount();
		double interval = o.pickTime();
		Node effect = o.pickNode();
		return (d, h) ->
		{
			for(int i = 0; i < number; i++)
			{
				h.schedule(i * interval, g -> effect.effects(d, g));
			}
		};
	}
}
