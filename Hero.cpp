#include <stdio.h>
#include "Hero.h"
#include "SDL/SDL.h"
#include "Display.h"//has blit method

Hero::Hero(double x, double y, int r, SDL_Surface* s) : Item(x,y,r) {
	jumps=0;
	tiedcount=0;
	sprite=s;
}
void Hero::update() {
	//apply gravity
	dy++;
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
void Hero::draw(SDL_Surface* screen){
	blit(x,y,sprite,screen);
}
