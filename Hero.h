#ifdef HERO_H
#else
#define HERO_H
#include "Item.h"
#include "Bumper.h"
#include "SDL/SDL.h"
class Hero : public Item {
	public:
		Hero(double,double,int,SDL_Surface*,int,Bumper*);
		double dy;
		double dx;
		void update();
		void jump();
		void draw(SDL_Surface*);
		void setAccel(int);
		void attack();
		void fastFall();
		bool onScreen(int,int);
	private:
		//color c
		bool overBumper();
		bool checkHBumper();
		bool checkVBumper();
		bool onBumper();
		int onWhatBumper();
		int checkBumper(double,double);
		bool checkBumper(Bumper,double,double);
		int jumps;
		int tiedcount;
		int accel;
		int xflash;
		int yflash;
		int flashcount;
		Bumper* bump;
		int numBumpers;
		static const int SPEED=3;
		SDL_Surface* sprite;
};
#endif
