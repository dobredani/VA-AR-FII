package com.amihaeseisergiu.laborator6;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class Node
{
    public String name = "";
    public int size = 40;
    public NodeShape shape;
    public ArrayList<Edge> Adj = new ArrayList<>();

    public Vector pos,vel,acc;
    public Vector posTodraw;
    public Color color;
    
    public Node()
    {
        pos = new Vector(300+(int)(Math.random()*300-150), 300+(int)(Math.random()*300-150));
        vel = new Vector(0,0);
        acc = new Vector(0,0);
        color = new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
    }

    public Node(Vector pos, int size, Color color, NodeShape shape)
    {
        this.pos = pos;
        this.size = size;
        this.shape = shape;
        vel = new Vector(0,0);
        acc = new Vector(0,0);
        this.color = color;
    }

    public Vector calForce(ArrayList<Node> ll)
    {
        Vector result =new Vector();
        for(int i=0;i<ll.size();i++)
        {
            if(ll.get(i)== this)continue;

            Vector temp = this.pos.sub(ll.get(i).pos);
            double temp2 = 10 / Math.pow(temp.Size(), 2);

            temp = temp.Unit().Mul(temp2);
            result=result.add(temp);

        }
        acc = this.acc.add(result); 
        return result;
    }

    public Vector calForceEdge( )
    {
        Vector result =new Vector();
        for(int i=0;i<Adj.size();i++)
        { 
            Vector temp = Adj.get(i). getForce(this); 
            result=result.add(temp);

        } 
        acc = this.acc.add(result); 
        return result;
    }

    public void move()
    {
        vel= vel.add(acc);
        pos= pos.add(vel);

        vel=vel.Mul(0.99);
    }

    public void Draw(Graphics g)
    {
        try
        {
            g.setColor(color);
            g.fillOval((int)(posTodraw.getX()-size/2), (int)(posTodraw.getY()-size/2), size, size);
            g.setColor(Color.black); 
            g.drawString(name,(int)(posTodraw.getX()-20), (int)(posTodraw.getY()-20));
    
        }catch(NullPointerException e) { }
    }

    public void setPosToDraw(Vector v)
    {
        posTodraw = v;
    }

    public Vector getCentroid(ArrayList<Node> ll)
    {
        double sx=0,sy=0;
        for(int i=0;i<ll.size();i++)
        {
            sx += ll.get(i).posTodraw.getX();
            sy += ll.get(i).posTodraw.getY();
        }
        return new Vector(sx/ll.size(),sy/ll.size());
    }
    
    public boolean checkPosition(int x, int y)
    {
        if(x >= pos.getX() - size/2 && x <= pos.getX() + size/2 && y >= pos.getY() - size/2 && y <= pos.getY() + size/2)
            return true;
        
        return false;
    }
    
    public Vector getPos()
    {
        return pos;
    }
}
