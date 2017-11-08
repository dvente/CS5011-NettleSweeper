package nettles;

import java.util.Random;

public class SingePointStrategy implements Strategy {

    Random rng;
    Map map;
    private int mapLength;
    private int mapWidth;

    public SingePointStrategy(Map map) {
        super();
        this.map = map;
        this.mapLength = map.getMapLength();
        this.mapWidth = map.getMapWidth();
    }

    @Override
    public MapCell deterimeMove() {

        return null;
    }

}
