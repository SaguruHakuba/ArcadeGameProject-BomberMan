/*
 * Class to handle with the map
 */
public class MapHandler {
	private int[][] map;
	private int level;

	public MapHandler(int[][] map, int level) {
		this.setMap(map);
		this.setLevel(level);
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getLevel() {
		return this.level;
	}

	public void setMap(int[][] map) {
		this.map = map;
	}

	public int[][] getMap() {
		return this.map;
	}

	public void setMapValue(int x, int y, int value) {
		this.map[x][y] = value;
	}

}
