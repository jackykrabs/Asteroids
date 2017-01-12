import java.util.Random;

public class Explosion extends GameObject{
	
	public Explosion(GameObject o){
		super(o, "explosion");
		setTeam(0);
	}
	
	public Explosion(double xPos, double yPos){
		super(xPos, yPos, "explosion");
		setTeam(0);
	}
	
	public Explosion(){
		super(new Random().nextDouble()*560+20, new Random().nextDouble()*560+20, "explosion");
		setTeam(0);
	}

}
