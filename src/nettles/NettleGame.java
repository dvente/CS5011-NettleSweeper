package nettles;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

public class NettleGame implements Observer {

    //
    // private MapCell[][] map;
    // private int mapLength = -1;
    // private int mapWidth = -1;
    private Map map;
    private boolean gameOver = false;
    private boolean failed = false;
    public static final boolean verbose = true;
    public static String tabs = "";

    NettleAgent agent;

    // public int probe(int i, int j) {
    //
    // MapCell probedCell = map.getCellAt(i, j);
    //
    // int numbOfNettles = map.revealCell(probedCell);
    // if (numbOfNettles == -1) {
    // failed = true;
    // endGame();
    // } else {
    // map.decrNumberOfHiddenCells();
    // if (map.getNumberOfHiddenCells() == map.getNumberOfNettels()) {
    // endGame();
    // } else if (numbOfNettles == 0) {
    // for (MapCell neighbour : map.getChildren(probedCell)) {
    // map.revealCell(neighbour);
    // }
    //
    // }
    //
    // }
    // return numbOfNettles;
    // }

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
        System.out.println(tabs + "Final number of probes:" + agent.getProbeCounter());
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
        File[] difficulties = { root.listFiles()[1] };
        File[] maps = { difficulties[0].listFiles()[0] };
        for (final File difficultyDir : difficulties) {
            System.out.println("Difficulty: " + difficultyDir.getName());
            tabs = "\t";
            for (final File mapDir : maps) {
                File map = new File(mapDir.toString() + File.separator + "map.txt");
                System.out.println(tabs + mapDir.getName());
                tabs += "\t";
                new NettleGame(map);

            }
        }

    }

    public void flag(int i, int j) {

        map.getCellAt(i, j).setFlagged(true);

    }

    @Override
    public void update(Observable o, Object arg) {

        System.out.println("Updating with " + arg.toString());
        MapCell cell = (MapCell) arg;
        if (cell.getNumberOfAdjacentNettles() == -1) {
            failed = true;
            endGame();
        } else {
            map.decrNumberOfHiddenCells();
            if (map.getNumberOfHiddenCells() == map.getNumberOfNettels()) {
                endGame();
            } else if (cell.getNumberOfAdjacentNettles() == 0) {
                for (MapCell neighbour : map.getChildren(cell)) {
                    map.revealCell(neighbour);
                }

            }

        }
    }

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
// end of functions taken from A2 practical
