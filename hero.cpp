#include <stdio.h>
#include <math.h>
#include "bumper.h"
#include "hero.h"
#include "item.h"
#include "SDL/SDL.h"
#include "display.h"//has blit method

Hero::Hero(double x, double y, int r, int l, SDL_Surface* s, int n, Bumper** b) : Item(x,y,r) {
	jumps=0;
	tiedcount=0;
	sprite=s;
	bump=b;
	numBumpers=n;
	dy=0;
	dx=0;
	accel=0;
	mass=1;
	lives=l;
	startx=x;
	starty=y;
}
void Hero::setAccel(int a){
	accel=a;
}
void Hero::update() {
	if (lives<=0)return;
	bool onbumper=onBumper();
	if (tiedcount>0)
		tiedcount--;
	if (onbumper){
		if (accel==0){
			if(tiedcount==0)
				dx=dx*.9;//friction
		}
		if(dy>=0){
			jumps=0;
			dy=0;
		}
	}
	if (tiedcount==0){
		if(accel>0&&dx<SPEED)
			dx++;
		if(accel<0&&dx>-SPEED)
			dx--;
	}
	//apply gravity
	if(!onbumper)
		dy+=.5;

	//apply motion
	x+=dx;
	y+=dy;

	if (checkVBumper()){
		//printf("==VBumper Collision==\n");
		dy=abs((int)dy);
	}
	else if (checkHBumper()){
		//printf("==HBumper Collision==\n");
		dx=-dx;
	}


	//TODO: check collisions
	if (offScreen()){
		lives--;
		printf("Lives left: %d\n",lives);
		x=startx;
		y=starty;
		dx=0;
		dy=0;
	}
	//printf("updated\n");
}
bool Hero::offScreen(){
	if (x>640||x<0||y>480||y<0)
		return true;
	return false;
}
bool Hero::isAlive(){
	return (lives>0);
}
void Hero::jump(){
	printf("jump! tiedcount:%d jumps:%d\n",tiedcount,jumps);
	if(tiedcount==0)
		if(jumps<2){
			dy = -1.5*SPEED;
			jumps++;
			if(!onBumper())
				dx=0;
		}
}
void Hero::attack(){
	if(tiedcount==0){
		dx+=accel*2*SPEED;
		tiedcount=15;
		xflash=(int)x;
		yflash=(int)y;
		flashcount=5;
	}
}
void Hero::fastFall(){
	if(tiedcount==0){
		if(onBumper()){
			if(bump[onWhatBumper()]->passthrough){
				y++;
				dy=10;
			}
		}
		else if (dy<10&&(jumps<2)){
			dy=10;
			jumps++;
			dx=0;
		}
	}
}
//TODO:Bumper Methods
void Hero::draw(SDL_Surface* screen){
	if(lives>0)
		blit((int)(x-r),(int)(y-r),sprite,screen);
}
bool Hero::overBumper(){
	for (int k=0; k<numBumpers;k++){
		if(bump[k]->overBumper(this)){
			return true;
		}
	}
	return false;
}
bool Hero::checkHBumper(){
	if (onBumper())
		return false;
	else
		return checkBumper(dx,0)!=-1;
}
bool Hero::checkVBumper(){
	int k = checkBumper(0,dy);
	if (k!=-1){
		if (y<bump[k]->getY()||dy>0){
			y= bump[k]->getY()-r;
			if(dy>0)
				dy=0;
			return false;
		}
		return true;
	}
	return false;
}
bool Hero::onBumper(){
	for(int k=0;k<numBumpers;k++){
		if (bump[k]->onBumper(this)){
			return true;
		}
	}
	return false;
}
int Hero::onWhatBumper(){
	for(int k=0;k<numBumpers;k++){
		if (bump[k]->onBumper(this)){
			return k;
		}
	}
	return false;
}
int Hero::checkBumper(double pdx, double pdy){
	for(int k=0;k<numBumpers;k++){
		if( checkBumper(*bump[k],pdx,pdy))
			return k;
	}
	return -1;
}
bool Hero::checkBumper(Bumper b, double pdx, double pdy){
	if ((b.overBumper(x+pdx,y,r)||b.onBumper(this))&&(y+r+pdy>b.getY())){
		y=b.getY()-r; 
		dy=0;
		return false;	
	}
	else if (b.check(x,y,pdx,pdy,r)){
		return true;
	}
	return false;
}
bool Hero::onScreen(int width, int height){
	if(x-r>0&&x+r<width&&y-r>0&&y+r<height)
		return true;
	return false;
}
bool Hero::intersects(Hero* p){
	return ( sqrt( pow(x - p->x,2) + pow(y - p->y,2) ) < r + p->r);
}
void Hero::checkPlayer(Hero* p){
	if(intersects(p)){
		double angle = atan2(p->y-y,p->x-x)+M_PI;
		double phi = angle;

		double v1i = sqrt(pow(dx,2)+pow(dy,2));
		double v2i = sqrt(pow(p->dx,2)+pow(p->dy,2));
		double ang1 = atan2(dy,dx);
		double ang2 = atan2(p->dy,p->dx);


		double v1xr = v1i*cos((ang1-phi));
		double v1yr = v1i*sin((ang1-phi));
		double v2xr = v2i*cos((ang2-phi));
		double v2yr = v2i*sin((ang2-phi));

		double v1fxr = ((mass-p->mass)*v1xr+(2*p->mass)*v2xr)/(mass+p->mass);
		double v2fxr = ((2*mass)*v1xr+(p->mass-mass)*v2xr)/(mass+p->mass);
		double v1fyr = v1yr;
		double v2fyr = v2yr;

		double v1fx = cos(phi)*v1fxr+cos(phi+M_PI/2)*v1fyr;
		double v1fy = sin(phi)*v1fxr+sin(phi+M_PI/2)*v1fyr;
		double v2fx = cos(phi)*v2fxr+cos(phi+M_PI/2)*v2fyr;
		double v2fy = sin(phi)*v2fxr+sin(phi+M_PI/2)*v2fyr;

		dx = v1fx;
		dy = v1fy;
		p->dx = v2fx;
		p->dy = v2fy;
		//System.out.println("DX: "+dx);
		//System.out.println("DY: "+dy);
		//System.out.println("P	DX: "+p.dx);
		//System.out.println("P	DY: "+p.dy);
		double newx = (p->x+x)/2+cos(angle)*(r+1);
		double newy = (p->y+y)/2+sin(angle)*(r+1);
		p->x = (p->x+x)/2+cos(angle+M_PI)*(p->r+1);
		p->y = (p->y+y)/2+sin(angle+M_PI)*(p->r+1);
		x=newx;
		y=newy;
		tiedcount+=2;
	}
}
