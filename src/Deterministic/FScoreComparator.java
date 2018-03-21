package Deterministic;
import java.util.Comparator;

public class FScoreComparator implements Comparator<AStarNode>
{
	@Override
	public int compare(AStarNode arg0, AStarNode arg1)
	{
		return arg0.getFScore() - arg1.getFScore();
	}

}
