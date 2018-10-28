package fxcore;

import javafx.scene.canvas.GraphicsContext;

/**
 * An object which can be drawn to a 2D screen, through a GraphicContext object.
 * @author Rajan
 *
 */
public interface Drawable
{
	/**
	 * Draws this object to the provided GraphicsContext.
	 * @param Graphics Context to draw this object to
	 */
	default public void draw(GraphicsContext g){};
}
