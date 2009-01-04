class Item {
	public:
		Item(double,double,int/*,color*/);
		double x;
		double y;
		int r;
		void draw();
		bool intersects(Item);
};
