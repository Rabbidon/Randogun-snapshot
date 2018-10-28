package util;

import java.lang.reflect.Field;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import fxcore.MainGame;
import objects.Locatable;
import sun.misc.Unsafe;

/**
 * Provides various utility functions used in multiple places, probably should be statically imported where used.
 * @author Rajan Troll
 *
 */
public class Util 
{
	public static final int NEXT_CHANGE_CODE = 9;
	static
	{
		
	}
	public static final  Random rand = Random.create();
	
	/**
	 * Main parallel executor for the program. Maintains one thread for each available processor for maximal parallelism.
	 * Provides as many convenience executor functionalities as possible. Should ONLY be used for tasks doing continuous work, 
	 * NOT for those waiting for something else to happen. A task which waits on this executor will block others from being processed.
	 *  
	 */
	public static final ListeningScheduledExecutorService compute = MoreExecutors.listeningDecorator(
			Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors()));
	
	/**
	 * Executor for tasks which block, wait, or are interdependent. All submitted tasks will be executed concurrently, 
	 * but with increasing overhead with greater numbers of tasks.
	 */
	public static final ListeningExecutorService exec = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
	
	public static int relu(int i)
	{
		return i > 0 ? i : 0;
	}
	/**returns input number if positive, 0 else**/
	public static int cap(int i, int cap)
	{
		return i < cap ? i : cap;
	}
	

	
	@SuppressWarnings("unchecked")
	public static final <T> T cast(Object obj)
	{
		return (T) obj;
	}
	
	static Unsafe unsafe = getUnsafe();
	
	
	public static double distance(double x1, double y1, double x2, double y2)
	{
		return Math.sqrt(distanceSquared(x1,y1,x2,y2));
	}
	
	public static double sqr(double x)
	{
		return x * x;
	}
	
	private static Unsafe getUnsafe()
	{
		Field theUnsafe = null;
		try
		{
			theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
		}
		catch (NoSuchFieldException | SecurityException e1)
		{
			e1.printStackTrace();
		}
		theUnsafe.setAccessible(true);
		try
		{
			return (Unsafe) theUnsafe.get(null);
		}
		catch (IllegalArgumentException | IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static double distance(Locatable g1, Locatable g2)
	{
		return distance(g1.getX(), g1.getY(), g2.getX(), g2.getY());
	}
	
	public static double distanceSquared(double x1, double y1, double x2, double y2)
	{
		return sqr(x1-x2) + sqr(y1-y2);
	}
	
	public static double distanceSquared(Locatable g1, Locatable g2)
	{
		return distanceSquared(g1.getX(),g1.getY(),g2.getX(),g2.getY());
	}
	
	public static double hypotSquared(double d1, double d2)
	{
		return d1 * d1 + d2 * d2;
	}
	
	public static Vector circleRandom(double radius)
	{
		double angle = MainGame.rand.nextDouble() * 2 * Math.PI;
		return new Vector(Math.cos(angle) * radius, Math.sin(angle) * radius);
	}
	
	public static double interpolate(double first, double second, double ratio)
	{
		return ((1 - ratio) * first + ratio * second);
	}
	
	public static <T> Stream<T> stream(Iterable<T> in) 
	{
	    return StreamSupport.stream(in.spliterator(), false);
	}

	public static <T> Stream<T> parallelStream(Iterable<T> in) 
	{
	    return StreamSupport.stream(in.spliterator(), true);
	}
	
	/**
	 * Sleeps for the specified time.
	 * If interrupted, returns true, else false.
	 * @param millis
	 * @return
	 */
	public static boolean sleep(long millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch(InterruptedException e)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if two circular objects collide at the given radius
	 * @param g1
	 * @param g2
	 * @param radius
	 * @return
	 */
	public static boolean circleCollide(Locatable g1, Locatable g2, double radius)
	{
		return distanceSquared(g1, g2) < radius * radius;
	}
	
	public static double angle(Locatable locatable, Locatable other)
	{
		double x = other.getX() - locatable.getX();
		double y = other.getY() - locatable.getY();
		return Math.atan2(y, x);
	}
	
	public static boolean isInRange(double value, double min, double max)
	{
		return (value >= min) && (value <= max);
	}
	
	public static double clamp(double value, double min, double max)
	{
		assert min <= max;
		if(value <= min) return min;
		if(value >= max) return max;
		return value;
	}
	
	
	
}
