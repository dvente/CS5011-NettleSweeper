package nettles;

public class MapCell {

    private final int i;
    private final int j;
    private final int numberOfAdjacentNettles;
    private boolean hidden;

    public MapCell(int i, int j, int adjacentNettles) {
        super();
        this.numberOfAdjacentNettles = adjacentNettles;
        this.i = i;
        this.j = j;
        hidden = true;
    }

    public int getI() {

        return i;
    }

    @Override
    public String toString() {

        return "(" + j + "," + i + ")";// ["+getNumberOfAdjacentNettles()+"]";
    }

    public String toMapString() {

        if (isHidden()) {
            return "  ?";
        } else {
            return String.format("%3d", getNumberOfAdjacentNettles());
        }
    }

    public int getJ() {

        return j;
    }

    public int getCost() {

        return 1;
    }

    // public int manhattanDist(SearchNode other) {
    // return Math.abs(getI() - other.getI()) + Math.abs(getJ() - other.getJ());
    // }

    public boolean isAdjacent(MapCell other) {

        return Math.max(Math.abs(getI() - other.getI()), Math.abs(getJ() - other.getJ())) == 1;
    }

    public int getNumberOfAdjacentNettles() {

        return numberOfAdjacentNettles;
    }

    public boolean isHidden() {

        return hidden;
    }

    public void setHidden(boolean hidden) {

        this.hidden = hidden;
    }

}
