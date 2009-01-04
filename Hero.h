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
		void setAccel(int);
		void attack();
		void fastFall();
	private:
		//color c
		int jumps;
		int tiedcount;
		int accel;
		static const int SPEED=3;
		SDL_Surface* sprite;
};
