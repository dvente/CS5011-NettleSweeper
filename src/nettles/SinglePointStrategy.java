package nettles;

import java.util.ArrayList;
import java.util.List;

public class SinglePointStrategy extends RandomGuessStrategy implements Strategy {

    private boolean shouldProbe = true;

    public SinglePointStrategy(Map map, List<MapCell> hiddenCells) {
        super(map, hiddenCells);
    }

    protected List<MapCell> singlePointMove() {

        List<MapCell> answer = new ArrayList<MapCell>();
        for (MapCell cell : map.getRevealedCells()) {
            if (allNeighboursAreSafe(cell) && map.getHiddenNeighbours(cell).size() != 0) {
                for (MapCell mapCell : map.getHiddenNeighbours(cell)) {
                    answer.add(mapCell);
                    setShouldProbe(true);
                }
            } else if (allNeighboursAreNettels(cell)) {
                for (MapCell mapCell : map.getHiddenNeighbours(cell)) {
                    answer.add(mapCell);
                    setShouldProbe(false);
                }
            }
        }
        return answer;
    }

    @Override
    public List<MapCell> deterimeMove() {

        List<MapCell> answer = singlePointMove();

        if (answer.isEmpty()) {
            answer = randomMove();
        }
        return answer;

    }

    public boolean allNeighboursAreSafe(MapCell cell) {

        // this is not strictly true, but we use it to avoid infinite loops
        if (cell.getNumberOfAdjacentNettles() == 0) {
            return false;
        }

        return map.getFlaggedNeighbours(cell).size() == cell.getNumberOfAdjacentNettles();
    }

    public boolean allNeighboursAreNettels(MapCell cell) {

        if (cell.getNumberOfAdjacentNettles() == 0 || map.getHiddenNeighbours(cell).size() == 0) {
            return false;
        }

        return map.getHiddenNeighbours(cell)
                .size() == (cell.getNumberOfAdjacentNettles() - map.getFlaggedNeighbours(cell).size());
    }

}
