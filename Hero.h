#include "Item.h"
#include "SDL/SDL.h"
class Hero : public Item {
	public:
		Hero(double,double,int,SDL_Surface*);
		double dy;
		double dx;
		void update();
		void jump();
		void draw(SDL_Surface*);
	private:
		//color c
		int jumps;
		int tiedcount;
		static const int SPEED=3;
		SDL_Surface* sprite;
};
