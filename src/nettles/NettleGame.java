package nettles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class NettleGame {
//
//	private MapCell[][] map;
//	private int mapLength = -1;
//	private int mapWidth = -1;
	private Map map;
	private int numberOfNettels = -1;
	private String tabs = "";

	NettleAgent agent;

	public NettleGame(File mapFile) {
		map = new Map(mapFile);
		agent = new NettleAgent(this);

	}






	
	public static void main(String[] args) {

		assert args.length >= 1;
		final File root = new File(args[0]);
		for (final File difficultyDir : root.listFiles()) {
			System.out.println("Difficulty: " + difficultyDir.getName());
			for (final File mapDir : difficultyDir.listFiles()) {
				File map = new File(mapDir.toString() + File.separator + "map.txt");
				System.out.println("\t" + mapDir.getName());
				new NettleGame(map);

			}
		}

	}
	// end of functions taken from A2 practical

}
