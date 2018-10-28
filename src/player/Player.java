package player;


import bounds.Bounds;
import bounds.Circle;
import engine.ObjectView;
import engine.WorldHandle;
import fxcore.KeyTracker;
import fxcore.MainGame;
import fxcore.MouseTracker;
import guns.Gun;
import guns.GunTree;
import guns.NodeOptions;
import guns.RandomNodeOptions;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import objects.Faction;
import objects.Locatable;
import objects.entities.Entity;
import util.Vector;


/**
 * Represents the player of the game. 
 * @author Rajan Troll
 *
 */
public class Player extends Entity implements ReadablePlayer
{
	public static double resources = 0;
	public static double radius = 30;
	public static Color color = Color.rgb(170, 0, 170);
	public static volatile boolean laserING = false;
	public static Player THE;
	
	
	
	private long fireTime;
	private Action action;	
	
	private Circle bounds = new Circle()
	{
		@Override
		public double centerX()
		{
			return x;
		}

		@Override
		public double centerY()
		{
			return y;
		}
		
		@Override
		public double radius()
		{
			return radius;
		}
	};
	/**
	 * Creates a new player at the specified coordinates
	 * @param x
	 * @param y
	 */
	public Player(double x, double y) 
	{
		super(x, y);
		faction = Faction.Player;
		maxHealth = 10;
		//maxHealth = Double.POSITIVE_INFINITY;
		health = maxHealth;
			
		
		THE = this;
	}
	/**
	 * Creates a new player at (0, 0)
	 */
	public Player()
	{
		this(0, 0);
	}
	/**
	 * Specifies what action the player will take next tick
	 * @param a
	 */
	public void setAction(Action a)
	{
		action = a;
	}
	{

	}
	NodeOptions gunDistribution = new RandomNodeOptions();
	
	Gun gun = new GunTree(gunDistribution.pickNode());
	//Gun puddleGun = new GunTree(AOEDistribution.pickTargetedEvent());
	
	@Override
	public void update(double dt, WorldHandle h) 
	{


		if(!isDead()) 
		{
			heal(2 * dt);
			
			//heal(Double.POSITIVE_INFINITY);
			KeyTracker k = MainGame.getKeyTracker();
			MouseTracker m = MainGame.getMouseTracker();

			if(m.isPressed(MouseButton.PRIMARY))
			{
				action = new Action(k.isKeyPressed(KeyCode.W), k.isKeyPressed(KeyCode.S), 
						k.isKeyPressed(KeyCode.A), k.isKeyPressed(KeyCode.D), m.gameX(this), m.gameY(this));
			}
			else
			{
				action = new Action(k.isKeyPressed(KeyCode.W), k.isKeyPressed(KeyCode.S), 
						k.isKeyPressed(KeyCode.A), k.isKeyPressed(KeyCode.D));
			}

			executeAction(h);
			Locatable target = new Vector(m.gameX(this), m.gameY(this));
			/*
		Bullet b = new Bullet.Builder()
					.from(this)
					.setDamage(5)
					.setTarget(new Vector(m.gameX(this), m.gameY(this)))
					.setColor(color)
					.setSpeed(1200)
					.build();
		h.add(b);
			 */
			//if(k.isKeyPressed(KeyCode.SPACE))
			//{
				//if(puddleGun.isReady(d))
				//{
					//puddleGun.fire(d, this, target, h);
				//}
			//}

			//*/
			if(gun.isReady(h))
				gun.fire(this, target, h);
		}


		super.update(dt, h);
		dx *= .97;
		dy *= .97;

		bounds = new Circle()
		{
			@Override
			public double centerX()
			{
				return x;
			}

			@Override
			public double centerY()
			{
				return y;
			}

			@Override
			public double radius()
			{
				return radius;
			}
		};

		THE = (Player) h.track(THE);
	}

	private void executeAction(WorldHandle state)
	{
		double moveSpeed = 300;
		if(action.isDown() || action.isLeft() || action.isRight() || action.isUp())
		{
			dx = 0;
			dy = 0;
		}
		if(action.isDown()) dy++;
		if(action.isUp()) dy--;
		if(action.isRight()) dx++;
		if(action.isLeft()) dx--;
		if(action.isDown() || action.isLeft() || action.isRight() || action.isUp())
		{
			double hypot = Math.hypot(dx, dy);
			if(hypot > 0)
			{
				dx /= hypot;
				dy /= hypot;
				dx *= moveSpeed;
				dy *= moveSpeed;
			}
		}
		
		
		
	}
	
	@Override
	public void renderHUD(GraphicsContext g)
	{
		double healthBar = 300;
		g.setFill(Player.color);
		g.fillRect(0, 0, healthBar * health() /maxHealth(), 50);
		g.setStroke(Color.WHITE);
		g.strokeRect(0, 0, healthBar, 50);
		g.setFill(Color.YELLOW);
		g.setFont(Font.font("Verdana", 75));
		g.setFill(Color.YELLOW);
		
		
	}
	
	@Override
	public int collisionClass()
	{
		return 1;
	}
	

	

	
	public static class Action
	{
		private boolean up = false, down = false, left = false, right = false;
		private boolean shoot = false;
		private double targetX, targetY;
		
		public Action(boolean up, boolean down, boolean left, boolean right, double targetX, double targetY)
		{
			this(up ,down, left, right);
			this.targetX = targetX;
			this.targetY = targetY;
			shoot = true;
		}
		
		public Action(boolean up, boolean down, boolean left, boolean right) 
		{
			this.up = up;
			this.down = down;
			this.left = left;
			this.right = right;
		}

		public Action(){}
		
		public boolean isShooting()
		{
			return shoot;
		}
		
		public double targetX()
		{
			if(!shoot) throw new IllegalStateException("Action does not call for shooting: no target");
			return targetX;
		}
		
		public double targetY()
		{
			if(!shoot) throw new IllegalStateException("Action does not call for shooting: no target");
			return targetY;
		}

		public boolean isUp() 
		{
			return up;
		}

		public boolean isDown() 
		{
			return down;
		}

		public boolean isLeft() 
		{
			return left;
		}

		public boolean isRight() 
		{
			return right;
		}
	}

	@Override
	public long fireTime()
	{
		return fireTime;
	}

	@Override
	public void draw(GraphicsContext g)
	{
		g.setFill(Player.color);
		g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
		
	}

	@Override
	public Bounds bounds()
	{
		return bounds;
	}
	
	@Override
	public void damage(double damage)
	{
		super.damage(damage);
		if(!isDead()) MainGame.sleep((long)(damage * 40));
	}
	
	public void message(int code, Object data)
	{
		switch(code)
		{
		case 1:
			double delta = (Double) data;
			heal(delta);
			break;
		case 2:
			resources += (Integer) data;
			break;
		default:
			super.message(code, data);
			break;
		}
	}
	
	
	
}
