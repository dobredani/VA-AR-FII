package com.amihaeseisergiu.laborator6;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class DrawingPanel extends JPanel {
    
    final MainFrame frame;
    final static int W = 800, H = 600;
    double x0;
    double y0;
    boolean firstPointSet;
    Node firstNode;
    
    BufferedImage image;
    String loadedImage;
    Graphics2D graphics;
    List<Shape> shapes = new LinkedList<>();
    List<Color> colors = new LinkedList<>();
    
    List<Node> nodes = new ArrayList<>();
    List<Edge> edges = new ArrayList<>();
    
    public DrawingPanel(MainFrame frame)
    {
        this.frame = frame;
        createOffscreenImage();
        init();
    }

    private void createOffscreenImage() {
        image = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, W, H);
    }
    
    public void init()
    {
        setPreferredSize(new Dimension(W, H));
        setBorder(BorderFactory.createEtchedBorder());
        
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                drawShape(e.getX(), e.getY());
                repaint();
            }
        });
    }
    
    private void drawShape(int x, int y)
    {
        Random rand = new Random();
        int radius;
        if((int) frame.configPanel.sizeField.getValue() == 0)
            radius = rand.nextInt(100);
        else radius = (int) frame.configPanel.sizeField.getValue();
        
        int sides = (int) frame.configPanel.sidesField.getValue();
        
        float trans = 0;
        if((int)frame.configPanel.transField.getValue() != 0)
            trans = (int)frame.configPanel.transField.getValue() / (float) 100;
        
        if(nodes.size() >= 0)frame.shapePanel.createGraphBtn.setVisible(true);
        
        Color color = Color.WHITE;
        switch((String)frame.configPanel.colorCombo.getSelectedItem())
        {
            case "Random" :
                color = new Color(rand.nextFloat()*1.0f, rand.nextFloat()*1.0f, rand.nextFloat()*1.0f, trans);
                break;
            case "Black" :
                color = new Color(0, 0, 0, trans);
                break;
        }
        colors.add(color);
        
        switch((String)frame.shapePanel.shapeCombo.getSelectedItem())
        {
            case "Regular Polygon" :
                shapes.add(new RegularPolygon(x, y, radius, sides));
                break;
            case "Circle" :
                shapes.add(new Circle(x, y, radius));
                break;
            case "Square" :
                shapes.add(new Square(x, y, radius));
                break;
            case "Triangle" :
                shapes.add(new Triangle(x, y, radius));
                break;
            case "Node" :
                shapes.add(new NodeShape(x, y, radius));
                nodes.add(new Node(new Vector(x,y), radius, color, (NodeShape)shapes.get(shapes.size() - 1)));
                break;
            case "Edge" :
                if(firstPointSet)
                {
                    for(Node n : nodes)
                    {
                        if(n.checkPosition(x, y))
                        {
                            Vector pos = n.getPos();
                            shapes.add(new EdgeShape(x0, y0, pos.getX(), pos.getY()));
                            edges.add(new Edge(firstNode, n, color, (int)(Math.random() * 20), (EdgeShape)shapes.get(shapes.size() - 1)));
                            firstNode.Adj.add(edges.get(edges.size() - 1));
                            n.Adj.add(edges.get(edges.size() - 1));
                            firstPointSet = false;
                            break;
                        }
                    }
                }
                else
                {
                    for(Node n : nodes)
                    {
                        if(n.checkPosition(x, y))
                        {
                            Vector pos = n.getPos();
                            x0 = pos.getX();
                            y0 = pos.getY();
                            firstPointSet = true;
                            firstNode = n;
                            break;
                        }
                    }
                    colors.remove(colors.size() - 1);
                }
                break;
        }
        
        redraw();
    }
    
    protected void redraw()
    {
        if(loadedImage == null)
        {
            image = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
            graphics = image.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, W, H);
            repaint();
        }
        else
        {
            try {
                image = ImageIO.read(new File(loadedImage));
            } catch (IOException ex) {
                Logger.getLogger(DrawingPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            graphics = image.createGraphics();
            repaint();
        }
        
        frame.shapePanel.deleteCombo.removeAllItems();
        for(int i = 0; i < shapes.size(); i++)
        {
            graphics.setColor(colors.get(i));
            if(shapes.get(i).toString().equals("Edge"))
                graphics.draw(shapes.get(i));
            else graphics.fill(shapes.get(i));
            frame.shapePanel.deleteCombo.addItem(shapes.get(i));
        }
    }
    
    @Override
    public void update(Graphics g) { }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        g.drawImage(image, 0, 0, this);
    }
    
}
