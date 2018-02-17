import java.awt.geom.Point2D;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class BomberWorld {
	protected static long UPDATE_INTERVAL_MS = 10;
	private boolean isPaused = false;
	public Runnable tickTock;
	private int level;
	protected List<Temporal> objects = new ArrayList<>();
	private List<Temporal> toAddObjects = new ArrayList<>();
	private List<Temporal> toRemoveObjects = new ArrayList<>();
	protected int totalPoints;
	protected ArrayList<MapHandler> myMaps;
	private BomberWorldComponent component;
	private int timeLag = 1;
	private boolean gameOverFlag = false;
	private DisplayLabel label;
	private boolean winFlag = false;

	public BomberWorld(int level) {
		this.level = level;
		this.totalPoints = 0;
		try {
			this.myMaps = HandleFile.openFile();
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
		}

		this.tickTock = new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						Thread.sleep(BomberWorld.UPDATE_INTERVAL_MS);
						timePassed();
					}
				} catch (InterruptedException exception) {
					//
				}
			}
		};
		new Thread(this.tickTock).start();
		
	}

	public void setLabel(DisplayLabel label) {
		this.label = label;
	}

	public DisplayLabel getLabel() {
		return this.label;
	}

	public MapHandler getMap() {
		return this.myMaps.get(this.level - 1);
	}

	public synchronized void addObject(Temporal object) {
		this.toAddObjects.add(object);
	}
	
	public synchronized void removeObject(Temporal object) {
		this.toRemoveObjects.add(object);
	}

	public List<Temporal> getToRemoveObjects() {
		return this.toRemoveObjects;
	}

	public synchronized void timePassed() {
		if (this.label != null) {
			this.label.timePassed();
			this.label.setScore(BomberWorld.this.totalPoints);
		}
		if (this.label != null && this.label.getTime() < 0) {
			this.isPaused = true;
			int count = 0;
			
			for (Temporal t : this.objects) {
				if (t instanceof YouLost) {
					count++;
				}
				if (!this.gameOverFlag) {
					this.gameOverFlag = true;
					this.addObject(new YouLost(this));
					this.isPaused = true;
				}
			}
		}	
		if (this.isPaused) {
			return;
		}
		
		if(this.timeLag ==0  && this.getObjects().size() >= 1){
			if(this.checkVictory()){
				if(this.level<this.myMaps.size()-15){
					try{
						Thread.sleep(800);
						this.levelUp();
					}catch(InterruptedException e){
						//
					}
				}else if(!this.winFlag){
					this.winFlag=true;
					this.addObject(new YouWin(this));
					this.isPaused=true;
						
				}
			}			
		}
		
		for (Temporal t : this.objects) {
			t.timePassed();
		}

		this.objects.removeAll(this.toRemoveObjects);
		this.toRemoveObjects.clear();
		this.objects.addAll(this.toAddObjects);
		this.toAddObjects.clear();
	}

	public void setIsPaused() {
		this.isPaused = !this.isPaused;

	}

	public boolean getIsPaused() {
		return this.isPaused;
	}
	
	public boolean  checkVictory(){
		for (Temporal t : this.objects) {
			if(t instanceof Monster){
				return false;
			}
		}
		
		
		return true;
	}

	public synchronized List<Temporal> getDrawableParts() {
		return new ArrayList<>(this.objects);
	}

	public boolean isPassable(Point2D currentPoint, String direction) {
		IntPoint currentIntPoint = IDecoder.pixelToArrayAdress(currentPoint, 48);
		int col = currentIntPoint.getX();
		int row = currentIntPoint.getY();
		Point2D leftPoint = IDecoder.toLeftPoint(currentPoint);
		Point2D rightPoint = IDecoder.toRightPoint(currentPoint);
		Point2D upPoint = IDecoder.toUpPoint(currentPoint);
		Point2D downPoint = IDecoder.toDownPoint(currentPoint);
		IntPoint leftIntPoint = IDecoder.pixelToArrayAdress(leftPoint, 48);
		IntPoint rightIntPoint = IDecoder.pixelToArrayAdress(rightPoint, 48);
		IntPoint upIntPoint = IDecoder.pixelToArrayAdress(upPoint, 48);
		IntPoint downIntPoint = IDecoder.pixelToArrayAdress(downPoint, 48);
		MapHandler thisLevelMap = this.myMaps.get(this.level - 1);
		if (direction.equals("u")) {
			int left = thisLevelMap.getMap()[leftIntPoint.getX()][leftIntPoint.getY() - 1];
			int right = thisLevelMap.getMap()[rightIntPoint.getX()][rightIntPoint.getY() - 1];
			int cur = thisLevelMap.getMap()[col][row - 1];
			if (cur < 0 || left < 0 || right < 0) {
				Point2D newPoint = IDecoder.arrayAdressToPixel(col, row - 1, 48);
				int distance = 48;
				return leftPoint.getY() - newPoint.getY() > distance && currentPoint.getY() - newPoint.getY() > distance
						&& rightPoint.getY() - newPoint.getY() > distance;
			}
			return true;
		}
		if (direction.equals("d")) {
			int left = thisLevelMap.getMap()[leftIntPoint.getX()][leftIntPoint.getY() + 1];
			int right = thisLevelMap.getMap()[rightIntPoint.getX()][rightIntPoint.getY() + 1];
			int cur = thisLevelMap.getMap()[col][row + 1];
			if (cur < 0 || left < 0 || right < 0) {
				Point2D newPoint = IDecoder.arrayAdressToPixel(col, row + 1, 48);
				int distance = 48;
				return newPoint.getY() - currentPoint.getY() > distance && newPoint.getY() - leftPoint.getY() > distance
						&& newPoint.getY() - rightPoint.getY() > distance;
			}
			return true;
		}
		if (direction.equals("l")) {
			int up = thisLevelMap.getMap()[upIntPoint.getX() - 1][upIntPoint.getY()];
			int down = thisLevelMap.getMap()[downIntPoint.getX() - 1][downIntPoint.getY()];
			int cur = thisLevelMap.getMap()[col - 1][row];
	
			if (cur < 0 || up < 0 || down < 0) {
				Point2D newPoint = IDecoder.arrayAdressToPixel(col - 1, row, 48);
				int distance = 48;
				return currentPoint.getX() - newPoint.getX() > distance && upPoint.getX() - newPoint.getX() > distance
						&& downPoint.getX() - newPoint.getX() > distance;
			}
			return true;
		}
		int up = thisLevelMap.getMap()[upIntPoint.getX() + 1][upIntPoint.getY()];
		int down = thisLevelMap.getMap()[downIntPoint.getX() + 1][downIntPoint.getY()];
		int cur = thisLevelMap.getMap()[col + 1][row];
		if (up < 0 || cur < 0 || down < 0) {

			Point2D newPoint = IDecoder.arrayAdressToPixel(col + 1, row, 48);
			int distance = 48;
			return newPoint.getX() - currentPoint.getX() > distance && newPoint.getX() - upPoint.getX() > distance
					&& newPoint.getX() - downPoint.getX() > distance;
		}
		return true;
	}

	public Temporal Objectat(IntPoint point) {
		for (Temporal t : this.objects) {
			if (IDecoder.pixelToArrayAdress(t.getCenterPoint(), 48).equals(point)) {
				return t;
			}
		}
		return null;

	}

	public void setMapPoint(int x, int y, int value) {
		this.myMaps.get(this.level - 1).setMapValue(x, y, value);
	}

	public synchronized int getLevel() {
		return this.level;
	}

	synchronized public void levelUp() {
		if (this.level < this.myMaps.size()-15) {
			this.level=this.level+14;
		
			BomberMan player = null;
			BomberMan player2 = null;
			for (Temporal t : this.objects) {
				if (t instanceof BomberMan) {
					if (((BomberMan) t).getNum() == 1) {
						player = (BomberMan) t;
					} else {
						player2 = (BomberMan) t;
					}
				}
			}
			if (player != null) 
				player.moveTo(IDecoder.arrayAdressToPixel(1, 1, 48));
			if (player2 != null) 
				player2.moveTo(IDecoder.arrayAdressToPixel(1, 11, 48));
			
			this.objects.removeAll(this.objects);
			this.objects.add(player);
			if (player2 != null) {
				this.objects.add(player2);
			}
			
			this.component.load();
			this.label.reset();
		}
	}


	public void restall() {
		this.objects = new ArrayList<>();
		this.toAddObjects = new ArrayList<>();
		this.toRemoveObjects = new ArrayList<>();
	}

	synchronized public void levelDown() {
		if (this.level > 1) {
			this.level=this.level-14;
			BomberMan player = null;
			BomberMan player2 = null;
			for (Temporal t : this.objects) {
				if (t instanceof BomberMan) {
					if (((BomberMan) t).getNum() == 1) {
						player = (BomberMan) t;
					} else {
						player2 = (BomberMan) t;
					}
				}
			}
			if (player != null) {
				player.moveTo(IDecoder.arrayAdressToPixel(1, 1, 48));
			}
			if (player2 != null) {
				player2.moveTo(IDecoder.arrayAdressToPixel(1, 11, 48));
			}
			this.objects.removeAll(this.objects);
			this.objects.add(player);
			if (player2 != null) {
				this.objects.add(player2);
			}
			
			this.component.load();
			this.label.reset();
		}
	}
	
	synchronized public void setUp(int level){
		this.level=level;
		BomberMan player = null;
		BomberMan player2 = null;
		for (Temporal t : this.objects) {
			if (t instanceof BomberMan) {
				if (((BomberMan) t).getNum() == 1) {
					player = (BomberMan) t;
				} else {
					player2 = (BomberMan) t;
				}
			}
		}
		if (player != null) 
			player.moveTo(IDecoder.arrayAdressToPixel(1, 1, 48));
		if (player2 != null) 
			player2.moveTo(IDecoder.arrayAdressToPixel(1, 11, 48));
		this.objects.removeAll(this.objects);
		this.objects.add(player);
		if (player2 != null) 
			this.objects.add(player2);
	}

	public int getTimeLag() {
		return this.timeLag;
	}

	public void setTimeLag(int timeLag) {
		this.timeLag = timeLag;
	}

	public synchronized void addPoints(int pointsToAdd) {
		this.totalPoints += pointsToAdd;
	}

	public synchronized int getPoints() {
		return this.totalPoints;
	}

	public List<Temporal> getObjects() {
		return this.objects;
	}

	public void setComponent(BomberWorldComponent component) {
		this.component = component;
	}
	

	

}
