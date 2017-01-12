/*
 * Created on Nov 6, 2004
 * for BLA Computer Programming class, 2004-2005
 * (asteroids)
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Animation class provides a general-purpose sprite animation interface.
 * The frames of an animation are stored in separate files. A text file named
 * images.txt tells what the various object types are for which an animation
 * exists, and lists off the image file names for each fram and tells how each
 * frame transitions into each other type of frame.
 */
public class Animation {
    private static HashMap animations = null;
   
    private String frame;
    private String name;
    private HashMap nextFrame;
    private HashMap images;
    private int frameLength;
    private int timer;
    
    public static class LoadingImage implements ImageObserver {
    	private Image image;
    	private String fileName;
    	private int width = -1, height = -1;
    	private boolean loaded = false, failed = false;
    	
    	private static HashMap images = new HashMap();
    	
    	private LoadingImage(String fileName) {
    		super();
    		this.fileName = fileName;
    		image = null;
    		images.put(fileName, this);
    	}
    	
	    private void loadImage() {
	    	failed = true;
	    	BufferedInputStream is = new BufferedInputStream(Animation.class.getResourceAsStream(fileName));
	    	try {
	    		byte[] buf = new byte[is.available()];
	    		is.read(buf);
	    		
	    		image = Toolkit.getDefaultToolkit().createImage(buf);
	    		image.getWidth(this);
	    		failed = false;
	    	} catch(IOException e) {
	    		System.err.println("Error in reading from " + fileName);
	    	} finally {
	    		try {
	    			is.close();
	    		} catch(IOException e) {
	    			System.err.println("Unable to close " + fileName);
	    		}
	    	}
	    }
	    
	    public boolean load() {
    		if(image == null && !failed) loadImage();
    		return loaded;
	    }

    	public void draw(Graphics g, int x, int y) {
            if(load()) {
                g.drawImage(image, x - width / 2, y - height / 2, this);
            }
    	}
    	
		public boolean imageUpdate(Image img, int flags, int x, int y, int w, int h) {
			if((flags & ImageObserver.WIDTH) != 0) width = w;
			if((flags & ImageObserver.HEIGHT) != 0) height = h;
			
			loaded = height > 0 && width > 0;
			return loaded;
		}
		
    	public static LoadingImage getImage(String fileName) {
    		LoadingImage image = (LoadingImage)images.get(fileName);
    		if(image == null) {
    			return new LoadingImage(fileName);
    		} else {
    			return image;
    		}
    	}
    }
    
    /**
     * Create a new animation that is an exact duplicate of the given one.
     * @param other Animation to copy
     */
    public Animation(Animation other) {
        super();
        this.name = other.name;
        this.frame = other.frame;
        this.nextFrame = other.nextFrame;
        this.images = other.images;
        this.frameLength = other.frameLength;
        this.timer = other.timer;
    }
    
    /**
     * Create a new animation using a predefined sprite with the given name.
     * Animations are defined in asteroidsImages/images.txt.
     * @param name Name of animation to load
     */
    public Animation(String name) {
        super();
        if(animations == null) {
            loadAnimations();
        }
        
        this.name = name;
        if(animations.containsKey(name)) {
            Animation prototype = (Animation)animations.get(name);
            this.frame = prototype.frame;
            this.nextFrame = prototype.nextFrame;
            this.images = prototype.images;
            this.frameLength = prototype.frameLength;
        } else {
            images = null;
            nextFrame = null;
            this.frameLength = 1;
        }
        timer = 0;
    }
    
    private Animation(BufferedReader input, String name, String frame, int frameLength) throws IOException {
        Pattern pattern = Pattern.compile("^\\s*(\\S+),\\s*(\\S+),\\s*(\\S+)\\.\\s*$");
        Matcher matcher = pattern.matcher("");
        String line;
        
        this.name = name;
        this.frameLength = frameLength;
        images = new HashMap();
        nextFrame = new HashMap();
        
        while ((line = input.readLine()) != null) {
            matcher.reset(line);
            if(!matcher.matches()) {
                break;
            } else {
                LoadingImage image = LoadingImage.getImage(
                		"asteroidsImages/" + matcher.group(2));
                
                String key = matcher.group(1);
                images.put(key, image);
                nextFrame.put(key, matcher.group(3));
            }
        }
        this.frame = frame;
        nextFrame.put(null, frame);
        animations.put(name, this);
    }
    
    /**
     * Draw this sprite at the given location. This may also cause the
     * animation to advance to the next frame. If the image is unavailable
     * for some reason, a gray placeholder box is drawn.
     * @param g Graphics to draw into
     * @param x x coordinate of center of object
     * @param y y coordinate of center of object
     */
    public void draw(Graphics g, int x, int y) {
        if(images == null) {
            g.setColor(Color.GRAY);
            g.drawRect(x - 20, y - 20, 40, 40);
            g.drawString(name, x - 18, y - 2);
            g.drawString("<invalid>", x - 18, y + 18);
      
            return;
        }
        LoadingImage image = (LoadingImage)images.get(frame);
        
        if(image != null && image.load()) {
            image.draw(g, x, y);
        } else {
            // No image available for this frame
	        g.setColor(Color.GRAY);
	        g.drawRect(x - 20, y - 20, 40, 40);
	        g.drawString(name, x - 18, y - 2);
	        g.drawString(frame, x - 18, y + 18);
	    }
        
        timer++;
        if(timer >= frameLength) {
            timer = 0;
            frame = (String)nextFrame.get(frame);
        }
    }
    
    /**
     * Set the current frame of this animation
     * @param frame Name of the frame to set
     */
    public void setFrame(String frame) {
        if(nextFrame.containsKey(frame)) {
            this.frame = frame;
        }
    }
    
    /**
     * Return the current frame of this animation.
     * @return Name of the current frame
     */
    public String getFrame() {
        return this.frame;
    }
    
    /**
     * Return the name of this animation.
     * @return Name of the animation
     */
    public String getName() {
        return this.name;
    }
    
    private static void loadAnimations() {
        BufferedReader input;
        String line;
        Pattern pattern = Pattern.compile("^\\s*(\\S+),\\s*([^\\s,\\.]+),?\\s*(\\d*)\\.\\s*$");
        Matcher matcher = pattern.matcher("");
        
        animations = new HashMap();
        
        try {
            input = new BufferedReader(new InputStreamReader(Animation.class.getResourceAsStream("images.txt")));
            while ((line = input.readLine()) != null) {
                matcher.reset(line);
                if(matcher.matches()) {
                    int frameLength = 1;
                    if(!matcher.group(3).equals("")) {
                        frameLength = Integer.parseInt(matcher.group(3));
                    }
                    new Animation(input, matcher.group(1), matcher.group(2), frameLength);
                }
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
