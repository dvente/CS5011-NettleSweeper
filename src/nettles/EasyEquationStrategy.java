package nettles;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javafx.util.Pair;

public class EasyEquationStrategy extends SinglePointStrategy implements Strategy {

	// MapCellODO get frontier implementation

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

	protected boolean easyEquationMove() {
		Collection<MapCell> frontier = map.getFronteir();
		for (Pair<MapCell, MapCell> pair : enumirateAdjacentPairs(frontier, frontier)) {
			System.out.println(NettleGame.tabs + "EES");
			MapCell A = pair.getKey();
			MapCell B = pair.getValue();
			if (map.getHiddenNeighbours(A).equals(map.getHiddenNeighbours(B))) {
				continue;
			}
			int diff = Math.abs((A.getNumberOfAdjacentNettles() - map.getFlaggedNeighbours(A).size())
					- (B.getNumberOfAdjacentNettles() - map.getFlaggedNeighbours(B).size()));
			Collection<MapCell> setDifferenceNeighbours = map.getHiddenNeighbours(A);
			setDifferenceNeighbours.removeAll(map.getHiddenNeighbours(B));
			if (setDifferenceNeighbours.size() == 1) {
				MapCell C = setDifferenceNeighbours.iterator().next();
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

	@Override
	public void deterimeMove() {

		if (!singlePointMove()) {
			if (!easyEquationMove()) {
				randomMove();
			}
		}
	}

}
