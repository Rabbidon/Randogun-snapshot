package bounds;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Supplier;

public class CollisionGrid<T> implements Iterable<T>
{
	private T[][] grid;
	
	private double height, width;
	private double xScale, yScale;

	/**Divides the game area into a grid of cells, allowing us to only check objects occupying the same cels for collision. Speeds up collision detection**/
	
	@SuppressWarnings("unchecked")
	/**Initialises the collision grid**/
	public CollisionGrid(int dim, double width, double height, Supplier<T> init)
	{
		this.height = height;
		this.width = width;
		xScale = 1.0 / width;
		yScale = 1.0 / height;
		grid = (T[][]) new Object[dim][dim];
		for(T[] r : grid)
			Arrays.setAll(r, i -> init.get());
		
	}

	/**Returns object at grid position**/
	public T atIndex(int x, int y)
	{
		return grid[x][y];
	}
	/**Assigns the object t to a place on the collision grid**/
	public void setIndex(int x, int y, T t)
	{
		grid[x][y] = t;
	}
	
	public T at(double x, double y)
	{
		return grid[(int)(xScale * x)][(int)(yScale * y)];
	}
	
	public int length()
	{
		return grid.length;
	}
	
	public int toXIndex(double x)
	{
		return (int)(xScale * x);
	}
	
	public int toYIndex(double y)
	{
		return (int)(yScale * y);
	}

	

	@Override
	public Iterator<T> iterator()
	{
		return Arrays.stream(grid).flatMap(Arrays::stream).iterator();
	}



	
	
}
