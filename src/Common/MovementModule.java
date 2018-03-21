package Common;
import java.awt.Color;
import java.awt.Graphics;

public class MovementModule
{   
    private int mCharacterWidth = 35;
    private Vector2 mLocationTile = new Vector2(0, 6);
    
    public MovementModule()
    {
    }
    
	public void paintComponent(Graphics g, int tileSize)
	{
		int offset = (tileSize - mCharacterWidth) / 2;
		g.setColor(Color.CYAN);
		g.fillArc(mLocationTile.X * tileSize + offset, mLocationTile.Y * tileSize + offset,
				mCharacterWidth, mCharacterWidth, 0, 360);
	}

	public void setLocationTile(Vector2 locationTile)
	{
		mLocationTile = locationTile;
	}
	
	public Vector2 getLocationTile()
	{
		return mLocationTile;
	}

	public void step(Vector2 nextLocation)
	{
		mLocationTile = nextLocation;
	}
}
