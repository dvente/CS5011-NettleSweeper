package nettles;

public class MapCell {

    private final int i;
    private final int j;
    private int numberOfAdjacentNettles;
    //    private boolean hidden;
    //    private boolean flagged;

    public MapCell(int i, int j, int adjacentNettles) {
        super();
        this.numberOfAdjacentNettles = adjacentNettles;
        this.i = i;
        this.j = j;
        //        hidden = true;
        //        setFlagged(false);
    }

    public int getI() {

        return i;
    }

    @Override
    public String toString() {

        return "(" + j + "," + i + ")";
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + i;
        result = prime * result + j;
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MapCell other = (MapCell) obj;
        if (i != other.i) {
            return false;
        }
        if (j != other.j) {
            return false;
        }
        return true;
    }

    public int getJ() {

        return j;
    }

    public boolean isAdjacent(MapCell other) {

        return Math.max(Math.abs(getI() - other.getI()), Math.abs(getJ() - other.getJ())) == 1;
    }

    public int getNumberOfAdjacentNettles() {

        return numberOfAdjacentNettles;
    }

    public void setNumberOfAdjacentNettles(int numberOfAdjacentnettles) {

        this.numberOfAdjacentNettles = numberOfAdjacentnettles;
    }

    //    public boolean isHidden() {
    //
    //        return hidden;
    //    }
    //
    //    public void setHidden(boolean hidden) {
    //
    //        this.hidden = hidden;
    //    }
    //
    //    public boolean isFlagged() {
    //
    //        return flagged;
    //    }
    //
    //    public void setFlagged(boolean flagged) {
    //
    //        this.flagged = flagged;
    //    }

}
