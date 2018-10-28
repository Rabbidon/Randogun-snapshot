package simulation;

import bounds.Rectangle;
import engine.*;
import fxcore.Renderer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import objects.Locatable;
import objects.entities.Talker;
import player.Player;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.List;
import java.util.concurrent.locks.LockSupport;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Class that allows simulation starting from a given state. Replay funcionality allows recording and display of simulations.
 * @author Edwin Fennell
 */

import static objects.Locatable.of;
import static util.Util.exec;

public class Simulation extends Application
{
    static long time;
    static State currentFrame;
    Stage stage;

    private static Application app;

    @Override
    public void start(Stage s)
    {
        try {
            this.stage = s;

            GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            int width = gd.getDisplayMode().getWidth();
            int height = gd.getDisplayMode().getHeight();
            Renderer renderer = new Renderer(width, height);
            Group root = new Group();
            root.getChildren().add(renderer.canvas);
            Scene scene = new Scene(root);


            stage.setScene(scene);
            scene.setCursor(Cursor.NONE);
            LinkedList<State> frames = runSimulation(new State(Arrays.asList(new Talker(1500,1500)),3000,3000, Rectangle.of(0,0,3000,3000),0),new Vector<Controller>(),new Vector<Listener<EngineObject>>(),new LinkedList<State>());
            stage.show();
            runGraphics(renderer,frames);
        }
        catch(Throwable t)
        {
            t.printStackTrace();
            System.exit(-1);
        }
    }
    private static LinkedList<State> runSimulation(State simState, List<Controller> controllers, List<Listener<EngineObject>> listeners, LinkedList<State> frames)
    {
        Engine engine = new Engine(simState);
        engine.handler.regGlobalDeathListener(new Printer());
        runEngine(engine,300, frames);
        return frames;
    }

    private static void runEngine(Engine engine,int UPS, List<State> frames)
    {
            //Thread.setDefaultUncaughtExceptionHandler((t, e) -> e.printStackTrace(System.err));
            long frameNanoTime = (1_000_000_000L / UPS);
                for(long time = System.nanoTime();; time = System.nanoTime())
                {
                    try
                    {
                        engine.update(1.0 / UPS);
                        frames.add(engine.getState());
                    }
                    catch(Throwable t)
                    {
                        t.printStackTrace();
                        System.exit(-1);
                    }

                    while(System.nanoTime() < time + frameNanoTime)
                        LockSupport.parkNanos((time + frameNanoTime) - System.nanoTime());
                    if (engine.getTime()>10)
                    {
                        return;
                    }
                }
    }

    public void runGraphics(Renderer renderer, LinkedList<State> frames)
    {
        currentFrame = frames.poll();
        renderer.render(currentFrame,of(1500,1500));
        time = System.nanoTime();
        new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                if (1000000000*currentFrame.time < (System.nanoTime() - time))
                {
                    while((1000000000*currentFrame.time < System.nanoTime() - time) & (frames.peek() != null))
                    {
                        currentFrame = frames.pop();
                        System.out.println(currentFrame.time);
                    }
                    renderer.render(currentFrame,of(1500,1500));
                }
            }

        }.start();
    }

    @Override
    public void stop()
    {
        System.exit(0);
    }

    public static void main(String[] args)
    {
        launch();
    }

    @Override
    public void init()
    {
        Platform.setImplicitExit(true);
        app = this;
    }

}
