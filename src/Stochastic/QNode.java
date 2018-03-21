package Stochastic;

import java.util.HashMap;

import Common.Vector2;

public class QNode
{
	public HashMap<Vector2, QInfo> mActions;
	
	public Vector2 Location;
	
	public QNode(Vector2 location, HashMap<Vector2, QInfo> actions)
	{
		Location = location;
		mActions = actions;
	}
}
