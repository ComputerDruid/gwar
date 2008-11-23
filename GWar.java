//Name: Daniel Johnson   License: GNU GPL 2   Date: Who Knows??
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javazoom.jl.player.*; 
import java.io.InputStream;
import java.io.FileInputStream;
public class GWar extends JPanel{
	public static boolean DEBUG=false;
	private static final int RADIUS = 25; 
	public static final int WIDTH = 600;
	public static final int HEIGHT = 400;
	public static final int NUMLEVELS=4;
	public double xScale=1024.0/WIDTH;
	public double yScale=768/HEIGHT;
	public static final Color[] PCOLOR = {new Color(200,0,200),Color.GREEN,Color.BLUE.brighter(),Color.RED,Color.WHITE,Color.YELLOW,Color.GREEN.darker().darker(),Color.GRAY,new Color(130,37,14)};
	private int TOTALPLAYERS=2;
	public int TOTALLIVES=5;
	private static final Color BACKGROUND = java.awt.Color.BLACK;
	private BufferedImage myImage;
	private Graphics myBuffer;
	private Timer t;
	public Hero[] p = new Hero[TOTALPLAYERS];
	public int[] lives = new int[TOTALPLAYERS];
	private int numalive = TOTALPLAYERS;  

	//public Bumper[] level4={new SpeedBumper(100,150,100,10,3,0,true),new SpeedBumper(400,150,100,10,-3,0,true),new Bumper(250,200,100,5,true), new SpeedBumper(50,300,75,5,0,-15), new SpeedBumper(475,300,75,5,0,-15),new Bumper(150,250,50,5,true),new Bumper(400,250,50,5,true),new Bumper(150,350,100,10,true),new Bumper(350,350,100,10,true)};
	public Bumper[] bump;
	private Bumper[][] levels=new Bumper[NUMLEVELS][];
	public int[] typeAI = new int[]{1, 1, 0};    
	public KListener kl = new KListener();
	public int running = 2; //0-Stopped 1-Running 2-Menu 3-Paused
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
		mm = new MainMenu(this, xScale,yScale);
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
		setFocusable(true);
		addKeyListener(kl);
		//newGame();
		for(int k=0;k<NUMLEVELS;k++)
			levels[k]=readLevel("level"+(k+1)+".gwar");
		bump=levels[0];
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
	public void toggleMusic(){
		ismusic=!ismusic;
		if(!ismusic)
			stopMusic();
	}
	public void loadLevel(int index){
		bump=levels[index];
	}
	private Bumper[] readLevel(String file){
		System.out.println(file);
		try{
			java.io.BufferedReader f = new java.io.BufferedReader(new java.io.FileReader(file));
			int size = Integer.parseInt(f.readLine());
			Bumper[] tempbump = new Bumper[size];
			for(int k = 0; k<size;k++){
				java.util.StringTokenizer st = new java.util.StringTokenizer(f.readLine());
				int blocktype=Integer.parseInt(st.nextToken());
				if(blocktype==0)
					tempbump[k]= new Bumper(Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),st.nextToken().equalsIgnoreCase("true"));
				else if(blocktype==1)
					tempbump[k]= new SpeedBumper(Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),st.nextToken().equalsIgnoreCase("true"));

			}
			/*size = Integer.parseInt(f.readLine());
			  e = new LinkedList<Enemy>();
			  for(int k = 0; k < size; k++){
			  java.util.StringTokenizer st = new java.util.StringTokenizer(f.readLine());
			  Double tempx = Double.parseDouble(st.nextToken());
			  Double tempy = Double.parseDouble(st.nextToken());
			  boolean type = st.nextToken().equalsIgnoreCase("true");
			  java.awt.Color tempcolor;
			  if(type)
			  tempcolor=java.awt.Color.red;
			  else
			  tempcolor=java.awt.Color.blue;
			  e.add(new Enemy(tempx,tempy,10,this,tempcolor,1,type));
			  }*/
			return tempbump;
		}
		catch(java.io.FileNotFoundException ex){
			System.out.println("Having been erased,");
			System.out.println("The document you're seeking");
			System.out.println("Must now be retyped.");
			System.out.println();
			System.out.println("Level file not found");
			System.exit(1);
		}
		catch(java.io.IOException ex){
			System.out.println("Problem reading file");
		}
		return null;
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
	public void newGame(int lives,int humanPlayers, int numAI, int numNick){
		TOTALPLAYERS=humanPlayers+numAI+numNick;
		TOTALLIVES=lives;
		typeAI = new int[TOTALPLAYERS];
		int k;
		for (k =humanPlayers;k<humanPlayers+numAI;k++)
			typeAI[k]=1;
		for (;k<humanPlayers+numAI+numNick;k++){
			typeAI[k]=2;
		}

		newGame();
	}
	public void newBackgroundGame(int n, int nick){
		newGame(5,0,n,nick);
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
			Bumper on;
			SpeedBumper sOn;
			for (int k = 0; k< p.length; k++){
				if (p[k]!=null){
					if(p[k].onBumper()){
						on = p[k].onWhatBumper();
						if(on instanceof SpeedBumper)
						{
							sOn = (SpeedBumper)on;
							p[k].dx+=sOn.getXVelocity();
							p[k].dy+=sOn.getYVelocity();
							p[k].jumps=0;
						}
					}
					p[k].update();
				}
			}
			for (int k = 0; k< p.length; k++){
				if (p[k]!=null)
					p[k].gravity();
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
				if(p[k]!=null){
					if(typeAI[k]==1)
						p[k].ai();
					if(typeAI[k]==2)
						p[k].nickAI();
				}
		}
		if(running==1)
			display();
		else if (running == 2){
			display();
			mm.drawB(myBuffer);
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
			if(DEBUG){
				myBuffer.setColor(Color.green);
				myBuffer.drawString(k+"",(int)(bump[k].getX()*xScale),(int)(bump[k].getY()*yScale)-2);
			}
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
		for (int k = 0;k<args.length;k++){
			if (args[k].equals("-d")||args[k].equals("--debug"))
				DEBUG=true;
		}
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
		mm.updateScreenInfo(xScale,yScale);
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
	public void toggleFullscreen(){
		window.setVisible(false);
		window=null;
		fullscreen = !fullscreen;
		setupFrame();
		window.requestFocus();
	}
}
