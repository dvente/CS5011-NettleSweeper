package nettles;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 170008773
 */
public class NettleAgent {

    private KnowledgeBase kb;
    private Strategy strat = null;
    private NettleGame game = null;
    private int probeCounter = 0;
    private final int numberOfNettles;
    private boolean done = false;

    /**
     * resets internal state of the agent.
     */
    public void reset() {

        kb.reset();
        strat.reset();
        probeCounter = 0;
        done = false;
    }

    /**
     * @param kb
     *            the knowledge base of the agent
     * @param numberOfNettels
     *            number of nettles present in the world
     */
    public NettleAgent(KnowledgeBase kb, int numberOfNettels) {
        this.kb = kb;
        this.numberOfNettles = numberOfNettels;
    }

    /**
     * set the reference to the game. Needen for probing. Not set in the
     * constructor to avoid circular dependency in agent creation
     *
     * @param game
     */
    public void setGame(NettleGame game) {

        this.game = game;
    }

    /**
     * @param strat
     *            strategy to use in solving the problem. needed to avoid
     *            circular dependency in agent creation
     */
    public void setStrat(Strategy strat) {

        this.strat = strat;
    }

    /**
     * as per the spec, the first more is always on 0,0
     */
    public void firstMove() {

        probe(kb.getCellAt(0, 0));
    }

    /**
     * repeadedly makes moves to uncover the world.
     */
    public void uncoverWorld() {

        while (!done) {
            if (kb.getFlaggedCells().size() == numberOfNettles) {
                // we found every nettle, so reveal all the hidden cells
                Set<MapCell> tempSet = new HashSet<MapCell>();
                tempSet.addAll(kb.getHiddenCells());
                for (MapCell cell : tempSet) {
                    probe(cell);
                }
            }
            if (kb.getHiddenCells().size() == numberOfNettles) {
                // all hidden cells must be nettles, so flag all of them
                Set<MapCell> tempSet = new HashSet<MapCell>();
                tempSet.addAll(kb.getHiddenCells());
                for (MapCell cell : tempSet) {
                    flag(cell);
                }
            }

            // if there are no more hidden cells we are done
            if (kb.getNumberOfHiddenCells() == 0) {
                done = true;
                NettleGame.setFailed(false);
                return;
            }

            // determine move according to strategy
            for (MapCell move : strat.deterimeMove()) {
                if (NettleGame.gameOver) {
                    return;
                }
                if (strat.shouldProbe()) {
                    probe(move);
                } else {
                    flag(move);
                }
            }
        }
    }

    /**
     * @return number of cells flagged in the knowledge base
     */
    public int getNumberOfFlaggedCells() {

        return kb.getFlaggedCells().size();
    }

    /**
     * @return number of times the agent made a random guess
     */
    public int getRandomGuessCounter() {

        return strat.getRandomGuessCounter();
    }

    /**
     * increases random guess coutner by 1
     */
    public void incrRandomGuessCounter() {

        strat.incrRandomGuessCounter();
    }

    /**
     * returns number of times the agent probed a cell
     *
     * @return probe counter
     */
    public int getProbeCounter() {

        return probeCounter;
    }

    /**
     * increases probecouner by 1
     */
    public void incrProbeCounter() {

        probeCounter += 1;
    }

    /**
     * returns whether the agent is done or not
     *
     * @return true iff agent is done
     */
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
        if (NettleGame.getVerbose()) {
            kb.printMap();
        }

    }

    /**
     * same as probe but doesn't increase the count. mainly to be used for
     * uncovering neigbours with a cell with value 0
     *
     * @param cell
     *            the cell to be revealed
     */
    public void reveal(MapCell cell) {

        NettleGame.printIfVerbose("Revealing: " + cell.toString());
        int numberOfNettles = game.probe(cell);
        kb.reveal(cell, numberOfNettles);
        if (NettleGame.getVerbose()) {
            kb.printMap();
        }

        //should we reveal any neighbours?
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

        // don't probe revealed cells
        if (kb.getRevealedCells().contains(cell)) {
            return;
        }

        NettleGame.printIfVerbose("Probing: " + cell.toString());

        incrProbeCounter();
        int numb = game.probe(cell);
        //update the knowledge base
        kb.reveal(cell, numb);

        if (NettleGame.getVerbose()) {
            kb.printMap();
        }

        // should we reveal any neighbours?
        if (numb == 0) {
            for (MapCell safeNeighbour : kb.getHiddenNeighbours(cell)) {
                reveal(safeNeighbour);
            }

        }

    }

    /**
     * sets the done variable to the given value.
     *
     * @param b
     *            boolean to set the done value to
     */
    public void setDone(boolean b) {

        done = b;

    }

}
