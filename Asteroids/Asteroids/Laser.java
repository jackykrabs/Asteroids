
public class Laser extends GameObject {
	public Laser(GameObject shooter){
		super(shooter,"laser");
		this.setVY(10);
		this.setRadius(4);
		this.setTeam(1);
		this.setVX(0);
	}
	
	//make it so the lasers go away when they go off the screen
	public void offScreen(){
		this.vanish();
	}
	
	
}
