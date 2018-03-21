import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import Common.Module;
import Common.Tilemap;

@SuppressWarnings("serial")
public class DisplayMap extends JPanel
{
	private final int GRID_SIZE = 50;
    private final Color[] COLOR_MAP = new Color[]
	{
		Color.WHITE,
		Color.BLACK
	};
	
    private final Tilemap mGridMap;
    private Module mDrawModule;
    
    public DisplayMap(Tilemap gridMap)
    {
    	mGridMap = gridMap;
    	
        int preferredWidth = mGridMap.SIZE.X * GRID_SIZE;
        int preferredHeight = mGridMap.SIZE.Y * GRID_SIZE;
        setPreferredSize(new Dimension(preferredWidth, preferredHeight));
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        
        // Clear the map
        g.clearRect(0, 0, getWidth(), getHeight());
        
        // Draw the grid
        int rectWidth = getWidth() / mGridMap.SIZE.X;
        int rectHeight = getHeight() / mGridMap.SIZE.Y;

        for (int column = 0; column < mGridMap.SIZE.X; column++)
        {
            for (int row = 0; row < mGridMap.SIZE.Y; row++)
            {
                int x = column * rectWidth;
                int y = row * rectHeight;
                
                Color terrainColor = COLOR_MAP[mGridMap.Map[column][row]];
                g.setColor(terrainColor);
                g.fillRect(x, y, rectWidth, rectHeight);
            }
        }
        
        // Draw the current module
        mDrawModule.paintComponent(g, rectWidth);
    }
    
    public void setDrawMod(Module drawModule)
    {
    	mDrawModule = drawModule;
    }
}
