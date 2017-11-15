package nettles;

public class NettleGame {

    private int[][] world;
    public static boolean gameOver = false;
    private boolean failed = false;
    private static boolean verbose;
    public static String tabs = "";

    public static boolean getVerbose() {

        return verbose;
    }

    NettleAgent agent;
    private int numberOfNettles;

    public static void prettyPrint(String str) {

        System.out.println(tabs + str);
    }

    public static void printIfVerbose(String str) {

        if (verbose) {
            prettyPrint(str);
        }
    }

    private void endGame() {

        tabs = "\t\t\t";
        agent.setDone(true);

        prettyPrint("Final number of random guesses: " + Integer.toString(agent.getRandomGuessCounter()));
        prettyPrint("Final number of probes: " + Integer.toString(agent.getProbeCounter()));
        prettyPrint("Final number of flags: " + Integer.toString(agent.getNumberOfFlaggedCells()));
        prettyPrint("Succeeded: " + !failed);

    }

    public void startGame() {

        agent.firstMove();
        agent.makeMove();
        if (!failed) {
            endGame();
        }
    }

    public NettleGame(NettleAgent agent, int[][] world, int numberOfNettles, boolean verbose) {
        NettleGame.verbose = verbose;
        this.world = world;
        this.numberOfNettles = numberOfNettles;
        this.agent = agent;
        tabs = "\t\t\t";

    }

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

    private int getWorldWidth() {

        return world[0].length;
    }

    private int getWorldLength() {

        return world.length;
    }

    public int probe(MapCell cell) {

        int numbOfNettles = world[cell.getI()][cell.getJ()];
        if (numbOfNettles == -1) {
            failed = true;
            endGame();
        }
        return numbOfNettles;

    }

}
