#include "SDL/SDL.h"
void blit(int x, int y, SDL_Surface* source, SDL_Surface* dest){
	SDL_Rect offset;
	offset.x=x;
	offset.y=y;
	SDL_BlitSurface(source, NULL, dest, &offset );
}
SDL_Surface* create_rectangle(int width, int height){
	//SDL_Surface* temp = 
}
