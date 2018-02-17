import java.awt.geom.Point2D;

public class PowerUp extends Power {

	public PowerUp(BomberWorld newWorld, Point2D newCenterPoint) {
		super(newWorld, newCenterPoint);
		// TODO Auto-generated constructor stub.
	}

	@Override
	public void timePassed() {
		// TODO Auto-generated method stub.

	}

	@Override
	public void die() {
		// TODO Auto-generated method stub.
		super.getWorld().removeObject(this);
	}

	@Override
	public String getImageName() {
		// TODO Auto-generated method stub.
		return "res/img/"+this.getClass().getSimpleName().toLowerCase()+".png";
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName()+String.format("(%f, %f)", super.getCenterPoint().getX(), super.getCenterPoint().getY());
	}

}
