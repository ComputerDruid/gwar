#ifdef BUMPER_H
#else
#define BUMPER_H
#include "SDL/SDL.h"
#include "Item.h"
//#include "Hero.h"
class Bumper{
	public:
		bool passthrough;
		SDL_Rect bouds;
		Bumper(double,double,double,double,SDL_Surface*);
		Bumper(double,double,double,double,bool,SDL_Surface* s);
		void init(double,double,double,double,bool,SDL_Surface* s);
		double getX();
		double getY();
		double getWidth();
		double getHeight();
		void draw(SDL_Surface*);
		//bool check(Hero);
		bool check(double,double,double,double,double);
		bool onBumper(Item*);
		bool overBumper(Item*);
		bool onBumper(double,double,double);
		bool overBumper(double,double,double);
	private:
		double x;
		double y;
		double width;
		double height;
		bool vIntersects(Item*);
		bool hIntersects(Item*);
		bool vIntersects(double,double,double);
		bool hIntersects(double,double,double);
		bool check2(double,double,double);
		bool cornerCheck(Item*);
		bool cornerCheck(double,double,double);
		SDL_Surface* suf;

};
#endif
