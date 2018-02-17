import java.awt.geom.Point2D;

public class YouLost extends Power {

	public YouLost(BomberWorld newWorld) {
		super(newWorld, new Point2D.Double(Main.BomberWorld_WIDTH/2, Main.BomberWorld_HEIGHT/2));
		// TODO Auto-generated constructor stub.
	}

	@Override
	public void timePassed() {
		// TODO Auto-generated method stub.

	}

	@Override
	public void die() {
		// TODO Auto-generated method stub.

	}

	@Override
	public String getImageName() {
		// TODO Auto-generainted method stub.
		return "res/img/youlost.png";
	}
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName()
				+ String.format("(%f, %f)", super.getCenterPoint().getX(), super.getCenterPoint().getY());
	}

}
