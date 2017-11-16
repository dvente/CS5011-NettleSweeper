package nettles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javafx.util.Pair;

public class EasyEquationStrategy extends SinglePointStrategy implements Strategy {

    private boolean shouldProbe = true;

    public EasyEquationStrategy(KnowledgeBase kb) {
        super(kb);
    }

    public static Collection<Pair<MapCell, MapCell>> enumirateAdjacentPairs(Collection<MapCell> first,
            Collection<MapCell> second) {

        Collection<Pair<MapCell, MapCell>> answer = new HashSet<Pair<MapCell, MapCell>>();

        for (MapCell firstObject : first) {
            for (MapCell secondObject : second) {
                // we only want combinations so don't add them double
                if (!answer.contains(new Pair<MapCell, MapCell>(secondObject, firstObject))
                        && firstObject.isAdjacent(secondObject)) {
                    answer.add(new Pair<MapCell, MapCell>(firstObject, secondObject));
                }
            }
        }
        return answer;

    }

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

    protected List<MapCell> easyEquationMove() {

        NettleGame.printIfVerbose("EES");
        List<MapCell> answer = new ArrayList<MapCell>();
        Collection<MapCell> frontier = kb.getFronteir();
        for (Pair<MapCell, MapCell> pair : enumirateAdjacentPairs(frontier, frontier)) {
            MapCell A = pair.getKey();
            MapCell B = pair.getValue();

            if (!(isASubsetB(kb.getHiddenNeighbours(A), kb.getHiddenNeighbours(B))
                    || isASubsetB(kb.getHiddenNeighbours(B), kb.getHiddenNeighbours(A)))) {
                continue;
            }
            List<MapCell> superSet;
            List<MapCell> subSet;
            if (isASubsetB(kb.getHiddenNeighbours(A), kb.getHiddenNeighbours(B))) {
                superSet = kb.getHiddenNeighbours(B);
                subSet = kb.getHiddenNeighbours(A);
            } else {
                superSet = kb.getHiddenNeighbours(A);
                subSet = kb.getHiddenNeighbours(B);
            }

            int diff = Math.abs((A.getNumberOfAdjacentNettles() - kb.getFlaggedNeighbours(A).size())
                    - (B.getNumberOfAdjacentNettles() - kb.getFlaggedNeighbours(B).size()));
            Collection<MapCell> setDifferenceNeighbours = superSet;
            setDifferenceNeighbours.removeAll(subSet);
            for (Iterator iterator = setDifferenceNeighbours.iterator(); iterator.hasNext();) {
                MapCell C = (MapCell) iterator.next();
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

    private boolean isASubsetB(Collection<MapCell> A, Collection<MapCell> B) {

        return B.containsAll(A);
    }

    @Override
    public List<MapCell> deterimeMove() {

        List<MapCell> answer = singlePointMove();
        if (answer.isEmpty()) {
            answer = easyEquationMove();
        }
        if (answer.isEmpty()) {
            answer = randomMove();
        }
        return answer;

    }

}
