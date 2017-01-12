/*
 * Created on Nov 10, 2004
 * for BLA Computer Programming, 2004-2005
 * (asteroids)
 */

import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

/**
 * The Game class contains the static methods restart() and step(), which describe
 * what needs to happen when a new game starts, and during each frame of a game.
 * You can use the step() method to program in things like the appearance of asteroids
 * that need to happen with certain timing. A new game is always begun by the creation
 * of a new Game object, so that that Game's step() is called first out of all objects.
 */
public class Game extends GameObject {
    /*
     * A game is constructed once, at the very start of each game.
     */
	ArrayList<Asteroid> asteroids;
	int timer;
	int points;
	Ship ship;
    public Game() {
       super(); // The game is an invisible object
       timer = 0;
       points = 0;
       ship = new Ship();
       asteroids = new ArrayList<Asteroid>();
       // Now, when keys are pressed, the methods this object has for
       // dealing with those keys will be called.
    }
    
    public void step(){
    	if(timer % 10 == 0 && ship.isAlive())
    	{
    		asteroids.add(new Asteroid());
    		timer = 0;
    	}
    	
    	//check the asteroids to see if they've been shot
    	for(int i = 0; i < asteroids.size(); i++){
    		if(asteroids.get(i).wasShot())
    			points+=100;
    	}
    	
    	//remove any asteroids that have gone off the screen (been shot or just let behind)
    	for(int i = 0; i < asteroids.size(); i++){
    		if(asteroids.get(i).isOffScreen())
    			asteroids.remove(i);
    	}
    	
    	if(!ship.isAlive()){
    		gameOver();
    		//newGame();
    	}
    	timer++;
    	super.step();
    }
	public void keySpacePressed() { 
	}
	
	public void keyEnterPressed(){
	}
	
	public void draw(Graphics g){
		g.setColor(Color.GREEN);
		g.drawString("Lasers: " + ship.getLasers() , 20, 20);
		g.drawString("Points: " + points, 20, 40);
		super.draw(g);
	}
}

