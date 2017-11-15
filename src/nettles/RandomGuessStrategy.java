package nettles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGuessStrategy extends AbstractStrategy implements Strategy {

    Random rng;
    KnowledgeBase kb;
    NettleAgent agent;
    private int randomGuessCounter = 0;
    private boolean shouldProbe = true;

    public RandomGuessStrategy(KnowledgeBase kb) {
        super();
        rng = new Random();
        this.agent = agent;
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
        int index = rng.nextInt(kb.getHiddenCells().size());
        int loopCounter = 0;
        for (MapCell cell : kb.getHiddenCells()) {
            if (loopCounter < index) {
                loopCounter++;
                continue;
            } else {
                answer.add(cell);
                break;
            }
        }
        incrRandomGuessCounter();
        return answer;
    }

    @Override
    public boolean shouldProbe() {

        return shouldProbe;
    }

    public void setShouldProbe(boolean shouldProbe) {

        this.shouldProbe = shouldProbe;
    }

}
