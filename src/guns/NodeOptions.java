package guns;

import bounds.Bounds;
import javafx.scene.paint.Color;

/**Distributions from which random values are picked**/
public interface NodeOptions
{
	Node pickNode(DataType... provided);
	double pickDamage();
	double pickDuration();
	double pickTime();
	Color pickColor();
	double pickSpeed();
	double pickSize();
	int pickCount();
	double pickAccel();
	Bounds pickBounds();
	double pickAngle();
}
