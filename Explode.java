import java.awt.*;
public class Explode {
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}


	int x,y;
	
	public Explode(int x, int y, boolean live) {
		this.x = x;
		this.y = y;
		this.live = live;
	}


	TankClient tc;
	private boolean live = true;
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	int[] diameter = {4,7,15,26,30,36,47,30,15,6};
	int step = 0;
	
	public void draw(Graphics g){
		if(!live){
			tc.explodes.remove(this);
			return;
		}
		
		Color c = g.getColor();
		g.setColor(Color.ORANGE);
		if(step < diameter.length) {
			g.fillOval(x, y, diameter[step], diameter[step]);
			step++;
		}
		else {
			step = 0;
			live = false;
		}
		g.setColor(c);
		

	}
	
}
