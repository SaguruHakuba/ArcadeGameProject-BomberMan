import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;

public final class SimulationPanel extends JPanel {

	private BomberWorldComponent component;

	public SimulationPanel(int level, int playerNum) {
		setLayout(new BorderLayout());
		
		BomberWorld world = new BomberWorld(level);
		DisplayLabel toDisplay = new DisplayLabel(world, 200, 0, 3);
		world.setLabel(toDisplay);
		
		Font font = new Font("Garamond", Font.BOLD, 30);
		toDisplay.setFont(font);
		toDisplay.setBackground(new Color(60, 60, 60));
		toDisplay.setForeground(Color.WHITE);
		add(toDisplay, BorderLayout.NORTH);

		this.component = new BomberWorldComponent(world, playerNum);
		this.component.requestFocus();
		
		world.setComponent(this.component);
		add(this.component, BorderLayout.CENTER);
	}

	public BomberWorldComponent getComponent() {
		return this.component;
	}
}
