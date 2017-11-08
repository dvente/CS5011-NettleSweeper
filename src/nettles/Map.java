package nettles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Map {
	// The next functions were taken from the A2 practical
	public Map(File file) {
		super();

		try (BufferedReader in = new BufferedReader(new FileReader(file));) {
			mapLength = Integer.parseInt(in.readLine().trim());
			mapWidth = Integer.parseInt(in.readLine().trim());
			setNumberOfNettels(Integer.parseInt(in.readLine().trim()));
			map = new MapCell[mapLength][mapWidth];
			String line;
			String[] splitLine;

			for (int i = 0; i < getMapLength(); i++) {
				line = in.readLine();
				splitLine = line.split(",");
				for (int j = 0; j < getMapWidth(); j++) {
					map[i][j] = new MapCell(i, j, Integer.parseInt(splitLine[j].trim()));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		} 
		checkWorld();
		printMap();
	}

	private MapCell[][] map = null;
	private int mapLength = -1;
	private int mapWidth = -1;
	private int numberOfNettels = -1;

	public void printMap() {
		String tabs = "\t\t";
		System.out.println(tabs + "number of nettles: " + Integer.toString(numberOfNettels));
		for (int i = 0; i < getMapLength(); i++) {
			System.out.print(tabs);
			for (int j = 0; j < getMapWidth(); j++) {
				System.out.print(getCellAt(i, j).toMapString());
			}
			System.out.println();
		}
		System.out.println();
	}

	public MapCell getCellAt(int i, int j) {
		return map[i][j];
	}

	public void setCell(MapCell cell) {
		map[cell.getI()][cell.getJ()] = cell;
	}

	public int getMapLength() {
		return mapLength;
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public void checkWorld() {
		int count = 0;
		for (int i = 0; i < getMapLength(); i++) {
			for (int j = 0; j < getMapWidth(); j++) {
				if (getCellAt(i, j).getNumberOfAdjacentNettles() == -1) {
					count++;
				}
			}
		}
		assert count == numberOfNettels : "INVALID NUMBER OF NETTLES \nnumber found: " + Integer.toString(count)
				+ "\nnumber provided: " + Integer.toString(numberOfNettels);

	}

	public List<MapCell> getChildren(MapCell cell) {

		List<MapCell> answer = new LinkedList<MapCell>();

		for (int k = -1; k < 2; k++) {
			for (int l = -1; l < 2; l++) {
				if (cell.getI() + k >= 0 && cell.getI() < getMapLength() && cell.getJ() + l >= 0
						&& cell.getJ() + l < getMapWidth()) {
					answer.add(getCellAt(cell.getI() + k, cell.getJ() + l));
				}
			}
		}

		return answer;
	}

	public int probe(int i, int j) {
		assert getCellAt(i, j).isHidden() : "Trying to probe revealed cell " + map[i][j].toString();
		getCellAt(i, j).setHidden(false);
		return getCellAt(i, j).getNumberOfAdjacentNettles();

	}

	public int getNumberOfNettels() {
		return numberOfNettels;
	}

	public void setNumberOfNettels(int numberOfNettels) {
		this.numberOfNettels = numberOfNettels;
	}

}
