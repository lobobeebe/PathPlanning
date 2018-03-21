package Stochastic;

public class QInfo
{
	public int Reward;
	public float Q;
	
	public QInfo()
	{
		this(0, 0);
	}
	
	public QInfo(int reward, float q)
	{
		Reward = reward;
		Q = q;
	}
}
