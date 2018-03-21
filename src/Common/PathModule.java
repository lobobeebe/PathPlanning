package Common;

import java.awt.Graphics;

public abstract class PathModule
{
	public enum Type
	{
		DETERMINISTIC,
		STOCHASTIC
	};

	public enum FinishType
	{
		Unfinished,
		Complete,
		Impossible
	}
	
	protected Tilemap mMap;
    
	protected Vector2 mStartPoint = new Vector2 (0, 9);
	protected Vector2 mEndPoint = new Vector2 (0, 6);
	
	public PathModule(Tilemap map)
	{
		mMap = map;
	}
	
	public void setStartPoint(Vector2 startPoint)
	{
		mStartPoint = startPoint;
	}
	
	public void setEndPoint(Vector2 endPoint)
	{
		mEndPoint = endPoint;
	}

	public abstract FinishType Step();
	public abstract FinishType Run();
	public abstract Vector2 GetNextLocation(Vector2 currentLocation);
	
	public abstract void paintComponent(Graphics g, int tileSize);
}
