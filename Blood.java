import java.awt.*;
public class Blood {

	private boolean live = true;

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	int x,y,w,h;
	TankClient tc;
	
	public Blood(){
		x = pos[0][0];
		y = pos[0][1];
		
		w = h = 10;
	}
	
	private int step = 0;
	
	private int[][]	pos = {
			{400,350},{380,320},{385,300},{375,275},{365,250},{360,240},{350,190},{340,180},{335,180},
			{330,175},{325,170},{320,1650},{315,2005},{310,255},{300,245},{295,195},{285,320},{270,290}

	};
	
	public void move(){
		step++;
		if(step == pos.length){
			step = 0;
		}else{
		x = pos[step][0];
		y = pos[step][1];
		}
	}
	
	public void draw(Graphics g){
		if(!live) return;
		Color c = g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		
		this.move();
		
	}
	
	public Rectangle getRect(){
		return new Rectangle(x,y,w,h);
	}
}
