package nettles;

import java.util.List;

public interface Strategy {
	public void recordCell(MapCell cell);
	public void deterimeMove();
    public int getRandomGuessCounter();
    public void incrRandomGuessCounter();
    public void randomMove();
    
}
