#include "Bumper.h"
Bumper::Bumper(double x, double y, double width, double height){
	Bumper(x,y,width,height,false);
}
Bumper::Bumper(double xnew, double ynew, double newwidth, double newheight, bool pt){
	passthrough=pt;
	x=xnew;
	y=ynew;
	width=newwidth;
	height=newheight;
}
double getX(){
	return x;
}
double getY(){
	return y;
}
double getWidth(){
	return width;
}
double getHeight(){
	return height;
}
void draw(SDL_Surface* screen){
	//TODO: Implement drawing of bumpers
}
