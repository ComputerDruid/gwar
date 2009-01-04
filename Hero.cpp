#include <stdio.h>
#include "Hero.h"
#include "SDL/SDL.h"
#include "Display.h"//has blit method

Hero::Hero(double x, double y, int r, SDL_Surface* s) : Item(x,y,r) {
	jumps=0;
	tiedcount=0;
	sprite=s;
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
				dx=dx*.975;//friction
		}
		if(dy>0){
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
	if (checkVBumper()){
		dy=abs(dy);
	}
	else if (checkHBumper()){
		dx=-dx;
	}


	//apply gravity
	if(!onbumper)
		dy+=.5;
	//apply motion
	x+=dx;
	y+=dy;
	//TODO: check collisions
	
	printf("updated\n");
}
void Hero::jump(){
	if(tiedcount==0)
		if(jumps<2){
			dy = -2.5*SPEED;
			jumps++;
			//if(!onbumper)
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
			if(onWhatBumper.passthrough){
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
	blit(x,y,sprite,screen);
}
