package nettles;

public class NettleAgent {

    // TODO agent should build internal map representation instead of what is
    // curently happening

    Map map;
    Strategy strat;
    private int probeCounter = 0;
    private int flagCounter = 0;

    public NettleAgent(Map map, Strategy strat) {

        this.map = map;
        this.strat = strat;
    }

    public void firstMove() {

        probe(0, 0);
    }

    public void flag(MapCell cell) {

        incrFlagCounter();
        map.flag(cell);

    }

    public int probe(int i, int j) {

        if (!map.getCellAt(i, j).isFlagged() && map.getCellAt(i, j).isHidden()) {
            NettleGame.printIfVerbose("Probing: " + map.getCellAt(i, j).toString());
            incrProbeCounter();
            return map.revealCell(map.getCellAt(i, j));
        } else {
            return -1;
        }

    }

    public int probe(MapCell cell) {

        if (!map.getCellAt(cell.getI(), cell.getJ()).isFlagged()
                && map.getCellAt(cell.getI(), cell.getJ()).isHidden()) {
            NettleGame.printIfVerbose("Probing: " + cell.toString());
            incrProbeCounter();
            return map.revealCell(map.getCellAt(cell.getI(), cell.getJ()));
        } else {
            return -1;
        }

    }

    public void makeMove() {

        for (MapCell move : strat.deterimeMove()) {
            if (NettleGame.gameOver) {
                return;
            }
            if (strat.shouldProbe()) {
                probe(move);
            } else {
                flag(move);
            }
        }

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

    public int getFlagCounter() {

        return flagCounter;
    }

    public void incrFlagCounter() {

        flagCounter += 1;
    }

}
