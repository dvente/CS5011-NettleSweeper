package nettles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Observable;

import javafx.util.Pair;

/**
 * @author 170008773
 */
public class NettleGame extends Observable {

    private int[][] world;
    public static boolean gameOver = false;
    public static boolean failed = false;

    NettleAgent agent;
    private int numberOfNettles;

    private int runsUntillSucess = 0;
    private final int maxRuns = 1000;
    private static boolean verbose;
    public static String tabs = "";

    @SuppressWarnings("javadoc")
    public static boolean getVerbose() {

        return verbose;
    }

    @SuppressWarnings("javadoc")
    public static boolean hasFailed() {

        return failed;
    }

    @SuppressWarnings("javadoc")
    public static void setFailed(boolean failed) {

        NettleGame.failed = failed;
    }

    @SuppressWarnings("javadoc")
    public static void prettyPrint(String str) {

        System.out.println(tabs + str);
    }

    @SuppressWarnings("javadoc")
    public static void printIfVerbose(String str) {

        if (verbose) {
            prettyPrint(str);
        }
    }

    private void report() {

        tabs = "\t\t\t";
        agent.setDone(true);

        printIfVerbose("Final number of random guesses: " + Integer.toString(agent.getRandomGuessCounter()));
        printIfVerbose("Final number of probes: " + Integer.toString(agent.getProbeCounter()));
        printIfVerbose("Final number of flags: " + Integer.toString(agent.getNumberOfFlaggedCells()));
        printIfVerbose("Number of runs untill success: " + Integer.toString(runsUntillSucess));

        setChanged();
        notifyObservers(new Pair<String, Integer>("randomGuesses", agent.getRandomGuessCounter()));
        setChanged();
        notifyObservers(new Pair<String, Integer>("probes", agent.getProbeCounter()));
        setChanged();
        notifyObservers(new Pair<String, Integer>("flags", agent.getNumberOfFlaggedCells()));
        setChanged();
        notifyObservers(new Pair<String, Integer>("runsUntilSuccess", runsUntillSucess));

    }

    /**
     * spinns up the agent and has it repeatedly traversing the world untill it
     * succeeds or until a maximum speciefied number of times. We thought this
     * was a better metric for performance then whether is simply succeeded once
     * or not
     */
    public void startGame() {

        do {
            printIfVerbose("Starting new game");
            agent.reset();
            agent.firstMove();
            agent.uncoverWorld();
            runsUntillSucess++;
        } while (failed && runsUntillSucess < maxRuns);
        report();

    }

    /**
     * @param agent
     *            the agent that will reveal the world
     * @param world
     *            the world the agent will traverse
     * @param numberOfNettles
     *            number of nettles present in the world
     * @param verbose
     *            should the agin print the steps it takes?
     */
    public NettleGame(NettleAgent agent, int[][] world, int numberOfNettles, boolean verbose) {
        NettleGame.verbose = verbose;
        this.world = world;
        this.numberOfNettles = numberOfNettles;
        this.agent = agent;
        tabs = "\t\t";

    }

    /**
     * reads the world form the file in the directory that is provided form the
     * command line. Then it set's up the game and runs it. this fuction was
     * taken and addapted form A2
     *
     * @param args
     *            arguments form the command line
     */
    public static void main(String[] args) {

        try (BufferedReader in = new BufferedReader(new FileReader(args[0] + File.separator + "map.txt"));) {
            int mapLength = Integer.parseInt(in.readLine().trim());
            int mapWidth = Integer.parseInt(in.readLine().trim());
            int numberOfNettels = Integer.parseInt(in.readLine().trim());
            int[][] map = new int[mapLength][mapWidth];
            String line;
            String[] splitLine;

            for (int i = 0; i < mapLength; i++) {
                line = in.readLine();
                splitLine = line.split(",");
                for (int j = 0; j < mapWidth; j++) {
                    map[i][j] = Integer.parseInt(splitLine[j].trim());
                }
            }
            KnowledgeBase kb = new KnowledgeBase(mapLength, mapWidth, numberOfNettels);
            Strategy strat = new EasyEquationStrategy(kb);
            NettleAgent agent = new NettleAgent(kb, numberOfNettels);
            NettleGame game = new NettleGame(agent, map, numberOfNettels, true);

            agent.setGame(game);
            agent.setStrat(strat);
            game.startGame();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * checks world for validity. At time of writing only checks if world has
     * the number of nettles that the file says it does. Throws assertion error
     * if it doesn't
     */
    public void checkWorld() {

        int count = 0;
        for (int i = 0; i < getWorldLength(); i++) {
            for (int j = 0; j < getWorldWidth(); j++) {
                if (world[i][j] == -1) {
                    count++;
                }
            }
        }
        assert count == numberOfNettles : "INVALID NUMBER OF NETTLES \nnumber found: " + Integer.toString(count)
                + "\nnumber provided: " + Integer.toString(numberOfNettles);

    }

    /**
     * @return width of the world
     */

    private int getWorldWidth() {

        return world[0].length;
    }

    /**
     * @return length of the world
     */
    private int getWorldLength() {

        return world.length;
    }

    /**
     * probes a cell and returns the appropriate number
     *
     * @param cell
     *            the cell to be probed
     * @return the number of nettles adjacent ot the provided cell.
     */
    public int probe(MapCell cell) {

        int numbOfNettles = world[cell.getI()][cell.getJ()];
        if (numbOfNettles == -1) {
            failed = true;
            agent.setDone(true);
        }
        return numbOfNettles;

    }

}
