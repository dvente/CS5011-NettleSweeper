package nettles;

import java.util.List;

public interface Strategy {

    public List<MapCell> deterimeMove();

    public int getRandomGuessCounter();

    public void incrRandomGuessCounter();

    public List<MapCell> randomMove();

    public boolean shouldProbe();
    
    public void reset();

}
