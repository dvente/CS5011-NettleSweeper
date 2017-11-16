package nettles;

public enum StrategyType {
    RANDOM_GUESS(RandomGuessStrategy.class), 
    SINGLE_POINT(SinglePointStrategy.class), 
    EASY_EQUATION(EasyEquationStrategy.class);

    private final Class<? extends Strategy> c;

    StrategyType(Class<? extends Strategy> c) {
        this.c = c;
    }

    public Class<? extends Strategy> getC() {

        return c;
    }
}