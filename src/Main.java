import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The main class for your arcade game.
 * 
 * You can design your game any way you like, but make the game start
 * by running main here.
 * 
 * Also don't forget to write javadocs for your classes and functions!
 * 
 * @author Buffalo
 *
 */
public class Main {
	protected static final int BomberWorld_WIDTH = 1065;
	protected static final int BomberWorld_HEIGHT = 693;
	protected static final int TITLE_BAR_HEIGHT = 48;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Write your cool arcade game here!");
		final JFrame start = new JFrame();
		JButton btn1 = new JButton();
		btn1.setText("1 Player");
		JButton btn2 = new JButton();
		btn2.setText("2 Player");
		IComponent ins = new IComponent();
		JPanel panel = new JPanel();
		panel.add(btn1);
		panel.add(btn2);
		start.setTitle("Bomber Man Arcade Game");
		start.add(ins, BorderLayout.CENTER);
		start.add(panel, BorderLayout.SOUTH);
		start.setSize(BomberWorld_WIDTH, BomberWorld_HEIGHT + TITLE_BAR_HEIGHT + 48);
		start.setVisible(true);

		btn1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BomberWorldFrame frame = new BomberWorldFrame(1);
				frame.setSize(BomberWorld_WIDTH,BomberWorld_HEIGHT + TITLE_BAR_HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				start.dispose();
			}

		});
		btn2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				BomberWorldFrame frame = new BomberWorldFrame(2);
				frame.setSize(BomberWorld_WIDTH, BomberWorld_HEIGHT + TITLE_BAR_HEIGHT);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
				start.dispose();
			}

		});
	
	}

}
