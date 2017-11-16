package nettles;

import java.util.ArrayList;
import java.util.List;

public class SinglePointStrategy extends RandomGuessStrategy implements Strategy {

    private boolean shouldProbe = true;

    public SinglePointStrategy(KnowledgeBase kb) {
        super(kb);
    }

    protected List<MapCell> singlePointMove() {
    	NettleGame.printIfVerbose("SPS");
        List<MapCell> answer = new ArrayList<MapCell>();
        for (MapCell cell : kb.getFronteir()) {
            if (allNeighboursAreSafe(cell)) {
                for (MapCell mapCell : kb.getHiddenNeighbours(cell)) {
                    answer.add(mapCell);
                    setShouldProbe(true);
                }
                return answer;
            } else if (allNeighboursAreNettels(cell)) {
                for (MapCell mapCell : kb.getHiddenNeighbours(cell)) {
                    answer.add(mapCell);
                    setShouldProbe(false);
                }
                return answer;
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

        return kb.getFlaggedNeighbours(cell).size() == cell.getNumberOfAdjacentNettles();
    }

    public boolean allNeighboursAreNettels(MapCell cell) {

        if (cell.getNumberOfAdjacentNettles() == 0 || kb.getHiddenNeighbours(cell).size() == 0) {
            return false;
        }
        return kb.getHiddenNeighbours(cell)
                .size() == (cell.getNumberOfAdjacentNettles() - kb.getFlaggedNeighbours(cell).size());
    }

}
