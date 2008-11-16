   //Name: Daniel Johnson   License: GNU GPL 2   Date: Who Knows??
   import javax.swing.*;
   import java.awt.*;
   import java.awt.event.*;
   import java.awt.image.*;
   import javazoom.jl.player.*; 
   import java.io.InputStream;
   import java.io.FileInputStream;
    public class GWar extends JPanel{
      private static final int RADIUS = 25; 
      public static final int WIDTH = 600;
      public static final int HEIGHT = 400;
      public double xScale=1024.0/WIDTH;
      public double yScale=768/HEIGHT;
      public static final Color[] PCOLOR = {new Color(200,0,200),Color.GREEN,Color.BLUE.brighter(),Color.RED,Color.WHITE,Color.GREEN.darker().darker()};
      private int TOTALPLAYERS=2;
      private int TOTALLIVES=5;
      private static final Color BACKGROUND = java.awt.Color.BLACK;
      private BufferedImage myImage;
      private Graphics myBuffer;
      private Timer t;
      public Hero[] p = new Hero[TOTALPLAYERS];
      private int[] lives = new int[TOTALPLAYERS];
      private int numalive = TOTALPLAYERS;  
      public Bumper[] level1={new Bumper(100,300,400,15),new Bumper(150,250,100,5,true),new Bumper(350,250,100,5,true), new Bumper(250,200,100,5,true)};
      public Bumper[] level2={new Bumper(100,300,400,25),new Bumper (125,325,350,25)};
      //public Bumper[] level2={/*A*/new Bumper(200,175,100,10), /*B*/new Bumper(150,250,100,10),/*C*/ new Bumper(225,325,60,10,true), /*D*/new Bumper(75, 400, 300, 15,true),
//         							/*V*/new Bumper(350,325,30,10),/*E*/new Bumper(350,75,20,175),/*N*/ new Bumper(400,500,50,15), /*M*/new Bumper(480, 440 ,40,10),
//         							/*L*/new Bumper(510, 375,80,10, true), /*J*/new Bumper(430,290,40,10,true),/*F*/new Bumper(410,160,40,5,true),/*H*/new Bumper(450,230,50,10,true),
//         							/*G*/new Bumper(530,125,300,20),/*I*/new Bumper(600,230,60,15),/*K*/new SpeedBumper(640,320,60,10,-5,0), /*U*/new SpeedBumper(700,600,300,20,0,-20), /*X*/new SpeedBumper(100,575,200,20,0,-20),
//         							/*Y*/new SpeedBumper(450,675,200,10,0,-15)};
								
      //public Bumper[] level2={new Bumper(100,300,400,25),new Bumper (125,325,350,25)};
      public Bumper[] level3={new Bumper(250,200,100,150),new Bumper(50,300,150,10),new Bumper(400,300,150,10)};
      public Bumper[] level4={new Bumper(100,150,100,10,true),new Bumper(400,150,100,10,true),new Bumper(250,200,100,5,true), new SpeedBumper(50,300,75,5,0,-15), new SpeedBumper(475,300,75,5,0,-15),new Bumper(150,250,50,5,true),new Bumper(400,250,50,5,true),new Bumper(150,350,100,10,true),new Bumper(350,350,100,10,true)};
      public Bumper[] bump=level1;   
      public boolean[] isAI = new boolean[]{true, true, false};    
      public KListener kl = new KListener();
      private int running = 2; //0-Stopped 1-Running 2-Menu 3-Paused
      private boolean fullscreen = false;
      private MainMenu mm;
      private JFrame window;
      private InputStream music;
      private boolean ismusic=true;
      public boolean sparkles=false;
      Player player;
      PlayerThread pt;
       public GWar(){
         ismusic=true;
         setupFrame();
         initMusic();
         TOTALPLAYERS = 2;
         p = new Hero[TOTALPLAYERS];
         lives = new int[TOTALPLAYERS];
         t = new Timer(50, 
                new ActionListener(){
                   public void actionPerformed(ActionEvent e){
                     update();
                  } 
               });
         mm = new MainMenu();
         setFocusable(true);
         addKeyListener(kl);
        //newGame();
         playMusic(music);
         t.start();
         mm.show();
      }
       public void initMusic(){
         try {
            music = new FileInputStream("Chrono_Symphonic_23_The_Last_Stand.mp3");
         } 
             catch(Exception e){
               System.out.println("Music failed to load:"+e.getMessage());
            }
      }
       public void playMusic(InputStream in){
         try{
            player = new Player(in);
            pt = new PlayerThread();
            pt.start();
         }
             catch(Exception e){
               System.out.println("Music failed to play");
            }
      }
       public void stopMusic(){
         if(player!=null)
            player.close();
      }
   
       public void newGame(){
         p = new Hero[TOTALPLAYERS];
         lives = new int[TOTALPLAYERS];
         numalive = TOTALPLAYERS;
         for(int k = 0; k< lives.length;k++)
            lives[k]=TOTALLIVES+1;
         running = 1;
         for(int k = 0; k< p.length;k++)
            respawn(k);
         for(int k = 0; k< lives.length;k++)
            lives[k]=TOTALLIVES;
         display();
         t.start();
      }
       public void newGame(int lives,int humanPlayers, int numAI){
         TOTALPLAYERS=humanPlayers+numAI;
         TOTALLIVES=lives;
         isAI = new boolean[TOTALPLAYERS];
         for (int k =humanPlayers;k<humanPlayers+numAI;k++)
            isAI[k]=true;
         newGame();
      }
       public void newBackgroundGame(){
         newGame(5,0,2);
         running = 2;
      }
       public void respawn(int index){
         if (index==0){
            p[0] = new Hero (400,200,10,this,PCOLOR[index],1);
         }
         else if (index==1){
            p[1] = new Hero (200,200,10,this,PCOLOR[index],1);
         }
         else if (index==2)
            p[2] = new Hero (300,150,10,this,PCOLOR[index],1);
         else
            p[index] =  new Hero (400,100,10,this,PCOLOR[index],1);
         if(running!=2)
            lives[index]--;
         if (lives[index] == 0){
            p[index]=null;
            numalive--;
         }
         if (numalive<=1)
            for(int k = 0; k<p.length;k++)
               if (p[k]!=null){
                  gameOver(k);
                  break;
               }
      }
       public void update(){
         if(ismusic&&pt!=null)
            if (!pt.isAlive()){
               player.close();
               initMusic();
               playMusic(music);
            }
         if(!hasFocus())
            requestFocus();
         if(running==1||running==2){
            for (int k = 0; k< p.length; k++){
               if (p[k]!=null)
                  p[k].update();
            }
            Bumper on;
            SpeedBumper sOn;
            for (int k = 0; k< p.length; k++){
               if (p[k]!=null)
                  p[k].gravity();
               if (p[k]!=null)
               {
                  if(p[k].onBumper())
                  {
                     on = p[k].onWhatBumper();
                     if(on instanceof SpeedBumper)
                     {
                        sOn = (SpeedBumper)on;
                        p[k].dx+=sOn.getXVelocity();
                        p[k].dy+=sOn.getYVelocity();
                     }
                  }
               }
            }
            for (int k = 0; k< p.length; k++)
               for (int n = k+1; n<p.length;n++)
                  if((k!=n)&&(p[k]!=null)&&(p[n]!=null))
                     p[k].checkPlayer(p[n]);
            for (int k = 0; k< p.length; k++){
               if (p[k]!=null)
                  if(!p[k].onScreen(WIDTH,HEIGHT))
                     respawn(k);
            } 
            kl.update();
            for (int k = 0; k<TOTALPLAYERS; k++)
               if(isAI[k])
                  if(p[k]!=null)
                     p[k].ai();
         }
         if(running==1)
            display();
         else if (running == 2){
            display();
           //mm.draw(myBuffer);
            mm.drawB();
            repaint();    
         }
      }
       public void display(){
         myBuffer.setColor(BACKGROUND);
         myBuffer.fillRect(0,0,(int)(WIDTH*xScale),(int)(HEIGHT*yScale));
         for (int k = 0; k< p.length; k++){
            if(p[k]!=null)
               p[k].draw(myBuffer);
         }
         for (int k=0;k<bump.length;k++)
         {
            bump[k].draw(myBuffer,xScale,yScale);
            myBuffer.setColor(Color.green);
            myBuffer.drawString(k+"",(int)(bump[k].getX()*xScale),(int)(bump[k].getY()*yScale)-2);
         }
         if(running==1){
            drawScore(0,WIDTH-30,30);
            drawScore(1,30,30);
            String temp;
            if (TOTALPLAYERS>=3){
               drawScore(2,30, HEIGHT-30);
               if (TOTALPLAYERS>=4){
                  drawScore(3,WIDTH-30,HEIGHT-30);
                  if(TOTALPLAYERS>=5){
                     drawScore(4,WIDTH/2-30,30);
                     if(TOTALPLAYERS>=6){
                        drawScore(5,WIDTH/2-30,HEIGHT-30);
                     }
                  }
               }
            }
         }
         repaint();
      }
       private void drawScore(int player,int xpos, int ypos){
         if(lives[player]<=0)
            return;
         myBuffer.setFont(new Font("Serif",Font.PLAIN,(int)(20*yScale)));
         myBuffer.setColor(PCOLOR[player]);
         myBuffer.fillOval((int)(xpos*xScale-(30*xScale*((double)(lives[player])/TOTALLIVES))/2),(int)(ypos*yScale-(30*yScale*((double)(lives[player])/TOTALLIVES))/2),(int)(30*xScale*((double)(lives[player])/TOTALLIVES)),(int)(30*yScale*((double)(lives[player])/TOTALLIVES)));
         myBuffer.setColor(new Color(255-PCOLOR[player].getRed(),255-PCOLOR[player].getGreen(),255-PCOLOR[player].getBlue()));
         myBuffer.drawString(lives[player]+"",(int)((xpos-5)*xScale),(int)((ypos+10)*yScale));
      
      }
       public void gameOver(int player){
         t.stop();
         running = 0;
         display();
         myBuffer.setColor(PCOLOR[player]);
         myBuffer.drawString("Game Over, Player " + (player+1) + " wins!!",(int)(((WIDTH/2.0)-125)*xScale),(int)((HEIGHT/2)*yScale));
         myBuffer.drawString("Press Esc for menu, Space to restart.",(int)(((WIDTH/2.0)-150)*xScale),(int)(((HEIGHT/2)+30)*yScale));
         repaint();
      }
       public void pause(){
         t.stop();
         running = 3;
         display();
         myBuffer.setColor(Color.white);
         myBuffer.drawString("Game Paused",(int)(((WIDTH/2.0)-75)*xScale),(int)((HEIGHT/2)*yScale));
         myBuffer.drawString("Press Esc for menu, Any other key to resume.",(int)(((WIDTH/2.0)-225)*xScale),(int)(((HEIGHT/2)+30)*yScale));
         repaint();
      }
       public void unpause(){
         running=1;
         t.start();
      }
       class MainMenu {
         //BufferedImage menubuffer = myImage;//new BufferedImage (WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
         //Graphics myBuffer = menubuffer.createGraphics();
         int selected=0;
         int numPlayers=1;
         int[] pNumPlayers = {1,2,3};
         int HPSel=5;
         int PlayerSizeSel=25;
         int PelletSizeSel=5;
         int LevelSel=1;
         int numAI=1;
         final int ListSize=9;
         final int spacebetweenlines = 25; 
         final int xoffset = 30;
         final int yoffset = 120;
         final int tabspace = 85;
      //final String[] menuTitles new String[]{"Start", "# Human Players:", "# AI Players:", "Lives:", "Level:", "Toggle Fullscreen Mode (experimental)", "Help", "Exit"};
      //final String[][] menuChoices = {{},{"0","1","2","3"},{"0","1","2","3"},{"1","2","3","4","5"},{"Battlefield","FD","new"},{},{}};
         boolean helpshowing = false;
         Image helpscreen;
         //Image background;
          MainMenu(){
            helpscreen = new ImageIcon(getClass().getClassLoader().getResource("helpscreen.PNG")).getImage();
            //background = new ImageIcon(getClass().getClassLoader().getResource("background.PNG")).getImage();
            myBuffer.setFont(new Font("SansSerif",Font.BOLD,(int)(15*yScale)));
            drawB();
         }
          void show(){
            newBackgroundGame();
            running=2;
         }
          /*void draw(Graphics g){
            if (helpshowing==true){
               g.drawImage(helpscreen,0,0,null);
            }
            else{
               g.drawImage(menubuffer,0,0,null);
            }
         }*/
          void drawB(){
            if (helpshowing==true){
               myBuffer.drawImage(helpscreen,0,0,(int)(WIDTH*xScale),(int)(HEIGHT*yScale),null);
            }
            else{
               int counter = 0;
               myBuffer.setFont(new Font("SansSerif",Font.BOLD,(int)(75*yScale)));
               myBuffer.setColor(new Color(0,255,255));
               myBuffer.drawString("GWar!",(int)(xoffset*xScale),(int)((spacebetweenlines+60)*yScale));
               myBuffer.setFont(new Font("SansSerif",Font.BOLD,(int)(15*yScale)));
            /*myBuffer.setColor(Color.BLACK);
            myBuffer.fillRect(0,0,WIDTH,HEIGHT);*/
            //myBuffer.drawImage(background,0,0,null);
               setSelectedColor(counter,selected);
               counter = 0;
               setSelectedColor(counter,selected);
               myBuffer.drawString("Start",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               counter = 1;
               setSelectedColor(counter,selected);
               myBuffer.drawString("# Human Players:",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(numPlayers,0);
               myBuffer.drawString("0",(int)(xScale*(tabspace*2+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(numPlayers,1);
               myBuffer.drawString("1",(int)(xScale*(tabspace*3+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(numPlayers,2);
               myBuffer.drawString("2",(int)(xScale*(tabspace*4+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(numPlayers,3);
               myBuffer.drawString("3",(int)(xScale*(tabspace*5+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               counter = 2;
               setSelectedColor(counter,selected);
               myBuffer.drawString("# AI Players:",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(numAI,0);
               myBuffer.drawString("0",(int)(xScale*(tabspace*2+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(numAI,1);
               myBuffer.drawString("1",(int)(xScale*(tabspace*3+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(numAI,2);
               myBuffer.drawString("2",(int)(xScale*(tabspace*4+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(numAI,3);
               myBuffer.drawString("3",(int)(xScale*(tabspace*5+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               counter = 3;
               setSelectedColor(counter,selected);
               myBuffer.drawString("Lives:",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(HPSel,1);
               myBuffer.drawString("1",(int)(xScale*(tabspace+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(HPSel,2);
               myBuffer.drawString("2",(int)(xScale*(tabspace*2+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(HPSel,3);
               myBuffer.drawString("3",(int)(xScale*(tabspace*3+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(HPSel,4);
               myBuffer.drawString("4",(int)(xScale*(tabspace*4+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(HPSel,5);
               myBuffer.drawString("5",(int)(xScale*(tabspace*5+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(HPSel,10);
               myBuffer.drawString("10",(int)(xScale*(tabspace*6+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               counter = 4;
               setSelectedColor(counter,selected);
               myBuffer.drawString("Level:",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(LevelSel,1);
               myBuffer.drawString("Totally Original",(int)(xScale*(tabspace+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(LevelSel,2);
               myBuffer.drawString("Even Ground",(int)(xScale*(tabspace*3+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(LevelSel,3);
               myBuffer.drawString("Overlook",(int)(xScale*(tabspace*5+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
		setSelectedColor(LevelSel,4);
		myBuffer.drawString("Islands",(int)(xScale*(tabspace*6+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               counter = 5;
               setSelectedColor(counter,selected);
               myBuffer.drawString("Toggle Fullscreen Mode",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               counter = 6;
               setSelectedColor(counter,selected);
               myBuffer.drawString("Music:",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(ismusic,true);
               myBuffer.drawString("On",(int)(xScale*(tabspace+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(ismusic,false);
               myBuffer.drawString("Off",(int)(xScale*(tabspace*2+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               counter = 7;
               setSelectedColor(counter,selected);
               myBuffer.drawString("Help",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               counter = 8;
               setSelectedColor(counter,selected);
               myBuffer.drawString("Exit",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
               setSelectedColor(counter,-1);
               myBuffer.setColor(new Color(0,255,255));
               myBuffer.drawString("Music by RoeTaKa -- OverClocked ReMix (www.ocremix.org)",(int)(xScale*(0+xoffset)),(int)(yScale*(HEIGHT-45)));
               myBuffer.drawString("By Daniel Johnson (johnmon2) for TJGames.org",(int)(xScale*(0+xoffset)),(int)(yScale*(HEIGHT-25)));
               myBuffer.drawString("Licensed under GNU GPL 2",(int)(xScale*(0+xoffset)),(int)(yScale*(HEIGHT-5)));
            }
            //drawPlayer(myBuffer,1,15,selected*spacebetweenlines+yoffset-7,90,0,25,true);
         }
          void setSelectedColor(int var1,int con1){
            if (var1==con1){
               myBuffer.setColor(Color.yellow);
            }
            else{
               myBuffer.setColor(Color.white);
            }
         }
          void setSelectedColor(boolean var1,boolean con1){
            if (var1==con1){
               myBuffer.setColor(Color.yellow);
            }
            else{
               myBuffer.setColor(Color.white);
            }
         }
          void moveUp(){
            selected--;
            if (selected < 0){
               selected=ListSize-1;
            }
            drawB();
         }
          void moveDown(){
            selected++;
            if (selected >= ListSize){
               selected=0;
            }
            drawB();
         }
         // void moveRight(){
      // 	
      // 	}
      // 	void moveLeft(){
      // 	
      // 	}
          void newGameCurrentSettings(){
            if(LevelSel==1)
               bump=level1;
            else if(LevelSel==2)
               bump=level2;
            else if(LevelSel==3)
               bump=level3;
	    else
		    bump=level4;
            if(numPlayers+numAI>=2)
               newGame(HPSel,numPlayers,numAI);
         }
          void enter(){
            if (helpshowing==true){
               helpshowing=false;
            }
            else{
               if (selected==0){
               
               // NGWin.setLocationRelativeTo(null);
               // NGWin.setVisible(true);
               // while (NGWin.getDone()!=true){
               // }
                  newGameCurrentSettings();
               
               }
               else if (selected==1){
                  if (numPlayers==0){
                     numPlayers=1;
                  }
                  else if (numPlayers==1){
                     numPlayers=2;
                  }
                  else if (numPlayers==2){
                     numPlayers=3;
                  }
                  else{
                     numPlayers=0;
                  }
                  drawB();
               }
               /*else if (selected==2){
                  if (p2selected==1){
                     p2selected=2;
                  }
                  else{
                     p2selected=1;
                  }
                  drawB();
               }
               else if (selected==3){
                  if (p3selected==1){
                     p3selected=0;
                  }
                  //else if (p3selected==2){
                     //p3selected=1;
                  //}
                  else{
                     p3selected=1;
                  }
                  drawB();
               }
               else if (selected==4){
                  if (p4selected==1){
                     p4selected=0;
                  }
                  //else if (p4selected==2){
                     //p4selected=1;
                  //}
                  else{
                     p4selected=1;
                  }
                  drawB();
               }*/
               else if (selected==2){
                  if (numAI==0){
                     numAI=1;
                  }
                  else if (numAI==1){
                     numAI=2;
                  }
                  else if (numAI==2){
                     numAI=3;
                  }
                  else if (numAI==3){
                     numAI=0;
                  }
                  drawB();
               }
               else if (selected==3){
                  if (HPSel==1){
                     HPSel=2;
                  }
                  else if (HPSel==2){
                     HPSel=3;
                  }
                  else if (HPSel==3){
                     HPSel=4;
                  }
                  else if (HPSel==4){
                     HPSel=5;
                  }
                  else if (HPSel==5){
                     HPSel=10;
                  }
                  else if (HPSel==10){
                     HPSel=1;
                  }
                  drawB();
               }
               else if (selected==4){
                  if (LevelSel==1){
                     LevelSel=2;
                  }
                  else if(LevelSel==2){
                     LevelSel=3;
                  }
                  else if (LevelSel==3){
                     LevelSel=4;
                  }
		  else if(LevelSel==4){
			  LevelSel=1;
		  }
                  if(LevelSel==1)
                     bump=level1;
                  else if(LevelSel==2)
                     bump=level2;
                  else if(LevelSel==3)
                     bump=level3;
                  else
                     bump=level4;
                  drawB();
               }
               else if (selected==5){
                  toggleFullscreen();
               }
               else if (selected==6){
                  ismusic=!ismusic;
                  if(!ismusic)
                     stopMusic();
               //else
               //playMusic(music);
                  drawB();
               }
               else if (selected==7){
                  helpshowing=true;
               }
               else if (selected==8){
                  /*gametimer.stop();
                  window.setVisible(false);
               //window.close();
                  window.dispose();
                  window=null;*/
                  System.exit(0);
               }
            }
         }
      }
   
       private class KListener extends KeyAdapter{
         private boolean p1left=false;
         private boolean p1right=false;
         private boolean p2left = false;
         private boolean p2right = false;
         private boolean p3left = false;
         private boolean p3right = false;
         private boolean p4left = false;
         private boolean p4right = false;
         private static final int P1JUMP=KeyEvent.VK_UP;
         private static final int P1LEFT=KeyEvent.VK_LEFT;
         private static final int P1RIGHT=KeyEvent.VK_RIGHT;
         private static final int P1DOWN=KeyEvent.VK_DOWN;
         private static final int P1ATTACK=KeyEvent.VK_SHIFT;
         private static final int P1ATTACKALT=KeyEvent.VK_CONTROL;
         private static final int P2JUMP=KeyEvent.VK_W;
         private static final int P2LEFT=KeyEvent.VK_A;
         private static final int P2RIGHT=KeyEvent.VK_D;
         private static final int P2DOWN=KeyEvent.VK_S;
         private static final int P2ATTACK=KeyEvent.VK_Q;
         private static final int P3JUMP=KeyEvent.VK_NUMPAD5;
         private static final int P3LEFT=KeyEvent.VK_NUMPAD1;
         private static final int P3RIGHT=KeyEvent.VK_NUMPAD3;
         private static final int P3DOWN=KeyEvent.VK_NUMPAD2;
         private static final int P3ATTACK=KeyEvent.VK_NUMPAD0;
         private static final int SPARKLE=KeyEvent.VK_0;
	 private boolean escpressed=true;
          public void keyPressed(KeyEvent e){
            if(running==3){
               if (e.getKeyCode()==KeyEvent.VK_ESCAPE){
                  if(!escpressed){
                     t.start();
                     mm.show();   
                     return;
                  }
               }
               else
                  unpause();
            }
            if(running==1){
               if (e.getKeyCode()==KeyEvent.VK_ESCAPE){
                  escpressed=true;
                  pause();
               }
	       else if(e.getKeyCode()==SPARKLE)
		       sparkles= !sparkles;
               else if(p[0]!=null)
                  if(e.getKeyCode()==P1JUMP){
                  //p1up=true;
                     p[0].jump();
                  }
                  else if(e.getKeyCode()==P1DOWN){
                     p[0].fastFall();
                  }
                  else if(e.getKeyCode()==P1RIGHT){
                     if (!p1right){
                        p1right=true;
                        p[0].moveRight();
                     }
                  }
                  else if(e.getKeyCode()==P1LEFT){
                     if (!p1left){
                        p1left=true;
                        p[0].moveLeft();
                     }
                  }
                  else if(e.getKeyCode()==P1ATTACK||e.getKeyCode()==P1ATTACKALT){
                     if(p1left)
                        p[0].attackLeft();
                     else if (p1right)
                        p[0].attackRight();
                  }
               if(p[1]!=null)
                  if(e.getKeyCode()==P2LEFT){
                     if(!p2left){
                        p2left=true;
                        p[1].moveLeft();
                     }
                  }
                  else if(e.getKeyCode()==P2RIGHT){
                     if(!p2right){
                        p2right=true;
                        p[1].moveRight();
                     }
                  }
                  else if(e.getKeyCode()==P2DOWN){
                     p[1].fastFall();
                  }
                  else if (e.getKeyCode()==P2JUMP)
                     p[1].jump();   
                  else if(e.getKeyCode()==P2ATTACK){
                     if(p2left)
                        p[1].attackLeft();
                     else if (p2right)
                        p[1].attackRight();
                  }
               if(TOTALPLAYERS>=3)
                  if(p[2]!=null)
                     if(e.getKeyCode()==P3LEFT){
                        if(!p3left){
                           p3left=true;
                           p[2].moveLeft();
                        }
                     }
                     else if(e.getKeyCode()==P3RIGHT){
                        if(!p3right){
                           p3right=true;
                           p[2].moveRight();
                        }
                     }
                     else if(e.getKeyCode()==P3DOWN){
                        p[2].fastFall();
                     }
                     else if (e.getKeyCode()==P3JUMP)
                        p[2].jump();   
                     else if(e.getKeyCode()==P3ATTACK){
                        if(p3left)
                           p[2].attackLeft();
                        else if (p3right)
                           p[2].attackRight();
                     }  
            
            }
            else if (running ==0){
               if(e.getKeyCode()==KeyEvent.VK_SPACE)
                  newGame();
               else if (e.getKeyCode()==KeyEvent.VK_ESCAPE){
                  t.start();
                  mm.show();   
               }
            }  
            else if (running ==2){
               if(e.getKeyCode()==KeyEvent.VK_ENTER)
                  mm.enter();
               else if(e.getKeyCode()==KeyEvent.VK_UP)
                  mm.moveUp();
               else if(e.getKeyCode()==KeyEvent.VK_DOWN)
                  mm.moveDown();
		else if(e.getKeyCode()==SPARKLE)
		       sparkles= !sparkles;

            }   
         
         }
          public void keyReleased(KeyEvent e){
            if (e.getKeyCode()==KeyEvent.VK_ESCAPE){
               escpressed=false; 
            }
            else if(p[0]!=null)
               if(e.getKeyCode()==P1RIGHT){
                  p1right=false;
                  p[0].unMoveRight();
               }
               else if(e.getKeyCode()==P1LEFT){
                  p1left=false;
                  p[0].unMoveLeft();
               }
            if(p[1]!=null)
               if(e.getKeyCode()==P2LEFT){
                  p2left=false;
                  p[1].unMoveLeft();
               }
               else if(e.getKeyCode()==P2RIGHT){
                  p2right=false;
                  p[1].unMoveRight();
               }
            if(TOTALPLAYERS>=3)
               if(p[2]!=null)
                  if(e.getKeyCode()==P3LEFT){
                     p3left=false;
                     p[2].unMoveLeft();
                  }
                  else if(e.getKeyCode()==P3RIGHT){
                     p3right=false;
                     p[2].unMoveRight();
                  }
         
         
         }
          public void update()
         {
           
         }
      }
       public class PlayerThread extends Thread
      {
          public void run()
         {
            try
            {
               player.play();
            }
                catch(Exception e)
               {
                  System.out.println("PlayerThread error.");
               }
         }
      }	
       public void paintComponent(Graphics g){
         g.drawImage(myImage, 0, 0, getWidth(), getHeight(), null);
      } 
       public static void main(String[] args){
         GWar game = new GWar();
      }
       public void setupFrame(){
         window = new JFrame ("GWar");
         window.setResizable(false);
         if(!fullscreen){
            setWindowed();
         }
         else{
            setFullscreen();
         }
      //System.out.println(frame.getWidth());
         window.setLocationRelativeTo(null);
         window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         window.setContentPane(this);
         //frame.pack();
         window.setVisible(true);
         Toolkit tk = Toolkit.getDefaultToolkit();
         xScale=(double)(tk.getScreenSize().width)/WIDTH;
         yScale=(double)(tk.getScreenSize().height)/HEIGHT;
         if(!fullscreen){
            xScale=1;
            yScale=1;
         }
         myImage =  new BufferedImage((int)(WIDTH*xScale), (int)(HEIGHT*yScale), BufferedImage.TYPE_INT_RGB);
         myBuffer = myImage.getGraphics();
         myBuffer.setColor(BACKGROUND);
         myBuffer.fillRect(0, 0, (int)(WIDTH*xScale), (int)(HEIGHT*yScale));
      }
       private void setFullscreen(){
         window.setUndecorated(true);
         Toolkit tk = Toolkit.getDefaultToolkit();
         window.setSize(tk.getScreenSize());
         GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();		  
         if(gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(window);
            System.out.println("Fulscreen Supported");
         }
         else
            System.out.println("Fullscreen not supported");
         fullscreen=true;
      }
       private void setWindowed(){
         window.setUndecorated(false);
         window.setVisible(true);
         Insets i = window.getInsets();
      //System.out.println(i.top+","+i.left+","+i.bottom+","+i.right);
      //System.out.println("Width:"+WIDTH);
         window.setVisible(false);	 
         window.setSize(WIDTH+i.left+i.right,HEIGHT+i.top+i.bottom);
         GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
         if(gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(null); //unfullscreen
         }
         fullscreen=false;
      }
       private void toggleFullscreen(){
         window.setVisible(false);
         window=null;
         fullscreen = !fullscreen;
         setupFrame();
         window.requestFocus();
      }
   }
