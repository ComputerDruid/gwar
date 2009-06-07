#ifdef HERO_H
#else
#define HERO_H
#include "item.h"
#include "bumper.h"
#include "SDL/SDL.h"
class Hero : public Item {
	public:
		Hero(double,double,int,int,SDL_Surface*,int,Bumper**);
		double dy;
		double dx;
		double mass;
		void update();
		void jump();
		void draw(SDL_Surface*);
		void setAccel(int);
		void attack();
		void fastFall();
		bool onScreen(int,int);
		void checkPlayer(Hero*);
	private:
		//color c
		double startx;
		double starty;
		bool overBumper();
		bool checkHBumper();
		bool checkVBumper();
		bool onBumper();
		int onWhatBumper();
		int checkBumper(double,double);
		bool checkBumper(Bumper,double,double);
		bool offScreen();
		bool intersects(Hero*);
		int jumps;
		int tiedcount;
		int accel;
		int xflash;
		int yflash;
		int flashcount;
		int lives;
		Bumper** bump;
		int numBumpers;
		static const int SPEED=6;
		SDL_Surface* sprite;
};
#endif
