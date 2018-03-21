package Stochastic;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.Set;

import Common.PathModule;
import Common.Tilemap;
import Common.Vector2;

public class QLearningModule extends PathModule
{	
	private static final int MAX_EPISODES = 100;
	
	// Discount Factor
	private static final float GAMMA = 0.9f;
	
	// Learning Rate
	private static final float ALPHA = 0.5f;
	
	// Epsilon-Greedy Chance of using a random action during training
	private static final float EPSILON = .8f;
	
	private static final float MAX_ACTIONS_PER_EPISODE = 500;
	
	private QMap mQMap;
	
	private int mNumEpisodes;
	
	public QLearningModule(Tilemap map)
	{
		super(map);
		
		mNumEpisodes = 0;
		
		mQMap = new QMap(mMap, mEndPoint);
	}
	
	public void setStartPoint(Vector2 startPoint)
	{
		super.setStartPoint(startPoint);
	}
	
	public void setEndPoint(Vector2 endPoint)
	{
		super.setEndPoint(endPoint);
		reset();
	}
	
	public void reset()
	{
		mQMap = new QMap(mMap, mEndPoint);
		mNumEpisodes = 0;
	}
	
	public FinishType Run()
	{
		while (mNumEpisodes < MAX_EPISODES)
		{
			Step();
			mNumEpisodes++;
		}
		
		return FinishType.Complete;
	}
	
	public FinishType Step()
	{
		// Choose a random initial location
		Vector2 randomLocation = new Vector2((int)(Math.random() * mMap.SIZE.X), (int)(Math.random() * mMap.SIZE.Y));
		while (!mMap.isOpen(randomLocation.X, randomLocation.Y))
		{
			randomLocation = new Vector2((int)(Math.random() * mMap.SIZE.X), (int)(Math.random() * mMap.SIZE.Y));
		}
		
		QNode currentNode = mQMap.getNode(randomLocation);
		
		int actionCounter = 0;
		
		// Until the goal is reached:
		// - Select a random possible action, a, from the current location, l, to arrive in new location, l'
		// - Get maximum Q value for location l' based on all possible actions in l'
		// - Compute and save Q(l, a) = (1 - Alpha) * Q(l, a) + Alpha * [R(l, a) + Gamma * MaxOfActions(Q(l', action))]
		// - Set the next state as the current state
		while (!currentNode.Location.equals(mEndPoint) && actionCounter < MAX_ACTIONS_PER_EPISODE)
		{
			HashMap<Vector2, QInfo> currentActions = currentNode.mActions;
			// Select random possible action, a, to get to location l'
			
			double rollForAction = Math.random();
			Vector2 action;
			if (rollForAction < EPSILON)
			{
				action = getRandomAction(currentActions);
			}
			else
			{
				action = getMaximumQAction(currentNode);
				
				// If the max Q is 0, just use a random action so we don't use the same action over and over again.
				if (currentNode.mActions.get(action).Q == 0)
				{
					action = getRandomAction(currentActions);
				}
			}
			
			// Get maximum Q value from actions in l'
			QNode lPrime = mQMap.getNode(action);
			float maxQ = getMaximumQ(lPrime);
			
			if (maxQ == 0 && lPrime.mActions.size() == 1)
			{
				Vector2 trappedAction = getMaximumQAction(lPrime);
				lPrime.mActions.get(trappedAction).Q = 255;
			}
			
			// Compute Q
			currentNode.mActions.get(action).Q = currentNode.mActions.get(action).Q * (1 - ALPHA) + 
					ALPHA * (currentActions.get(action).Reward + GAMMA * maxQ);
			
			// Set the current state to the random action state
			currentNode = lPrime;
			
			// Increment action counter to ensure we fail at a reasonable time
			++actionCounter;
		}
		
		return FinishType.Unfinished;
	}
	
	private float getMaximumQ(QNode node)
	{
		float maxQ = 0;
		Vector2 maxQAction = getMaximumQAction(node);
		
		if(maxQAction != null)
		{
			maxQ = node.mActions.get(maxQAction).Q;
		}
		
		return maxQ;
	}
	
	private Vector2 getMaximumQAction(QNode node)
	{
		Vector2 maxQAction = null;
		float maxQ = -1;
		for (Vector2 action : node.mActions.keySet())
		{
			QInfo info = node.mActions.get(action);
			if (info.Q > maxQ)
			{
				maxQ = info.Q;
				maxQAction = action;
			}
		}
		
		return maxQAction;
	}
	
	private Vector2 getRandomAction(HashMap<Vector2, QInfo> actions)
	{
		Set<Vector2> actionSet = actions.keySet();
		int randomIndex = (int)(Math.random() * actionSet.size());
		int i = 0;
		for (Vector2 action : actionSet)
		{
			if (i == randomIndex)
				return action;
			
			++i;
		}
		
		return null;
	}
	
	@Override
	public Vector2 GetNextLocation(Vector2 currentLocation)
	{
		Vector2 nextLocation = currentLocation;
		
		QNode currentNode = mQMap.getNode(currentLocation);
		Vector2 maxQLocation = getMaximumQAction(currentNode);

		double random = Math.random();

		if (random < .6)
		{
			// 60% Chance of choosing the best action
			// Use the action with the highest Q Value
			nextLocation = maxQLocation;
		}
		else if (random < .9)
		{
			// 30% Chance of taking a random action that is not the best action
			HashMap<Vector2, QInfo> otherActions = new HashMap<>(currentNode.mActions);
			otherActions.remove(maxQLocation);
			
			if (!otherActions.isEmpty())
			{
				nextLocation = getRandomAction(otherActions);
			}
		}
		// 10% Chance of staying in the same place
		
		return nextLocation;
	}

	public void paintComponent(Graphics g, int rectWidth)
	{
		mQMap.paintComponent(g, rectWidth);
		
		// Paint the end item        
        g.setColor(new Color(75, 0, 130));
        g.fillRect(mEndPoint.X * rectWidth, mEndPoint.Y * rectWidth, rectWidth, rectWidth);
	}
}
