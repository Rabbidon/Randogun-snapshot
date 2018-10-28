package objects.entities;

import bounds.Bounds;
import bounds.Circle;
import engine.WorldHandle;
import fxcore.MainGame;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static fxcore.MainGame.rand;

/**
 * Entity designed to test Simulation class.
 * @author Edwin Fennell
 */

public class Talker extends Entity
{
    public static double radius = 20;
    public static final double startHealth = 50;
    public static final long talkInterval = 1;
    private double talkTime;
    private double travelTime;
    private double dashTime = 0.5;

    public Talker(double x, double y)
    {
        super(x, y);
        health = startHealth;
        maxHealth = startHealth;
        talkTime = 0;
        travelTime = 0;
        dx = 50;
        dy = 50;

    }

    private Bounds bounds = new Circle()
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

    @Override
    public void draw(GraphicsContext g)
    {
        g.setFill(Color.BLUE);
        g.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    @Override
    public void update(double dt, WorldHandle h)
    {
        super.update(dt,h);
        if (h.currentState().time > talkTime + talkInterval)
        {
            talkTime = h.currentState().time;
            System.out.println("Hi there");
        }
    }

    @Override
    public Bounds bounds()
    {
        return bounds;
    }
}

