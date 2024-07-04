import java.io.*;
import java.util.*;

// Node class represents a point in the grid
class GridPoint {
    int row, col; // coordinates of the point
    int stepCount; // number of steps taken to reach this point
    GridPoint previousPoint; // previous point in the path

    GridPoint(int row, int col, int stepCount, GridPoint previousPoint) {
        this.row = row;
        this.col = col;
        this.stepCount = stepCount;
        this.previousPoint = previousPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GridPoint gridPoint = (GridPoint) o;
        return row == gridPoint.row && col == gridPoint.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}

public class PathFinder {
    static char[][] maze; // the grid
    static int[] dRow = {0, 0, 1, -1}; // row directions for moving up, down, left, right
    static int[] dCol = {1, -1, 0, 0}; // column directions for moving up, down, left, right
    static int startRow, startCol, endRow, endCol; // start and end coordinates

    public static void main(String[] args) throws IOException {
        String dirPath = "C:\\Users\\User\\Desktop\\Algo friends codes\\radith_algo\\examples";
        File dir = new File(dirPath);
        File[] fileArray = dir.listFiles();

        if (fileArray != null) {
            for (File file : fileArray) {
                if (file.isFile()) {
                    long startTime = System.nanoTime(); // start timing
                    System.out.println("Processing file: " + file.getName());
                    List<String> lineList = getLinesFromFile(file);
                    processLines(lineList);
                    long endTime = System.nanoTime(); // end timing
                    double duration = (endTime - startTime) / 1_000_000_000.0; // calculate duration in seconds
                    System.out.printf("Time taken for %s: %.4f seconds%n", file.getName(), duration);
                }
            }
        } else {
            System.out.println("Directory not found: " + dirPath);
        }
    }
    // Reads lines from a file and returns them as a list
    private static List<String> getLinesFromFile(File file) throws IOException {
        List<String> lineList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            lineList.add(line);
        }
        reader.close();
        return lineList;
    }

    // Processes lines from a file to create the grid and find the start and end points
    private static void processLines(List<String> lineList) {
        maze = new char[lineList.size()][lineList.get(0).length()];
        for (int i = 0; i < lineList.size(); i++) {
            maze[i] = lineList.get(i).toCharArray();
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == 'S') {
                    startRow = i;
                    startCol = j;
                } else if (maze[i][j] == 'F') {
                    endRow = i;
                    endCol = j;
                }
            }
        }

        System.out.println("Start: (" + startRow + ", " + startCol + ")");
        System.out.println("End: (" + endRow + ", " + endCol + ")");

        runBFS();
    }

    // Runs the BFS algorithm to find the shortest path
    static void runBFS() {
        System.out.println("Running BFS algorithm...");
        GridPoint[][] pointGrid = new GridPoint[maze.length][maze[0].length];
        boolean[][] visitedGrid = new boolean[maze.length][maze[0].length];
        Queue<GridPoint> queue = new LinkedList<>();
        GridPoint startPoint = new GridPoint(startRow, startCol, 0, null);
        pointGrid[startRow][startCol] = startPoint;
        queue.offer(startPoint);

        while (!queue.isEmpty()) {
            GridPoint currentPoint = queue.poll();
            if (visitedGrid[currentPoint.row][currentPoint.col]) continue;
            visitedGrid[currentPoint.row][currentPoint.col] = true;

            if (currentPoint.row == endRow && currentPoint.col == endCol) {
                System.out.println("Shortest path length: " + currentPoint.stepCount);
                printPath(currentPoint);
                return;
            }

            for (int i = 0; i < 4; i++) {
                int nextRow = currentPoint.row + dRow[i];
                int nextCol = currentPoint.col + dCol[i];
                if (nextRow < 0 || nextRow >= maze.length || nextCol < 0 || nextCol >= maze[0].length || maze[nextRow][nextCol] == '0')
                    continue;
                if (pointGrid[nextRow][nextCol] == null || !visitedGrid[nextRow][nextCol]) {
                    GridPoint nextPoint = new GridPoint(nextRow, nextCol, currentPoint.stepCount + 1, currentPoint);
                    pointGrid[nextRow][nextCol] = nextPoint;
                    queue.offer(nextPoint);
                }
            }
        }
    }

    // Prints the path from start to end
    // Prints the path from start to end
    static void printPath(GridPoint endPoint) {
        System.out.println("Path:");
        int step = 1;
        GridPoint currentPoint = endPoint;
        List<GridPoint> path = new ArrayList<>();
        while (currentPoint != null) {
            path.add(currentPoint);
            currentPoint = currentPoint.previousPoint;
        }
        Collections.reverse(path);
        System.out.println(step + ". Start at (" + (path.get(0).col + 1) + "," + (path.get(0).row + 1) + ")");
        step++;
        for (int i = 1; i < path.size(); i++) {
            currentPoint = path.get(i);
            GridPoint previousPoint = path.get(i - 1);
            int dRow = previousPoint.row - currentPoint.row;
            int dCol = previousPoint.col - currentPoint.col;
            String direction;
            if (dRow == 0 && dCol == 1) {
                direction = "right";
            } else if (dRow == 0 && dCol == -1) {
                direction = "left";
            } else if (dRow == 1 && dCol == 0) {
                direction = "down";
            } else {
                direction = "up";
            }
            System.out.println(step + ". Move " + direction + " to (" + (currentPoint.col + 1) + "," + (currentPoint.row + 1) + ")");
            step++;
        }
        System.out.println(step + ". Done!");
    }
}