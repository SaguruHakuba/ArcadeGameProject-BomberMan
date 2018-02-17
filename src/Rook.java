import java.awt.geom.Point2D;
import java.util.Random;

/**
 * TODO Put here a description of what this class does.
 *
 * @author wangnn.
 *         Created May 18, 2017.
 */
public class Rook extends Monster {
	private BomberWorld world;
	private Point2D centerPoint;
	private int speed, direction;
	private static final int POINT = 100;
	private Random random = new Random();
	
	/**
	 * TODO Put here a description of what this constructor does.
	 *
	 * @param world
	 * @param centerPoint
	 */
	public Rook(BomberWorld world, Point2D centerPoint) {
		super(world, centerPoint);
		// TODO Auto-generated constructor stub.
		this.world = world;
		this.centerPoint = centerPoint;
		this.speed = 4;
		this.direction = this.random.nextInt(4);
	}

	@Override
	public void move() {
		
		double newX = this.centerPoint.getX();
		double newY = this.centerPoint.getY();
		Point2D newPoint;
		String mapDirection = "";
		switch (this.direction) {
		case 0:// up
			newY -= this.speed;
			mapDirection = "u";
			break;
		case 1:// down
			newY += this.speed;
			mapDirection = "d";
			break;
		case 2:// left
			newX -= this.speed;
			mapDirection = "l";
			break;
		default:// right
			newX += this.speed;
			mapDirection = "r";
			break;
		}
		newPoint = new Point2D.Double(newX, newY);
		if (!this.world.isPassable(this.centerPoint, mapDirection)) {
			switch (this.direction) {
			case 0:
				this.direction = 1;
				break;
			case 1:
				this.direction = 0;
				break;
			case 2:
				this.direction = 3;
				break;
			default:
				this.direction = 2;
				break;
			}
			return;
		}
		this.centerPoint = newPoint;
		moveTo(newPoint);
	}

	@Override
	public int getPoints() {
		return POINT;
	}

}
