package bounds;

import static util.Util.*;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

import javafx.scene.canvas.GraphicsContext;
import util.Util;

@SuppressWarnings("unchecked")
public abstract class Bounds 
{
	/**Type of collision object - required for collision classes**/
	protected int type;
	/**Declaration for empty bounds**/
	public static final Bounds NONE = new Bounds()
	{
		{
			type = nextType++;
		}
		@Override
		public boolean contains(double x, double y)
		{
			return false;
		}

		@Override
		public <T> void gridIntersect(CollisionGrid<T> g, Consumer<T> action)
		{
			
		}
	};
	/**Painting stuff**/
	public void fill(GraphicsContext g)
	{
		
	}
	
	public void stroke(GraphicsContext g)
	{
		
	}

    /**Defines intersection and containment tables**/
	protected static int nextType = 0;
	protected static BiPredicate<Bounds, Bounds>[][] intersectTable;
	protected static BiPredicate<Bounds, Bounds>[][] containTable;

    /** Sets the values in the intersection table**/
	private static void setInter(int i, int j, BiPredicate<Bounds, Bounds> b)
	{
		intersectTable[i][j] = intersectTable[j][i] = b;
	}

    /**Sets up lookup tables for collision classes**/
	public static void initTables()
	{
		int numTypes = 4;
		intersectTable = new BiPredicate[numTypes][numTypes];
		containTable = new BiPredicate[numTypes][numTypes];

        /**Initialses all table values to false**/
		BiPredicate<Bounds, Bounds> alwaysFalse = (a, b) -> false;
		for(int i = 0; i < numTypes; i++) {
            for (int j = 0; j < numTypes; j++) {
                intersectTable[i][j] = containTable[i][j] = alwaysFalse;
            }
        }

        /**Describes whether a circle is contained within a rectangle**/
        setInter(Circle.TYPE, Circle.TYPE,  (b1, b2) ->
		{
			Circle c1 = cast(b1), c2 = cast(b2);
			return Util.distanceSquared(c1, c2) <= Util.sqr(c1.radius() + c2.radius());
		});

        /**Describes whether a circle is contained within a rectangle**/
		containTable[Rectangle.TYPE][Circle.TYPE] = (br, bc) ->
		{
			Circle c = cast(bc);
			Rectangle r = cast(br);
			return
					!((c.centerX() + c.radius() > r.getX() + r.width()) |
					(c.centerX() - c.radius() < r.getX()) |
					(c.centerY() + c.radius() > r.getY() + r.height()) |
					(c.centerY() - c.radius() < r.getY()));
		};
		
	}

    /**Tests if two sets of bounds intersect by looking in the intersection table**/
	public static boolean intersect(Bounds a, Bounds b)
	{
		return intersectTable[a.type][b.type].test(a, b);
	}

	public abstract boolean contains(double x, double y);

    /**General method that determines which cells in the collision grid intersect the bounds.
     * In the actual implementation, objects are stored as integers in the grid and read from a list
     * instead of storing the objects themselves**/
	public abstract <T> void gridIntersect(CollisionGrid<T> g, Consumer<T> action);

    /** Tests if one set of bounds contains another**/
	public final boolean contains(Bounds b)
	{
		return containTable[this.type][b.type].test(this, b);
	}

	
	
	
	
	
}
