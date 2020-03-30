package com.amihaeseisergiu.laborator6;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class ConfigPanel extends JPanel {

    final MainFrame frame;
    JLabel sidesLabel;
    JSpinner sidesField;
    JLabel sizeLabel;
    JSpinner sizeField;
    JLabel transLabel;
    JSpinner transField;
    JLabel colorLabel;
    JComboBox colorCombo;
    
    public ConfigPanel(MainFrame frame)
    {
        this.frame = frame;
        init();
    }
    
    private void init()
    {
        sidesLabel = new JLabel("Number of Sides:");
        sizeLabel = new JLabel("Size:");
        transLabel = new JLabel("Transparency:");
        sidesField = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        sidesField.setValue(6);
        sizeField = new JSpinner(new SpinnerNumberModel(0, 0, 500, 1));
        sizeField.setValue(50);
        transField = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        transField.setValue(50);
        
        String[] colorString = {"Random", "Black"};
        colorLabel = new JLabel("Color:");
        colorCombo = new JComboBox(colorString);
        
        add(sidesLabel);
        add(sidesField);
        add(sizeLabel);
        add(sizeField);
        add(transLabel);
        add(transField);
        add(colorLabel);
        add(colorCombo);
    }
}