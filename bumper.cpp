#include "bumper.h"
#include "display.h"
#include <math.h>
Bumper::Bumper(double xnew, double ynew, double width, double height, Uint32 c){
	init(xnew,ynew,width,height,false,c);
}
Bumper::Bumper(double xnew, double ynew, double newwidth, double newheight, bool pt, Uint32 c){
	init(xnew,ynew,newwidth,newheight,pt,c);
}
void Bumper::init(double xnew, double ynew, double newwidth, double newheight, bool pt, Uint32 c){
	passthrough=pt;
	x=xnew;
	y=ynew;
	width=newwidth;
	height=newheight;
	suf=create_rectangle(400,20,c);
}
double Bumper::getX(){
	return x;
}
double Bumper::getY(){
	return y;
}
double Bumper::getWidth(){
	return width;
}
double Bumper::getHeight(){
	return height;
}
/*void Bumper::draw(SDL_Surface* screen){
	//TODO: Implement drawing of bumpers
	
}*/
void Bumper::draw(SDL_Surface* screen){
	blit((int)x,(int)y,suf,screen);
	//printf("===Drawing bumper (%d) at (%f,%f)===\n",suf,x,y);
}
bool Bumper::hIntersects(Item* i){
	return hIntersects(i->x,i->y,i->r);
}
bool Bumper::vIntersects(Item* i){
	return vIntersects(i->x,i->y,i->r);
}
bool Bumper::hIntersects(double ix, double iy, double ir){
	if(ix+ir>x && ix+ir<x+width && iy>y && iy<y+height)
		return true;
	else if(ix-ir>x && ix-ir<x+width && iy>y && iy<y+height)
		return true;
	else
		return false;
}
bool Bumper::vIntersects(double ix, double iy, double ir){
	if(ix>x && ix<width+x && iy+ir>y && iy+ir<y+height)
		return true;
	else if(ix>x && ix<x+width && iy-ir>y && iy-ir<y+height)
		return true;
	else
		return false;
}
bool Bumper::check2(double ix, double iy, double ir){
	return (vIntersects(ix,iy,ir)||hIntersects(ix,iy,ir)||cornerCheck(ix,iy,ir));
}
//bool Bumper::check(Hero i){
//	return check(i.x,i.y,i.dx,i.dy,i.r);
//}
bool Bumper::check(double ix, double iy, double idx, double idy, double ir){
	if (passthrough){
		if (overBumper(ix+idx,iy,ir)){
			if (overBumper(ix+idx,iy+idy,ir))
				return false;
			else
				return true;
		}
		else
			return onBumper(ix,iy,ir)&&idy>=0;

	}
	else
		return check2(ix,iy,ir);
}
double distance(double x1, double y1, double x2, double y2){
	return sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
}
bool Bumper::cornerCheck(Item* i){
	return cornerCheck(i->x,i->y,i->r);
}
bool Bumper::cornerCheck(double ix, double iy, double ir){
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
bool Bumper::onBumper(Item* i){
	return onBumper(i->x,i->y,i->r);
}
bool Bumper::onBumper(double ix, double iy, double ir){
	if ((iy+ir==y)&&(ix>=x)&&(ix<=x+width))
		return true;
	else if (distance(x,y,ix,iy)==ir)
		return true;
	else if (distance(x+width,y,ix,iy)==ir)
		return true;
	else
		return false;
}
bool Bumper::overBumper(Item* i){
	return overBumper(i->x,i->y,i->r);
}
bool Bumper::overBumper(double ix, double iy, double ir){
	if ((iy+ir<y)&&(ix>=x)&&(ix<=x+width))
		return true;
	else if ((distance(x,y,ix,iy)>ir)&&(iy<y)&&(ix>=x-ir)&&(ix<=x))
		return true;
	else if ((distance(x+width,y,ix,iy)>ir)&&(iy<y)&&(ix<=x+width+ir)&&(ix>=x+width))
		return true;
	else
		return false;
}
