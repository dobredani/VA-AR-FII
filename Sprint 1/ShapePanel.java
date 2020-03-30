package com.amihaeseisergiu.laborator6;

import static java.awt.BorderLayout.CENTER;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class ShapePanel extends JPanel {
    
    final MainFrame frame;
    JComboBox shapeCombo;
    JComboBox deleteCombo;
    JButton deleteBtn;
    JButton createGraphBtn;
    
    public ShapePanel(MainFrame frame)
    {
        this.frame = frame;
        init();
    }
    
    private void init()
    {
        String[] shapesString = {"Regular Polygon", "Circle", "Square", "Triangle", "Node", "Edge"};
        shapeCombo = new JComboBox(shapesString);
        deleteCombo = new JComboBox();
        deleteBtn = new JButton("Delete shape");
        deleteCombo.setVisible(false);
        deleteBtn.setVisible(false);
        createGraphBtn = new JButton("Create Graph");
        createGraphBtn.setVisible(false);

        add(shapeCombo);
        add(deleteCombo);
        add(deleteBtn);
        add(createGraphBtn);
        
        shapeCombo.addActionListener(this::refresh);
        deleteCombo.addActionListener(this::checkCount);
        deleteBtn.addActionListener(this::deleteShape);
        createGraphBtn.addActionListener(this::drawGraph);
    }
    
    private void refresh(ActionEvent e)
    {
        switch((String)shapeCombo.getSelectedItem())
        {
            case "Regular Polygon" :
                frame.configPanel.sidesLabel.setVisible(true);
                frame.configPanel.sidesField.setVisible(true);
                frame.configPanel.sizeLabel.setVisible(true);
                frame.configPanel.sizeField.setVisible(true);
                frame.configPanel.sizeLabel.setText("Size:");
                break;
            case "Circle" :
                frame.configPanel.sidesLabel.setVisible(false);
                frame.configPanel.sidesField.setVisible(false);
                frame.configPanel.sizeLabel.setVisible(true);
                frame.configPanel.sizeField.setVisible(true);
                frame.configPanel.sizeLabel.setText("Radius:");
                break;
            case "Square" :
                frame.configPanel.sidesLabel.setVisible(false);
                frame.configPanel.sidesField.setVisible(false);
                frame.configPanel.sizeLabel.setVisible(true);
                frame.configPanel.sizeField.setVisible(true);
                frame.configPanel.sizeLabel.setText("Size:");
                break;
            case "Triangle" :
                frame.configPanel.sidesLabel.setVisible(false);
                frame.configPanel.sidesField.setVisible(false);
                frame.configPanel.sizeLabel.setVisible(true);
                frame.configPanel.sizeField.setVisible(true);
                frame.configPanel.sizeLabel.setText("Size:");
                break;
            case "Node" :
                frame.configPanel.sidesLabel.setVisible(false);
                frame.configPanel.sidesField.setVisible(false);
                frame.configPanel.sizeLabel.setVisible(true);
                frame.configPanel.sizeField.setVisible(true);
                break;
            case "Edge" :
                frame.configPanel.sidesLabel.setVisible(false);
                frame.configPanel.sidesField.setVisible(false);
                frame.configPanel.sizeLabel.setVisible(false);
                frame.configPanel.sizeField.setVisible(false);
                break;
        }
    }
    
    private void deleteShape(ActionEvent e)
    {
        int index = deleteCombo.getSelectedIndex();
        frame.canvas.shapes.remove(index);
        frame.canvas.colors.remove(index);
        if(deleteCombo.getSelectedItem().toString().equals("Node"))
        {
            Node nod = null;
            for(Node n : frame.canvas.nodes)
            {
                if(n.shape == (NodeShape)deleteCombo.getSelectedItem())
                {
                    nod = n;
                    break;
                }
            }
            
            List<Edge> edgeRem = new LinkedList<>();
            for(Edge ed : frame.canvas.edges)
            {
                if(ed.a == nod || ed.b == nod)
                {
                    edgeRem.add(ed);
                    Shape shapeRem = null;
                    for(Shape s : frame.canvas.shapes)
                    {
                        if(s == ed.shape)
                        {
                            shapeRem = s;
                            frame.canvas.colors.remove(frame.canvas.shapes.indexOf(s));
                            break;
                        }
                    }
                    frame.canvas.shapes.remove(shapeRem);
                }
            }
            frame.canvas.edges.removeAll(edgeRem);
            frame.canvas.nodes.remove(deleteCombo.getSelectedIndex());
        }
        if(deleteCombo.getSelectedItem().toString().equals("Edge"))
        {
            Edge edge = null;
            
            for(Edge ed : frame.canvas.edges)
            {
                if(ed.shape == deleteCombo.getSelectedItem())
                {
                    edge = ed;
                    break;
                }
            }
            
            for(Node n : frame.canvas.nodes)
            {
                if(n.Adj.contains(edge))
                {
                    n.Adj.remove(edge);
                }
            }
            
            frame.canvas.edges.remove(edge);
            frame.canvas.shapes.remove((EdgeShape)deleteCombo.getSelectedItem());
        }
        frame.canvas.redraw();
    }
    
    private void checkCount(ActionEvent e)
    {
        if(deleteCombo.getItemCount() <= 0)
        {
            deleteCombo.setVisible(false);
            deleteBtn.setVisible(false);
        }
        else
        {
            deleteCombo.setVisible(true);
            deleteBtn.setVisible(true);
        }
    }
    
    private void drawGraph(ActionEvent e)
    {
        if(createGraphBtn.getText().equals("Create Graph"))
        {
            frame.remove(frame.canvas);
            frame.graph = new DrawGraph(frame.canvas.nodes, frame.canvas.edges);
            frame.add(frame.graph, CENTER);
            frame.revalidate();
            createGraphBtn.setText("Destroy Graph");
            deleteCombo.removeAllItems();
            frame.canvas.shapes.clear();
            frame.canvas.colors.clear();
            frame.configPanel.setVisible(false);
            deleteCombo.setVisible(false);
            shapeCombo.setVisible(false);
        }
        else
        {
            frame.remove(frame.graph);
            frame.canvas = new DrawingPanel(frame);
            frame.add(frame.canvas, CENTER);
            frame.configPanel.setVisible(true);
            shapeCombo.setVisible(true);
            createGraphBtn.setVisible(false);
            frame.canvas.redraw();
            frame.revalidate();
            createGraphBtn.setText("Create Graph");
        }
    }
}