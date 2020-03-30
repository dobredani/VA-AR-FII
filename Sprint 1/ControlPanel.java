package com.amihaeseisergiu.laborator6;

import static com.amihaeseisergiu.laborator6.DrawingPanel.H;
import static com.amihaeseisergiu.laborator6.DrawingPanel.W;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {
    
    final MainFrame frame;
    JButton saveBtn = new JButton("Save");
    JButton loadBtn = new JButton("Load");
    JButton resetBtn = new JButton("Reset");
    JButton exitBtn = new JButton("Exit");
    
    public ControlPanel(MainFrame frame)
    {
        this.frame = frame;
        init();
    }
    
    private void init()
    {
        setLayout(new GridLayout(1, 4));
        
        add(saveBtn);
        add(loadBtn);
        add(resetBtn);
        add(exitBtn);
        
        saveBtn.addActionListener(this::save);
        loadBtn.addActionListener(this::load);
        resetBtn.addActionListener(this::reset);
        exitBtn.addActionListener(this::exit);
    }

    private void save(ActionEvent e) {
        try {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Save file");
            fc.setCurrentDirectory(new File("d:/"));
            if(fc.showOpenDialog(saveBtn) == JFileChooser.APPROVE_OPTION)
            {
                ImageIO.write(frame.canvas.image, "PNG", new File(fc.getSelectedFile().getAbsolutePath()));
            }
        } catch (IOException ex) {
            Logger.getLogger(ControlPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void load(ActionEvent e)
    {
        try {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Load file");
            fc.setCurrentDirectory(new File("d:/"));
            if(fc.showOpenDialog(saveBtn) == JFileChooser.APPROVE_OPTION)
            {
                frame.canvas.image = ImageIO.read(new File(fc.getSelectedFile().getAbsolutePath()));
                frame.canvas.loadedImage = fc.getSelectedFile().getAbsolutePath();
            }
        } catch (IOException ex) {
            Logger.getLogger(ControlPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        frame.canvas.graphics = frame.canvas.image.createGraphics();
        frame.canvas.repaint();
    }
    
    private void reset(ActionEvent e)
    {
        frame.canvas.image = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        frame.canvas.graphics = frame.canvas.image.createGraphics();
        frame.canvas.graphics.setColor(Color.WHITE);
        frame.canvas.graphics.fillRect(0, 0, W, H);
        frame.canvas.repaint();
        frame.canvas.shapes.clear();
        frame.canvas.colors.clear();
        frame.shapePanel.deleteCombo.removeAllItems();
    }
    
    private void exit(ActionEvent e)
    {
        System.exit(0);
    }
}
