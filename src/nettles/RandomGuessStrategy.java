package nettles;

import java.util.Random;

public class RandomGuessStrategy implements Strategy {

    Random rng;
    NettleGame game;
    Map map;
    private int mapLength;
    private int mapWidth;

    public RandomGuessStrategy(Map map) {
        super();
        rng = new Random();
        this.map = map;
        this.mapLength = map.getMapLength();
        this.mapWidth = map.getMapWidth();
    }

    @Override
    public MapCell deterimeMove() {

        // return new Pair<Integer, Integer>(rng.nextInt(mapLength),
        // rng.nextInt(mapWidth));
        return new MapCell(rng.nextInt(mapLength), rng.nextInt(mapWidth), -2);

    }

}
