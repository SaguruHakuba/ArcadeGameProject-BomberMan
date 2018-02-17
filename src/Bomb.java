import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Bomb extends Brick {
	private Point2D centerPoint;
	private BomberWorld world;
	private int explosionR;
	private IntPoint intCenterPoint;
	private BomberMan owner;
	private int TimeCounter;

	public Bomb(BomberWorld newWorld, int newexplosionR, Point2D newCenterPoint, BomberMan owner) {
		super(newWorld, newCenterPoint);
		this.world = newWorld;
		this.explosionR = newexplosionR;
		this.centerPoint = newCenterPoint;
		this.owner = owner;
		this.intCenterPoint = IDecoder.pixelToArrayAdress(newCenterPoint, 48);
		this.world.addObject(this);
	}

	

	@Override
	public String getImageName() {
		return "res/img/bomb.png";
	}

	@Override
	public Point2D getCenterPoint() {
		return this.centerPoint;
	}



	@Override
	public String toString() {
		return this.getClass().getName() + IDecoder.pixelToArrayAdress(this.centerPoint, 48).toString().replace("IntPoint", " ");
	}



	@Override
	public void timePassed() {
		// TODO Auto-generated method stub.
		this.TimeCounter++;
		if (this.TimeCounter == 300) {//300ms before explosion
			this.die();
		}
	}



	@Override
	public void die() {
		// TODO Auto-generated method stub.
		this.world.setMapPoint(this.intCenterPoint.getX(), this.intCenterPoint.getY(), 0);
		for (IntPoint cur : this.toExplode()) {
			this.world.addObject(new ShockWave(this.world, IDecoder.arrayAdressToPixel(cur.getX(), cur.getY(), 48)));
		}
		this.world.removeObject(this);
		this.owner.removeBomb();
	}

	public ArrayList<IntPoint> toExplode() {
		ArrayList<IntPoint> potentialShockWave = new ArrayList<IntPoint>();
		IntPoint decodeC = IDecoder.pixelToArrayAdress(this.centerPoint, 48);
		int[][] cField = this.world.getMap().getMap();
		for (int i = 1; i <= this.explosionR; i++) {
			boolean upCheck = (decodeC.getX()) > 0 && (decodeC.getX()) < 26
					&& (decodeC.getY() - i) > 0 && (decodeC.getY() - i) < 13;
			if (cField[decodeC.getX()][decodeC.getY() - i] == -1) {
				break;
			}
			if (cField[decodeC.getX()][decodeC.getY() - i] == -2) {
				potentialShockWave.add(new IntPoint(decodeC.getX(), decodeC.getY() - i));
				break;
			}

			if (upCheck) {
				potentialShockWave.add(new IntPoint(decodeC.getX(), decodeC.getY() - i));
			}

		}
		for (int i = 0; i <= this.explosionR; i++) {
			boolean downCheck = (decodeC.getX()) > 0 && (decodeC.getX()) < 26
					&& (decodeC.getY() + i) > 0 && (decodeC.getY() + i) < 13;
			if (cField[decodeC.getX()][decodeC.getY() + i] == -1) {
				break;
			}
			if (cField[decodeC.getX()][decodeC.getY() + i] == -2) {
				potentialShockWave.add(new IntPoint(decodeC.getX(), decodeC.getY() + i));
				break;
			}
			if (downCheck) {
				potentialShockWave.add(new IntPoint(decodeC.getX(), decodeC.getY() + i));
			}
		}
		for (int i = 1; i <= this.explosionR; i++) {
			boolean leftCheck = (decodeC.getX() - i) > 0 && (decodeC.getX() - i) < 26
					&& (decodeC.getY()) > 0 && (decodeC.getY()) < 13;
			if (cField[decodeC.getX() - i][decodeC.getY()] == -1) {
				break;
			}
			if (cField[decodeC.getX() - i][decodeC.getY()] == -2) {
				potentialShockWave.add(new IntPoint(decodeC.getX() - i, decodeC.getY()));
				break;
			}

			if (leftCheck) {
				potentialShockWave.add(new IntPoint(decodeC.getX() - i, decodeC.getY()));
			}
		}
		for (int i = 1; i <= this.explosionR; i++) {
			boolean rightCheck = (decodeC.getX() + i) > 0 && (decodeC.getX() + i) < 26
					&& (decodeC.getY()) > 0 && (decodeC.getY()) < 13;
			if (cField[decodeC.getX() + i][decodeC.getY()] == -1) {
				break;
			}
			if (cField[decodeC.getX() + i][decodeC.getY()] == -2) {
				potentialShockWave.add(new IntPoint(decodeC.getX() + i, decodeC.getY()));
				break;
			}
			if (rightCheck) {
				potentialShockWave.add(new IntPoint(decodeC.getX() + i, decodeC.getY()));
			}
		}

		return potentialShockWave;
	}

}