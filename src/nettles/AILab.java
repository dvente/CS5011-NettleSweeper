package nettles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class AILab {

    private final File testingDir;

    public static String tabs = "";
    private int[][] world;
    private int numberOfNettles;
    private boolean verbose = false;

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void runSingleExperiment(File dataDir, StrategyType type, boolean verbose)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {

        System.out.println(
                tabs + "Running experiment: " + dataDir.getName() + " with strategy: " + type.getC().getName());

        readWorld(new File(dataDir + File.separator + "map.txt"));
        KnowledgeBase kb = new KnowledgeBase(world.length, world[0].length, numberOfNettles);
        Strategy strat = type.getC().getConstructor(KnowledgeBase.class).newInstance(kb);
        NettleAgent agent = new NettleAgent(kb, numberOfNettles);
        NettleGame game = new NettleGame(agent, world, numberOfNettles, verbose);
        agent.setGame(game);
        agent.setStrat(strat);
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

}
