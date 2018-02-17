import java.awt.geom.Point2D;

public class BomberMan implements Relocatable, Temporal {
	public int ONE_STEP;
	private Point2D centerPoint;
	protected BomberWorld world;
	private String direction;
	boolean state = true;
	private int bombNumber;
	private int curBombNum = 0;
	private int num;
	private int expRadius;
	private boolean wudi = false;
	private int wudicount = 0;

	public BomberMan(BomberWorld world, int num) {
		this.world = world;
		this.num = num;
		if (num == 1) {
			this.centerPoint = new Point2D.Double(72, 72);
		} else {
			this.centerPoint = new Point2D.Double(72, 552);
		}
		this.direction = "d";
		this.expRadius = 2;
		this.bombNumber = 1;
		this.ONE_STEP = 8;
	}

	public int getNum() {
		return this.num;
	}

	public boolean addBomb() {
		if (this.curBombNum < this.bombNumber) {
			this.curBombNum++;
			return true;
		}
		return false;
	}

	public void removeBomb() {
		this.curBombNum--;
	}

	public int getBombNumber() {
		return this.bombNumber;
	}
	
	public void addBombNumber() {
		this.bombNumber++;
	}

	public int getONE_STEP() {
		return this.ONE_STEP;
	}

	public void setONE_STEP(int oNE_STEP) {
		this.ONE_STEP = oNE_STEP;
	}
	
	public void addSpeed() {
		this.ONE_STEP = this.ONE_STEP + 1;
	}

	public void setBombNumber(int bombNumber) {
		this.bombNumber = bombNumber;
	}

	public int getExpRadius() {
		return this.expRadius;
	}

	public void addExpRadius() {
		this.expRadius++;
	}
	

	public void setCenterPoint(Point2D centerPoint) {
		this.centerPoint = centerPoint;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getDirection() {
		return this.direction;
	}

	@Override
	public void moveTo(Point2D point) {
		setCenterPoint(point);
	}

	@Override
	public Point2D getCenterPoint() {
		return this.centerPoint;
	}

	@Override
	public String getImageName() {
		if (this.num == 1) {
			if (!this.state) {
				return "res/img/mario_o.png";
			}
			return "res/img/mario_" + this.direction + ".png";
		}
		if (!this.state) {
			return "res/img/play2_o.png";
		}
		return "res/img/play2_" + this.direction + ".png";
	}

	@Override
	public void timePassed() {
		
		if (this.wudi) {
			this.wudicount++;
			if (this.wudicount > 200) {
				this.wudi = false;
				this.wudicount = 0;
			}
		}

		for (Temporal cur : this.world.getObjects()) {
			if (cur instanceof PowerUp) {
				if (cur.getCenterPoint().distance(this.centerPoint) < 30) {
					if (classNameCheck(cur, "RangePowerUp") || classNameCheck(cur, "BombPowerUp")
							|| classNameCheck(cur, "SpeedPowerUp")) {
						System.out.println(cur.getClass().getSimpleName());

						cur.die();
						if (cur.getClass().getSimpleName().equals("RangePowerUp")) {
							this.addExpRadius();
						} else if (cur.getClass().getSimpleName().equals("SpeedPowerUp")) {
							this.addSpeed();
						} else {
							this.addBombNumber();
						}

					}
				}
			}
		}

	}

	boolean classNameCheck(Temporal obj, String nameToCheck) {
		return obj.getClass().getName().toLowerCase().equals(nameToCheck.toLowerCase());
	}

	public boolean getState() {
		return this.state;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + IDecoder.pixelToArrayAdress(this.centerPoint, 48).toString().replace("IntPoint", " ");
	}

	@Override
	public void die() {
		// TODO Auto-generated method stub.
		if (this.world.getLabel().getLife() > 0) {
			if (this.state && !this.wudi) {
				this.world.getLabel().reduceLife();
				Runnable tickTock = new Runnable() {
					@Override
					public void run() {
						try {
							BomberMan.this.state = false;
							BomberMan.this.world.setIsPaused();
							Thread.sleep(1000);
							setCenterPoint(new Point2D.Double(72, 72));
							BomberMan.this.state = true;
							BomberMan.this.world.setIsPaused();
							BomberMan.this.wudi = true;
						} catch (InterruptedException exception) {
							// 
						}
					}
				};
				new Thread(tickTock).start();
			}
		} else {

			try {
				BomberMan.this.state = false;
				Thread.sleep(1000);
				BomberMan.this.world.removeObject(this);
				BomberMan.this.world.addObject(new YouLost(BomberMan.this.world));
			} catch (InterruptedException exception) {
				// 
			}

		}
	}

}
