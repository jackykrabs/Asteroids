/*
 * Created on Nov 6, 2004
 * for BLA Computer Programming, 2004-2005
 * (asteroids)
 */
 

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

/*
 * Asteroids is a game in which you, in a spaceship moving back and forth at the
 * bottom of the screen, try to dodge the asteroids falling toward you from the
 * top of the screen.
 * 
 * This file is the main applet. It takes care of drawing and of receiving events.
 * Key events are forwarded to the GameObject class.
 */
public class Asteroids extends Applet implements MouseListener {
    private Image buffer = null;
    private Timer timer = null;
    private static Asteroids instance;
    
    public void init() {
        instance = this;
        addKeyListener(GameObject.getKeyInterpreter());
        addMouseListener(this);
        setSize((int)GameObject.screenWidth, (int)GameObject.screenHeight);
        buffer = createImage((int)GameObject.screenWidth, (int)GameObject.screenHeight);
    }
    
    public void stop() {
        if(timer != null) {
            timer.cancel();
        }
    }

    public void start() {
        if(timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                GameObject.stepAll();
                repaint();
            }
        }, 1000, 50);
    }
    
    public void update(Graphics g) {
        Graphics bufgc = buffer.getGraphics();
        bufgc.setColor(Color.BLACK);
        bufgc.fillRect(0, 0, getWidth(), getHeight());
        
        GameObject.drawAll(bufgc);
        g.drawImage(buffer, 0, 0, this);
        if(GameObject.isGameOver()) {
            g.setColor(Color.WHITE);
            g.drawString("Click to begin.", getWidth() / 2 - 50, getHeight() / 2 - 50);
        }
    }
    
    public static Asteroids getInstance() {
        return instance;
    }
    
    /* * * Implementation of MouseListener * * */
    public void mouseClicked(MouseEvent event) {
    }

    public void mousePressed(MouseEvent event) {
        requestFocus();
        if(GameObject.isGameOver()) {
            GameObject.newGame();
        }
    }

    public void mouseReleased(MouseEvent event) {
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

}
