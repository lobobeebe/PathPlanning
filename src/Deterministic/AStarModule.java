package Deterministic;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;

import Common.PathModule;
import Common.Tilemap;
import Common.Vector2;

public class AStarModule extends PathModule
{	
	private final Color END_COLOR = Color.RED;
	private final Color CLOSEST_COLOR = Color.PINK;
	private final Color OPEN_COLOR = Color.GREEN;
	private final Color CLOSED_COLOR = Color.GRAY;
	
	private ArrayList<AStarNode> mOpenList;
	private ArrayList<AStarNode> mClosedList;
	
	private ArrayList<Vector2> mPath;
	
	private FScoreComparator mComparator;
	
	public AStarModule(Tilemap map)
	{
		super(map);

		mOpenList = new ArrayList<AStarNode>();
		mClosedList = new ArrayList<AStarNode>();
		
		mComparator = new FScoreComparator();
	}
	
	public void reset()
	{
		mOpenList = new ArrayList<AStarNode>();
		mClosedList = new ArrayList<AStarNode>();
		
		AStarNode startNode = new AStarNode(mStartPoint);
		startNode.Gaussian = 0;
		startNode.Heuristic = getHeuristicDistance(mStartPoint, mEndPoint);

		mOpenList.add(startNode);
	}
	
	public void setStartPoint(Vector2 startPoint)
	{
		super.setStartPoint(startPoint);
		reset();
	}
	
	public void setEndPoint(Vector2 endPoint)
	{
		super.setEndPoint(endPoint);
		reset();
	}
	
	public FinishType Run()
	{
		FinishType finish = Step(); 
		while (finish == AStarModule.FinishType.Unfinished)
		{
			finish = Step();
		}
		
		return finish;
	}
	
	public FinishType Step()
	{
		if (!mOpenList.isEmpty())
		{
			// The List is in descending order. The first has the lowest F Score.
			AStarNode currentNode = mOpenList.get(0);
			
			// Check if the current node is the end node
			if (currentNode.Location.equals(mEndPoint))
			{
				mPath = getPathBackToStart();
				// When the end node is found, return a path;
				return FinishType.Complete;
			}
			
			// Remove the current node from the open list of nodes to process and add to the closed list since
			// we have already looked at this node.
			moveFromOpenToClosedList(currentNode);
			
			// Get all of the empty neighbors that have not yet been processed
			ArrayList<Vector2> neighborLocations = mMap.getOpenNeighbors(currentNode.Location);
			
			for (Vector2 neighborLocation : neighborLocations)
			{ 
				// Do nothing if in the Closed List, this location has already been processed.
				if (!(getNodeMatchingLocation(mClosedList, neighborLocation) != null))
				{	
					// Get the neighbor from the open list if it exists
					AStarNode neighborNode = getNodeMatchingLocation(mOpenList, neighborLocation);
					
					// If the neighbor is not in the Open List, create it and add it to the open list
					if (neighborNode == null)
					{
						neighborNode = new AStarNode(neighborLocation);
						mOpenList.add(neighborNode);
					}
					
					// Calculate the potential new Gaussian score for the neighbor based on the current node
					int tempGaussian = currentNode.Gaussian + 1; // Always add one for Gaussian score since this is 4 direction only
					if (tempGaussian < neighborNode.Gaussian)
					{
						neighborNode.FromNode = currentNode;
						neighborNode.Gaussian = tempGaussian;
						neighborNode.Heuristic = getHeuristicDistance(neighborLocation, mEndPoint);
					}
				}
			}
			
			// Sort the list by their F Score to ensure the first is always the best option.
			Collections.sort(mOpenList, mComparator);
		}
		
		if (mOpenList.isEmpty())
			return FinishType.Impossible;
		
		return FinishType.Unfinished;
	}

	private ArrayList<Vector2> getPathBackToStart()
	{
		ArrayList<Vector2> path = new ArrayList<>();
		
		AStarNode pathNode = null;
        if (mOpenList.size() > 0)
        {
        	pathNode = mOpenList.get(0);
        }
		
		while(pathNode != null && pathNode.FromNode != null)
		{
			path.add(pathNode.Location);
			pathNode = pathNode.FromNode;
		}
		
		return path;
	}
	
	@Override
	public Vector2 GetNextLocation(Vector2 currentLocation)
	{		
		Vector2 nextLocation = null;
		
		if (mPath != null)
		{
			if (mPath.size() > 0)	
			{
				nextLocation = mPath.remove(mPath.size() - 1);
			}
			else
			{
				nextLocation = currentLocation;
			}
		}
		
		return nextLocation;
	}
	
	public void paintComponent(Graphics g, int rectWidth)
	{        
		// Paint all closed list items
		for (int i = 0; i < mClosedList.size(); ++i)
		{
			AStarNode node = mClosedList.get(i);
			
            int x = node.Location.X * rectWidth;
            int y = node.Location.Y * rectWidth;
            
            g.setColor(CLOSED_COLOR);
            g.fillRect(x, y, rectWidth, rectWidth);
		}
		
		// Paint all open list items
		for (int i = 0; i < mOpenList.size(); ++i)
		{
			AStarNode node = mOpenList.get(i);
			
            int x = node.Location.X * rectWidth;
            int y = node.Location.Y * rectWidth;
            
            g.setColor(OPEN_COLOR);
            g.fillRect(x, y, rectWidth, rectWidth);
		}
        
		// Paint the path items
        AStarNode pathNode = null;
        if (mOpenList.size() > 0)
        {
        	pathNode = mOpenList.get(0);
        }
        
        while (pathNode != null)
        {
            g.setColor(CLOSEST_COLOR);
            g.fillRect(pathNode.Location.X * rectWidth, pathNode.Location.Y * rectWidth, rectWidth, rectWidth);
            
            pathNode = pathNode.FromNode;
        }
        
		// Paint the start item        
        g.setColor(END_COLOR);
        g.fillRect(mEndPoint.X * rectWidth, mEndPoint.Y * rectWidth, rectWidth, rectWidth);
	}
	
	public void addToOpenList(AStarNode node)
	{
		node.State = AStarNode.NodeState.Open;
		mOpenList.add(node);
	}
	
	public void moveFromOpenToClosedList(AStarNode node)
	{
		mClosedList.add(node);
		mOpenList.remove(0);
		node.State = AStarNode.NodeState.Closed;
	}
	
	public int getHeuristicDistance(Vector2 start, Vector2 end)
	{
		return start.getManhattanDistance(end);
	}
	
	public AStarNode getNodeMatchingLocation(ArrayList<AStarNode> nodeList, Vector2 location)
	{
		for (AStarNode node : nodeList)
			if (node.Location.equals(location))
				return node;
		
		return null;
	}
	
}
