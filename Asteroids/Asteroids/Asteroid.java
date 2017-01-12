/*
 This class extends GameObject in order to more easily create asteroids for use in our 
 asteroids game
 */
import java.util.Random;
import java.awt.Color;
import java.awt.Graphics;

public class Asteroid extends GameObject {
	//default constructor
	
	boolean offScreen;
	boolean shot;
	public Asteroid(){
		super(new Random().nextDouble()*560+20, 600, "asteroid");
		offScreen = false;
		this.setRadius(16);
		this.setTeam(2);
		this.setRadius(16);
		this.setVY(-5);
		shot = false;
	}
	
	//constructor to create the asteroids that spawn after getting shot
	public Asteroid(Asteroid oldRock){
		Asteroid asteroid = new Asteroid();
		asteroid.setPosition(oldRock.getX(), oldRock.getY());
		asteroid.setVX(10);
	}

	public void die(){
		super.die();
		new Explosion(this);
		shot = true;
		offScreen = true;
	}
	
	//override draw method to make fun circles!
	public void draw(Graphics g){
		super.draw(g);
	}
	
	//make it so the asteroids go away when they go off the screen
	public void offScreen(){
		this.vanish();
		offScreen = true;
	}
	
	public boolean wasShot(){
		return shot;
	}
	
	public boolean isOffScreen(){
		return offScreen;
	}
	
}

