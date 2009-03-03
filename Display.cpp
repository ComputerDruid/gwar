#include "SDL/SDL.h"
void blit(int x, int y, SDL_Surface* source, SDL_Surface* dest){
	SDL_Rect offset;
	offset.x=x;
	offset.y=y;
	SDL_BlitSurface(source, NULL, dest, &offset );
}
SDL_Surface* create_rectangle(int width, int height){
	SDL_Surface* temp = SDL_CreateRGBSurface(SDL_SWSURFACE,width,height,32,0,0,0,0);
}
SDL_Surface* create_rectangle(int width, int height, Uint32 color){
	SDL_Surface* temp = create_rectangle(width,height);
	SDL_Rect size;
	size.x=0; size.y=0; size.w=width; size.h=height;
	SDL_FillRect(temp, &size, color);
	return temp;
}
