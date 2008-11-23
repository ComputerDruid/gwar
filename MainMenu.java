import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.Image;
class MainMenu {
		//BufferedImage menubuffer = myImage;//new BufferedImage (WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		//Graphics myBuffer = menubuffer.createGraphics();
		int selected=0;
		double xScale;
		double yScale;
		GWar world;
		int menustatus=0;//0-main 1-help 2-alt
		int numPlayers=1;
		int oldnumPlayers=2;
		int[] pNumPlayers = {1,2,3};
		int HPSel=5;
		int PlayerSizeSel=25;
		int PelletSizeSel=5;
		int LevelSel=1;
		int numAI=1;
		int numNick=0;
		final int ListSize=7;
		final int AltListSize=5;
		final int spacebetweenlines = 25; 
		final int xoffset = 30;
		final int yoffset = 120;
		final int tabspace = 85;
		final int HEIGHT=400;
		final int WIDTH=600;
		boolean ismusic=true;
		//final String[] menuTitles new String[]{"Start", "# Human Players:", "# AI Players:", "Lives:", "Level:", "Toggle Fullscreen Mode (experimental)", "Help", "Exit"};
		//final String[][] menuChoices = {{},{"0","1","2","3"},{"0","1","2","3"},{"1","2","3","4","5"},{"Battlefield","FD","new"},{},{}};
		Image helpscreen;
		//Image background;
		MainMenu(GWar parent, double xs, double ys){
			world=parent;
			xScale=xs;
			yScale=ys;
			helpscreen = new ImageIcon(getClass().getClassLoader().getResource("helpscreen.PNG")).getImage();
			//background = new ImageIcon(getClass().getClassLoader().getResource("background.PNG")).getImage();
			//drawB();
		}
		void show(){
			world.newBackgroundGame(numPlayers+numAI,numNick);
			world.running=2;
		}
		void updateScreenInfo(double xs, double ys){
			xScale=xs;
			yScale=ys;
		}
		/*void draw(Graphics g){
		  if (helpshowing==true){
		  g.drawImage(helpscreen,0,0,null);
		  }
		  else{
		  g.drawImage(menubuffer,0,0,null);
		  }
		  }*/
		void drawB(java.awt.Graphics myBuffer){
			myBuffer.setFont(new Font("SansSerif",Font.BOLD,(int)(15*yScale)));
			if (menustatus==1){
				myBuffer.drawImage(helpscreen,0,0,(int)(WIDTH*xScale),(int)(HEIGHT*yScale),null);
			}
			else if (menustatus==2){
				int counter = 0;
				setSelectedColor(myBuffer,counter,selected);
				myBuffer.drawString("Back",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				myBuffer.setColor(new Color(0.0f,0.0f,0.8f,0.3f));
				myBuffer.fillRect((int)(xScale*(tabspace*4.75+xoffset)),90,150,200);
				setSelectedColor(myBuffer,0,0);
				counter++;
				int k;
				for (k=0;k<numPlayers;k++){
					myBuffer.setColor(world.PCOLOR[k]);
					myBuffer.drawString("Human Player",(int)(xScale*(tabspace*5+xoffset)),(int)(yScale*((counter+k)*spacebetweenlines+yoffset)));
				}
				for (;k<numPlayers+numAI;k++){
					myBuffer.setColor(world.PCOLOR[k]);
					myBuffer.drawString("AI Player",(int)(xScale*(tabspace*5+xoffset)),(int)(yScale*((counter+k)*spacebetweenlines+yoffset)));
				}
				for (;k<numPlayers+numAI+numNick;k++){
					myBuffer.setColor(world.PCOLOR[k]);
					myBuffer.drawString("NickAI Player",(int)(xScale*(tabspace*5+xoffset)),(int)(yScale*((counter+k)*spacebetweenlines+yoffset)));
				}
				k=-1;
				if(numPlayers+numAI+numNick<=1)
					myBuffer.setColor(Color.GREEN);
				else if(numPlayers+numAI+numNick>=6)
					myBuffer.setColor(Color.RED);
				else
					myBuffer.setColor(Color.YELLOW);
				myBuffer.drawString("PlayerList: ("+(numPlayers+numAI+numNick)+")",(int)(xScale*(tabspace*5+xoffset)),(int)(yScale*((counter+k)*spacebetweenlines+yoffset)));

				counter = 1;
				setSelectedColor(myBuffer,counter,selected);
				myBuffer.drawString("Add Human Player",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				//setSelectedColor(numPlayers,0);
				//setSelectedColor(0,0);
				//myBuffer.drawString(numPlayers+"",(int)(xScale*(tabspace*2+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				counter = 2;
				setSelectedColor(myBuffer,counter,selected);
				myBuffer.drawString("Add AI Player",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				//setSelectedColor(numAI,0);
				//setSelectedColor(0,0);
				//myBuffer.drawString(numAI+"",(int)(xScale*(tabspace*2+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				counter = 3;
				setSelectedColor(myBuffer,counter,selected);
				myBuffer.drawString("Add NickAI Player",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				//setSelectedColor(numAI,0);
				//setSelectedColor(0,0);
				//myBuffer.drawString(numNick+"",(int)(xScale*(tabspace*2+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				
				counter = 4;
				setSelectedColor(myBuffer,counter,selected);
				myBuffer.drawString("Lives:",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				setSelectedColor(myBuffer,HPSel,1);
				setSelectedColor(myBuffer,0,0);
				myBuffer.drawString(HPSel+"",(int)(xScale*(tabspace+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));

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
				setSelectedColor(myBuffer,counter,selected);
				counter = 0;
				setSelectedColor(myBuffer,counter,selected);
				myBuffer.drawString("Start",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				counter = 1;
				setSelectedColor(myBuffer,counter,selected);
				myBuffer.drawString("Level:",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				setSelectedColor(myBuffer,LevelSel,1);
				myBuffer.drawString("Totally Original",(int)(xScale*(tabspace+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				setSelectedColor(myBuffer,LevelSel,2);
				myBuffer.drawString("Even Ground",(int)(xScale*(tabspace*3+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				setSelectedColor(myBuffer,LevelSel,3);
				myBuffer.drawString("Overlook",(int)(xScale*(tabspace*5+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				setSelectedColor(myBuffer,LevelSel,4);
				myBuffer.drawString("Islands",(int)(xScale*(tabspace*6+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				counter = 2;
				setSelectedColor(myBuffer,counter,selected);
				myBuffer.drawString("Game Setup ...     (Human: "+numPlayers+", AI: "+(numAI+numNick)+", Lives:"+HPSel+")",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				counter = 3;
				setSelectedColor(myBuffer,counter,selected);
				myBuffer.drawString("Toggle Fullscreen Mode",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				counter = 4;
				setSelectedColor(myBuffer,counter,selected);
				myBuffer.drawString("Music:",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				setSelectedColor(myBuffer,ismusic,true);
				myBuffer.drawString("On",(int)(xScale*(tabspace+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				setSelectedColor(myBuffer,ismusic,false);
				myBuffer.drawString("Off",(int)(xScale*(tabspace*2+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				counter = 5;
				setSelectedColor(myBuffer,counter,selected);
				myBuffer.drawString("Help",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				counter = 6;
				setSelectedColor(myBuffer,counter,selected);
				myBuffer.drawString("Exit",(int)(xScale*(0+xoffset)),(int)(yScale*(counter*spacebetweenlines+yoffset)));
				setSelectedColor(myBuffer,counter,-1);
				myBuffer.setColor(new Color(0,255,255));
				myBuffer.drawString("Music by RoeTaKa -- OverClocked ReMix (www.ocremix.org)",(int)(xScale*(0+xoffset)),(int)(yScale*(HEIGHT-45)));
				myBuffer.drawString("By Daniel Johnson (johnmon2) for TJGames.org",(int)(xScale*(0+xoffset)),(int)(yScale*(HEIGHT-25)));
				myBuffer.drawString("Licensed under GNU GPL 2",(int)(xScale*(0+xoffset)),(int)(yScale*(HEIGHT-5)));
			}
			//drawPlayer(myBuffer,1,15,selected*spacebetweenlines+yoffset-7,90,0,25,true);
		}
		void setSelectedColor(java.awt.Graphics myBuffer, int var1,int con1){
			if (var1==con1){
				myBuffer.setColor(Color.yellow);
			}
			else{
				myBuffer.setColor(Color.white);
			}
		}
		void setSelectedColor(java.awt.Graphics myBuffer, boolean var1,boolean con1){
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
				if(menustatus==2)
					selected=AltListSize-1;
				else
					selected=ListSize-1;
			}
			//drawB();
		}
		void moveDown(){
			selected++;
			if(menustatus==2){
				if (selected >= AltListSize)
					selected=0;
			}
			else if (selected >= ListSize){
				selected=0;
			}
			//drawB();
		}
		void newGameCurrentSettings(){
			world.loadLevel(LevelSel-1);
			world.newGame(HPSel,numPlayers,numAI,numNick);
		}
		void enter(){
			if (menustatus==1){
				menustatus=0;
			}
			else if (menustatus==2){
				if(selected==0){
					if(numPlayers+numAI+numNick>=2&&numPlayers+numAI+numNick<=6){
						menustatus=0;
						selected=2;
						if(oldnumPlayers!=numPlayers+numAI+numNick)
							world.newBackgroundGame(numPlayers+numAI,numNick);
						//drawB();
					}
				}
				else if (selected==1){
					if (numPlayers+numAI+numNick<6){
						numPlayers++;
					}
					else{
						numPlayers=0;
					}
					//drawB();
				}
				else if (selected==2){
					if (numPlayers+numAI+numNick<6){
						numAI++;
					}
					else{
						numAI=0;
					}
					//drawB();
				}
				else if (selected==3){
					if (numPlayers+numAI+numNick<6){
						numNick++;
					}
					else{
						numNick=0;
					}
					//drawB();
				}
				else if (selected==4){
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
					//drawB();
				}
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
					world.loadLevel(LevelSel-1);
					//drawB();
				}
				else if (selected==2){
					menustatus=2;
					selected=0;
					//drawB();
				}
				else if (selected==3){
					world.toggleFullscreen();
				}
				else if (selected==4){
					world.toggleMusic();
					ismusic=!ismusic;
					//drawB();
				}
				else if (selected==5){
					oldnumPlayers=numPlayers+numAI+numNick;
					menustatus=1;
					//drawB();
				}
				else if (selected==6){
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
