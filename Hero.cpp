#include <stdio.h>
#include "Hero.h"

Hero::Hero(double x, double y, int r) : Item(x,y,r) {
	
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
