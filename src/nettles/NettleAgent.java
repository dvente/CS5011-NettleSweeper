package nettles;

public class NettleAgent {

    // NettleGame game;
    Map map;
    Strategy strat;
    private int probeCounter = 0;
    // Set<MapCell> revealedCells = null;
    // Set<MapCell> flaggedCells = null;

    public NettleAgent(Map map) {
        // this.game = game;
        this.map = map;
        strat = new RandomGuessStrategy(map);
        // revealedCells = new HashSet<MapCell>();
        // flaggedCells = new HashSet<MapCell>();
    }

    public void firstMove() {

        probe(0, 0);
    }

    public int probe(int i, int j) {

        incrProbeCounter();
        return map.revealCell(map.getCellAt(i, j));

    }

    public void flag(MapCell cell) {

        if (NettleGame.verbose) {
            System.out.println(NettleGame.tabs + "Flagged: " + cell.toString());
        }
        map.flag(cell.getI(), cell.getJ());
    }

    public void makeMove() {

        MapCell move = strat.deterimeMove();
        if (NettleGame.verbose) {
            System.out.println(NettleGame.tabs + "Probing :" + move.toString());
        }
        probe(move.getI(), move.getJ());
    }

    public int getProbeCounter() {

        return probeCounter;
    }

    public void incrProbeCounter() {

        this.probeCounter += 1;
    }

}
