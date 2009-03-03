#include "item.h"
#include <stdio.h>
#include <math.h>
Item::Item(double startx, double starty, int startr/*,Color c*/ ){
	x=startx;
	y=starty;
	r=startr;
}
void Item::draw(){
	printf("Draw Method Not Implemented\n");
	printf("Drawing an item at %f, %f with radius %d\n",x,y,r);
}
bool Item::intersects(Item b){
	return sqrt((x-b.x)*(x-b.x)+(y-b.y)*(y-b.y))<=r+b.r;
}
/*
import javax.swing.*;
import java.awt.*;

class Item
{
	int r;
	double x,y,lX,lY;
	java.awt.Color c;
	public Item(double x, double y, int r, java.awt.Color c)
	{
		//probably shouldn't be using shadowing
		this.x=x;
		this.y=y;
		this.lX = x;
		this.lY = y;
		this.r=r;
		this.c=c;
	}
	public boolean intersects(Item b)
	{
		return Math.sqrt(Math.pow(x-b.x,2)+Math.pow(y-b.y,2))<=r+b.r;
	}
	public void draw(java.awt.Graphics g)
	{
		g.setColor(c);
		g.fillOval((int)x-r,(int)y-r,2*r,2*r);
	}
	public void draw(java.awt.Graphics g, double xScale, double yScale)
	{
		g.setColor(c);
		g.fillOval((int)((x-r)*xScale),(int)((y-r)*yScale),(int)(2*r*xScale),(int)(2*r*yScale));
	}
}
*/
