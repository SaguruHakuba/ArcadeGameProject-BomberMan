import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

public class BomberWorldFrame extends JFrame {
	public BomberWorldFrame(int playerNum) {
		super();
		setTitle("BomberMan Game");
		
		final SimulationPanel panel = new SimulationPanel(1, playerNum);
		add(panel);
		setResizable(false);
		
		this.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {
				BomberWorldComponent component = panel.getComponent();
				component.requestFocus();
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub.

			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub.

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub.

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub.

			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub.

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub.

			}

		});
		pack();
	}

}
