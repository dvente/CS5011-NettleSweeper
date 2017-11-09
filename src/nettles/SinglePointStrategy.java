package nettles;

import java.util.List;
import java.util.Random;

public class SinglePointStrategy implements Strategy {

    // TODO get frontier implementation
    Random rng;
    Map map;
    NettleAgent agent;
    private int mapLength;
    private int mapWidth;
    private List<MapCell> hiddenCells = null;
    private int randomGuessCounter = 0;

    public SinglePointStrategy(NettleAgent agent, Map map, List<MapCell> hiddenCells) {
        super();
        this.agent = agent;
        rng = new Random();
        this.map = map;
        this.mapLength = map.getMapLength();
        this.mapWidth = map.getMapWidth();
        this.hiddenCells = hiddenCells;
    }

    @Override
    public void deterimeMove() {

        for (MapCell cell : map.getHiddenCells()) {
            if (allNeighboursAreSafe(cell)) {
                for (MapCell mapCell : map.getHiddenNeighbours(cell)) {
                    agent.probe(mapCell);
                }
                continue;
            } else if (allNeighboursAreNettels(cell)) {
                for (MapCell mapCell : map.getHiddenNeighbours(cell)) {
                    agent.flag(mapCell);
                }
                continue;
            }
        }
        randomMove();

    }

    public boolean allNeighboursAreSafe(MapCell cell) {

        return map.getFlaggedNeighbours(cell).size() == cell.getNumberOfAdjacentNettles();
    }

    public boolean allNeighboursAreNettels(MapCell cell) {

        return map.getHiddenNeighbours(cell).size() == cell.getNumberOfAdjacentNettles()
                - map.getFlaggedNeighbours(cell).size();
    }

    @Override
    public int getRandomGuessCounter() {

        return randomGuessCounter;
    }

    @Override
    public void incrRandomGuessCounter() {

        this.randomGuessCounter += 1;
    }

    public void flag(MapCell cell) {

        if (NettleGame.verbose) {
            System.out.println(NettleGame.tabs + "Flagged: " + cell.toString());
        }
        map.flag(cell.getI(), cell.getJ());
    }

    @Override
    public void randomMove() {

        MapCell cell = hiddenCells.get(rng.nextInt(hiddenCells.size()));
        incrRandomGuessCounter();
        agent.probe(cell);
    }

}
