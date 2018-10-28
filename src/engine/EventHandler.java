package engine;

/**An interface through which game objects communicate. Messages can be sent to modify the behaviour of another game object**/
public interface EventHandler 
{
	/**Adds an object to the list that the handler checks for messages**/
	public void add(EngineObject obj);

	/**Adds an instruction to the handler, to be executed at the given time**/
	public void schedule(double time, Effect event);
	
	public default void addAll(Iterable<? extends EngineObject> gameObjects)
	{
		for(EngineObject obj : gameObjects)
		{
			add(obj);
		}
	}
	/**Sends a message to a target object**/
	void message(ObjectView target, int code, Object data);
	
}
