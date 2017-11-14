package nettles;

import java.util.List;
import java.util.Random;

public class SinglePointStrategy extends RandomGuessStrategy implements Strategy {

	public SinglePointStrategy(NettleAgent agent, Map map, List<MapCell> hiddenCells) {
		super(agent, map, hiddenCells);
	}

	protected boolean singlePointMove() {
		for (MapCell cell : map.getRevealedCells()) {
			if (allNeighboursAreSafe(cell) && map.getHiddenNeighbours(cell).size() != 0
					&& map.getHiddenNeighbours(cell).size() != 0) {

				for (MapCell mapCell : map.getHiddenNeighbours(cell)) {
					agent.probe(mapCell);
				}
				return true;
			} else if (allNeighboursAreNettels(cell)) {
				for (MapCell mapCell : map.getHiddenNeighbours(cell)) {
					agent.flag(mapCell);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void deterimeMove() {
		if (!singlePointMove()) {
			randomMove();
		}

	}

	public boolean allNeighboursAreSafe(MapCell cell) {
		if (cell.getNumberOfAdjacentNettles() == 0) {
			return false;
		}
		return map.getFlaggedNeighbours(cell).size() == cell.getNumberOfAdjacentNettles();
	}

	public boolean allNeighboursAreNettels(MapCell cell) {
		if (cell.getNumberOfAdjacentNettles() == 0 || map.getHiddenNeighbours(cell).size() == 0) {
			return false;
		}
		return map.getHiddenNeighbours(cell).size() == cell.getNumberOfAdjacentNettles()
				- map.getFlaggedNeighbours(cell).size();
	}

}
