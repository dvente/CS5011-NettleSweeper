package nettles;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javafx.util.Pair;

public class EasyEquationStrategy extends SinglePointStrategy implements Strategy {

    public EasyEquationStrategy(NettleAgent agent, Map map, List<MapCell> hiddenCells) {
        super(agent, map, hiddenCells);
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

    protected boolean easyEquationMove() {

        Collection<MapCell> frontier = map.getFronteir();
        for (Pair<MapCell, MapCell> pair : enumirateAdjacentPairs(frontier, frontier)) {
            NettleGame.printIfVerbose("EES");

            MapCell A = pair.getKey();
            MapCell B = pair.getValue();
            NettleGame.printIfVerbose("[A,B]: [" + A.toString() + "," + B.toString() + "]");
            List<MapCell> copy = map.getHiddenNeighbours(A);
            copy.addAll(map.getHiddenNeighbours(B));

            if (!(isASubsetB(map.getHiddenNeighbours(A), map.getHiddenNeighbours(B))
                    || isASubsetB(map.getHiddenNeighbours(B), map.getHiddenNeighbours(A)))) {
                continue;
            }
            List<MapCell> superSet;
            List<MapCell> subSet;
            if (isASubsetB(map.getHiddenNeighbours(A), map.getHiddenNeighbours(B))) {
                superSet = map.getHiddenNeighbours(B);
                subSet = map.getHiddenNeighbours(A);
            } else {
                superSet = map.getHiddenNeighbours(A);
                subSet = map.getHiddenNeighbours(B);
            }

            int diff = Math.abs((A.getNumberOfAdjacentNettles() - map.getFlaggedNeighbours(A).size())
                    - (B.getNumberOfAdjacentNettles() - map.getFlaggedNeighbours(B).size()));
            Collection<MapCell> setDifferenceNeighbours = superSet;
            setDifferenceNeighbours.removeAll(subSet);
            for (Iterator iterator = setDifferenceNeighbours.iterator(); iterator.hasNext();) {
                MapCell C = (MapCell) iterator.next();
                if (diff == 0) {
                    agent.probe(C);
                } else {
                    agent.flag(C);
                }
                return true;
            }
        }

        return false;
    }

    private boolean isASubsetB(Collection<MapCell> A, Collection<MapCell> B) {

        return B.containsAll(A);
    }

    @Override
    public void deterimeMove() {

        if (!singlePointMove()) {
            if (!easyEquationMove()) {
                randomMove();
            }
        }
    }

}
