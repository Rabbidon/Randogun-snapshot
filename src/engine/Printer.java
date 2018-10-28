package engine;

import javafx.scene.paint.Color;
import objects.HealthPickup;
import objects.ResourcePickup;

public class Printer implements Listener<ObjectView>
{
    public Printer()
    {

    }

    public void onEvent(ObjectView entity, WorldHandle h)
    {
        System.out.println("An entity has died");
    }

    public boolean active(){return true;}
}
