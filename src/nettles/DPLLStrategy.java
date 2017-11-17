package nettles;

import java.util.List;

import aima.core.logic.propositional.inference.DPLLSatisfiable;

public class DPLLStrategy extends SinglePointStrategy implements Strategy {

    private DPLLSatisfiable dpll = new DPLLSatisfiable();

    public DPLLStrategy(KnowledgeBase kb) {
        super(kb);

    }

    private boolean shouldProbe = true;

    @Override
    public List<MapCell> deterimeMove() {

        List<MapCell> answer = singlePointMove();
        if (answer.isEmpty()) {
            answer = DPLLMove();
        }
        if (answer.isEmpty()) {
            answer = randomMove();
        }
        return answer;

    }

    private List<MapCell> DPLLMove() {

        return null;
    }

}
