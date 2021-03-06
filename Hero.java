	import javax.swing.*;
	import java.awt.*;
	 class Hero	extends Item{
		public static final int	NONE = 0, BORDER = 1, BUMPER = 2, FIGHT = 3;
		public double dy,dx;
		private double	SPEED	= 3;
		private GWar world;
		int jumps=0;
		private int	sparkindex=0;
		private int	sparkcounter=0;
		private int	tiedcount=0;
		public double mass=1;
		private boolean lacceleration;
		private boolean racceleration;
		private boolean onbumper =	false;
		private final static	boolean DEBUG=false;
		private double[] xspark	= new	double[5];
		private double[] yspark	= new	double[5];
		private int	xflash;
		private int	yflash;
		private boolean flashleft;
		private int	flashcount = 0;
		private int	lastPositionValue=-500,	actionType,	actionValue=-1;
		 public Hero(double x, double	y,	int r,GWar tp,	java.awt.Color	c,double	mass){
			super(x,y,r,c);
			dy=0;
			dx=0;
			world=tp;
			this.mass =mass;
			SPEED=(3/mass);
			actionType = NONE;
			actionValue = -1;
		}
		 private	void addsparkle(){
			for (int	k = xspark.length-1;k>0;k--){
				xspark[k]=xspark[k-1];
				yspark[k]=yspark[k-1];
			}
			xspark[0]=x;
			yspark[0]=y;
		}
		public void reset(){
			actionType = NONE;
			actionValue = -1;
			lastPositionValue = -500;
		}
		 public void update(){
			flashcount--;
			onbumper=onBumper();
			if	(tiedcount>0)
				tiedcount --;
			sparkcounter++;
			if	(sparkcounter>=3){
				sparkcounter=0;
				addsparkle();
			}
			if	(onbumper){
			//System.out.println("onbumper");
				if	(racceleration==lacceleration){
					if(tiedcount==0) 
						dx=dx*.975;//friction?
				}
				else{
				
				}
				if(dy>=0)
					jumps	= 0;
			
				if	(Math.abs(dx) < .05)
					dx=0;	
			}
			if(tiedcount==0){
				if(lacceleration&&dx>-SPEED)
					dx	-=	1;
				if(racceleration&&dx<SPEED)
					dx	+=	1;
			}
			if	(checkVBumper()){
				dy	= Math.abs(dy);
			}
			else if (checkHBumper()){
				dx	= -dx;
			}
			lX	= x;
			lY	= y;
			y += dy;
			x += dx;
			onbumper=onBumper();
		}
		 public double	nearestBorder(Hero h){
			double wL =	h.x,wR =	GWar.WIDTH-h.x;
			double hT =	h.y,hB =	GWar.HEIGHT-h.y;
			double min=wL;
			if(wR	< min)
			{
				min =	wR;
			}
			if(hT	< min)
			{
				min =	hT;
			}
			if(hB	< min)
			{
				min =	hB;
			}
			return min;
		}
		 public int	getBorderValue(Hero h){
			double minDist	= nearestBorder(h);
			if(minDist<40)
			{
				return -6;
			}
			else if(minDist<80)
			{
				return -3;
			}
			else if(minDist <	120)
			{
				return 3;
			}
			else
			{
				return 6;
			}
		}
		 public int	getBumperValue(Hero h){
			if(h.onBumper())
			{
				return 6;
			}
			else if(h.overBumper())
			{
				return 3;
			}
			else
			{
				return -6;
			}
		}
		 public int	getPositionValue(Hero h){
			int val = 0;
			val+=getBorderValue(h);
			val+=getBumperValue(h);
			return val;
		}	
		 public int	getProblem(Hero	h){
			int bord	= getBorderValue(h);
			int bump	= getBumperValue(h);
			if(bump<0)
			{
				return BUMPER;
			}
			if(bord<0)
			{
				return BORDER;
			}
			return NONE;
		}
		 public void	fixBorder(){
			actionType = BORDER;
			actionValue	= -1;
		}
		 public void	fixBumper(){
			actionType = BUMPER;
			actionValue	= -1;
		}
		public void setTarget(){
			actionType = FIGHT;
			actionValue = findNickTarget(this);
		}
		public void fixBumperProblem(){
			if(actionValue == -1)
			{
				actionValue = nearestTargetBumper(0,0,this,false);
			}
			else
			{
				try
				{
					Bumper tar = world.bump[actionValue];
					goToTarget(tar);
					Bumper on =	onWhatBumper();
					if(on!=null && on.x == tar.x && on.y == tar.y)
					{
							stop();
							actionType	= NONE;
					}
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
				}
			}
		}
		 public void fixBorderProblem(){
			if(actionValue	==	-1)
			{
				actionValue	= nearestTargetBumper(GWar.WIDTH/2,GWar.HEIGHT/2,this,true);
			}
			else
			{
				try
				{
					Bumper tar = world.bump[actionValue];
					goToTarget(tar);
					Bumper on =	onWhatBumper();
					if(on!=null && on.x == tar.x && on.y == tar.y)
					{
							stop();
							actionType	= NONE;
					}
				}
				catch(ArrayIndexOutOfBoundsException e)
				{
					System.out.println("Wrong index!");
				}
			}
		}
		 public void goToTarget(Bumper b)
		{
			double cX =	b.x +	b.width/2, dif	= cX-x;
			if(dif >	3)
			{
				racceleration = true;
				lacceleration = false;
			}
			else if(dif	< -3)
			{
				lacceleration = true;
				racceleration = false;
			}
			if(b.y <	y && dy >= 0)
			{
				jump();
			}
		}
		public void checkVelocityProblem()
		{
			if(dx>9)
			{
				if(onWhatBumper() instanceof SpeedBumper)
				{
					jump();
				}
				attackLeft();
			}
			else if(dx>6&&GWar.WIDTH-x<50)
			{
				attackLeft();
			}
			if(dx<-9)
			{
				if(onWhatBumper() instanceof SpeedBumper)
				{
					jump();
				}
				attackRight();
			}
			else if(dx<-6&&x<50)
			{
				attackRight();
			}
			if(dy<-8&&overBumper()&&y<50)
			{
				fastFall();
			}
		}
		 public void nickAI(){
			int position =	getPositionValue(this);
			int problem	= NONE;
			checkVelocityProblem();
			if(actionType == NONE)
			{
				problem = getProblem(this);
			}
			else if(actionType == BORDER)
			{
				fixBorderProblem();
			}
			else if(actionType == BUMPER)
			{
				fixBumperProblem();
			}
			if(problem == BORDER)
			{
				fixBorder();
			}
			else if(problem == BUMPER)
			{
				fixBumper();
			}
			else if(problem == NONE)
			{
			//	setTarget();
			}
			/*if(position < lastPositionValue)
			{
				reverseLastAction();
			}*/
			lastPositionValue	= position;
		}
		public int	findNickTarget(Hero h){
			int mVal = 9999,value,distValue=0, lifeValue, totValue;
			int min=-1;
			Hero t;
			for(int k =0; k <	world.p.length;k++)
			{
				t = world.p[k];
				distValue = (int)(distance(h,t.x,t.y)/50);
				value = getPositionValue(t);
				lifeValue = world.TOTALLIVES-world.lives[k];
				totValue = distValue+value+lifeValue;
			/*	System.out.println("---- " + k+" ----");
				System.out.println("Dif: "+(b.y-h.y));
				System.out.println(dist1);
				System.out.println(dist2);
				System.out.println(distTot);*/
				if(totValue<mVal)
				{
					mVal = totValue;
					min =	k;
				}
			}
			return min;
		}
		public double getMaxXDist(double tx, double ty, Hero h){
			int lJumps = 2-jumps;
			if(lJumps==2)
			{
				return 170;
			}
			else if(lJumps==1)
			{
				return 90;
			}
			else if(lJumps==0)
			{
				return 50;
			}
			return 0;
		}
		 public int	nearestTargetBumper(double	tx,double ty, Hero h, boolean both){
			double mDist =	999999999,dist1,dist2=0,distTot;
			int min=-1;
			Bumper b;
			for(int k =0; k <	world.bump.length;k++)
			{
				b = world.bump[k];
				dist1	= distance(h,b.x+b.width/2,b.y);
				if(b.y - h.y < -(2-jumps)*55)
				{
					dist1*=5;
				}
				if(Math.abs(b.x+b.width/2-h.x)>getMaxXDist(b.x,b.y,h))
				{
					dist1*=5;
				}
				if(both)
				{
					dist2	= distance(tx,ty,b.x+b.width/2,b.y)*1.5;
				}
				distTot = dist1+dist2;
			/*	System.out.println("---- " + k+" ----");
				System.out.println("Dif: "+(b.y-h.y));
				System.out.println(dist1);
				System.out.println(dist2);
				System.out.println(distTot);*/
				if(distTot<mDist)
				{
					mDist = distTot;
					min =	k;
				}
			}
			return min;
		}
		 public void ai(){
			if(!overBumper()&&!onBumper()){
				if(dy>=0)
					jump();
				Bumper goal	= world.bump[0];
				double tdistance=distance(this, goal.getX(),goal.getY());
				for (int	k = 0; k	<world.bump.length;k++){
					if(distance(this,	world.bump[k].getX(),world.bump[k].getY())<tdistance){
						goal=world.bump[k];
						tdistance=distance(this, world.bump[k].getX(),world.bump[k].getY());
					}
					if(distance(this,	world.bump[k].getX()	+ world.bump[k].getWidth(),world.bump[k].getY())<tdistance){
						goal=world.bump[k];
						tdistance=distance(this, world.bump[k].getX()+world.bump[k].getWidth(),world.bump[k].getY());
					}
				}
				if(goal.getX()>x){
					racceleration=true;
					lacceleration=false;
					if(goal.getY()>y)
						attackRight();
				}
				else{
					racceleration=false;
					lacceleration=true;
					if(goal.getY()>y)
						attackLeft();
				}
			
			}
			else{
				Hero target	= findTarget();
				if	(target ==null) 
					return;
				if((Math.abs(target.x-x)<r)&&(y<target.y)){
					fastFall();
				}
				if	(target.x<x){
					lacceleration=true;
					racceleration=false;
				}
				else{
					lacceleration=false;
					racceleration=true;
				}
				if(target.y<y&&dy>=0)
					jump();
				if(Math.abs(target.y-y)<5)
					if	(target.x<x)
						attackLeft();
					else
						attackRight();
			}
		}
		public void stop(){
			dx=0;
			racceleration = false;
			lacceleration = false;
		}
		 private	Hero findTarget(){
			Hero target	= null;
			double distance =	-1;
			for (int	k = 0; k< world.p.length; k++){
				if	(target==null){
					target=world.p[k];
					if(target==null)
						continue;
					distance	= distance(this,target);
					if	(target==this)
						target=null;
				}
				Hero ptarget =	world.p[k];
				if(ptarget!=null){
					double pdist =	distance(this,ptarget);
					if(ptarget!=this&&pdist<distance){
						distance=pdist;
						target=ptarget;
					}
				}
			}
			return target;
		}
		 public void moveLeft(){
			lacceleration=true;
		}
		 public void attackLeft(){
			if(tiedcount==0){
				dx-=2*SPEED;
				tiedcount =	15;
				xflash=(int)x;
				yflash=(int)y;
				flashcount=5;
				flashleft=true;
			}
		}
		 public void unMoveRight(){
			racceleration=false;
		}
		 public void unMoveLeft(){
			lacceleration=false;
		}
		 public void moveRight(){
			racceleration=true;
		}
		 public void attackRight(){
			if(tiedcount==0){
				dx+=2*SPEED;
				tiedcount =	15; 
				flashleft=false;
				xflash=(int)x;
				yflash=(int)y;
				flashcount=5;
			}
		}
		 public void jump(){
			if(tiedcount==0)
				if	(jumps <	2){
					dy	= -2.5*SPEED;
					jumps++;	
					if(!onbumper)
						dx=0;	
				}
		}
		 public void fastFall(){
			if(tiedcount==0){
				if	(onBumper()){
					if	(onWhatBumper().passthrough){
						y++;
						dy=10;
					}
				}
				else if ((dy <10)&&(jumps<2)){
					dy=10;
					jumps++;	
					dx=0;
				}
			}
		}
		 public void gravity(){
			if(!onBumper())
				dy+=.5;
		}
		 public boolean overBumper(){
			boolean temp =	false;
			for(int k =	0;k<world.bump.length;k++){
				if(world.bump[k].overBumper(this)){
					temp = true;
					break;
				}
			}
			return temp;
		}
		 public boolean checkHBumper()
		{
			if	(onBumper())
				return false;
			else
				return checkBumper(dx,0)!=-1;
		}
		 public boolean checkVBumper()
		{
			int k	= checkBumper(0,dy);
			if	(k!=-1){
				if	(y<world.bump[k].y){
					y = world.bump[k].y-r;
					dy	= 0;
					return false;	 
				}
				return true;
			}
			return false;
		}
		 public boolean onBumper(){
			for(int k=0;k<world.bump.length;k++)
				if	(world.bump[k].onBumper(this)){
					return true;
				}
			return false;
		}
		 public Bumper	onWhatBumper(){
			for(int k=0;k<world.bump.length;k++)
				if	(world.bump[k].onBumper(this)){
					return world.bump[k];
				}
			return null;
		}
		 public int	checkBumper(double pdx,	double pdy){
			for(int k=0;k<world.bump.length;k++)
				if	(checkBumper(world.bump[k],pdx,pdy))
					return k;
			return -1;
		}
		 public boolean checkBumper(Bumper b,double pdx, double pdy){
			if	((b.overBumper(x+pdx,y,r)||b.onBumper(this))&&(y+r+pdy>b.y)){
				y=	b.y-r; 
				dy=0;
				return false;	
			}
			else if (b.check(x,y,pdx,pdy,r)){
			
				return true;
			}
			return false;
		}
		 public boolean onScreen(int width,	int height)
		{
			if(x-r>0&&x+r<width&&y-r>0&&y+r<height)
				return true;
			return false;
		}
		 public void checkPlayer(Hero	p){
			if	(intersects(p)){
			/*if(y<p.y){
			  dy = Math.min(dy-3,-3);
			}
			else if(p.y<y){
			dy	= Math.max(dy+3,3);
			}
			
			if	(p.x<x){
			dx	+=5;
			dy	-=1;
			}
			else if (x<p.x){
			dx	-=5;
			dy	-=1;
			}
			*/
				double angle =	Math.atan2(p.y-y,p.x-x)+Math.PI;
			
			//dx+=((1/mass)*8*(p.mass)/distance(this,p))*Math.cos(angle);
			//dy+=((1/mass)*8*(p.mass)/distance(this,p))*Math.sin(angle);
			//double	dxAdd
			
				double phi = angle;
			
			//double	term = Math.PI/180;
				double v1i = Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2));
				double v2i = Math.sqrt(Math.pow(p.dx,2)+Math.pow(p.dy,2));
				double ang1	=Math.atan2(dy,dx);
				double ang2	= Math.atan2(p.dy,p.dx);
			
			
				double v1xr	= v1i*Math.cos((ang1-phi)); 
				double v1yr	= v1i*Math.sin((ang1-phi)); 
				double v2xr	= v2i*Math.cos((ang2-phi)); 
				double v2yr	= v2i*Math.sin((ang2-phi));
			
				double v1fxr =	((mass-p.mass)*v1xr+(2*p.mass)*v2xr)/(mass+p.mass);
				double v2fxr =	((2*mass)*v1xr+(p.mass-mass)*v2xr)/(mass+p.mass);
				double v1fyr =	v1yr;	
				double v2fyr =	v2yr;	
			
				double v1fx	= Math.cos(phi)*v1fxr+Math.cos(phi+Math.PI/2)*v1fyr;
				double v1fy	= Math.sin(phi)*v1fxr+Math.sin(phi+Math.PI/2)*v1fyr;
				double v2fx	= Math.cos(phi)*v2fxr+Math.cos(phi+Math.PI/2)*v2fyr;
				double v2fy	= Math.sin(phi)*v2fxr+Math.sin(phi+Math.PI/2)*v2fyr; 
			
				dx	= v1fx;
				dy	= v1fy;
				p.dx = v2fx;
				p.dy = v2fy;
			//System.out.println("DX: "+dx);	  
			//System.out.println("DY: "+dy);
			//System.out.println("P	DX: "+p.dx);	
			//System.out.println("P	DY: "+p.dy);
				double newx	= (p.x+x)/2+Math.cos(angle)*(r+1);
				double newy	= (p.y+y)/2+Math.sin(angle)*(r+1);
				p.x =	(p.x+x)/2+Math.cos(angle+Math.PI)*(p.r+1);
				p.y =	(p.y+y)/2+Math.sin(angle+Math.PI)*(p.r+1);
				x=newx;
				y=newy;
				tiedcount+=2;
			}
		}
	
		 public static	void main(String[] aahrghs){
			GWar.main(aahrghs);
		}
	
	/*public	void goOnScreen(int n)
	  {
	  if (x-r<=0)
	  {
	  x+=SPEED;
	  }
	  if (y-r<=0)
	  {
	  y+=SPEED;
	  }
	  if (y+r>=n)
	  {
	  dy = -.75*Math.abs(dy);
	
	  }
	  if (x+r>=n)
	  {
	  x-=SPEED;
	  }
	  }*/	  
		 public static	double distance(Item	i1, Item	i2){
			return Math.sqrt(Math.pow(i1.x-i2.x,2)+Math.pow(i1.y-i2.y,2));
		}
		 public static	double distance(Item	i1, double x2,	double y2){
			return Math.sqrt(Math.pow(i1.x-x2,2)+Math.pow(i1.y-y2,2));
		}
		 public static	double distance(double x1,	double y1, double	x2, double y2){
			return Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
		}
		 public void draw(java.awt.Graphics	g){
			super.draw(g,world.xScale,world.yScale);
			if	(flashcount>0){
				if(world.sparkles){
					Color[] colors	= {Color.white,Color.blue,Color.yellow};
					int ranColor,ranX,ranY;
					for(int a=0;a<10;a++){
						ranColor	 =	(int)(Math.random()*colors.length);
						ranX = (int)(Math.random()*(this.r*2+10))-5;
						ranY = (int)(Math.random()*(this.r*2+10))-5;
						g.setColor(colors[ranColor]);
						g.fillOval((int)((this.lX-this.r+ranX-1)*world.xScale),(int)((this.lY-this.r+ranY-1)*world.yScale),(int)(2*world.xScale),(int)(2*world.yScale));
					}
				}
				else{
					g.setColor(new	java.awt.Color(255,75,75));
					if(!flashleft)
						g.fillPolygon(new	int[]{(int)(xflash*world.xScale),(int)((xflash-r)*world.xScale),(int)((xflash-(r/2))*world.xScale),(int)((xflash-r)*world.xScale)},new	int[]{(int)(yflash*world.yScale),(int)((yflash+r)*world.yScale),(int)(yflash*world.yScale),(int)((yflash-r)*world.yScale)},4);
					else
						g.fillPolygon(new	int[]{(int)(xflash*world.xScale),(int)((xflash+r)*world.xScale),(int)((xflash+(r/2))*world.xScale),(int)((xflash+r)*world.xScale)},new	int[]{(int)(yflash*world.yScale),(int)((yflash+r)*world.yScale),(int)(yflash*world.yScale),(int)((yflash-r)*world.yScale)},4);
				
				}
			}
			if(DEBUG==true){
				g.setColor(new	java.awt.Color(255,255,0));
				g.drawLine((int)(x*world.xScale),(int)(y*world.yScale),(int)((x+(dx*3))*world.xScale),(int)((y+(dy*3))*world.yScale));
			}
		}
	/*public	void draw(java.awt.Graphics g){
	  super.draw(g);
	  g.setColor(java.awt.Color.WHITE);
	  for	(int k =	0;	k < xspark.length;k++)
	  g.fillRect((int)xspark[k]-3,(int)yspark[k]-3,6,6);
	  }*/
	}
