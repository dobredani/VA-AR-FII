package com.amihaeseisergiu.laborator6;

import java.awt.Color;
import java.awt.Graphics;

public class Edge
{
	public Node a,b;
	public double length;
        public Color color = Color.black;
        public EdgeShape shape;
        
        public Edge() { }
        
        public Edge(Node a, Node b, Color color, double length, EdgeShape shape)
        {
            this.a = a;
            this.b = b;
            this.color = color;
            this.length = length;
            this.shape = shape;
        }
	
	public void Draw(Graphics g)
	{
            try
            {
                g.setColor(color);
                g.drawLine((int)(a.posTodraw.getX() ), (int)(a.posTodraw.getY() ),(int)(b.posTodraw.getX() ),(int)(b.posTodraw.getY()));
            } catch(NullPointerException e) { }
	}
	
	public Vector getForce(Node toCal)
	{
            Vector dir ;
            if(a== toCal) {
                    dir = b.pos.sub(a.pos);
            }
            else
            {
                    dir = a.pos.sub(b.pos);
            }
             double t=dir.Size()-10*length;
            double ss = Math.signum(t) *Math.log( Math.abs(t))*0.01;
            dir = dir.Unit().Mul(ss);
            return dir;
	}

}
