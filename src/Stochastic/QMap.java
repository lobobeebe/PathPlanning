package Stochastic;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import Common.Tilemap;
import Common.Vector2;

public class QMap
{
	private Tilemap mMap;
	private QNode[][] mQMap;
	
	public QMap(Tilemap map, Vector2 endPoint)
	{
		mMap = map;
		
		mQMap = new QNode[map.SIZE.X][map.SIZE.Y];
		for (int col = 0; col < map.SIZE.X; ++col)
		{
			for (int row = 0; row < map.SIZE.Y; ++row)
			{
				Vector2 location = new Vector2(col, row);
				
				if (map.isOpen(col,  row))
				{
					HashMap<Vector2, QInfo> actions = new HashMap<>();
					
					if (location.equals(endPoint))
					{
						actions.put(endPoint, new QInfo(100, 0));
					}
					else
					{
						ArrayList<Vector2> neighbors = map.getOpenNeighbors(location);
						
						// Add a reward for each neighbor. If the neighbor is the end point, the reward is set to a high value
						for (Vector2 neighbor : neighbors)
						{
							int reward = 0;
							if (neighbor.equals(endPoint))
							{
								reward = 100;
							}
							
							actions.put(neighbor, new QInfo(reward, 0));
						}
					}

					QNode node = new QNode(location, actions);
					mQMap[col][row] = node;
				}
			}
		}
	}
	
	public QNode getNode(Vector2 location)
	{
		return mQMap[location.X][location.Y];
	}
	
	public void paintComponent(Graphics g, int rectWidth)
	{
		float maxQ = 1;
		
		for (int col = 0; col < mMap.SIZE.X; ++col)
		{
			for (int row = 0; row < mMap.SIZE.Y; ++row)
			{
				QNode node = mQMap[col][row];
				
				if (node != null)
				{
					for (Vector2 neighbor : node.mActions.keySet())	
		            {
		            	QInfo info = node.mActions.get(neighbor);
		            	if (info.Q > maxQ)
		            	{
		            		maxQ = info.Q;
		            	}
					}
				}
			}
		}
		
		for (int col = 0; col < mMap.SIZE.X; ++col)
		{
			for (int row = 0; row < mMap.SIZE.Y; ++row)
			{
				QNode node = mQMap[col][row];
				
				if (node != null)
				{
		            int x = (int) ((node.Location.X + .5) * rectWidth);
		            int y = (int) ((node.Location.Y + .5) * rectWidth);
		            
		            for (Vector2 neighbor : node.mActions.keySet())
		            {
		            	QInfo info = node.mActions.get(neighbor);
		            	
		            	int xNeighbor = (int) ((neighbor.X + .5) * rectWidth);
		            	int yNeighbor = (int) ((neighbor.Y + .5) * rectWidth);
		            	
			            g.setColor(new Color(75, 0, 130, (int)(info.Q * 255 / maxQ)));
			            
			            if (Math.abs(x - xNeighbor) > Math.abs(y - yNeighbor))
			            {
				            g.fillPolygon(
			            		new int[] {x, (x + xNeighbor) / 2, (x + xNeighbor) / 2},
			            		new int[] {y, y + (rectWidth / 2), y - (rectWidth / 2)},
			            		3);
			            }
			            else
			            {
				            g.fillPolygon(
			            		new int[] {x, x + (rectWidth / 2), x - (rectWidth / 2)},
			            		new int[] {y, (y + yNeighbor) / 2, (y + yNeighbor) / 2},
			            		3);
			            }
		            }
				}
			}
		}
	}
}
