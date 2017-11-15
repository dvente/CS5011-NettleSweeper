package nettles;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class AILab {

    private final File testingDir;

    public static String tabs = "";

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
            // runAllExperiments(testingDir);
            runSingleExperiment(new File(testingDir + File.separator + "medium" + File.separator + "nworld2"),
                    StrategyType.SINGLE_POINT);
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

    public void runSingleExperiment(File dataDir, StrategyType type)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {

        System.out.println(
                tabs + "Running experiment: " + dataDir.getName() + " with strategy: " + type.getC().getName());

        Map map = new Map(new File(dataDir + File.separator + "map.txt"));
        Strategy strat = type.getC().getConstructor(Map.class, List.class).newInstance(map, map.getHiddenCells());
        NettleAgent agent = new NettleAgent(map, strat);
        new NettleGame(agent, map, true);

    }

    public void runAllExperiments(File dataDir) throws InstantiationException, IllegalAccessException,
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
                    runAllExperiments(dataSubDir);
                    tabs = tabs.substring(0, tabs.length() - 1);
                }
            }
            return;
        }

        // we are in an experiment directory so wel'll run them
        for (StrategyType type : StrategyType.values()) {
            runSingleExperiment(dataDir, type);
        }

    }

}
