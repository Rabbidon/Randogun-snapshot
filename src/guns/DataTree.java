package guns;

import java.io.InputStream;
import java.net.Socket;

public class DataTree
{

	/**Structure designed to export gun tree to C++ via strings**/
	DataNode root;
	double fireTime;

	/**The nodes making up the tree**/
	class DataNode
	{
		DataNode[] children;
		double[] cparams;
		int[] dparams;
		String[] ctypes;
		String[] dtypes;
		String type;



		/**Gives a string representation of the tree depth-first.
		 * The order in which each data node is represented is 'type - number of continuous parameters - pairs of the form
		 * (continuous variable name, continuous variable value) - number of discrete parameters - pairs of the form
		 * (discrete variable name, discrete variable value) - repeat for children'**/
		static final char SEP = ' ';
		public String toString()
		{
			StringBuilder b = new StringBuilder();
			b.append(type);
			b.append(SEP);
			b.append(cparams.length);
			b.append(SEP);
			for(int i = 0; i < cparams.length; i++)
			{
				b.append(ctypes[i]).append(SEP);
				b.append(cparams[i]).append(SEP);
			}
			b.append(dparams.length);
			b.append(SEP);
			for(int i = 0; i < dparams.length; i++)
			{
				b.append(dtypes[i]).append(SEP);
				b.append(dparams[i]).append(SEP);
			}
			b.append(children.length).append(SEP);
			for(DataNode n : children)
				b.append(n.toString()).append(SEP);
			return b.toString();
		}
	}

	/**Prepends fire rate to the gun string**/
	public String toString()
	{
		return fireTime + root.toString();
	}

	static DataTree read(InputStream s)
	{
		return null;
	}
	
	
	
	
	
}
