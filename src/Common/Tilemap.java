package Common;
import java.util.ArrayList;

public class Tilemap
{
	public final Vector2 SIZE = new Vector2(10, 10);
    
    public int[][] Map =
	{
    	{1, 0, 0, 0, 0, 0, 0, 1, 0, 0},
    	{1, 0, 0, 1, 1, 1, 1, 1, 0, 0},
    	{1, 0, 1, 1, 1, 0, 0, 0, 0, 1},
    	{1, 0, 1, 1, 1, 1, 0, 1, 0, 1},
    	{0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
    	{0, 1, 0, 0, 0, 0, 0, 1, 1, 0},
    	{0, 1, 0, 1, 1, 1, 0, 0, 0, 0},
    	{0, 1, 0, 1, 0, 0, 0, 0, 0, 0},
    	{0, 0, 0, 0, 1, 1, 0, 1, 1, 1},
    	{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    public boolean isInBounds(int x, int y)
    {
    	return (x >= 0 && x < SIZE.X) && (y >= 0 && y < SIZE.Y);
    }

    public boolean isOpen(int x, int y)
    {
    	return isInBounds(x, y) && (Map[x][y] == 0);
    }
    
    public ArrayList<Vector2> getNeighors(Vector2 node)
    {
		ArrayList<Vector2> neighborList = new ArrayList<>();

		if (isInBounds(node.X + 1, node.Y))
		{
			neighborList.add(new Vector2(node.X + 1, node.Y));
		}
		
		if (isInBounds(node.X - 1, node.Y))
		{
			neighborList.add(new Vector2(node.X - 1, node.Y));
		}
		
		if (isInBounds(node.X, node.Y + 1))
		{
			neighborList.add(new Vector2(node.X, node.Y + 1));
		}
		
		if (isInBounds(node.X, node.Y - 1))
		{
			neighborList.add(new Vector2(node.X, node.Y - 1));
		}
		
		return neighborList;
    }
    
    public ArrayList<Vector2> getOpenNeighbors(Vector2 node)
    {
		ArrayList<Vector2> neighborList = new ArrayList<>();
		
		if (isOpen(node.X + 1, node.Y))
		{
			neighborList.add(new Vector2(node.X + 1, node.Y));
		}
		
		if (isOpen(node.X - 1, node.Y))
		{
			neighborList.add(new Vector2(node.X - 1, node.Y));
		}
		
		if (isOpen(node.X, node.Y + 1))
		{
			neighborList.add(new Vector2(node.X, node.Y + 1));
		}
		
		if (isOpen(node.X, node.Y - 1))
		{
			neighborList.add(new Vector2(node.X, node.Y - 1));
		}
		
		return neighborList;
    }
}