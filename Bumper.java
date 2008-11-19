import java.awt.Rectangle;
public class Bumper{
	double x,y,width,height;
	boolean passthrough = false;
	Rectangle bounds;
	public Bumper(double x,double y,double width, double height){
		this(x,y,width,height,false);
	}
	public Bumper(double x,double y,double width,double height,boolean pt){
		this.x=x;    
		this.y=y;
		this.width=width;
		this.height=height;
		bounds = new Rectangle((int)x,(int)y,(int)width,(int)height);
		passthrough = pt;
	}
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public double getWidth(){
		return width;
	}
	public double getHeight(){
		return height;
	}
	public void draw(java.awt.Graphics g){
		if(this instanceof SpeedBumper)
		{
			g.setColor(java.awt.Color.green);
		}
		else
		{
			g.setColor(java.awt.Color.BLUE.darker().darker());//new java.awt.Color(133,57,0));
			g.setColor(java.awt.Color.BLUE.darker().darker());//new java.awt.Color(133,57,0));
			g.fillRect((int)x,(int)y,(int)width,(int)height);
		}
	}
	public void draw(java.awt.Graphics g, double xScale, double yScale){
		if(this instanceof SpeedBumper)
		{
			g.setColor(java.awt.Color.green);
		}
		else
		{
			g.setColor(java.awt.Color.BLUE.darker().darker());//new java.awt.Color(133,57,0));
		}
		g.fillRect((int)(x*xScale),(int)(y*yScale),(int)(width*xScale),(int)(height*yScale));
	}
	public void draw(java.awt.Graphics g, double xScale, double yScale,java.awt.Color c){
		g.setColor(c);//new java.awt.Color(133,57,0));
		g.fillRect((int)(x*xScale),(int)(y*yScale),(int)(width*xScale),(int)(height*yScale));
	}
	public boolean hIntersects(Item i){   
		return hIntersects(i.x,i.y,i.r);
	}
	public boolean hIntersects(double ix, double iy, double ir){
		if(ix+ir>x && ix+ir<x+width && iy>y && iy<y+height)
			return true;
		else if(ix-ir>x && ix-ir<x+width && iy>y && iy<y+height)
			return true;
		else
			return false;
	}
	public boolean vIntersects(Item i){
		return vIntersects(i.x,i.y,i.r);
	}
	public boolean vIntersects(double ix, double iy, double ir){
		if(ix>x && ix<width+x && iy+ir>y && iy+ir<y+height)
			return true;
		else if(ix>x && ix<x+width && iy-ir>y && iy-ir<y+height)
			return true;
		else
			return false;
	}
	private boolean check2(double hx, double hy, double hradius){
		return (vIntersects(hx,hy,hradius)||hIntersects(hx,hy,hradius)||cornerCheck(hx,hy,hradius));
	}
	public boolean check(Hero h){
		return check(h.x,h.y,h.dx,h.dy,h.r);
	}
	public boolean check(double hx, double hy,double hdx, double hdy, double hr){
		if (passthrough){
			if(overBumper(hx+hdx,hy,hr)){
				if (overBumper(hx+hdx,hy+hdy,hr)){
					return false;
				}
				else{
					return true;
				}
			}
			else
				return (onBumper(hx,hy,hr)&&(hdy>=0));
		}
		else
			return check2(hx+hdx,hy+hdy,hr);
	}
	public boolean cornerCheck(Item i){
		return cornerCheck(i.x,i.y,i.r);
	}
	public boolean cornerCheck(double ix, double iy, double ir){
		if (distance(x,y,ix,iy)<ir)
			return true; 
		else if (distance(x+width,y,ix,iy)<ir)
			return true;
		else if (distance(x,y+height,ix,iy)<ir)
			return true;
		else if (distance(x+width,y+height,ix,iy)<ir)
			return true;
		else
			return false;
	}
	public boolean onBumper(double ix, double iy, double ir){
		if ((iy+ir==y)&&(ix>=x)&&(ix<=x+width))
			return true;
		else if (distance(x,y,ix,iy)==ir)
			return true;
		else if (distance(x+width,y,ix,iy)==ir)
			return true;
		else
			return false;
	}
	public boolean onBumper(Hero h){
		return onBumper(h.x,h.y,h.r);
	}
	public boolean overBumper(double ix, double iy, double ir){
		if ((iy+ir<y)&&(ix>=x)&&(ix<=x+width))
			return true;
		else if ((distance(x,y,ix,iy)>ir)&&(iy<y)&&(ix>=x-ir)&&(ix<=x))
			return true;
		else if ((distance(x+width,y,ix,iy)>ir)&&(iy<y)&&(ix<=x+width+ir)&&(ix>=x+width))
			return true;
		else
			return false;
	}
	public boolean overBumper(Item h){
		return overBumper(h.x,h.y,h.r);
	}
	private static double distance(double x1, double y1, double x2, double y2){
		return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
	}
	private static double distance(double x1, double y1, Item i){
		return distance(x1,y1,i.x,i.y);
	}
}
