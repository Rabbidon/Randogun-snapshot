package guns;

/**Takes a node and causes it to run on a delay by caliing the scheduler
 * @author Edwin Fennell
 */
public enum DelayNode implements NodeType
{
	FACTORY;
	@Override
	public Node generate(NodeOptions o)
	{
		double delay = o.pickTime();
		Node effect = o.pickNode();
		return (d, h) ->
		{
			h.schedule(delay, g -> effect.effects(d, g));
		};
	}
}