class Bumper{
	public:
		bool passthrough;
		SDL_Rect bouds;
		Bumper(double,double,double,double);
		Bumper(double,double,double,double,bool);
		double getX();
		double getY();
		double getWidth();
		double getHeight();
		void draw(SDL_Surface*);

