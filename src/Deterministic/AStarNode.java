package Deterministic;
import Common.Vector2;

public class AStarNode implements Comparable<AStarNode>
{
	public enum NodeState
	{
		Idle,
		Open,
		Closed
	}
	
	public Vector2 Location;
	public AStarNode FromNode;
	
	public int Gaussian;
	public int Heuristic;
	
	public NodeState State;
	
	public AStarNode(Vector2 location)
	{
		Location = location;
		Gaussian = Integer.MAX_VALUE;
		Heuristic = Integer.MAX_VALUE;
		State = NodeState.Idle;
	}
	
	public int getFScore()
	{
		return Gaussian + Heuristic;
	}

	@Override
	public int compareTo(AStarNode other)
	{
		return getFScore() - other.getFScore();
	}
	
	@Override
	public boolean equals(Object other)
	{
		if (other == this)
			return true;
		
		if (!(other instanceof AStarNode))
			return false;
		
		AStarNode otherNode = (AStarNode) other;
		
		return Location.equals(otherNode.Location);
	}
}
