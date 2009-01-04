#include <stdio.h>
#include "Hero.h"

Hero* h;
void display();
int main(){
	h = new Hero(400,200,10);
	h->update();
	display();
}
void display(){
	printf("Drawing screen\n");
	printf("blanking screen\n");
	h->draw();
}
