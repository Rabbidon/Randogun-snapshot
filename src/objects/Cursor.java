package objects;



import bounds.Bounds;
import engine.WorldHandle;
import fxcore.MainGame;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import player.Player;
import player.ReadablePlayer;

public class Cursor extends GameObject
{
	public static final int radius = 10;
	public static final Color color = Player.color;
	
	public Cursor(double x, double y)
	{
		super(x, y, Faction.Neutral);
	}
	
	public Cursor()
	{
		this(0, 0);
	}

	@Override
	public void update(double dt, WorldHandle h)
	{
		ReadablePlayer player = Player.THE;
		x = MainGame.getMouseTracker().gameX(player);
		y = MainGame.getMouseTracker().gameY(player);
	}

	@Override
	public void draw(GraphicsContext g)
	{
		g.setStroke(color);
		g.setLineWidth(5);
		g.strokeOval(x - radius, y - radius,  2 * radius, 2 * radius);
		g.setStroke(Color.WHITE);
		double radius = Cursor.radius - 5;
		g.strokeOval(x - radius, y - radius, 2 * radius, 2 * radius);
	}

	@Override
	public Bounds bounds()
	{
		return Bounds.NONE;
	}

	@Override
	public boolean isDead()
	{
		return false;
	}
	

}
