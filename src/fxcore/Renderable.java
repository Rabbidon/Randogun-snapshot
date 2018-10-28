package fxcore;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;

public interface Renderable extends Drawable
{

	default Effect specialEffects()
	{
		return null;
	}
	
	default void renderHUD(GraphicsContext g){};

}
