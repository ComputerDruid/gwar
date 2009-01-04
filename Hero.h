#include "Item.h"
class Hero : public Item {
	public:
		Hero(double,double,int);
		double dy;
		double dx;
		void update();
	private:
		//color c
};
