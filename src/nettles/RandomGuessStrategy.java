package nettles;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomGuessStrategy extends AbstractStrategy implements Strategy {

    Random rng;
    Map map;
    NettleAgent agent;
    private int mapLength;
    private int mapWidth;
    private List<MapCell> hiddenCells = null;
    private int randomGuessCounter = 0;
    private boolean shouldProbe = true;

    public RandomGuessStrategy(Map map, List<MapCell> hiddenCells) {
        super();
        rng = new Random();
        this.agent = agent;
        this.map = map;
        this.mapLength = map.getMapLength();
        this.hiddenCells = hiddenCells;
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

        assert new Random().nextBoolean();
        List<MapCell> answer = new ArrayList<MapCell>();
        NettleGame.printIfVerbose("Making random move");
        // System.out.println("MAKEING RANDOM MOVE");
        MapCell cell = hiddenCells.get(rng.nextInt(hiddenCells.size()));
        incrRandomGuessCounter();
        answer.add(cell);
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
