package guns;

import java.util.EnumSet;
import java.util.Set;

/**The class for node types, along with checking functions**/
public interface NodeType
{
	default Set<DataType> required()
	{
		return EnumSet.noneOf(DataType.class);
	}
	
	default boolean checkRequiredInfo(Set<DataType> providedInfo)
	{
		return providedInfo.containsAll(required());
	}
	
	Node generate(NodeOptions o);
}

