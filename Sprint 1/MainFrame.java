package com.amihaeseisergiu.laborator6;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame {
    ConfigPanel configPanel;
    ShapePanel shapePanel;
    ControlPanel controlPanel;
    DrawingPanel canvas;
    DrawGraph graph;
    
    public MainFrame()
    {
        super("My Drawing Application");
        init();
    }
    
    private void init()
    {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        configPanel = new ConfigPanel(this);
        shapePanel = new ShapePanel(this);
        controlPanel = new ControlPanel(this);
        canvas = new DrawingPanel(this);
        
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2,8));
        topPanel.add(configPanel, NORTH);
        topPanel.add(shapePanel, SOUTH);
        
        add(topPanel, NORTH);
        add(controlPanel, SOUTH);
        add(canvas, CENTER);
        
        pack();
    }
}
