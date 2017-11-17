package nettles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Generates Nettlesweeper worlds for testing.
 *
 * @author 170008773
 */
public class NettleWorldGenerator {

    private final int maxWorldWidth = 10;
    private final int maxNettles = 2 * maxWorldWidth;
    private Random rng;

    /**
     * does some input checking and calls the generator function
     *
     * @param args
     *            the arguments from the command-line
     */
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

    /**
     * Generates a provided number of random Nettle worlds
     *
     * @param root
     *            the directory to place the generated worlds
     * @param numberOfWorldsToGenerate
     *            the number of worlds to generate
     */
    public NettleWorldGenerator(File root, int numberOfWorldsToGenerate) {
        rng = new Random();

        for (int i = 0; i < numberOfWorldsToGenerate; i++) {

            int worldWidth = Math.max(3, rng.nextInt(maxWorldWidth));
            int numbOfNettles = Math.min(Math.max(worldWidth, rng.nextInt(maxNettles)), worldWidth * 2);
            int[][] world = generateWorld(worldWidth, numbOfNettles);
            File dir = new File(
                    root + File.separator + "L" + Integer.toString(worldWidth) + "N" + Integer.toString(numbOfNettles));
            // don't overwrite worlds that are already there
            if (dir.exists()) {
                continue;
            }
            dir.mkdir();
            writeWorldToFile(dir, world, numbOfNettles);
        }

    }

    /**
     * Writes a generated Nettle world to the given directory.
     *
     * @param dir
     *            the directory to write the file to
     * @param world
     *            the array containing the generated world
     * @param numbOfNettles
     *            the number of nettles in the generated world
     */
    private void writeWorldToFile(File dir, int[][] world, int numbOfNettles) {

        File file = new File(dir + File.separator + "map.txt");
        try (FileWriter writer = new FileWriter(file);
                BufferedWriter br = new BufferedWriter(writer);
                PrintWriter out = new PrintWriter(br);) {
            //simply loop thorough the world and write each cell along the way
            for (int i = 0; i < world.length; i++) {
                for (int j = 0; j < world.length; j++) {
                    out.print(Integer.toString(world[i][j]) + ", ");
                }
                out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Generate a random nettle world with the given parameters
     *
     * @param width
     *            the widht of the world to be generated
     * @param numberOfNettles
     *            the number of nettles to place in the world
     * @return an array containing the generated wordl
     */
    public int[][] generateWorld(int width, int numberOfNettles) {

        int[][] world = new int[width][width];
        int nettlesLeft = numberOfNettles;
        while (nettlesLeft > 0) {
            int i = rng.nextInt(width);
            int j = rng.nextInt(width);
            // make sure we don't place nettles on top of each other
            // and that 0,0 is safe.
            if (world[i][j] != -1 && !(i == 0 && j == 0)) {
                //place the nettle
                world[i][j] = -1;
                nettlesLeft--;
            }

        }

        //nettles are placed so fill the rest of the world
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (world[i][j] != -1) {
                    world[i][j] = countAdjacentNettles(world, i, j);
                }
            }
        }

        return world;

    }

    /**
     * counts number of nettles adjacent to the provided coordinates
     *
     * @param world
     *            the array containing the world
     * @param i
     *            the vetical coordiante of the cell to count
     * @param j
     *            the horizontal coordinate of the cell to count
     * @return the number of adjacent nettles of the cell at the provided
     *         coordinates
     */
    private int countAdjacentNettles(int[][] world, int i, int j) {

        int count = 0;
        for (int k = -1; k < 2; k++) {
            for (int l = -1; l < 2; l++) {
                if (i + k >= 0 && i + k < world.length && j + l >= 0 && j + l < world[0].length && !(k == 0 && l == 0)
                        && world[i + k][j + l] == -1) {
                    count++;

                }
            }
        }
        return count;
    }

}
