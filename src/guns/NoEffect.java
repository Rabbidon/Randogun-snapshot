package guns;

/**The empty node**/
public enum NoEffect implements NodeType 
{
	FACTORY;
	@Override
	public Node generate(NodeOptions o)
	{
		return Node.NO_EFFECT;
	}
}

