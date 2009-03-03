#include <stdio.h>
#include "Bumper.h"
#include "Hero.h"
#include "Item.h"
#include "SDL/SDL.h"
#include "Display.h"//has blit method

Hero::Hero(double x, double y, int r, SDL_Surface* s, int n, Bumper** b) : Item(x,y,r) {
	jumps=0;
	tiedcount=0;
	sprite=s;
	bump=b;
	numBumpers=n;
	dy=0;
	dx=0;
	accel=0;
}
void Hero::setAccel(int a){
	accel=a;
}
void Hero::update() {
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
	
	//printf("updated\n");
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
		if( checkBumper(*bump[k],pdy,pdx))
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
//void Hero::checkPlayer(Hero p)}{
//	if (intersects(p)){

