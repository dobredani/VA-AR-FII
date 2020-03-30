package com.amihaeseisergiu.laborator6;

import static com.amihaeseisergiu.laborator6.DrawingPanel.H;
import static com.amihaeseisergiu.laborator6.DrawingPanel.W;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class DrawGraph extends JPanel
{
    List<Node> nodes;
    List<Edge> edges;

    public DrawGraph(List<Node> nodes, List<Edge> edges)
    {
        this.nodes = nodes;
        this.edges = edges;

        Thread t = new Thread(() -> {
            while(true)
            {
                try
                {
                    Thread.sleep(50);
                } catch (InterruptedException e)
                {
                }
                for(int j=0;j< nodes.size();j++)
                {
                    nodes.get(j).acc=new Vector();
                    nodes.get(j).calForce((ArrayList<Node>) nodes);
                    nodes.get(j).calForceEdge( );
                    nodes.get(j).move();
                }
                Scale();
                for(int j=0;j< nodes.size();j++)
                {
                    nodes.get(j).posTodraw = nodes.get(j).pos ;
                }

                repaint();
            }
        });
        t.start();

        setBackground(Color.white);
        setPreferredSize(new Dimension(W, H));
        setBorder(BorderFactory.createEtchedBorder());
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        for(int j=0;j< nodes.size();j++)
        {
                nodes.get(j).Draw(g);
        }
        for(int j=0;j< edges.size();j++)
        {
                edges.get(j).Draw(g);
        }
    }

    public void Scale()
    {
        double XMin= Integer.MAX_VALUE;
        double YMin= Integer.MAX_VALUE;
        double XMax= Integer.MIN_VALUE;
        double YMax= Integer.MIN_VALUE;

        for(int j=0;j< nodes.size();j++)
        {
            if(nodes.get(j).pos.getX() < XMin)
            {
                XMin=nodes.get(j).pos.getX();
            }
            if(nodes.get(j).pos.getY() < YMin)
            {
                YMin=nodes.get(j).pos.getY();
            }
            if(nodes.get(j).pos.getX() > XMax)
            {
                XMax=nodes.get(j).pos.getX();
            }
            if(nodes.get(j).pos.getY() > YMax)
            {
                YMax=nodes.get(j).pos.getY();
            }
        }

        double length_x = XMax- XMin;
        double length_y = YMax- YMin;
        double length  = Math.max(length_x, length_y);
        for(int j=0;j< nodes.size();j++)
        {
            Vector vv = nodes.get(j).pos;
            vv = vv.Mul(400.0 / length);
            nodes.get(j).setPosToDraw(vv);
        }
    }
}
