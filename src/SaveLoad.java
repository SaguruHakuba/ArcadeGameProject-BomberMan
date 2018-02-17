import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class SaveLoad {
	
	private JFileChooser Filechooser;
	private BomberWorldComponent component;
	
	public SaveLoad(BomberWorldComponent component){
		this.component=component;
		this.Filechooser=new JFileChooser();
	}
	
	public void loadGame() throws IOException{
		this.component.pause();
		
		if(this.Filechooser.showOpenDialog(this.component) != JFileChooser.APPROVE_OPTION)
			return;
		File input = this.Filechooser.getSelectedFile();
		Scanner inScanner = new Scanner(input);
		Point2D hero = new Point2D.Double(inScanner.nextDouble(), inScanner.nextDouble());
		
		this.component.getWorld().getLabel().setTime(inScanner.nextInt());
		this.component.getWorld().getLabel().setLife(inScanner.nextInt());
		this.component.getWorld().setUp(inScanner.nextInt());
		
		try{
			ArrayList<MapHandler> map = new ArrayList<>();
			int n = inScanner.nextInt();
			for(int k =0; k<n; k++){
				int[][] tempMap = new int[22][14];
				int m = inScanner.nextInt();
				for (int i = 0; i < 14; i++)
					for (int j = 0; j < 22; j++) {
						tempMap[j][i] = inScanner.nextInt();
					}
				MapHandler tmap = new MapHandler(tempMap, k + 1);
				map.add(tmap);
			}
			this.component.setMap(map);		
		}finally{
			this.component.reload(hero);
			inScanner.close();
		}
	}

	public void saveGame(){
		ArrayList<MapHandler> curmap = this.component.getMap();
		if (this.Filechooser.showSaveDialog(this.component) != JFileChooser.APPROVE_OPTION) {
			return;
		}
		File outputFile = this.Filechooser.getSelectedFile();
		try {
			PrintWriter writer = new PrintWriter(outputFile);
			try {
				writer.printf("%f %f%n", this.component.getManLocation().getX(), this.component.getManLocation().getY());
				writer.printf("%d%n", this.component.getWorld().getLabel().getTime());
				writer.printf("%d%n", this.component.getWorld().getLabel().getLife());
				writer.printf("%d%n", this.component.getWorld().getLevel());
				writer.printf("%d%n", curmap.size());
				for (MapHandler c : curmap) {
					writer.printf("%d%n", c.getLevel());
					for (int i = 0; i < 14; i++) {
						for (int j = 0; j < 22; j++) {
							writer.print(c.getMap()[j][i] + " ");
						}
						writer.println();
					}
				}
			} finally {
				writer.close();
				this.component.requestFocus();
			}
		} catch (FileNotFoundException fnfException) {
			String msg = "Unable to save game: " + fnfException.getMessage();
			JOptionPane.showMessageDialog(this.component, msg, "Save Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
