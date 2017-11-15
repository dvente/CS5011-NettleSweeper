package nettles;

import java.util.Observable;
import java.util.Observer;

public class NettleGame implements Observer {

    private Map map;
    public static boolean gameOver = false;
    private boolean failed = false;
    private static boolean verbose;
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
        prettyPrint("Final number of flags: " + Integer.toString(agent.getFlagCounter()));
        prettyPrint("Succeeded: " + !failed);

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

    public NettleGame(NettleAgent agent, Map map, boolean verbose) {
        NettleGame.verbose = verbose;
        this.agent = agent;
        this.map = map;
        map.addObserver(this);
        tabs = "\t\t\t";
        gameLoop();

    }

    @Override
    public void update(Observable o, Object arg) {

        MapCell cell = (MapCell) arg;
        if (cell.getNumberOfAdjacentNettles() == -1 && !cell.isHidden()) {
            failed = true;
            endGame();
        } else {
            if (map.getNumberOfHiddenCells() == map.getNumberOfNettels()) {
                endGame();
            } else if (cell.getNumberOfAdjacentNettles() == 0) {
                for (MapCell neighbour : map.getNeighbours(cell)) {
                    map.revealCell(neighbour);
                }

            }

        }
    }

}
