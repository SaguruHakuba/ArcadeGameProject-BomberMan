import java.awt.geom.Point2D;
import java.util.ArrayList;

public class ShockWave extends BreakableBrick{
	protected BomberWorld world;
	protected Point2D centerPoint;
	private int TimeCounter;

	public ShockWave(BomberWorld world, Point2D centerPoint) {
		super(world, centerPoint);
		// TODO Auto-generated constructor stub.
		this.world = world;
		this.centerPoint = centerPoint;
		ArrayList<Temporal> tempList = new ArrayList<>();
		for (Temporal cur : this.world.getObjects()) {
			if (cur.getCenterPoint().distance(this.centerPoint) < Math.sqrt(2 * 24 * 24)) {
				if (!classNameCheck(cur, "Bomb") && !classNameCheck(cur, "RangePowerUp")
						&& !classNameCheck(cur, "SpeedPowerUp") && !classNameCheck(cur, "BombPowerUp")
						&& (!tempList.contains(cur))) {
					cur.die();
				}
			}
		}
	}
	
	boolean classNameCheck(Temporal obj, String nameToCheck) {
		return obj.getClass().getSimpleName().toLowerCase().equals(nameToCheck.toLowerCase());
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub.
		this.world.removeObject(this);
	}

	@Override
	public String getImageName() {
		// TODO Auto-generated method stub.
		return "res/img/explosion.png";
	}

	@Override
	public void timePassed() {
		// TODO Auto-generated method stub.
		this.TimeCounter++;
		if (this.TimeCounter == 75) {
			this.die();
		}
		for (Temporal t : this.world.getObjects()) {
			if (t.getCenterPoint().distance(this.centerPoint) < Math.sqrt(2 * 24 * 24)) {
				if (!(t instanceof Power || t instanceof ShockWave)) {
					t.die();
				}
			}
		}
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub.
		return this.getClass().getName() 
				+ IDecoder.pixelToArrayAdress(this.centerPoint, 48).toString().replace("IntPoint", " ");
	}

}
