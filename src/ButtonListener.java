import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class ButtonListener implements ActionListener {
	private JFrame frame;
	private BomberWorld world;
	
	public ButtonListener(JFrame frame,BomberWorld world){
		this.frame = frame;
		this.world = world;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.frame.dispose();
		this.world.setIsPaused();
	}

}
