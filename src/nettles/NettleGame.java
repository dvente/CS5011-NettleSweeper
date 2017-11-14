package nettles;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class NettleGame implements Observer {

    private Map map;
    private boolean gameOver = false;
    private boolean failed = false;
    public static final boolean verbose = false;
    public static String tabs = "";

    NettleAgent agent;

    private void endGame() {

        String tabs = "\t\t";
        gameOver = true;
        if (failed) {
            if (verbose) {
                System.out.println(tabs + "GAME OVER");
            }
        } else {
            if (verbose) {
                System.out.println(tabs + "YOU WON! CONGLATURATIONS!");
            }

        }
        System.out.println(tabs + "Final number of random guesses: " + Integer.toString(agent.getRandomGuessCounter()));
        System.out.println(tabs + "Final number of probes: " + Integer.toString(agent.getProbeCounter()));
        System.out.println(tabs + "Sucseeded: " + !failed);

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

    public NettleGame(File mapFile) {
        map = new Map(mapFile, this);
        agent = new NettleAgent(this.map);
        gameLoop();

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

    public void flag(int i, int j) {

        map.getCellAt(i, j).setFlagged(true);

    }

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

// end of functions taken from A2 practical
