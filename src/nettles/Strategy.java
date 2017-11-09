package nettles;

public interface Strategy {

    public void deterimeMove();

    public int getRandomGuessCounter();

    public void incrRandomGuessCounter();

    public void randomMove();

}
