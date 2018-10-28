package guns;

import java.util.EnumMap;
import java.util.Map;

import util.Util;

/**Storage container for all the data that a node needs - stores a piece of data and its type**/
public class DataPacket 
{
	Map<DataType, Object> data;


	public DataPacket()
	{
		data = new EnumMap<DataType, Object>(DataType.class);
	}
	
	public <T> T get(DataType d)
	{
		if(!has(d)) throw new IllegalArgumentException();
		return Util.cast(data.get(d));
	}
	
	public boolean has(DataType d)
	{
		return data.containsKey(d);
	}

	/**Inserts data into the data packet**/
	public void set(DataType d, Object o)
	{
		data.put(d, o);
	}
}
