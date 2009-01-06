#ifdef ITEM_H
#else
#define ITEM_H
class Item {
	public:
		Item(double,double,int/*,color*/);
		double x;
		double y;
		int r;
		void draw();
		bool intersects(Item);
};
#endif
