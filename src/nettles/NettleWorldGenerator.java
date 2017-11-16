package nettles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class NettleWorldGenerator {

	private final int maxWorldWidth = 10;
	private final int maxNettles = 2 * maxWorldWidth;
	private Random rng;
	private final File root;
	private int currentNumberOfNettles;
	private final int numberOfWorldsToGenerate;

	public static void main(String[] args) {
		try {
			int numberOfWorlds = Integer.parseInt(args[1]);
			File root = new File(args[0]);
			new NettleWorldGenerator(root, numberOfWorlds);
		} catch (Exception e) {
			System.out.println("Usage: java NettleWorldGenerator <root> <numberOfWorlds>");
			e.printStackTrace();
		}
	}

	public NettleWorldGenerator(File root, int numberOfWorldsToGenerate) {
		this.root = root;
		this.numberOfWorldsToGenerate = numberOfWorldsToGenerate;
		rng = new Random();
		
		for (int i = 0; i < numberOfWorldsToGenerate; i++) {
			System.out.println(numberOfWorldsToGenerate);
			int worldWidth = Math.max(1,rng.nextInt(maxWorldWidth));
			int numbOfNettles = Math.min(2 * worldWidth, rng.nextInt(maxNettles));
			int[][] world = generateWorld(worldWidth, numbOfNettles);
			File dir = new File(root + File.separator + "L" + Integer.toString(worldWidth) + "N" + Integer.toString(numbOfNettles));
			dir.mkdir();
			writeWorldToFile(dir,world, numbOfNettles);
		}

	}

	private void writeWorldToFile(File dir, int[][] world, int numbOfNettles) {
		File file = new File(dir+File.separator+"map.txt");
		try(FileWriter writer = new FileWriter(file);
			BufferedWriter br = new BufferedWriter(writer);
				PrintWriter out = new PrintWriter(br);){
			out.println(world.length);
			out.println(world.length);
			out.println(numbOfNettles);
			for(int i = 0; i < world.length; i++) {
				for (int j = 0; j < world.length; j++) {
					out.print(Integer.toString(world[i][j])+", ");
				}
				out.println();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

	public int[][] generateWorld(int width, int numberOfNettles) {
		int[][] world = new int[width][width];
		int nettlesLeft = numberOfNettles;
		while (nettlesLeft > 0) {
			int i = rng.nextInt(width);
			int j = rng.nextInt(width);
			if (world[i][j] != -1 && (i != 0 && j != 0)) {
				world[i][j] = -1;
				nettlesLeft--;
			}

		}
		
		for(int i =0; i< width; i++) {
			for(int j = 0; j< width; j++) {
				if(world[i][j] != -1) {
					world[i][j] = countAdjacentNettles(world, i, j);
				}
			}
		}

		return world;

	}
	
	private int countAdjacentNettles(int[][] world, int i, int j ) {
		int count = 0;
		for (int k = -1; k < 2; k++) {
			for (int l = -1; l < 2; l++) {
				if (i + k >= 0 && i + k < world.length && j + l >= 0 && j + l < world[0].length
						&& !(k == 0 && l == 0) && world[i+k][j+l] == -1) {
					count++;
					
				}
			}
		}
		return count;
	}


}
