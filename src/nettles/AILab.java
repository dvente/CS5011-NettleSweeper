package nettles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import javafx.util.Pair;

public class AILab implements Observer {

	private final File testingDir;

	public static String tabs = "";
	private int[][] world;
	private int numberOfNettles;
	private boolean verbose = false;

	// data should be stored in world -> algorithm -> variables
	private Map<String, Map<String, Map<String, Integer>>> data;
	private String currentExperimentDir;
	private String currentType;

	private final PrintMode printMode = PrintMode.TABLE;

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
		data = new TreeMap<String, Map<String, Map<String, Integer>>>();
		boolean verbose = false;
		try {
			if(printMode.equals(PrintMode.VERBOSE)) {
				verbose = true;
			}
			runAllExperiments(testingDir, verbose);
			// runSingleExperiment(new File(testingDir + File.separator + "medium" +
			// File.separator + "nworld5"),
			// StrategyType.EASY_EQUATION, verbose);
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
		// System.out.println(data.toString());
	}

	public void runSingleExperiment(File dataDir, StrategyType type, boolean verbose)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		currentType = type.name();
		data.get(currentExperimentDir).put(currentType, new TreeMap<String, Integer>());

		// System.out.println(
		// tabs + "Running experiment: " + currentExperimentDir + " with strategy: " +
		// currentType);

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

			// System.out.println(tabs + dataDir.getName());
			for (File dataSubDir : dataDir.listFiles()) {
				// don't try to run an experiment on a file somewhere up the
				// testing tree
				// experiments are located only in leaf directories
				if (dataSubDir.isDirectory()) {
					data.put(dataSubDir.getPath(), new TreeMap<String, Map<String, Integer>>());
					currentExperimentDir = dataSubDir.getPath();
					// tabs += "\t";
					runAllExperiments(dataSubDir, verbose);
					// tabs = tabs.substring(0, tabs.length() - 1);
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
		if(arg1 instanceof Pair) {
			Pair<String, Integer> pair = (Pair<String, Integer>) arg1;
			data.get(currentExperimentDir).get(currentType).put(pair.getKey(), pair.getValue());
		}
		else if( arg1 instanceof String) {
			if(printMode.equals(PrintMode.VERBOSE)) {
				System.out.println(arg1);
			}
		}


	}

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

	private void printHeader(int minWidth) {
		// print the header
		for (String typeName : data.get(currentExperimentDir).keySet()) {
			String typeNameFormated = String.format("%"+Integer.toString(minWidth+1)+"s",typeName);
			System.out.print( typeNameFormated + ",");
			
		}
		System.out.println();

	}

	private void printTable() {
		int colWidth = 1;
		for(StrategyType type : StrategyType.values()) {
			colWidth = Math.max(colWidth, type.name().length());
		}
		int firstColWidth = 1;
		for(String worldName : data.keySet()) {
			firstColWidth = Math.max(firstColWidth, worldName.length());
		}
		for (String varName : data.get(currentExperimentDir).get(currentType).keySet()) {
			System.out.println(varName);
			System.out.print("".join("", Collections.nCopies(firstColWidth + 1, " ")));
			printHeader(colWidth);
			for (String worldName : data.keySet()) {
				if (data.get(worldName).keySet().isEmpty()) {
					continue;
				}
				String worldNameFormated = String.format("%-"+Integer.toString(firstColWidth)+"s",worldName);
				System.out.print(worldNameFormated);
				for (String algoName : data.get(worldName).keySet()) {
					System.out.print( String.format("%"+Integer.toString(colWidth)+"d",data.get(worldName).get(algoName).get(varName)) + ",");
				}
				System.out.println();
			}
			System.out.println();
		}
	}

	// data should be stored in world -> algorithm -> variables
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
