import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;
public class TankClient extends Frame{
	Image offScreenImage = null;

	int x = 50, y = 50;
	
	public static final int GAME_WIDTH = 800;
	public static final int GAME_LENGTH = 600;
	
	Tank myTank = new Tank(50,50,true,this);
	//Missile m;
	List<Missile> missiles = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> tanks = new ArrayList<Tank>();
	
	Wall w1 = new Wall(200,200,20,300,this);
	Wall w2 = new Wall(500,450,260,20,this);
	Blood b = new Blood();
	
	public static void main(String[] args) {
		new TankClient().launchFrame();
	}
	
	public void launchFrame(){
		
		for(int i = 0; i<10; i++){
			tanks.add(new Tank(50+40*(i+1),50,false,this));
		}
		
		this.setBackground(new Color(204,204,255));
		this.setLocation(300,400);
		this.setSize(GAME_WIDTH, GAME_LENGTH);
		this.addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		this.addKeyListener(new KeyMonitor());
		
		this.setResizable(false);
		this.setTitle("TankWar");
		this.setVisible(true);
		new Thread(new ClientThread()).start();
	}
	
	public void paint(Graphics g) {
		g.drawString("missiles count:"+missiles.size(), 10, 50);
		g.drawString("HP:"+myTank.getLife(), 10, 70);
		
		if(tanks.size() <= 0){
			for(int i = 0; i<15; i++){
				tanks.add(new Tank(50+40*(i+1),50,false,this));
			}
		}
		
		for(int i = 0; i<missiles.size(); i++){
			Missile m = missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
		}
		
		for(int i = 0; i<explodes.size(); i++){
			Explode e = explodes.get(i);
			e.draw(g);
		}
		
		for(int i = 0; i<tanks.size(); i++){
			Tank t = tanks.get(i);
			t.draw(g);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithTank(tanks);
			
		}
		
		myTank.draw(g);
		myTank.collidesWithWall(w1);
		myTank.collidesWithWall(w2);
		myTank.collidesWithTank(tanks);
		myTank.eat(b);
		
		
		
		//enemyTank.draw(g);
		
		//y += 1;
		w1.draw(g);
		w2.draw(g);
		
		b.draw(g);
	}
	
	public void update(Graphics g){
		if(offScreenImage == null){
			offScreenImage = this.createImage(GAME_WIDTH,GAME_LENGTH);
		}
		Graphics goffScreen = offScreenImage.getGraphics();
		Color c = goffScreen.getColor();
		goffScreen.setColor(new Color(204,204,255));
		goffScreen.fillRect(0, 0, GAME_WIDTH, GAME_LENGTH);
		goffScreen.setColor(c);
		paint(goffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
		
	}
	
	private class KeyMonitor extends KeyAdapter{

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
			
		}
		
	}
	
	private class ClientThread implements Runnable{

		@Override
		public void run() {
			try{
				while(true){
					repaint();
					Thread.sleep(20);
				}
			} catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
	}

}
