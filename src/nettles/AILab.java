package nettles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javafx.util.Pair;

public class AILab implements Observer {

    private final File testingDir;

    public static String tabs = "";
    private int[][] world;
    private int numberOfNettles;
    private boolean verbose = false;
    private List<Map<String,Integer>> recordList = new ArrayList<Map<String,Integer>>();

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

    public AILab(File testingDir) {
        this.testingDir = testingDir;
        tabs = "";

        try {
                        runAllExperiments(testingDir,false);
//            runSingleExperiment(new File(testingDir + File.separator + "medium" + File.separator + "nworld5"),
//                    StrategyType.EASY_EQUATION, true);
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
        System.out.println(recordList.toString());
    }

    public void runSingleExperiment(File dataDir, StrategyType type, boolean verbose)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
    	recordList.add(new HashMap<String, Integer>());
        System.out.println(
                tabs + "Running experiment: " + dataDir.getName() + " with strategy: " + type.getC().getName());

        readWorld(new File(dataDir + File.separator + "map.txt"));
        KnowledgeBase kb = new KnowledgeBase(world.length, world[0].length, numberOfNettles);
        Strategy strat = type.getC().getConstructor(KnowledgeBase.class).newInstance(kb);
        NettleAgent agent = new NettleAgent(kb, numberOfNettles);
        NettleGame game = new NettleGame(agent, world, numberOfNettles, verbose);
        agent.setGame(game);
        agent.setStrat(strat);
        game.addObserver(this);
        game.startGame();

    }

    public void runAllExperiments(File dataDir, boolean verbose) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

        // recursively walk the direcotry tree looking for experiments
        if (!isLeafDir(dataDir)) {

            System.out.println(tabs + dataDir.getName());
            for (File dataSubDir : dataDir.listFiles()) {
                // don't try to run an experiment on a file somewhere up the
                // testing tree
                // experiments are located only in leaf directories
                if (dataSubDir.isDirectory()) {
                    tabs += "\t";
                    runAllExperiments(dataSubDir, verbose);
                    tabs = tabs.substring(0, tabs.length() - 1);
                }
            }
            return;
        }

        // we are in an experiment directory so wel'll run them
        for (StrategyType type : StrategyType.values()) {
            runSingleExperiment(dataDir, type, verbose);
        }

    }

    public int[][] readWorld(File file) {

        try (BufferedReader in = new BufferedReader(new FileReader(file));) {
            int mapLength = Integer.parseInt(in.readLine().trim());
            int mapWidth = Integer.parseInt(in.readLine().trim());
            int numberOfNettels = Integer.parseInt(in.readLine().trim());
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
		Pair<String,Integer> pair = (Pair<String,Integer>)arg1; 
		recordList.get(recordList.size() - 1).put(pair.getKey(), pair.getValue());
		
	}

}
