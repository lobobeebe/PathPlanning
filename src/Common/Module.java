package Common;

import java.awt.Graphics;

import Common.PathModule.FinishType;

public class Module
{
	protected Vector2 mStartPoint = new Vector2 (0, 9);
	protected Vector2 mEndPoint = new Vector2 (0, 6);
	
	protected PathModule mPathModule;
	protected MovementModule mMovementModule;
	
	public Module(PathModule module)
	{
		mPathModule = module;
		mMovementModule = new MovementModule();
	}
	
	public void setStartPoint(Vector2 startPoint)
	{
		mStartPoint = startPoint;
		mPathModule.setStartPoint(startPoint);
		mMovementModule.setLocationTile(mStartPoint);
	}
	
	public void setEndPoint(Vector2 endPoint)
	{
		mEndPoint = endPoint;
		mPathModule.setEndPoint(endPoint);
		mMovementModule.setLocationTile(mStartPoint);
	}
	
	public void Run()
	{
		mPathModule.Run();
	}
	
	public void Step()
	{
		mPathModule.Step();
	}
	
	public FinishType moveRun()
	{
		FinishType finish = moveStep();
		while (finish == FinishType.Unfinished)
		{
			finish = moveStep();
		}
		
		return finish;
	}
	
	public FinishType moveStep()
	{
		FinishType finish = FinishType.Unfinished;
		
		Vector2 nextLocation = mPathModule.GetNextLocation(mMovementModule.getLocationTile());
		
		if (nextLocation == null)
		{
			finish = FinishType.Impossible;
		}
		else
		{
			mMovementModule.setLocationTile(nextLocation);
			
			if (nextLocation.equals(mEndPoint))
			{
				finish = FinishType.Complete;
			}
		}
		
		return finish;
	}
	
	public void paintComponent(Graphics g, int tileSize)
	{
		mPathModule.paintComponent(g, tileSize);
		mMovementModule.paintComponent(g, tileSize);
	}
}
