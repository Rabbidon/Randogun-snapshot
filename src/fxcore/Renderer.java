package fxcore;

import engine.State;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import objects.Locatable;
import player.Player;

public class Renderer 
{
	public final Canvas canvas;
	private final GraphicsContext g;
	public final double width, height;
	{
		
	}
	public Renderer(int width, int height)
	{
		canvas = new Canvas(width, height);
		g = canvas.getGraphicsContext2D();
		
		this.width  = width;
		this.height = height;
	}
	double FPS = 0;
	long lastFrame = System.nanoTime();
	double minFPS = 10000;
	
	public void render(State d, Locatable center)
	{
		long now = System.nanoTime();
		double thisFPS = 1_000_000_000.0 / (now - lastFrame);
		lastFrame = now;
		FPS = 0.95 * FPS + 0.05 * thisFPS;
		double mp = -10.0;
		minFPS = Math.pow((0.999 * Math.pow(minFPS, mp) + 0.001 * Math.pow(thisFPS, mp)), 1.0 / mp);
		g.save();
		
		//g.setGlobalAlpha(.95);
		//g.clearRect(0, 0, width, height);
		g.setFill(Color.ORANGE);
		g.fillRect(0, 0, width, height);
		scaleGraphics(d);
		//g.setFill(Color.BLACK);
		//g.setFill(new LinearGradient(0, 0, .1, .2, true, CycleMethod.REFLECT, new Stop(0, Color.BLACK), new Stop(.5, Color.SEAGREEN.darker()), new Stop(1, Color.DARKBLUE)));
		g.setFill(new RadialGradient(0, 0, .5, .5, .05, true, CycleMethod.REFLECT, new Stop(0, Color.BLACK), new Stop(1, Color.DARKMAGENTA)));
		//g.fillRect(0, 0, d.width, d.height);
		d.gameBounds.fill(g);
		drawObjects(d);
		
		//drawPlayer(d);
		g.restore();
		displayHUD(d);
	
		
	}
	
	private void drawObjects(State d)
	{
		for(Drawable obj : d.objects)
		{
			obj.draw(g);
		}
	}

	private void displayHUD(State d)
	{
		g.fillText(Double.toString(minFPS), width - 300, 100);
		g.fillText(Integer.toString(d.objects.size()), width - 300, 200);
		for(Renderable r : d.objects)
		{
			r.renderHUD(g);
		}
	}
	
	private void scaleGraphics(State d)
	{
		Locatable center = Locatable.of((d.height)/2,(d.width)/2);
		double playerX = center.getX(), playerY = center.getY();
		g.translate(width / 2, height / 2);
		//g.scale(width / d.width, height / d.height);
		g.scale(.8, .8);
		g.translate(-playerX , -playerY);
	}
	
	
	
}
