package nettles;

public class NettleAgent {

    private KnowledgeBase kb;
    private Strategy strat = null;
    private NettleGame game = null;
    private int probeCounter = 0;
    private final int numberOfNettles;
    private boolean done = false;

    public NettleAgent(KnowledgeBase kb, int numberOfNettels) {
        this.kb = kb;
        this.numberOfNettles = numberOfNettels;
    }

    public void setGame(NettleGame game) {

        this.game = game;
    }

    public void setStrat(Strategy strat) {

        this.strat = strat;
    }

    public void firstMove() {

        probe(kb.getCellAt(0, 0));
    }

    public void makeMove() {

        while (!done) {
            for (MapCell move : strat.deterimeMove()) {
                if (NettleGame.gameOver) {
                    return;
                }
                if (strat.shouldProbe()) {
                    probe(move);
                } else {
                    flag(move);
                }
                if (kb.getNumberOfHiddenCells() == numberOfNettles) {
                    // we found every nettle, so reveal all the hidden cells
                    for (MapCell cell : kb.getHiddenCells()) {
                        probe(cell);
                    }
                    done = true;
                }
            }
        }

    }

    public int getNumberOfFlaggedCells() {

        return kb.getFlaggedCells().size();
    }

    public int getRandomGuessCounter() {

        return strat.getRandomGuessCounter();
    }

    public void incrRandomGuessCounter() {

        strat.incrRandomGuessCounter();
    }

    public int getProbeCounter() {

        return probeCounter;
    }

    public void incrProbeCounter() {

        probeCounter += 1;
    }

    public boolean isDone() {

        return done;
    }

    /**
     * Flag the cell as containing a nettle
     *
     * @param cell
     *            the cell to be flagged
     */
    public void flag(MapCell cell) {

        NettleGame.printIfVerbose("Flagging: " + cell.toString());
        kb.flag(cell);
        //        kb.printMap();

    }

    /**
     * same as probe but doesn't increase the count
     *
     * @param cell
     *            the cell to be revealed
     */
    public void reveal(MapCell cell) {

        NettleGame.printIfVerbose("Revealing: " + cell.toString());
        int numberOfNettles = game.probe(cell);
        kb.reveal(cell, numberOfNettles);
        if (numberOfNettles == 0) {
            for (MapCell safeNeighbour : kb.getHiddenNeighbours(cell)) {
                reveal(safeNeighbour);
            }

        }
    }

    /**
     * Reveals the cell, increases the probe counter and records the number of
     * nettles
     *
     * @param cell
     *            the cell to be probed
     */
    public void probe(MapCell cell) {

        if (kb.getRevealedCells().contains(cell)) {
            return;
        }

        NettleGame.printIfVerbose("Probing: " + cell.toString());
        incrProbeCounter();
        int numb = game.probe(cell);
        kb.reveal(cell, numb);
        //        kb.printMap();
        if (numb == 0) {
            for (MapCell safeNeighbour : kb.getHiddenNeighbours(cell)) {
                probe(safeNeighbour);
            }

        }

    }

    public void setDone(boolean b) {

        done = b;

    }

}
