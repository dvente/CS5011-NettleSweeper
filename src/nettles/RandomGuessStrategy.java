package nettles;

import java.util.List;
import java.util.Random;

public class RandomGuessStrategy implements Strategy {

    Random rng;
    Map map;
    NettleAgent agent;
    private int mapLength;
    private int mapWidth;
    private List<MapCell> hiddenCells = null;
    private int randomGuessCounter = 0;

    public RandomGuessStrategy(NettleAgent agent, Map map, List<MapCell> hiddenCells) {
        super();
        rng = new Random();
        this.agent = agent;
        this.map = map;
        this.mapLength = map.getMapLength();
        this.hiddenCells = hiddenCells;
    }

    @Override
    public void deterimeMove() {

        randomMove();

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
    public void randomMove() {

        NettleGame.printIfVerbose("Making random move");
        MapCell cell = hiddenCells.get(rng.nextInt(hiddenCells.size()));
        incrRandomGuessCounter();
        agent.probe(cell);
    }

}
