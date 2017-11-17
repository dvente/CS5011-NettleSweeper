package nettles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javafx.util.Pair;

/**
 * Uses Easy equation (as detailed in the lecture slides), Single point and
 * Random guessing strategies to uncover the Nettlesweeper world.
 *
 * @author 170008773
 */
public class EasyEquationStrategy extends SinglePointStrategy implements Strategy {

    /**
     * Constructor, simply calls super
     *
     * @param kb
     *            The knowledge base of the agent
     */
    public EasyEquationStrategy(KnowledgeBase kb) {
        super(kb);
    }

    /**
     * Creates a collection of pairs of adjacent cells from the two sets
     * provided
     *
     * @param first
     *            the first collection to make pairs of
     * @param second
     *            the second collection to construct pairs from
     * @return A Collection containing all pairs of adjacent cells from the two
     *         provided collections
     */
    public static Collection<Pair<MapCell, MapCell>> enumirateAdjacentPairs(Collection<MapCell> first,
            Collection<MapCell> second) {

        Collection<Pair<MapCell, MapCell>> answer = new HashSet<Pair<MapCell, MapCell>>();

        for (MapCell firstObject : first) {
            for (MapCell secondObject : second) {

                // we only want combinations (instead of permutations) so don't add them double
                if (!answer.contains(new Pair<MapCell, MapCell>(secondObject, firstObject))
                        && firstObject.isAdjacent(secondObject)) {
                    answer.add(new Pair<MapCell, MapCell>(firstObject, secondObject));
                }
            }
        }
        return answer;

    }

    /**
     * Computes the symmetric difference of two sets (i.e. (A - B) + (B - A) as
     * sets)
     *
     * @param first
     *            the first set
     * @param second
     *            the second sets
     * @return a collection containing all elements of the symmetric difference
     */
    public static Collection<MapCell> symmetricDifference(Collection<MapCell> first, Collection<MapCell> second) {

        Collection<MapCell> answer = new HashSet<MapCell>();

        for (MapCell object : first) {
            if (!second.contains(object)) {
                answer.add(object);
            }
        }

        for (MapCell object : second) {
            if (!first.contains(object)) {
                answer.add(object);
            }
        }

        return answer;

    }

    /**
     * uses the easy equation strategy to determine a move
     *
     * @return a list of cells that can either be probed or flagged.
     */
    protected List<MapCell> easyEquationMove() {

        NettleGame.printIfVerbose("EES");
        List<MapCell> answer = new ArrayList<MapCell>();
        Collection<MapCell> frontier = kb.getFronteir();

        for (Pair<MapCell, MapCell> pair : enumirateAdjacentPairs(frontier, frontier)) {
            MapCell A = pair.getKey();
            MapCell B = pair.getValue();
            List<MapCell> neigboursOfA = kb.getHiddenNeighbours(A);
            List<MapCell> neighboursOfB = kb.getHiddenNeighbours(B);
            NettleGame.printIfVerbose("Checking pair: " + A.toString() + " and " + B.toString());
            //this strategy only works if the set of hidden neighbours of one is a strict subset of the other
            if (!(isASubsetB(neigboursOfA, neighboursOfB) || isASubsetB(neighboursOfB, neigboursOfA))) {
                continue;
            }

            // determine which is the sub set and which is the super set.
            List<MapCell> superSet;
            List<MapCell> subSet;
            if (isASubsetB(neigboursOfA, neighboursOfB)) {
                superSet = neighboursOfB;
                subSet = neigboursOfA;
            } else {
                superSet = neigboursOfA;
                subSet = neighboursOfB;
            }

            // determine the number of nettles that must be in the set difference of the neighbours
            int diff = Math.abs((A.getNumberOfAdjacentNettles() - kb.getFlaggedNeighbours(A).size())
                    - (B.getNumberOfAdjacentNettles() - kb.getFlaggedNeighbours(B).size()));

            //compute the set difference between the hidden neighbours
            Collection<MapCell> setDifferenceNeighbours = superSet;
            setDifferenceNeighbours.removeAll(subSet);

            for (Iterator<MapCell> iterator = setDifferenceNeighbours.iterator(); iterator.hasNext();) {
                MapCell C = iterator.next();
                //Are the neighbours safe?
                if (diff == 0) {
                    answer.add(C);
                    setShouldProbe(true);
                    return answer;
                } else {
                    answer.add(C);
                    setShouldProbe(false);
                    return answer;
                }
            }
        }

        return answer;
    }

    /**
     * Checks whether A is a subset of B
     *
     * @param A
     *            the possible subset
     * @param B
     *            the possible super set
     * @return true iff A \subset B
     */
    public static boolean isASubsetB(Collection<MapCell> A, Collection<MapCell> B) {

        return B.containsAll(A);
    }

    @Override
    public List<MapCell> deterimeMove() {

        //can we make a SPS move?
        List<MapCell> answer = singlePointMove();
        if (answer.isEmpty()) {
            //if not try EES
            answer = easyEquationMove();
        }
        if (answer.isEmpty()) {
            //if we can't do anything else make a random move
            answer = randomMove();
        }
        return answer;

    }

}
