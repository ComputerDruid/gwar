#include <stdio.h>
#include "hero.h"
#include "bumper.h"
#include "SDL/SDL.h"
#include "SDL/SDL_image.h"
#include <string>
#include "display.h" //contains blit method
Hero *h1;
Hero *h2;
SDL_Surface* background = NULL;
SDL_Surface* screen = NULL;
SDL_Surface* sprite = NULL;
void display();
int initDisplay();
int a1=0;
int a2=0;
Bumper *bump[1];
SDL_Event event;
enum state {
	init = 0,
	running = 1,
	stopped = 2,
	quitting = -1
};

state gamestate = init;

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
	a1=0;
	a2=0;
	if(keystates[SDLK_LEFT]){
		a1--;
	}
	if(keystates[SDLK_RIGHT]){
		a1++;
	}
	if(keystates[SDLK_a]){
		a2--;
	}
	if(keystates[SDLK_d]){
		a2++;
	}
}	
int countPlayers(){
	int count = 0;
	if (h1->isAlive())
		count++;
	if (h2->isAlive())
		count++;
	printf("count: %d\n",count);
	return count;
}
void gameLoop(){
	gamestate=running;
	while (gamestate == running){
		while(SDL_PollEvent( &event) ){
			if(event.type==SDL_QUIT){
				gamestate=quitting;
			}
			if(event.type==SDL_KEYDOWN){
				if (event.key.keysym.sym==SDLK_q)
					gamestate=quitting;
				else if(event.key.keysym.sym==SDLK_UP)
					h1->jump();
				else if(event.key.keysym.sym==SDLK_DOWN)
					h1->fastFall();
				else if(event.key.keysym.sym==SDLK_RSHIFT)
					h1->attack();
				else if(event.key.keysym.sym==SDLK_RCTRL)
					h1->attack();
				else if(event.key.keysym.sym==SDLK_LSHIFT)
					h1->attack();
				else if(event.key.keysym.sym==SDLK_LCTRL)
					h1->attack();
				else if(event.key.keysym.sym==SDLK_w)
					h2->jump();
				else if(event.key.keysym.sym==SDLK_s)
					h2->fastFall();
				else if(event.key.keysym.sym==SDLK_q)
					h2->attack();
				else if(event.key.keysym.sym==SDLK_e)
					h2->attack();
				//else if(event.key.keysym.sym==SDLK_LSHIFT)
				//	h2->attack();
				//else if(event.key.keysym.sym==SDLK_LCTRL)
				//	h2->attack();
			}
		}
		input();
		h1->setAccel(a1);
		h2->setAccel(a2);
		h1->update();
		h2->update();
		h1->checkPlayer(h2);
		if (countPlayers()<=1){
			gamestate = stopped;
			return;
		}
		display();
		SDL_Delay(50);
	}
}
void newGame(){
	//bump = Bumper[1];
	//bump[0]= Bumper(100,300,400,10);
	Uint32 black = SDL_MapRGB(screen->format, 0, 0, 0);
	Uint32 white = SDL_MapRGB(screen->format, 255, 255, 255);
	Uint32 dblue = SDL_MapRGB(screen->format, 0, 0, 50);
	bump[0] = new Bumper(100,300,400,20,dblue);
	h1 = new Hero(400,200,10,3,create_rectangle(10*2,10*2,white),1,bump);
	h2 = new Hero(200,200,10,3,create_rectangle(10*2,10*2,black),1,bump);
	printf("=Heroes Created=\n");
	gameLoop();
}
int main(int argc, char* argv[]){
	printf("=Ready=\n");
	if(initDisplay()!=0)
		return 1;
	printf("=Display Initialized=\n");
	int k = 0;
	do {
		newGame();
	} while (gamestate != quitting);
	printf("done, gamestate= %d\n", gamestate);
	SDL_FreeSurface(background);
	SDL_Quit();
	return 0;
}
void display(){
	blit(0,0,background,screen);
	h1->draw(screen);
	h2->draw(screen);
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
