package nettles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class NettleWorld {

    private int[][] map;
    private int mapLength = -1;
    private int mapWidth = -1;
    private int numberOfNettels = -1;
    private String tabs = "";

    public NettleWorld(File mapFile) {
        map = readMap(mapFile);
        checkWorld();
        tabs = "\t\t";
        for (int i = 0; i < mapLength; i++) {
            System.out.print(tabs);
            for (int j = 0; j < mapWidth; j++) {
                System.out.print(String.format("%3d", map[i][j]));
            }
            System.out.println();
        }
        System.out.println();

    }
    //
    // @Override
    // protected List<SearchNode> getChildren(SearchNode node) {
    //
    // List<SearchNode> answer = new LinkedList<SearchNode>();
    // if (node.getI() <= mapLength - 2) {
    // answer.add(map[node.getI() + 1][node.getJ()]);
    // }
    //
    // if (node.getI() > 0) {
    // answer.add(map[node.getI() - 1][node.getJ()]);
    // }
    // if (node.getJ() <= mapWidth - 2) {
    // answer.add(map[node.getI()][node.getJ() + 1]);
    // }
    // if (node.getJ() > 0) {
    // answer.add(map[node.getI()][node.getJ() - 1]);
    // }
    // return answer;
    // }

    public void checkWorld() {

        int count = 0;
        for (int i = 0; i < mapLength; i++) {
            for (int j = 0; j < mapWidth; j++) {
                if (map[i][j] == -1) {
                    count++;
                }
            }
        }

        assert count == numberOfNettels : "INVALID NUMBER OF NETTLES \nnumber found: " + Integer.toString(count)
                + "\nnumber provided: " + Integer.toString(numberOfNettels);

    }

    // The next functions were taken from the A2 practical
    public int[][] readMap(File file) {

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            mapLength = Integer.parseInt(in.readLine().trim());
            mapWidth = Integer.parseInt(in.readLine().trim());
            numberOfNettels = Integer.parseInt(in.readLine().trim());
            final int[][] answer = new int[mapLength][mapWidth];
            String line;
            String[] splitLine;

            for (int i = 0; i < mapLength; i++) {
                line = in.readLine();
                splitLine = line.split(",");
                for (int j = 0; j < mapWidth; j++) {
                    answer[i][j] = Integer.parseInt(splitLine[j].trim());
                }
            }

            return answer;
        } catch (final Exception e) {
            e.printStackTrace();
            System.out.println("FATAL ERROR;");
            System.exit(1);
        }
        return null;
    }

    public static void main(String[] args) {

        assert args.length >= 1;
        final File root = new File(args[0]);
        for (final File difficultyDir : root.listFiles()) {
            System.out.println("Difficulty: " + difficultyDir.getName());
            for (final File mapDir : difficultyDir.listFiles()) {
                File map = new File(mapDir.toString() + File.separator + "map.txt");
                System.out.println("\t" + mapDir.getName());
                new NettleWorld(map);

            }
        }

    }
    // end of functions taken from A2 practical

}
