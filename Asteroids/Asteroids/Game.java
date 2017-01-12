/*
 * Created on Nov 10, 2004
 * for BLA Computer Programming, 2004-2005
 * (asteroids)
 */

import java.util.Random;
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
	Ship ship;
	Random r;
    public Game() {
       super(); // The game is an invisible object
       r = new Random();
       timer = 0;
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
    	
    	if(!ship.isAlive()){
    		gameOver();
    		newGame();
    	}
    	timer++;
    	super.step();
    }
	public void keySpacePressed() { 
	}
	
	public void keyEnterPressed(){
	}
}
