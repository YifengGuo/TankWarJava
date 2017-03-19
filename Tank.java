import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Random;
import java.util.List;
public class Tank {
	int x, y;
	private int oldX,oldY;
	
	BloodBar bb = new BloodBar();
	
	private int life = 100;
	
	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	
	private static Random r = new Random();
	private int step = r.nextInt(12) + 3;
	
	private boolean good = true;
	
	public boolean isGood() {
		return good;
	}

	private boolean live = true;
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	private boolean bL = false, bR = false, bU = false, bD = false;
	enum Direction{L,LU,U,RU,R,RD,D,LD,STOP}
	private Direction dir = Direction.STOP;
	private Direction ConnonBoreDir = Direction.D;
	
	
	TankClient tc;
	
	public Tank(int x, int y,int oldX,int oldY,boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = oldX;
		this.oldY = oldY;
		this.good = good;
	}
	
	public Tank(int x,int y,boolean good,TankClient tc){
		this.x = x;
		this.y = y;
		this.good = good;
		this.tc = tc;
	}
	
	public void draw(Graphics g){
		if(!live){
			if(!good){
				tc.tanks.remove(this);
			}
			return;
		}
		
		Color c = g.getColor();
		if(good == true) g.setColor(Color.GREEN);
		if(good == false) g.setColor(Color.RED);
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		
		if(good) bb.draw(g);
		
		switch(ConnonBoreDir){
		case L:
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x, y+Tank.HEIGHT/2);
			break;
		case LU:
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x, y);

			break;
		case U:
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH/2, y);

			break;
		case RU:
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH, y);

			break;
		case R:
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH, y+Tank.HEIGHT/2);

			break;
		case RD:
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH, y+Tank.HEIGHT);

			break;
		case D:
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x+Tank.WIDTH/2, y+Tank.HEIGHT);

			break;
		case LD:
			g.drawLine(x+Tank.WIDTH/2, y+Tank.HEIGHT/2, x, y+Tank.HEIGHT);
			break;
		}
		move();
	}
	public void move(){
		this.oldX = x;
		this.oldY = y;
		
		switch(dir){
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case RD:
			x += XSPEED;
			y += YSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
		case STOP:
			break;
		}
		if(this.dir != Direction.STOP){//移动时，炮筒方向与坦克移动方向一致;
			this.ConnonBoreDir = this.dir;
		}
		if(x < 0) x = 0;
		if(y < Tank.HEIGHT) y = Tank.HEIGHT;
		if(x + Tank.WIDTH > tc.GAME_WIDTH) x = tc.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > tc.GAME_LENGTH) y = tc.GAME_LENGTH - Tank.HEIGHT;
		
		if(!good){
			if(step == 0){
			Direction[] dirs = dir.values();
			int rn = r.nextInt(dirs.length);
			dir = dirs[rn];
			step = r.nextInt(12) +3;
			}
			step--;
			
			if(r.nextInt(40) > 38) this.fire();
		}
	}
	
	public void stay(){
		x = oldX;
		y = oldY;
	}
	
	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		}
		locateDirection();
		
	}
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		switch(key){
		case KeyEvent.VK_F2:
			if(good && !live){
				this.setLive(true);
				this.setLife(100);
			}
			break;
		case KeyEvent.VK_A:
			superFire();
			break;
		case KeyEvent.VK_SPACE:
			fire();
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		}
		locateDirection();
		
	}
	

	public void locateDirection(){
		if(bL && !bR && !bU && !bD) dir = Direction.L;
		if(bL && !bR && bU && !bD) dir = Direction.LU;
		if(!bL && !bR && bU && !bD) dir = Direction.U;
		if(!bL && bR && bU && !bD) dir = Direction.RU;
		if(!bL && bR && !bU && !bD) dir = Direction.R;
		if(!bL && bR && !bU && bD) dir = Direction.RD;
		if(!bL && !bR && !bU && bD) dir = Direction.D;
		if(bL && !bR && !bU && bD) dir = Direction.LD;
		if(!bL && !bR && !bU && !bD) dir = Direction.STOP;

	}
	
	public Missile fire(){
		if(!this.isLive()) return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x,y,good,ConnonBoreDir,this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	public Missile fire(Direction dir){
		if(!this.isLive()) return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x,y,good,dir,this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	public void superFire(){
		Direction[] dirs = dir.values();
		for(int i = 0; i<8; i++){
			fire(dirs[i]);
		}
	}
	
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	
	public boolean collidesWithWall(Wall w){
		if(this.live && this.getRect().intersects(w.getRect())){
			this.stay();
			return true;
		}
		return false;
	}
	
	public boolean collidesWithTank(List<Tank> tanks){
		for(int i = 0; i<tanks.size(); i++){
			Tank t = tanks.get(i);
			if(this != t){
				if(this.live && t.isLive() && this.getRect().intersects(t.getRect())){
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	
	private class BloodBar{
		public void draw(Graphics g){
			Color c = g.getColor();
			g.setColor(Color.RED);
			int w = WIDTH*life/100;
			g.fillRect(x, y-15,w,10);
			g.drawRect(x, y-15, WIDTH, 10);
			g.setColor(c);
		}
	}
	
	public boolean eat(Blood b){
			if(this.live && b.isLive() && this.getRect().intersects(b.getRect())){
				b.setLive(false);
				this.setLife(100);
				return true;
			}
		return false;
	}
}
