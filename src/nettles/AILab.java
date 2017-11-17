package nettles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import javafx.util.Pair;

/**
 * A class to easily and automaticall run all tests in a specified path for all
 * implementations of specified in the StrategyType enum. Design adapted from an
 * oral interaction wtih Dr Ozgur Akgun (ozgur.akgun@st-andrews.ac.uk)
 *
 * @author 170008773
 */
public class AILab implements Observer {

    // data should be stored in world -> algorithm -> variables
    private Map<String, Map<String, Map<String, Integer>>> data;
    private int currentNumberOfNettels;
    private String currentExperimentDir;
    private String currentType;
    public static String tabs = "";
    private int[][] world;
    private PrintMode printMode = PrintMode.REPORT;

    /**
     * main function. does some basic input checking
     *
     * @param args
     *            command line argumetns
     */
    public static void main(String[] args) {

        File file = new File(args[0]);
        if (file.exists()) {
            AILab lab = new AILab(file);
        } else {
            System.out.println("TESTING DIRECTORY NOT FOUND!");
            System.exit(1);
        }

    }

    /**
     * Checks whether the directory contains any extra directories
     *
     * @param dir
     *            the file to check
     * @return
     */
    public boolean isLeafDir(File dir) {

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param testingDir
     *            directory where the experiments are located
     */
    public AILab(File testingDir) {
        tabs = "";
        data = new TreeMap<String, Map<String, Map<String, Integer>>>();
        boolean verbose = false;
        try {
            if (printMode.equals(PrintMode.VERBOSE)) {
                verbose = true;
            }
            //run all experiment
            runAllExperiments(testingDir, verbose);
            printMode = PrintMode.REPORT;

            // example of how to setup a single experiment

            //			File testDir = new File(testingDir + File.separator + "medium" + File.separator + "nworld5");
            //			data.put(testDir.getPath(), new TreeMap<String, Map<String, Integer>>());
            //			 runSingleExperiment(testDir, StrategyType.EASY_EQUATION, true);
            //			 printMode = PrintMode.VERBOSE;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        report(printMode);
    }

    /**
     * Runs a experiment in the specified way
     *
     * @param dataDir
     *            the directory where the experiments is located
     * @param type
     *            the method to run the experiment with
     * @param verbose
     *            should the experiment print what it is doing?
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public void runSingleExperiment(File dataDir, StrategyType type, boolean verbose)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {

        currentType = type.name();
        currentExperimentDir = dataDir.getPath();
        data.get(currentExperimentDir).put(currentType, new TreeMap<String, Integer>());

        readWorld(new File(dataDir + File.separator + "map.txt"));
        KnowledgeBase kb = new KnowledgeBase(world.length, world[0].length, currentNumberOfNettels);
        Strategy strat = type.getC().getConstructor(KnowledgeBase.class).newInstance(kb);
        NettleAgent agent = new NettleAgent(kb, currentNumberOfNettels);
        NettleGame game = new NettleGame(agent, world, currentNumberOfNettels, verbose);

        agent.setGame(game);
        agent.setStrat(strat);
        game.addObserver(this);
        game.startGame();

    }

    /**
     * Recursively walks through the specified directory and runs all
     * experiments that it finds
     *
     * @param dataDir
     *            the directory where the experiments are located
     * @param verbose
     *            should the experiment print what it is doing?
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public void runAllExperiments(File dataDir, boolean verbose) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

        // ignore directories that don't contain files
        if (dataDir.listFiles().length == 0) {
            return;
        }

        // recursively walk the direcotry tree looking for experiments
        if (!isLeafDir(dataDir)) {

            for (File dataSubDir : dataDir.listFiles()) {
                // don't try to run an experiment on a file somewhere up the testing tree
                // experiments are located only in leaf directories
                if (dataSubDir.isDirectory()) {
                    data.put(dataSubDir.getPath(), new TreeMap<String, Map<String, Integer>>());
                    runAllExperiments(dataSubDir, verbose);
                }
            }
            return;
        }

        // we are in an experiment directory so wel'll run the experiments
        for (StrategyType type : StrategyType.values()) {
            runSingleExperiment(dataDir, type, verbose);
        }

    }

    /**
     * Reads the world from the given file. Taken and adapted from A2
     *
     * @param file
     *            the file containing the world
     * @return an array containing the world in the provided file
     */
    public int[][] readWorld(File file) {

        try (BufferedReader in = new BufferedReader(new FileReader(file));) {
            int mapLength = Integer.parseInt(in.readLine().trim());
            int mapWidth = Integer.parseInt(in.readLine().trim());
            currentNumberOfNettels = Integer.parseInt(in.readLine().trim());
            world = new int[mapLength][mapWidth];
            String line;
            String[] splitLine;

            for (int i = 0; i < mapLength; i++) {
                line = in.readLine();
                splitLine = line.split(",");
                for (int j = 0; j < mapWidth; j++) {
                    world[i][j] = Integer.parseInt(splitLine[j].trim());
                }
            }
            return world;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    @Override
    public void update(Observable arg0, Object arg1) {

        if (arg1 instanceof Pair) {
            Pair<String, Integer> pair = (Pair<String, Integer>) arg1;
            data.get(currentExperimentDir).get(currentType).put(pair.getKey(), pair.getValue());
        } else if (arg1 instanceof String) {
            if (printMode.equals(PrintMode.VERBOSE)) {
                System.out.println(arg1);
            }
        }

    }

    /**
     * prints revieved data in specified format.
     *
     * @param printMode
     */
    public void report(PrintMode printMode) {

        switch (printMode) {
            case REPORT:
                printReport();
                break;
            case TABLE:
                printTable();
                break;
            case VERBOSE:
                break;
            default:
                break;

        }
    }

    /**
     * prints the header of the table to specified width
     *
     * @param minWidth
     *            minimum width of the columns
     */
    private void printHeader(int minWidth) {

        // print the header
        for (String typeName : data.get(currentExperimentDir).keySet()) {
            String typeNameFormated = String.format("%" + Integer.toString(minWidth + 1) + "s", typeName);
            System.out.print(typeNameFormated + ",");

        }
        System.out.println();

    }

    /**
     * prints all received data in table format
     */
    private void printTable() {

        //figure out the minimum column width (determined by length of the names)
        int colWidth = 1;
        for (StrategyType type : StrategyType.values()) {
            colWidth = Math.max(colWidth, type.name().length());
        }

        //first column might have a different width to the data columns so figure that out
        int firstColWidth = 1;
        for (String worldName : data.keySet()) {
            firstColWidth = Math.max(firstColWidth, worldName.length());
        }

        // loop through the nested maps in the correct order
        for (String varName : data.get(currentExperimentDir).get(currentType).keySet()) {
            System.out.println(varName);
            System.out.print(String.join("", Collections.nCopies(firstColWidth + 1, " ")));
            printHeader(colWidth);

            for (String worldName : data.keySet()) {

                //don't print anything for directories that have no data
                if (data.get(worldName).keySet().isEmpty()) {
                    continue;
                }

                //print name of the experiment
                String worldNameFormated = String.format("%-" + Integer.toString(firstColWidth) + "s", worldName);
                System.out.print(worldNameFormated);

                //print all the data
                for (String algoName : data.get(worldName).keySet()) {
                    System.out.print(String.format("%" + Integer.toString(colWidth) + "d",
                            data.get(worldName).get(algoName).get(varName)) + ",");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    /***
     * prints all recieved data in report style
     */
    //data should be stored in world -> algorithm -> variables
    private void printReport() {

        tabs = "\t";
        for (String worldName : data.keySet()) {
            System.out.println(tabs + worldName + ": ");
            tabs += "\t";
            for (String algoName : data.get(worldName).keySet()) {
                System.out.println(tabs + algoName + ": ");
                tabs += "\t";
                for (String varName : data.get(worldName).get(algoName).keySet()) {
                    System.out.println(tabs + varName + ": " + data.get(worldName).get(algoName).get(varName));
                }
                tabs = tabs.substring(0, tabs.length() - 1);
            }
            tabs = tabs.substring(0, tabs.length() - 1);
        }
        tabs = tabs.substring(0, tabs.length() - 1);

    }

}
