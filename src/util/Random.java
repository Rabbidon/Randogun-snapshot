package util;

public interface Random
{	
	public static Random create()
	{
		return new RandomXOR1024();
	}
	
	public long nextULong();
	
	public default byte[] nextBytes(int bytes)
	{
		byte[] result = new byte[bytes];
		int n = 0;
		while( bytes != 0 ) 
		{
			n = Math.min( bytes, 8 );
			for ( long bits = nextULong(); n-- != 0; bits >>= 8 ) result[ --bytes ] = (byte)bits;
		}
		return result;	
	}
	
	public default int nextInt()
	{
		return ((int) nextULong()) >>> 1;
	}
	
	public default int nextInt(int bound)
	{
		return (int) nextLong(bound);
	}
	
	public default boolean nextBoolean()
	{
		return (nextULong() & 1) == 0;
	}
	
	double DW53 = 1.0 / (1L << 53);
	public default double nextDouble()
	{
		return (nextULong() >>> 11) * DW53;
	}
	
	
	double FW24 = 1.0 / (1L << 24);
	public default float nextFloat()
	{
		return (float) ((nextULong() >>> 40) * FW24); 
	}
	
	
	public default long nextLong()
	{
		return nextULong() >>> 1; 
	}
	
	
	default long next(int bits)
	{
		return nextULong() >>> (64 - bits);
	}
	
	
	public default long nextLong(long bound)
	{
		if(bound <= 0) throw new IllegalArgumentException("Negative Bound: " + bound);
		while(true)
		{
			final long bits = nextLong();
			final long value = bits % bound;
			if ( bits - value + ( bound - 1 ) >= 0 ) return value;
		}
	}
	
}
