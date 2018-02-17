import java.awt.geom.Point2D;

public abstract class Monster implements Relocatable, Temporal{

	private BomberWorld world;
	private Point2D centerPoint;
	private IntPoint intCenterPoint;
	protected boolean isPausled;
	private int MONSTER_SIZE=48;
	
	public Monster(BomberWorld world, Point2D centerPoint){
		this.world=world;
		this.centerPoint=centerPoint;
		this.intCenterPoint = IDecoder.pixelToArrayAdress(centerPoint, MONSTER_SIZE);
	}
	
	public abstract void move();
	
	public abstract int getPoints();

	@Override
	public void die() {
		// TODO Auto-generated method stub.
		this.world.addPoints(getPoints());
		this.world.removeObject(this);
		this.world.setMapPoint(this.intCenterPoint.getX(), this.intCenterPoint.getY(), 0);
	}

	@Override
	public void moveTo(Point2D point) {
		// TODO Auto-generated method stub.
		IntPoint newPoint = IDecoder.pixelToArrayAdress(point, 48);
		this.centerPoint = point;
		this.world.setMapPoint(this.intCenterPoint.getX(), this.intCenterPoint.getY(), 0);
		this.intCenterPoint = newPoint;
	}

	@Override
	public Point2D getCenterPoint() {
		// TODO Auto-generated method stub.
		return this.centerPoint;
	}

	@Override
	public void timePassed() {
		// TODO Auto-generated method stub.
		if (this.isPausled) 
			return;
		
		move();
		for (Temporal t : this.world.getObjects()) {
			if (t.getCenterPoint().distance(this.getCenterPoint()) < Math.sqrt(2 * 24 * 24)) {
				if ((t.getClass().getSimpleName().equals("BomberMan"))) {
					t.die();
				}
			}
		}
	}

	@Override
	public String getImageName() {
		// TODO Auto-generated method stub.
		String name = this.getClass().getSimpleName();
		return "res/img/" + name + ".png";
	}
	
	@Override
	public String toString(){
		return this.getClass().getSimpleName() + 
				IDecoder.pixelToArrayAdress(this.getCenterPoint(), 48).toString().replace("IntPoint", "");
	}
	
}
