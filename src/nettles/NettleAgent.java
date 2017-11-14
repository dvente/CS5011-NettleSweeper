package nettles;

public class NettleAgent {

    // TODO agent should build internal map representation instead of what is
    // curently happening

    Map map;
    Strategy strat;
    private int randomGuessCounter = 0;
    private int probeCounter = 0;

    public NettleAgent(Map map) {
        this.map = map;
        // strat = new RandomGuessStrategy(this,map);
        strat = new EasyEquationStrategy(this, this.map, map.getHiddenCells());
    }

    public void firstMove() {

        probe(0, 0);
    }

    public void flag(MapCell cell) {

        if (map.getCellAt(cell.getI(), cell.getJ()).isHidden()) {
            if (NettleGame.verbose) {
                System.out.println(NettleGame.tabs + "Flagging :" + cell.toString());
            }
            map.getCellAt(cell.getI(), cell.getJ()).setFlagged(true);
        }

    }

    public void flag(int i, int j) {

        if (map.getCellAt(i, j).isHidden()) {
            if (NettleGame.verbose) {
                System.out.println(NettleGame.tabs + "Flagging: " + map.getCellAt(i, j).toString());
            }
            map.getCellAt(i, j).setFlagged(true);
        }

    }

    public int probe(int i, int j) {

        if (!map.getCellAt(i, j).isFlagged() && map.getCellAt(i, j).isHidden()) {
            if (NettleGame.verbose) {
                System.out.println(NettleGame.tabs + "Probing: " + map.getCellAt(i, j).toString());
            }
            incrProbeCounter();
            return map.revealCell(map.getCellAt(i, j));
        } else {
            return -1;
        }

    }

    public int probe(MapCell cell) {

        if (!map.getCellAt(cell.getI(), cell.getJ()).isFlagged()
                && map.getCellAt(cell.getI(), cell.getJ()).isHidden()) {
            if (NettleGame.verbose) {
                System.out.println(NettleGame.tabs + "Probing: " + cell.toString());
            }
            incrProbeCounter();
            return map.revealCell(map.getCellAt(cell.getI(), cell.getJ()));
        } else {
            return -1;
        }

    }

    public void makeMove() {

        strat.deterimeMove();
    }

    public int getRandomGuessCounter() {

        return strat.getRandomGuessCounter();
    }

    public void incrRandomGuessCounter() {

        strat.incrRandomGuessCounter();
    }

    public int getProbeCounter() {

        return probeCounter;
    }

    public void incrProbeCounter() {

        probeCounter += 1;
    }

}
