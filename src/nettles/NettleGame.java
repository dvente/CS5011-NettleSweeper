package nettles;

import java.util.Observable;
import java.util.Observer;

public class NettleGame implements Observer {

    private Map map;
    private boolean gameOver = false;
    private boolean failed = false;
    private static final boolean verbose = true;
    public static String tabs = "";

    NettleAgent agent;

    public static void prettyPrint(String str) {

        System.out.println(tabs + str);
    }

    public static void printIfVerbose(String str) {

        if (verbose) {
            prettyPrint(str);
        }
    }

    private void endGame() {

        tabs = "\t\t";
        gameOver = true;
        if (failed) {
            printIfVerbose("GAME OVER");
        } else {
            printIfVerbose("YOU WON! CONGLATURATIONS!");

        }
        prettyPrint("Final number of random guesses: " + Integer.toString(agent.getRandomGuessCounter()));
        prettyPrint("Final number of probes: " + Integer.toString(agent.getProbeCounter()));
        prettyPrint("Sucseeded: " + !failed);

    }

    public void gameLoop() {

        agent.firstMove();
        while (!gameOver) {
            agent.makeMove();
            if (verbose) {
                map.printMap();
            }
        }
    }

    public NettleGame(NettleAgent agent, Map map) {
        this.agent = agent;
        this.map = map;
        map.addObserver(this);
        tabs = "\t\t\t";
        gameLoop();

    }

    // public static void main(String[] args) {
    //
    // assert args.length >= 1;
    // final File root = new File(args[0]);
    // for (final File difficultyDir : root.listFiles()) {
    // System.out.println("Difficulty: " + difficultyDir.getName());
    // for (final File mapDir : difficultyDir.listFiles()) {
    // File map = new File(mapDir.toString() + File.separator + "map.txt");
    // System.out.println("\t" + mapDir.getName());
    // new NettleGame(map);
    //
    // }
    // }
    //
    // }
    // public static void main(String[] args) {
    //
    // assert args.length >= 1;
    // final File root = new File(args[0]);
    // File[] difficulties = { new File(args[0] + File.separator + "hard") };
    // File[] maps = { new File(args[0] + File.separator + "hard" +
    // File.separator + "nworld1") };
    // for (final File difficultyDir : difficulties) {
    // System.out.println("Difficulty: " + difficultyDir.getName());
    // tabs = "\t";
    // for (final File mapDir : maps) {
    // File map = new File(mapDir.toString() + File.separator + "map.txt");
    // System.out.println(tabs + mapDir.getName());
    // tabs += "\t";
    // new NettleGame(map);
    //
    // }
    // }
    //
    // }

    // public void flag(int i, int j) {
    //
    // map.getCellAt(i, j).setFlagged(true);
    //
    // }

    @Override
    public void update(Observable o, Object arg) {

        MapCell cell = (MapCell) arg;
        if (cell.getNumberOfAdjacentNettles() == -1) {
            failed = true;
            endGame();
        } else {
            map.decrNumberOfHiddenCells();
            if (map.getNumberOfHiddenCells() == map.getNumberOfNettels()) {
                endGame();
            } else if (cell.getNumberOfAdjacentNettles() == 0) {
                for (MapCell neighbour : map.getNeighbours(cell)) {
                    agent.probe(neighbour);
                }

            }

        }
    }

}
