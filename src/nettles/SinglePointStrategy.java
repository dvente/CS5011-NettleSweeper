package nettles;

import java.util.ArrayList;
import java.util.List;

/**
 * A Strategy which scanns for cells of which all neighbours are known to be
 * either nettles or safe
 *
 * @author 170008773
 */
public class SinglePointStrategy extends RandomGuessStrategy implements Strategy {

    /**
     * Constructor, simply calls super
     *
     * @param kb
     *            the knowledge base to base the decisions on
     */
    public SinglePointStrategy(KnowledgeBase kb) {
        super(kb);
    }

    /**
     * Determine a list of cells with the Single point strategy which can be
     * either probed for blagged
     *
     * @return A list of cells to either probe or flag
     */
    protected List<MapCell> singlePointMove() {

        NettleGame.printIfVerbose("SPS");
        List<MapCell> answer = new ArrayList<MapCell>();

        //we only have to check the fronteir
        for (MapCell cell : kb.getFronteir()) {
            NettleGame.printIfVerbose("Checking Cell " + cell.toString());
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

        //check if we can make a SPS move first
        List<MapCell> answer = singlePointMove();

        if (answer.isEmpty()) {
            //if not do a random move
            answer = randomMove();
        }
        return answer;

    }

    /**
     * Checks whether all neighbours of a given cell are safe to probe
     *
     * @param cell
     *            the cell to check
     * @return true iff all neighbours of the provided cell are safe
     */
    public boolean allNeighboursAreSafe(MapCell cell) {

        return kb.getFlaggedNeighbours(cell).size() == cell.getNumberOfAdjacentNettles();
    }

    /**
     * Checks whether all neighbours of a given cell should be flagged
     *
     * @param cell
     *            the cell to check
     * @return true iff all neighbours of the provided cell should be flagged
     */
    public boolean allNeighboursAreNettels(MapCell cell) {

        return kb.getHiddenNeighbours(cell)
                .size() == (cell.getNumberOfAdjacentNettles() - kb.getFlaggedNeighbours(cell).size());
    }

}
