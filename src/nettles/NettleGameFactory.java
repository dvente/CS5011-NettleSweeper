package nettles;

import java.util.ArrayList;
import java.util.List;

public class NettleGameFactory {

    private List<Class<? extends AbstractStrategy>> strats = new ArrayList<Class<? extends AbstractStrategy>>();

    public void registerStrategy(Class<? extends AbstractStrategy> c) {

        strats.add(c);
    }

    public NettleGameFactory() {

    }

}
