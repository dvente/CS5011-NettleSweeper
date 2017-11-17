package nettles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * A strategy that pics a random hidden cell and makes the agent probe it
 *
 * @author 170008773
 */
public class RandomGuessStrategy implements Strategy {

    Random rng;
    KnowledgeBase kb;
    private int randomGuessCounter = 0;
    private boolean shouldProbe = true;

    @Override
    public void reset() {

        randomGuessCounter = 0;
    }

    /**
     * Constructor
     *
     * @param kb
     *            the knowledge base of the agent
     */
    public RandomGuessStrategy(KnowledgeBase kb) {
        super();
        rng = new Random();
        this.kb = kb;
    }

    @Override
    public List<MapCell> deterimeMove() {

        List<MapCell> answer = new ArrayList<MapCell>();
        answer = randomMove();
        return answer;

    }

    @Override
    public int getRandomGuessCounter() {

        return randomGuessCounter;
    }

    @Override
    public void incrRandomGuessCounter() {

        this.randomGuessCounter += 1;
    }

    @Override
    public List<MapCell> randomMove() {

        List<MapCell> answer = new ArrayList<MapCell>();
        NettleGame.printIfVerbose("Making random move");
        answer.add(getRandomElementFromSet(kb.getHiddenCells()));
        incrRandomGuessCounter();
        return answer;
    }

    /**
     * returns a random element from a given set
     * 
     * @param set
     *            the set from which to select a random element
     * @return a random element from the set
     */
    public <T> T getRandomElementFromSet(Set<T> set) {

        int index = rng.nextInt(set.size());
        int loopCounter = 0;
        for (T elmnt : set) {
            if (loopCounter < index) {
                loopCounter++;
                continue;
            } else {
                return elmnt;
            }
        }
        return null;

    }

    @Override
    public boolean shouldProbe() {

        return shouldProbe;
    }

    /**
     * Sets the boolean indicating whether the agent should probe or flag
     * 
     * @param shouldProbe
     *            the boolean to set the variable to
     */
    public void setShouldProbe(boolean shouldProbe) {

        this.shouldProbe = shouldProbe;
    }

}
