import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class BomberWorldComponent extends JComponent implements KeyListener {
	private BomberWorld world;
	private HashMap<Integer, String> intToPicName;
	private BomberMan player;
	private BomberMan player2;
	private boolean isTwoPlayer;
	private HashMap<Integer, Integer> keyCodes;
	private SaveLoad IHandler;
	int freshCount = 0;

	@SuppressWarnings("boxing")
	public BomberWorldComponent(BomberWorld World, int playerNum) {
		this.world = World;
		this.intToPicName = new HashMap<>();
		this.intToPicName.put(0, "grass");
		this.intToPicName.put(-2, "breakablebrick");
		this.intToPicName.put(-1, "concrete");
		this.keyCodes = new HashMap<>();
		this.IHandler = new SaveLoad(this);

		
		Runnable repainter = new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						Thread.sleep(10);
						repaint();
						BomberWorldComponent.this.freshCount++;
						if (BomberWorldComponent.this.freshCount >= 3) {
							handleKeyCodes();
							BomberWorldComponent.this.freshCount = 0;
						}
					}
				} catch (InterruptedException exception) {
					// Stop when interrupted
				}
			}
		};
		new Thread(repainter).start();
		this.player = new BomberMan(this.world, 1);
		this.world.addObject(this.player);
		if (playerNum == 2) {
			this.isTwoPlayer = true;
			this.player2 = new BomberMan(this.world, 2);
			this.world.addObject(this.player2);
		} else {
			this.isTwoPlayer = false;
		}
		this.addKeyListener(this);
		load();
	}

	
	public void reload(Point2D point) {
		for (Temporal t : this.world.getObjects()) {
			if (!(t instanceof BomberMan)) {
				this.world.removeObject(t);
			} else {
				this.player = (BomberMan) t;
				this.player.setCenterPoint(point);
			}
		}
		this.load();
	}
	
	public Point2D getManLocation() {
		return this.player.getCenterPoint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		MapHandler map = this.world.getMap();
		Graphics2D g2 = (Graphics2D) g;

		BufferedImage img;
		try {
			for (int i = 0; i < map.getMap().length; i++) {
				for (int j = 0; j < map.getMap()[i].length; j++) {
					img = ImageIO.read(new File("res/img/grass.png"));
					g2.drawImage(img, i*48, j*48, null);
				}
			}
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		List<Temporal> toAdd = this.world.getDrawableParts();
		for (Temporal d : toAdd) {
			drawTemporal(g2, d);
		}
	}

	public void setMap(ArrayList<MapHandler> newMap) {
		this.world.myMaps = newMap;
	}

	public ArrayList<MapHandler> getMap() {
		return this.world.myMaps;
	}

	
	private static void drawTemporal(Graphics2D g2, Temporal d) {
		BufferedImage img;
		try {
			img = ImageIO.read(new File(d.getImageName()));// "res/img/bomb.png"
			Point2D centerPoint = d.getCenterPoint();
			g2.drawImage(img, (int) (centerPoint.getX() - img.getWidth() / 2),
					(int) (centerPoint.getY() - img.getHeight() / 2), null);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	public void load() {
		MapHandler map = this.world.getMap();
		for (int i = 0; i < map.getMap().length; i++) {
			for (int j = 0; j < map.getMap()[i].length; j++) {
				int imageNum = map.getMap()[i][j];
				Point2D centerPoint = IDecoder.arrayAdressToPixel(i, j, 48);
				switch (imageNum) {
				case -1:
					Concrete concrete = new Concrete(this.world, centerPoint);
					this.world.addObject(concrete);
					break;
				case -2:
					BreakableBrick breakbrick = new BreakableBrick(this.world, centerPoint);
					this.world.addObject(breakbrick);
					break;
				case 1:
					Goomba goomba = new Goomba(this.world,centerPoint);
					this.world.addObject(goomba);
					break;
				case 2:
					Blooper caonima = new Blooper(this.world,centerPoint);
					this.world.addObject(caonima);
					
					break;
				default:
					break;
				}
			}
		}
		
	}

	public BomberWorld getWorld() {
		return this.world;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub.

	}

	@Override
	public void keyPressed(KeyEvent e) {
		Integer keyCode = e.getKeyCode();
		this.keyCodes.put(keyCode, 0);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Integer keyCode = e.getKeyCode();
		this.keyCodes.remove(keyCode);
		switch (e.getKeyCode()) {
		case KeyEvent.VK_EQUALS:
			this.world.levelUp();
			this.world.setTimeLag(2);
			break;
		case KeyEvent.VK_MINUS:
			this.world.levelDown();
			this.world.setTimeLag(2);
			break;
		case KeyEvent.VK_P:
			this.world.setIsPaused();
			break;
		case KeyEvent.VK_Q:
			this.world.setIsPaused();
			JFrame frame = new JFrame();
			frame.setTitle("Key Menu");
			frame.setLocation(150, 300);
			final JTextArea label = new JTextArea();
			label.setBackground(new Color(60, 60, 60));
			frame.add(label, BorderLayout.CENTER);
			label.setText("I load\nP:pause\nU: Save\n\nPlayer1:\nup down left right\nM drop bomb\nPlayer2:\nW A S D\nZ drop bomb\n");
			label.setEditable(false);
			label.setForeground(Color.WHITE);
			label.setFont(new Font("Courier", Font.BOLD, 25));
			JButton temp = new JButton("close");
			temp.addActionListener(new ButtonListener(frame, this.world));
			frame.add(temp, BorderLayout.SOUTH);
			frame.pack();
			frame.setVisible(true);
			frame.addWindowListener(new WindowsAdapter(this.world));
			break;
		default:
			break;
		}

	}

	public void pause() {
		this.world.setIsPaused();
	}

	public void handleKeyCodes() {
		double newX, newY;
		double newX2, newY2;
		Point2D newPoint = null;
		Point2D newPoint2 = null;
		for (Integer keyCode : this.keyCodes.keySet()) {
			switch (keyCode) {
			case KeyEvent.VK_UP:
				newX = this.player.getCenterPoint().getX();
				newY = this.player.getCenterPoint().getY() - this.player.ONE_STEP;
				newPoint = new Point2D.Double(newX, newY);
				this.player.setDirection("u");
				break;
			case KeyEvent.VK_DOWN:
				newX = this.player.getCenterPoint().getX();
				newY = this.player.getCenterPoint().getY() + this.player.ONE_STEP;
				newPoint = new Point2D.Double(newX, newY);
				this.player.setDirection("d");
				break;
			case KeyEvent.VK_LEFT:
				newX = this.player.getCenterPoint().getX() - this.player.ONE_STEP;
				newY = this.player.getCenterPoint().getY();
				newPoint = new Point2D.Double(newX, newY);
				this.player.setDirection("l");
				break;
			case KeyEvent.VK_RIGHT:
				newX = this.player.getCenterPoint().getX() + this.player.ONE_STEP;
				newY = this.player.getCenterPoint().getY();
				newPoint = new Point2D.Double(newX, newY);
				this.player.setDirection("r");
				break;

			case KeyEvent.VK_M:
				IntPoint decodedIntPoint = IDecoder.pixelToArrayAdress(this.player.getCenterPoint(),48);
				Point2D bombCenter = IDecoder.arrayAdressToPixel(decodedIntPoint.getX(), decodedIntPoint.getY(),48);
				this.world.setTimeLag(0);
				if (this.player.addBomb()) {
					Bomb newBomb = new Bomb(this.world, this.player.getExpRadius(), bombCenter, this.player);
					this.world.addObject(newBomb);
					this.world.setMapPoint(decodedIntPoint.getX(), decodedIntPoint.getY(), -3);
				}
				break;


			case KeyEvent.VK_W:
				if (!this.isTwoPlayer) {
					return;
				}
				newX2 = this.player2.getCenterPoint().getX();
				newY2 = this.player2.getCenterPoint().getY() - this.player2.ONE_STEP;
				newPoint2 = new Point2D.Double(newX2, newY2);
				this.player2.setDirection("u");
				break;
			case KeyEvent.VK_S:
				if (!this.isTwoPlayer) {
					return;
				}
				newX2 = this.player2.getCenterPoint().getX();
				newY2 = this.player2.getCenterPoint().getY() + this.player2.ONE_STEP;
				newPoint2 = new Point2D.Double(newX2, newY2);
				this.player2.setDirection("d");
				break;
			case KeyEvent.VK_A:
				if (!this.isTwoPlayer) {
					return;
				}
				newX2 = this.player2.getCenterPoint().getX() - this.player2.ONE_STEP;
				newY2 = this.player2.getCenterPoint().getY();
				newPoint2 = new Point2D.Double(newX2, newY2);
				this.player2.setDirection("l");
				break;
			case KeyEvent.VK_D:
				if (!this.isTwoPlayer) {
					return;
				}
				newX2 = this.player2.getCenterPoint().getX() + this.player2.ONE_STEP;
				newY2 = this.player2.getCenterPoint().getY();
				newPoint2 = new Point2D.Double(newX2, newY2);
				this.player2.setDirection("r");
				break;
			case KeyEvent.VK_Z:
				if (!this.isTwoPlayer) {
					return;
				}
				IntPoint decodedIntPoint2 = IDecoder.pixelToArrayAdress(this.player2.getCenterPoint(),48);
				Point2D bombCenter2 = IDecoder.arrayAdressToPixel(decodedIntPoint2.getX(), decodedIntPoint2.getY(),48);
				this.world.setTimeLag(0);
				if (this.player2.addBomb()) {
					Bomb newBomb = new Bomb(this.world, this.player2.getExpRadius(), bombCenter2, this.player2);
					this.world.addObject(newBomb);
					this.world.setMapPoint(decodedIntPoint2.getX(), decodedIntPoint2.getY(), -3);
				}
				break;
				
			case KeyEvent.VK_U:// save
				if (this.isTwoPlayer) {
					return;
				}
				this.IHandler.saveGame();
				this.pause();
				break;
			case KeyEvent.VK_I:// load
				if (this.isTwoPlayer) {
					return;
				}
				try {
					this.IHandler.loadGame();
				} catch (IOException exception) {
					exception.printStackTrace();
				}
				break;
				
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
			default:
				break;
			}
			if (newPoint != null && this.world.isPassable(this.player.getCenterPoint(), this.player.getDirection())) {
				if (!this.world.getIsPaused()) {
					this.player.moveTo(newPoint);
				}
			}
			if (newPoint2 != null && this.world.isPassable(this.player2.getCenterPoint(), this.player2.getDirection())) {
				if (!this.world.getIsPaused()) {
					this.player2.moveTo(newPoint2);
				}
			}
		}
	}



}
