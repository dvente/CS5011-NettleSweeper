package nettles;

import java.util.LinkedList;
import java.util.List;

public class NettleWorldGenerator {

	private final int maxWorldWidth = 10;
	private final int maxNettles = 2*maxWorldWidth;
	
	
    public NettleWorldGenerator() {
        
    }
    
    
    /**
     * @param cell
     * @return a list containing the neighbours of the cell
     */
//    public List<MapCell> getNeighbours(MapCell cell) {
//
//        List<MapCell> answer = new LinkedList<MapCell>();
//        int i = cell.getI();
//        int j = cell.getJ();
//        for (int k = -1; k < 2; k++) {
//            for (int l = -1; l < 2; l++) {
//                if (i + k >= 0 && i + k < getMapLength() && j + l >= 0 && j + l < getMapWidth()
//                        && !(k == 0 && l == 0)) {
//                    answer.add(getCellAt(i + k, j + l));
//                }
//            }
//        }
//
//        return answer;
//    }

    public static void main(String[] args) {

    }

}
