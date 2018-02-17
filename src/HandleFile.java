import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class HandleFile {

	public static ArrayList<MapHandler> openFile() throws FileNotFoundException {
		ArrayList<MapHandler> toReturn = new ArrayList<>();
		File file = new File("res/levelfiles/level.txt");
		Scanner scanner = new Scanner(file);
		int n = scanner.nextInt();
		for(int k=0; k< n;k++){
			int[][] tempMap = new int[22][14];
			int m = scanner.nextInt();
			for (int i = 0; i < 14; i++) {
				for (int j = 0; j < 22; j++) {
					tempMap[j][i] = scanner.nextInt();
				}
				
				MapHandler temmap = new MapHandler(tempMap, k);
				toReturn.add(temmap);
			}
		}
		scanner.close();
		return toReturn;
	}
}
