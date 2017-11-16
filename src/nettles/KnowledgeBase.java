package nettles;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Set;

public class KnowledgeBase{

    private MapCell[][] map = null;
    private int mapLength = -1;
    private int mapWidth = -1;
    private Set<MapCell> hiddenCells;
    private Set<MapCell> revealedCells;
    private Set<MapCell> flaggedCells;
    private final int numberOfNettels;
    
    public void reset() {
    	hiddenCells.addAll(revealedCells);
    	hiddenCells.addAll(flaggedCells);
    	revealedCells.clear();
    	flaggedCells.clear();
    }

    public KnowledgeBase(int mapLength, int mapWidth, int NumberOfNettles) {
        this.mapLength = mapLength;
        this.mapWidth = mapWidth;
        this.numberOfNettels = NumberOfNettles;
        map = new MapCell[mapLength][mapWidth];
        hiddenCells = new HashSet<MapCell>();
        revealedCells = new HashSet<MapCell>();
        flaggedCells = new HashSet<MapCell>();

        for (int i = 0; i < getMapLength(); i++) {
            for (int j = 0; j < getMapWidth(); j++) {
                map[i][j] = new MapCell(i, j, -8);
                hiddenCells.add(map[i][j]);
            }
        }

    }

    public Set<MapCell> getFlaggedCells() {

        return flaggedCells;
    }

    public Set<MapCell> getRevealedCells() {

        return revealedCells;
    }

    public int getNumberOfHiddenCells() {

        return hiddenCells.size();
    }

    public Set<MapCell> getHiddenCells() {

        return hiddenCells;
    }

    public Collection<MapCell> getFronteir() {

        Collection<MapCell> answer = new HashSet<MapCell>();
        for (MapCell cell : getRevealedCells()) {
            if (getHiddenNeighbours(cell).size() > 0 && !flaggedCells.contains(cell)) {
                answer.add(cell);
            }
        }

        return answer;
    }

    public String toMapString(MapCell cell) {

        if (hiddenCells.contains(cell) || flaggedCells.contains(cell)) {
            if (flaggedCells.contains(cell)) {
                return "  F";
            } else {
                return "  ?";
            }
        } else {
            return String.format("%3d", cell.getNumberOfAdjacentNettles());
        }
    }

    public void printMap() {

        for (int i = 0; i < getMapLength(); i++) {
            System.out.print(NettleGame.tabs);
            for (int j = 0; j < getMapWidth(); j++) {
                System.out.print(toMapString(getCellAt(i, j)));
            }
            System.out.println();
        }
        System.out.println();
    }

    public MapCell getCellAt(int i, int j) {

        return map[i][j];
    }

    public int getMapLength() {

        return mapLength;
    }

    public int getMapWidth() {

        return mapWidth;
    }

    /**
     * @param cell
     * @return a list containing the neighbours of the cell
     */
    public List<MapCell> getNeighbours(MapCell cell) {

        List<MapCell> answer = new LinkedList<MapCell>();
        int i = cell.getI();
        int j = cell.getJ();
        for (int k = -1; k < 2; k++) {
            for (int l = -1; l < 2; l++) {
                if (i + k >= 0 && i + k < getMapLength() && j + l >= 0 && j + l < getMapWidth()
                        && !(k == 0 && l == 0)) {
                    answer.add(getCellAt(i + k, j + l));
                }
            }
        }

        return answer;
    }

    public List<MapCell> getHiddenNeighbours(MapCell cell) {

        List<MapCell> answer = getNeighbours(cell);
        for (Iterator<MapCell> iterator = answer.iterator(); iterator.hasNext();) {
            MapCell neighbour = iterator.next();
            if (!hiddenCells.contains(neighbour)) {
                iterator.remove();
            }
        }

        return answer;
    }

    public List<MapCell> getFlaggedNeighbours(MapCell cell) {

        List<MapCell> answer = getNeighbours(cell);
        for (Iterator<MapCell> iterator = answer.iterator(); iterator.hasNext();) {
            MapCell neighbour = iterator.next();
            if (!flaggedCells.contains(neighbour)) {
                iterator.remove();
            }
        }

        return answer;
    }

    public int getNumberOfNettels() {

        return numberOfNettels;
    }

    public void flag(MapCell cell) {

        hiddenCells.remove(cell);
        flaggedCells.add(cell);
    }

    public void reveal(MapCell cell, int numberOfAdjacentNettles) {

        hiddenCells.remove(cell);
        revealedCells.add(cell);
        cell.setNumberOfAdjacentNettles(numberOfAdjacentNettles);

    }

}
