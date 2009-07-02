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
