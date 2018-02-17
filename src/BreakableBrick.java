import java.awt.geom.Point2D;
import java.util.Random;

public class BreakableBrick extends Brick{
	private IntPoint intCenterPoint;
	private int BLOCK_SIZE=48;
	private Random random = new Random();

	public BreakableBrick(BomberWorld world, Point2D centerPoint) {
		super(world, centerPoint);
		// TODO Auto-generated constructor stub.
		this.intCenterPoint=IDecoder.pixelToArrayAdress(centerPoint, BLOCK_SIZE);
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub.
		super.getWorld().removeObject(this);
		super.getWorld().setMapPoint(this.intCenterPoint.getX(), this.intCenterPoint.getY(), 0);
		if (this.random.nextInt(4) == 1) {//25% of the bricks containing power-ups
			int det = this.random.nextInt(3);
			if (det == 0) {
				RangePowerUp rangePowerUp = new RangePowerUp(super.getWorld(), super.getCenterPoint());
				super.getWorld().addObject(rangePowerUp);
			} else if (det == 1) {
				SpeedPowerUp speedPowerUp = new SpeedPowerUp(super.getWorld(), super.getCenterPoint());
				super.getWorld().addObject(speedPowerUp);
			} else if (det == 2) {
				BombPowerUp bombPowerUp = new BombPowerUp(super.getWorld(), super.getCenterPoint());
				super.getWorld().addObject(bombPowerUp);
			}
		}
	}

	@Override
	public String getImageName() {
		// TODO Auto-generated method stub.
		return "res/img/breakbrick.png";
	}

	@Override
	public void timePassed() {
		// TODO Auto-generated method stub.
		
	}
	
	@Override
	public String toString() {
		return this.getClass().getName()
				+ IDecoder.pixelToArrayAdress(super.getCenterPoint(), 48).toString().replace("IntPoint", " ");
	}

}
