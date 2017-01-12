import java.awt.Graphics;
import java.awt.Color;

public class Ship extends GameObject{
	//instance variables
	int lasers;
	int shields;
	boolean alive;
	
	public Ship(){
		super(300,20,"ship");
		this.getKeyFocus();
		setTeam(1);
		lasers = 20;
		shields = 3;
		alive = true;
	}
	
	public void keyLeftPressed(){
		this.setVX(-5);
	}
	
	public void keyLeftReleased(){
		this.setVX(0);
	}
	
	public void keyRightPressed(){
		this.setVX(5);
	}
	
	public void keyRightReleased(){
		this.setVX(0);
	}
	
	public void keySpacePressed(){
		if(lasers > 0){
			new Laser(this);
			lasers--;
		}
	}
	
	public void collision(GameObject o){
		shields--;
		o.die();
		if(shields == 0){
			super.collision(o);
			alive = false;
		}
	}
	
	//return the number of shots the ship has left
	public int getLasers(){
		return lasers;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public void draw(Graphics g){
		drawCircle(g, Color.GREEN);
		super.draw(g);
		if(shields == 2)
			drawCircle(g, Color.YELLOW);
		if(shields == 1)
			drawCircle(g, Color.RED);
		
		drawString(g, "Player", Color.GREEN);
	}
}
