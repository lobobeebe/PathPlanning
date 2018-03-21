import javax.swing.*;
import javax.swing.border.TitledBorder;

import Common.Module;
import Common.Tilemap;
import Common.Vector2;
import Deterministic.AStarModule;
import Stochastic.QLearningModule;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class RobotLearningDemo
{    	
	private static final String[] ENVIRONMENT_NAMES = new String[] {"Deterministic", "Stochastic"};
	
	private static final int START_X = 0;
	private static final int START_Y = 9;
	private static final int END_X = 9;
	private static final int END_Y = 0;

	private static final Integer[] ZERO_TO_NINE = new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    JComboBox<Integer> mStartXCombo = new JComboBox<>(ZERO_TO_NINE);
    JComboBox<Integer> mStartYCombo = new JComboBox<>(ZERO_TO_NINE);
    JComboBox<Integer> mEndXCombo = new JComboBox<>(ZERO_TO_NINE);
    JComboBox<Integer> mEndYCombo = new JComboBox<>(ZERO_TO_NINE);

    private final Tilemap mGridMap = new Tilemap();
    
	private DisplayMap mMap = new DisplayMap(mGridMap);	
	private Module mDeterministicModule = new Module(new AStarModule(mGridMap));
	private Module mStochasticModule = new Module(new QLearningModule(mGridMap));
	
    private Module mCurrentModule = mDeterministicModule;
	
    public RobotLearningDemo()
    {
    	mMap.setDrawMod(mCurrentModule);
    }
    
	public void addComponentToPane(Container pane)
	{
		// Create Top Panel 
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        // Add the Environment Panel
        buttonPanel.add(createEnvironmentPanel());
        
        // Create the Pathing and Movement Panels
        JPanel pathMovePanel = new JPanel(new BorderLayout());
        pathMovePanel.add(createPathPanel(), BorderLayout.NORTH);
        pathMovePanel.add(createMovementPanel(), BorderLayout.SOUTH);
        buttonPanel.add(pathMovePanel);
        
        pane.add(buttonPanel, BorderLayout.NORTH);
        pane.add(mMap, BorderLayout.SOUTH);
            	
		mCurrentModule.setStartPoint(getStartPointFromComboBoxes());
		mCurrentModule.setEndPoint(getEndPointFromComboBoxes());
	}
	
	public JPanel createEnvironmentPanel()
	{
		// Create the Type Panel
        JPanel envPanel = new JPanel(new BorderLayout());
        TitledBorder typeBorder = new TitledBorder("Environment");
        typeBorder.setTitleJustification(TitledBorder.CENTER);
        typeBorder.setTitlePosition(TitledBorder.CENTER);
        envPanel.setBorder(typeBorder);
        
        JComboBox<String> switchCombo = new JComboBox<>(ENVIRONMENT_NAMES);
        switchCombo.setEditable(false);
        switchCombo.addItemListener(new ItemListener()
        {			
        	@Override
        	public void itemStateChanged(ItemEvent arg0)
        	{        		
        		if (arg0.getItem() == ENVIRONMENT_NAMES[0])
        		{
        			mCurrentModule = mDeterministicModule;
        		}
        		else
        		{
        			mCurrentModule = mStochasticModule;
        		}
        		
        		mCurrentModule.setStartPoint(getStartPointFromComboBoxes());
        		mCurrentModule.setEndPoint(getEndPointFromComboBoxes());

            	mMap.setDrawMod(mCurrentModule);
        		mMap.repaint();
        	}
		});
        
        // Create the Panel for Start
        JPanel startPanel = new JPanel(new FlowLayout());
        startPanel.add(new JLabel("Start: "));
        mStartXCombo.setSelectedIndex(START_X);
        startPanel.add(mStartXCombo);
        mStartYCombo.setSelectedIndex(START_Y);
        startPanel.add(mStartYCombo);
        JButton setStart = new JButton("Set");
        setStart.addActionListener(new ActionListener()
        {			
			@Override
			public void actionPerformed(ActionEvent e)
			{
        		mCurrentModule.setStartPoint(getStartPointFromComboBoxes());
        	    mMap.repaint();
			}
		});
        startPanel.add(setStart);

        // Create the Panel for End
        JPanel endPanel = new JPanel(new FlowLayout());
        endPanel.add(new JLabel("  End: "));
        mEndXCombo.setSelectedIndex(END_X);
        endPanel.add(mEndXCombo);
        mEndYCombo.setSelectedIndex(END_Y);
        endPanel.add(mEndYCombo);
        JButton setEnd = new JButton("Set");
        setEnd.addActionListener(new ActionListener()
        {			
			@Override
			public void actionPerformed(ActionEvent e)
			{
        		mCurrentModule.setEndPoint(getEndPointFromComboBoxes());
        	    mMap.repaint();
			}
		});
        endPanel.add(setEnd);

        envPanel.add(switchCombo, BorderLayout.NORTH);
        envPanel.add(startPanel, BorderLayout.CENTER);
        envPanel.add(endPanel, BorderLayout.SOUTH);
        return envPanel;
	}
	
	private Vector2 getStartPointFromComboBoxes()
	{
		return new Vector2(mStartXCombo.getSelectedIndex(), mStartYCombo.getSelectedIndex());
	}
	
	private Vector2 getEndPointFromComboBoxes()
	{
		return new Vector2(mEndXCombo.getSelectedIndex(), mEndYCombo.getSelectedIndex());
	}

	public JPanel createPathPanel()
	{        
        JPanel pathPanel = new JPanel();
        TitledBorder pathBorder = new TitledBorder("Path");
        pathBorder.setTitleJustification(TitledBorder.CENTER);
        pathBorder.setTitlePosition(TitledBorder.TOP);
        pathPanel.setBorder(pathBorder);
        
        JButton stepButton = new JButton("Step");
        stepButton.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{ 
        		mCurrentModule.Step();
        	    mMap.repaint();
    	    } 
    	});
        pathPanel.add(stepButton);
        JButton runButton = new JButton("Run");
        runButton.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		mCurrentModule.Run();
        		mMap.repaint();
    	    } 
    	});
        pathPanel.add(runButton);
        
        return pathPanel;
	}
	
	private JPanel createMovementPanel()
	{
		JPanel movementPanel = new JPanel();
        TitledBorder movementBorder = new TitledBorder("Movement");
        movementBorder.setTitleJustification(TitledBorder.CENTER);
        movementBorder.setTitlePosition(TitledBorder.TOP);
        movementPanel.setBorder(movementBorder);
        
        JButton stepButton = new JButton("Step");
        stepButton.addActionListener(new ActionListener()
        {		
			@Override
			public void actionPerformed(ActionEvent e)
			{
				mCurrentModule.moveStep();
        	    mMap.repaint();
			}
		});
        movementPanel.add(stepButton);
        
        JButton runButton = new JButton("Run");
        runButton.addActionListener(new ActionListener()
        {		
			@Override
			public void actionPerformed(ActionEvent e)
			{
				mCurrentModule.moveRun();
        	    mMap.repaint();
			}
		});
        movementPanel.add(runButton);
        
        return movementPanel;
	}
	
    public static void createAndShowGui()
    {
    	// Create application frame
        JFrame frame = new JFrame("Robot Learning");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create content
        RobotLearningDemo demo = new RobotLearningDemo();
        demo.addComponentToPane(frame.getContentPane());
        
        // Display Window
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGui();
            }
        });
    }
}