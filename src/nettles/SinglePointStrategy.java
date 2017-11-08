package nettles;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class SinglePointStrategy implements Strategy {

	Random rng;
	Map map;
	NettleAgent agent;
	private int mapLength;
	private int mapWidth;
	Set<MapCell> revealedCells = null;
	private int randomGuessCounter = 0;

	public SinglePointStrategy(NettleAgent agent, Map map) {
		super();
		this.agent = agent;
		rng = new Random();
		this.map = map;
		this.mapLength = map.getMapLength();
		this.mapWidth = map.getMapWidth();
		revealedCells = new HashSet<MapCell>();
	}	
	
	@Override
	public void deterimeMove() {
		for(MapCell cell : map.getHiddenCells()) {
			if(map.getFlaggedNeighbours(cell).size() == cell.getNumberOfAdjacentNettles()) {
				for (MapCell mapCell : map.getHiddenNeighbours(cell)) {
					agent.probe(mapCell);
				}
				continue;
			} else if(map.getHiddenNeighbours(cell).size() == cell.getNumberOfAdjacentNettles()) {
				for (MapCell mapCell : map.getHiddenNeighbours(cell)) {
					agent.flag(mapCell);
				}
				continue;
			}
		}
		randomMove();
		
	}

	@Override
	public int getRandomGuessCounter() {

		return randomGuessCounter;
	}

	@Override
	public void incrRandomGuessCounter() {

		this.randomGuessCounter += 1;
	}
	
	 public void flag(MapCell cell) {

	        if (NettleGame.verbose) {
	            System.out.println(NettleGame.tabs + "Flagged: " + cell.toString());
	        }
	        map.flag(cell.getI(), cell.getJ());
	    }

		@Override
		public void randomMove() {
	        MapCell cell;
	        do {
	            cell = new MapCell(rng.nextInt(mapLength), rng.nextInt(mapWidth), -2);
	        } while (revealedCells.contains(cell));
	        incrRandomGuessCounter();
	        agent.probe(cell);
		}

		@Override
		public void recordCell(MapCell cell) {
			revealedCells.add(cell);
			
		}

}
