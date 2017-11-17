package nettles;

/**
 * Simiple enum to deterimine the print mode
 *
 * @author 170008773
 */
public enum PrintMode {
    /**
     * Prints results in human readable form prints the maps, the name of the
     * world, the strategy used and any information the game returns
     */
    REPORT,

    /**
     * prints the same information REPORT prints but in a table format
     */
    TABLE,

    /**
     * prints every move the agent makes and change to its state but no end
     * information
     */
    VERBOSE
}
