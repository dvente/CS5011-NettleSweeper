package nettles;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * the representation of the agents knowledge of the world. Partially Taken and
 * addapted from A2
 *
 * @author 170008773
 */
public class KnowledgeBase {

    private MapCell[][] map = null;
    private int mapLength = -1;
    private int mapWidth = -1;
    private final Set<MapCell> hiddenCells;
    private final Set<MapCell> revealedCells;
    private final Set<MapCell> flaggedCells;
    private final int numberOfNettels;

    /**
     * reset the internal state of the knowledge base. Resets all cells to be
     * hidden
     */
    public void reset() {

        hiddenCells.addAll(revealedCells);
        hiddenCells.addAll(flaggedCells);
        revealedCells.clear();
        flaggedCells.clear();
    }

    /**
     * Constructor. Taken and addapted from A2
     *
     * @param mapLength
     *            length of the world
     * @param mapWidth
     *            width of the world
     * @param NumberOfNettles
     *            number of nettles present in the world.
     */
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
                // -8 is a random out of bounds value that should never be displayed
                map[i][j] = new MapCell(i, j, -8);
                hiddenCells.add(map[i][j]);
            }
        }

    }

    /**
     * returns a Set containing all the flagged cells
     *
     * @return Set containing all the flagged cells
     */
    public Set<MapCell> getFlaggedCells() {

        return flaggedCells;
    }

    /**
     * returns a Set containing all the revealed cells
     *
     * @return Set containing all the revealed cells
     */
    public Set<MapCell> getRevealedCells() {

        return revealedCells;
    }

    /**
     * returns the number of hidden cells left in the world
     *
     * @return the number of hidden cells left in the world
     */
    public int getNumberOfHiddenCells() {

        return hiddenCells.size();
    }

    /**
     * returns a Set containing all the hidden cells
     *
     * @return Set containing all the hidden cells
     */
    public Set<MapCell> getHiddenCells() {

        return hiddenCells;
    }

    /**
     * returns a Set containing the fronteir of the agent (i.e. all revealed
     * cells that have at least one hidden neighbour)
     *
     * @return Set containing the agent' fronteir
     */
    public Collection<MapCell> getFronteir() {

        final Collection<MapCell> answer = new HashSet<MapCell>();
        for (final MapCell cell : getRevealedCells()) {
            if (getHiddenNeighbours(cell).size() > 0 && !flaggedCells.contains(cell)) {
                answer.add(cell);
            }
        }

        return answer;
    }

    /**
     * returns a string representing a cell to be used in the displaying of the
     * map
     *
     * @param cell
     *            the cell which to print
     * @return as string containing the appropriate representation of a cell to
     *         be used in the map printing
     */
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

    /**
     * Prints the map in a readable format. Taken and addapted from A2
     */
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

    /**
     * returns the cell in the map at the given coordinates. Taken and addapted
     * from A2
     *
     * @param i
     *            vertical coordinate
     * @param j
     *            horizontal coordinate
     * @return the cell at the given coordinate
     */
    public MapCell getCellAt(int i, int j) {

        return map[i][j];
    }

    /**
     * @return returns the length of the map
     */
    public int getMapLength() {

        return mapLength;
    }

    /**
     * @return returns the width of the map
     */
    public int getMapWidth() {

        return mapWidth;
    }

    /**
     * finds all neighbours to a give cell. Taken and addapted from A2
     *
     * @param cell
     *            the cell who's neighbours to return
     * @return a list containing the neighbours of the provided cell
     */
    public List<MapCell> getNeighbours(MapCell cell) {

        final List<MapCell> answer = new LinkedList<MapCell>();
        final int i = cell.getI();
        final int j = cell.getJ();
        for (int k = -1; k < 2; k++) { // go thorough all adjacent cells
            for (int l = -1; l < 2; l++) {
                if (i + k >= 0 && i + k < getMapLength() && j + l >= 0 && j + l < getMapWidth() //is the neighbour in bounds?
                        && !(k == 0 && l == 0)//don't select the cell itself
                ) {
                    answer.add(getCellAt(i + k, j + l));
                }
            }
        }

        return answer;
    }

    /**
     * finds all hidde nneighbours to a give cell
     *
     * @param cell
     *            the cell who's neighbours to return
     * @return a list containing the hidden neighbours of the provided cell
     */
    public List<MapCell> getHiddenNeighbours(MapCell cell) {

        //get all neighbours and remove the ones that are not hidden
        final List<MapCell> answer = getNeighbours(cell);
        for (final Iterator<MapCell> iterator = answer.iterator(); iterator.hasNext();) {
            final MapCell neighbour = iterator.next();
            if (!hiddenCells.contains(neighbour)) {
                iterator.remove();
            }
        }

        return answer;
    }

    /**
     * finds all flagged neighbours to a give cell
     *
     * @param cell
     *            the cell who's neighbours to return
     * @return a list containing the flagged neighbours of the provided cell
     */
    public List<MapCell> getFlaggedNeighbours(MapCell cell) {

        //get all neighbours and remove the ones that are not flagged
        final List<MapCell> answer = getNeighbours(cell);
        for (final Iterator<MapCell> iterator = answer.iterator(); iterator.hasNext();) {
            final MapCell neighbour = iterator.next();
            if (!flaggedCells.contains(neighbour)) {
                iterator.remove();
            }
        }

        return answer;
    }

    /**
     * @return number of nettles present in the current world
     */
    public int getNumberOfNettels() {

        return numberOfNettels;
    }

    /**
     * flag a given cell as containing a Nettle
     *
     * @param cell
     *            the cell to be flagged
     */
    public void flag(MapCell cell) {

        hiddenCells.remove(cell);
        flaggedCells.add(cell);
    }

    /**
     * reveal a given cell
     *
     * @param cell
     *            the cell to be revealed
     * @param numberOfAdjacentNettles
     *            the number of nettles adjacent to that cell as returned by the
     *            game
     */
    public void reveal(MapCell cell, int numberOfAdjacentNettles) {

        hiddenCells.remove(cell);
        revealedCells.add(cell);
        cell.setNumberOfAdjacentNettles(numberOfAdjacentNettles);

    }

}
