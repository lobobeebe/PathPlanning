package Common;
public class Vector2
{
	public int X;
	public int Y;

	public Vector2()
	{
		this(0, 0);
	}
	
	public Vector2(int x, int y)
	{
		X = x;
		Y = y;
	}
	
	public int getManhattanDistance(Vector2 endPoint)
	{
		return Math.abs(endPoint.X - X) + Math.abs(endPoint.Y - Y);
	}
	
	public int getRoundedDistance(Vector2 endPoint)
	{
		return (int) Math.round(Math.sqrt(Math.pow((endPoint.X - X), 2) + Math.pow((endPoint.Y - Y), 2)));
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other == this)
			return true;
		
		if (!(other instanceof Vector2))
			return false;
		
		Vector2 otherVector = (Vector2) other;
		
		return X == otherVector.X && Y == otherVector.Y;
	}
}
