package util;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.concurrent.Callable;

import com.google.common.primitives.Longs;

public class RandomXOR1024 implements Random
{
	private long[] state = new long[16];
	int p = 0;
	
	public RandomXOR1024()
	{
		this(getValid(() -> Longs.fromByteArray(new SecureRandom().generateSeed(8))));
	}
	
	private static long getValid(Callable<Long> source)
	{
		try
		{
			long temp = source.call();
			while(temp == 0)
			{
				temp = source.call();
			}
			return temp;
		}
		catch (Exception e)
		{
			throw new InternalError(e);
		}
	}
	
	public RandomXOR1024(long seed)
	{
		XOR64 temp = new XOR64(seed);
		for(int i = 0; i < state.length; i++)
		{
			state[i] = temp.nextULong();
		}
	}
	
	public RandomXOR1024(long... seed)
	{
		state = Arrays.copyOf(seed, 16);
	}
	
	@Override
	public long nextULong()
	{
		long temp1 = state[p];
		p = (p + 1) & 0xF;
		long temp2 = state[p];
		temp2 ^= temp2 << 31;
		temp2 ^= temp2 >>> 11;
		temp1 ^= temp1 >>> 30;
		state[p] = (temp1 ^ temp2);
		return state[p] * 1181783497276652981L;
	}
	
	private static class XOR64 implements Random
	{
		private long state;
		
		public XOR64(long state)
		{
			if(state == 0) throw new IllegalArgumentException();
			this.state = state;
		}
		
		@Override
		public long nextULong()
		{
			state ^= state >>> 12;
			state ^= state << 25;
			state ^= state >>> 27;
			return state * 2685821657736338717L;
		}
	}
}
