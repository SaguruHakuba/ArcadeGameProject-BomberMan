import java.awt.geom.Point2D;

public abstract class Brick implements Temporal {
	private Point2D centerPoint;
	private BomberWorld world;

	public Brick(BomberWorld world) {
		this(world, new Point2D.Double());
	}

	public Brick(BomberWorld world, Point2D centerPoint) {
		this.world = world;
		this.centerPoint = centerPoint;
	}

	protected void setCenterPoint(Point2D centerPoint) {
		this.centerPoint = centerPoint;
	}
	
	protected BomberWorld getWorld() {
		return this.world;
	}
	
	@Override
	public Point2D getCenterPoint() {
		return this.centerPoint;
	}

	@Override
	public abstract String getImageName();

	@Override
	public abstract void timePassed();



}