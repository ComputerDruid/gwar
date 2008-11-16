    public class SpeedBumper extends Bumper
   {
      double xVel,yVel;
       public SpeedBumper(double x, double y, double width, double height, double xV, double yV)
      {
         super(x,y,width,height);
			xVel = xV;
			yVel = yV;
      }            
       public double getXVelocity()
      {
         return xVel;
      }
       public double getYVelocity()
      {
         return yVel;
      }
   }
