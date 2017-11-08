package nettles;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomGuessStrategy implements Strategy {

    Random rng;
    Map map;
    NettleAgent agent;
    private int mapLength;
    private int mapWidth;
    public Set<MapCell> revealedCells = null;
    private int randomGuessCounter = 0;

    public RandomGuessStrategy(NettleAgent agent, Map map) {
        super();
        rng = new Random();
        this.agent = agent;
        this.map = map;
        this.mapLength = map.getMapLength();
        this.mapWidth = map.getMapWidth();
        revealedCells = new HashSet<MapCell>();
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
        MapCell cell;
        do {
            cell = new MapCell(rng.nextInt(mapLength), rng.nextInt(mapWidth), -2);
        } while (revealedCells.contains(cell));
        incrRandomGuessCounter();
        agent.probe(cell);
	}

	@Override
	public void recordCell(MapCell cell) {
		revealedCells.add(cell);
		
	}

}
