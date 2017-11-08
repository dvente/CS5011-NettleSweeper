package nettles;

public class NettleAgent {

    NettleGame game;
    Strategy strat;
    private int probeCounter = 0;

    public NettleAgent(NettleGame game, Map map) {
        this.game = game;
        strat = new RandomGuessStrategy(map);
    }

    public void makeMove() {

        MapCell move = strat.deterimeMove();
        String tabs = "\t\t";
        if (NettleGame.verbose) {
            System.out.println(tabs + "Probing :" + move.toString());
        }
        incrProbeCounter();
        game.probe(move.getI(), move.getJ());
    }

    public int getProbeCounter() {

        return probeCounter;
    }

    public void incrProbeCounter() {

        this.probeCounter += 1;
    }

}
