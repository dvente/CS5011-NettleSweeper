package nettles;

import java.io.File;

public class NettleGame {

    //
    // private MapCell[][] map;
    // private int mapLength = -1;
    // private int mapWidth = -1;
    private Map map;
    private String tabs = "";
    private boolean gameOver = false;
    private boolean failed = false;
    public static final boolean verbose = false;

    NettleAgent agent;

    public void probe(int i, int j) {

        MapCell requestedCell = map.getCellAt(i, j);
        assert requestedCell.isHidden() : "PROBING REVEALED CELL" + requestedCell.toString();
        requestedCell.setHidden(false);
        if (requestedCell.getNumberOfAdjacentNettles() == -1) {
            failed = true;
            endGame();
        } else {
            map.decrNumberOfHiddenCells();
            if (map.getNumberOfHiddenCells() == map.getNumberOfNettels()) {
                endGame();
            }
        }

    }

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

        while (!gameOver) {
            agent.makeMove();
            if (verbose) {
                map.printMap();
            }
        }
    }

    public NettleGame(File mapFile) {
        map = new Map(mapFile);
        agent = new NettleAgent(this, this.map);
        gameLoop();

    }

    public static void main(String[] args) {

        assert args.length >= 1;
        final File root = new File(args[0]);
        File[] difficulties = { root.listFiles()[1] };
        File[] maps = { difficulties[0].listFiles()[0] };
        for (final File difficultyDir : difficulties) {
            System.out.println("Difficulty: " + difficultyDir.getName());
            for (final File mapDir : maps) {
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

}
