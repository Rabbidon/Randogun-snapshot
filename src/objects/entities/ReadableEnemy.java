package objects.entities;

import javafx.scene.canvas.GraphicsContext;

public interface ReadableEnemy extends ReadableEntity
{
	@Override
	public void draw(GraphicsContext g);
	
	
}
