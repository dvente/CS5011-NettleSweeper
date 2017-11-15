package nettles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class Map extends Observable {

    private int numberOfHiddenCells = -1;
    private MapCell[][] map = null;
    private int mapLength = -1;
    private int mapWidth = -1;
    private int numberOfNettels = -1;

    public List<MapCell> getRevealedCells() {

        List<MapCell> answer = new ArrayList<MapCell>();
        for (int i = 0; i < getMapLength(); i++) {
            for (int j = 0; j < getMapWidth(); j++) {
                if (!getCellAt(i, j).isHidden()) {
                    assert !getCellAt(i, j).isHidden();
                    answer.add(getCellAt(i, j));
                }

            }
        }
        return answer;
    }

    public int getNumberOfHiddenCells() {

        int counter = 0;
        for (int i = 0; i < getMapLength(); i++) {
            for (int j = 0; j < getMapWidth(); j++) {
                if (getCellAt(i, j).isHidden() && !getCellAt(i, j).isFlagged()) {
                    counter++;
                }

            }
        }
        return counter;
    }

    public List<MapCell> getHiddenCells() {

        List<MapCell> answer = new ArrayList<MapCell>();
        for (int i = 0; i < getMapLength(); i++) {
            for (int j = 0; j < getMapWidth(); j++) {
                if (getCellAt(i, j).isHidden()) {
                    answer.add(getCellAt(i, j));
                }

            }
        }
        return answer;
    }

    public Collection<MapCell> getFronteir() {

        Collection<MapCell> answer = new HashSet<MapCell>();
        for (MapCell[] row : map) {
            for (MapCell cell : row) {
                if (!cell.isHidden() && getHiddenNeighbours(cell).size() > 0) {
                    answer.add(cell);
                }
            }

        }
        return answer;
    }

    // The next functions were taken from the A2 practical
    public Map(File file) {
        super();

        try (BufferedReader in = new BufferedReader(new FileReader(file));) {
            mapLength = Integer.parseInt(in.readLine().trim());
            mapWidth = Integer.parseInt(in.readLine().trim());
            setNumberOfNettels(Integer.parseInt(in.readLine().trim()));
            map = new MapCell[mapLength][mapWidth];
            String line;
            String[] splitLine;

            for (int i = 0; i < getMapLength(); i++) {
                line = in.readLine();
                splitLine = line.split(",");
                for (int j = 0; j < getMapWidth(); j++) {
                    map[i][j] = new MapCell(i, j, Integer.parseInt(splitLine[j].trim()));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        checkWorld();
        numberOfHiddenCells = getMapLength() * getMapWidth();

    }

    public void printMap() {

        for (int i = 0; i < getMapLength(); i++) {
            System.out.print(NettleGame.tabs);
            for (int j = 0; j < getMapWidth(); j++) {
                System.out.print(getCellAt(i, j).toMapString());
            }
            System.out.println();
        }
        System.out.println();
    }

    public MapCell getCellAt(int i, int j) {

        return map[i][j];
    }

    public void setCell(MapCell cell) {

        map[cell.getI()][cell.getJ()] = cell;
    }

    public int getMapLength() {

        return mapLength;
    }

    public int getMapWidth() {

        return mapWidth;
    }

    public void checkWorld() {

        int count = 0;
        for (int i = 0; i < getMapLength(); i++) {
            for (int j = 0; j < getMapWidth(); j++) {
                if (getCellAt(i, j).getNumberOfAdjacentNettles() == -1) {
                    count++;
                }
            }
        }
        assert count == numberOfNettels : "INVALID NUMBER OF NETTLES \nnumber found: " + Integer.toString(count)
                + "\nnumber provided: " + Integer.toString(numberOfNettels);

    }

    public int revealCell(MapCell cell) {

        if (cell.isHidden()) {
            cell.setHidden(false);
            setChanged();
            notifyObservers(cell);
        }

        return cell.getNumberOfAdjacentNettles();
    }

    /**
     * @param cell
     * @return a list containing the neighbours of the cell
     */
    public List<MapCell> getNeighbours(MapCell cell) {

        List<MapCell> answer = new LinkedList<MapCell>();

        for (int k = -1; k < 2; k++) {
            for (int l = -1; l < 2; l++) {
                if (cell.getI() + k >= 0 && cell.getI() + k < getMapLength() && cell.getJ() + l >= 0
                        && cell.getJ() + l < getMapWidth()) {
                    answer.add(getCellAt(cell.getI() + k, cell.getJ() + l));
                }
            }
        }
        return answer;
    }

    public List<MapCell> getHiddenNeighbours(MapCell cell) {

        List<MapCell> answer = new LinkedList<MapCell>();

        for (int k = -1; k < 2; k++) {
            for (int l = -1; l < 2; l++) {
                if (cell.getI() + k >= 0 && cell.getI() + k < getMapLength() && cell.getJ() + l >= 0
                        && cell.getJ() + l < getMapWidth() && getCellAt(cell.getI() + k, cell.getJ() + l).isHidden()
                        && !getCellAt(cell.getI() + k, cell.getJ() + l).isFlagged()) {
                    answer.add(getCellAt(cell.getI() + k, cell.getJ() + l));
                }
            }
        }

        return answer;
    }

    public List<MapCell> getFlaggedNeighbours(MapCell cell) {

        List<MapCell> answer = getNeighbours(cell);
        List<MapCell> toRemove = new LinkedList<MapCell>();
        for (MapCell mapCell : answer) {
            if (!mapCell.isFlagged()) {
                toRemove.add(mapCell);
            }
        }
        answer.removeAll(toRemove);

        return answer;
    }

    public int probe(MapCell cell) {

        return probe(cell.getI(), cell.getJ());
    }

    public int probe(int i, int j) {

        assert getCellAt(i, j).isHidden() : "Trying to probe revealed cell " + map[i][j].toString();
        getCellAt(i, j).setHidden(false);
        return getCellAt(i, j).getNumberOfAdjacentNettles();

    }

    public int getNumberOfNettels() {

        return numberOfNettels;
    }

    public void setNumberOfNettels(int numberOfNettels) {

        this.numberOfNettels = numberOfNettels;
    }

    public void flag(MapCell cell) {

        flag(cell.getI(), cell.getJ());

    }

    public void flag(int i, int j) {

        MapCell flagedCell = getCellAt(i, j);
        if (flagedCell.isHidden() && !flagedCell.isFlagged()) {
            NettleGame.printIfVerbose("Flagging: " + flagedCell.toString());
            flagedCell.setFlagged(true);
            setChanged();
            notifyObservers(flagedCell);
        }

    }

}
