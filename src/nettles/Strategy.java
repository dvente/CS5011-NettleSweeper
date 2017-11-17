package nettles;

import java.util.List;

/**
 * Interface so that the agent can use them interchangebly
 * 
 * @author 170008773
 */
public interface Strategy {

    /**
     * Deterimne the next cells to be probed or flagged according to the
     * relevant strategy
     * 
     * @return a list of cells to either probe or flag
     */
    public List<MapCell> deterimeMove();

    /**
     * A way to meansure the amount of random guesses we had to make
     * 
     * @return the number of random guesses made by the strategy so far
     */
    public int getRandomGuessCounter();

    /**
     * increases the random guess counter by 1
     */
    public void incrRandomGuessCounter();

    /**
     * Make a random move
     * 
     * @return a list containing one cell to probe
     */
    public List<MapCell> randomMove();

    /**
     * returns whether the list of returned cells is to be probed or flagged
     * 
     * @return true iff the agent should probe
     */
    public boolean shouldProbe();

    /**
     * Reset the internal state of the strategy (i.e. the random guess counter)
     */
    public void reset();

}
