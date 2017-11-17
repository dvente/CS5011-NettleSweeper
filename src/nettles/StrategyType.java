package nettles;

/**
 * An enum to easily iterate over implementations to the strategy interface.
 * Design adapted from an oral interaction wtih Dr Ozgur Akgun
 * (ozgur.akgun@st-andrews.ac.uk)
 *
 * @author 170008773
 */
public enum StrategyType {
    RANDOM_GUESS(RandomGuessStrategy.class), SINGLE_POINT(SinglePointStrategy.class), EASY_EQUATION(
            EasyEquationStrategy.class);

    private final Class<? extends Strategy> c;

    StrategyType(Class<? extends Strategy> c) {
        this.c = c;
    }

    public Class<? extends Strategy> getC() {

        return c;
    }
}