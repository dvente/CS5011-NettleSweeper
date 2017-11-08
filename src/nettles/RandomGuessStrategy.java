package nettles;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomGuessStrategy implements Strategy {

    Random rng;
    Map map;
    private int mapLength;
    private int mapWidth;
    Set<MapCell> revealedCells = null;

    public RandomGuessStrategy(Map map) {
        super();
        rng = new Random();
        this.map = map;
        this.mapLength = map.getMapLength();
        this.mapWidth = map.getMapWidth();
        revealedCells = new HashSet<MapCell>();
    }

    @Override
    public MapCell deterimeMove() {

        MapCell cell;
        do {
            cell = new MapCell(rng.nextInt(mapLength), rng.nextInt(mapWidth), -2);
        } while (revealedCells.contains(cell));

        return cell;

    }

}
