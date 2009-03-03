#include <stdio.h>
#include "Hero.h"
#include "Bumper.h"
#include "SDL/SDL.h"
#include "SDL/SDL_image.h"
#include <string>
#include "Display.h" //contains blit method
Hero* h;
SDL_Surface* background = NULL;
SDL_Surface* screen = NULL;
SDL_Surface* sprite = NULL;
void display();
int initDisplay();
int a=0;
Bumper *bump[1];

SDL_Surface* load_image(std::string fname){
	SDL_Surface* tImage = NULL;
	SDL_Surface* image = NULL;
	tImage = IMG_Load(fname.c_str());
	image = SDL_DisplayFormat(tImage);
	SDL_FreeSurface(tImage);
	return image;
}

void input(){
	Uint8* keystates = SDL_GetKeyState(NULL);
	a=0;
	//if(keystates[SDLK_UP])
		//printf("up key pressed\n");
	//if(keystates[SDLK_DOWN])
		//printf("down key pressed\n");
	if(keystates[SDLK_LEFT]){
		//printf("left key pressed\n");
		a--;
	}
	if(keystates[SDLK_RIGHT]){
		//printf("right key pressed\n");
		a++;
	}
}	

int main(int argc, char* argv[]){
	bool running = true;
	printf("=Ready=\n");
	if(initDisplay()!=0)
		return 1;
	printf("=Display Initialized=\n");
	int k = 0;
	//bump = Bumper[1];
	//bump[0]= Bumper(100,300,400,10);
	Uint32 black = SDL_MapRGB(screen->format, 0, 0, 0);
	Uint32 white = SDL_MapRGB(screen->format, 255, 255, 255);
	bump[0] = new Bumper(100,300,400,20,create_rectangle(400,20,black));
	h = new Hero(400,200,10,create_rectangle(10*2,10*2,white),1,bump);
	printf("=Hero Created=\n");
	SDL_Event event;
	while (running){
		while(SDL_PollEvent( &event) ){
			if(event.type==SDL_QUIT){
				running=false;
			}
			if(event.type==SDL_KEYDOWN){
				if(event.key.keysym.sym==SDLK_UP)
					h->jump();
				else if(event.key.keysym.sym==SDLK_DOWN)
					h->fastFall();
				else if(event.key.keysym.sym==SDLK_RSHIFT)
					h->attack();
				else if(event.key.keysym.sym==SDLK_RCTRL)
					h->attack();
			}
		}
		input();
		h->setAccel(a);
		h->update();
		display();
		SDL_Delay(100);
	}
	SDL_FreeSurface(background);
	SDL_Quit();
	return 0;
}
void display(){
	//printf("Drawing screen\n");
	//printf("blanking screen\n");
	blit(0,0,background,screen);
	h->draw(screen);
	bump[0]->draw(screen);
	SDL_Flip(screen);
}
int initDisplay(){
	if ( SDL_Init( SDL_INIT_EVERYTHING ) == -1 ){
		printf ("Error loading SDL\n");
		return 1;
	}
	screen = SDL_SetVideoMode( 640, 480, 32, SDL_SWSURFACE );
	printf("==Loading background.png==\n");
	background=load_image("background.png");
	printf("==Loading hello_world.png==\n");
	sprite=load_image("hello_world.png");
	SDL_WM_SetCaption( "GWar", NULL);
	//display();
	return 0;
}
