package engine;


/**Called by objects when a specific event happens.
 * Effectively 'listens' for this by going over each relevant object at each point in time, and can be copied**/
public interface Listener<T>
{
	public void onEvent(T event, WorldHandle h);
	
	public boolean active();
}
