/******************************************************************************
 *      author: Theresa Van
 *      file: Main.java
 *      class: CS 4200-01 Artificial Intelligence
 *
 *      description: This file will contain the UI.
 ******************************************************************************/

import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        int option;

        do {
            System.out.println(" Choose an option (1-3)");
            System.out.println("    1 - Generate a random 8-puzzle problem");
            System.out.println("    2 - Enter a specific 8-puzzle configuration");
            System.out.println("    3 - Generate test cases");
            System.out.println("    0 - Exit");

            System.out.print(" Enter an option: ");
            option = input.nextInt();

            if (option == 1) {
                int[][] initialState = generateRandomProblem();
                printState(initialState);

                AStarSearch search = new AStarSearch();
                System.out.println("   Search with heuristic 1.");
                Node h1Goal = search.graphSearch(initialState, 1);
                List<Node> h1Path = getPath(h1Goal);

                for (Node e : h1Path) {
                    printState(e.getState());
                }

                System.out.println("   Search with heuristic 2.");
                Node h2Goal = search.graphSearch(initialState, 2);
                List<Node> h2Path = getPath(h2Goal);

                for (Node e : h2Path) {
                    printState(e.getState());
                }
            }

            if (option == 2) {
                int[] inputState = new int[9];
                input.nextLine();

                do {
                    System.out.print("  Enter your custom puzzle (separate integers with spaces): ");
                    String boardInput = input.nextLine();
                    StringTokenizer tk = new StringTokenizer(boardInput, " ");

                    for (int i = 0; i < inputState.length; i++) {
                        inputState[i] = Integer.parseInt(tk.nextToken());
                    }

                    if (!validConfiguration(inputState))
                        System.out.println("        Invalid configuration entered.");

                    else {
                        int[][] initialState = convertToGameBoard(inputState);
                        printState(initialState);

                        System.out.println("    Search with heuristic 1.");
                        AStarSearch search = new AStarSearch();
                        Node h1Goal = search.graphSearch(initialState, 1);
                        List<Node> h1Path = getPath(h1Goal);

                        for (Node e : h1Path) {
                            printState(e.getState());
                        }

                        System.out.println("    Search with heuristic 2.");
                        Node h2Goal = search.graphSearch(initialState, 2);
                        List<Node> h2Path = getPath(h2Goal);

                        for (Node e : h2Path) {
                            printState(e.getState());
                        }
                    }
                } while (!validConfiguration(inputState));
            }

            if (option == 3) {
                FileWriter testCases = new FileWriter(new File("testCases.csv"));
                testCases.append("depth,Graph.h1 Avg Time ms,Graph.h1 Avg Nodes,Graph.h2 Avg Time,Graph.h2 Avg Nodes\n");

                double graphH1AvgTime = 0;
                double graphH2AvgTime = 0;
                int graphH1AvgNodes = 0;
                int graphH2AvgNodes = 0;

                for (int i = 2; i < 26; i += 2) {
                    for (int j = 0; j < 100; j++) {
                        int[][] initialState = generateProblemWithDepth(i);
                        AStarSearch search = new AStarSearch();

                        double startTime = System.nanoTime();
                        Node graphH1Goal = search.graphSearch(initialState, 1);
                        double durationTime = (System.nanoTime() - startTime);
                        graphH1AvgTime += durationTime;

                        List<Node> graphH1Path = getPath(graphH1Goal);
                        for (Node e : graphH1Path) {
                            graphH1AvgNodes++;
                        }

                        startTime = System.nanoTime();
                        Node graphH2Goal = search.graphSearch(initialState, 2);
                        durationTime = (System.nanoTime() - startTime);
                        graphH2AvgTime += durationTime;

                        List<Node> graphH2Path = getPath(graphH2Goal);
                        for (Node e : graphH2Path) {
                            graphH2AvgNodes++;
                        }
                    }

                    graphH1AvgTime = graphH1AvgTime / 100;
                    graphH2AvgTime = graphH2AvgTime / 100;
                    graphH1AvgNodes = graphH1AvgNodes / 100;
                    graphH2AvgNodes = graphH2AvgNodes / 100;

                    testCases.append(String.valueOf(i));
                    testCases.append(',');
                    testCases.append(String.valueOf((int)(graphH1AvgTime)));
                    testCases.append(',');
                    testCases.append(String.valueOf(graphH1AvgNodes));
                    testCases.append(',');
                    testCases.append(String.valueOf((int)(graphH2AvgTime)));
                    testCases.append(',');
                    testCases.append(String.valueOf(graphH2AvgNodes));
                    testCases.append('\n');
                }

                testCases.flush();
                testCases.close();
                System.out.println("File testCases.csv is finished.");

                for (int i = 0; i < 3; i++) {
                    int[][] initialState = generateRandomProblem();
                    System.out.println("    Search with heuristic 1.");
                    int h1Count = 1;
                    printState(initialState);

                    AStarSearch search = new AStarSearch();
                    Node h1Goal = search.graphSearch(initialState, 1);
                    List<Node> h1Path = getPath(h1Goal);

                    for (Node e : h1Path) {
                        System.out.println("    Step " + h1Count);
                        printState(e.getState());
                        h1Count++;
                    }

                    System.out.println("    Search with heuristic 2.");
                    int h2Count = 1;
                    printState(initialState);

                    Node h2Goal = search.graphSearch(initialState, 1);
                    List<Node> h2Path = getPath(h2Goal);

                    for (Node e : h2Path) {
                        System.out.println("    Step " + h2Count);
                        printState(e.getState());
                        h2Count++;
                    }
                }
            }
        } while (option != 0);
    }

    private static int[][] generateRandomProblem() {
        Random random = new Random();
        int[] array;

        do {
            array = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
            for (int i = 0; i < array.length; i++) {
                int tile = random.nextInt(9) + 1;
                while (Arrays.asList(Arrays.stream(array).boxed().toArray(Integer[]::new)).contains(tile)) {
                    tile = random.nextInt(9) + 1;
                }

                array[i] = tile;
            }

            for (int j = 0; j < array.length; j++) {
                array[j] -= 1;
            }
        } while (!validConfiguration(array));

        return convertToGameBoard(array);
    }

    private static boolean validConfiguration(int[] problem) {
        int inversions = 0;
        for (int i = 0; i < problem.length; i++) {
            for (int j = i; j < problem.length; j++) {
                if (problem[i] > problem[j] && problem[j] != 0)
                    inversions++;
            }
        }

        return inversions % 2 == 0;
    }

    private static void printState(int[][] state) {
        for (int i = 0; i < state.length; i++) {
            System.out.print("  ");
            for (int j = 0; j < state[i].length; j++) {
                System.out.print(state[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static int[][] convertToGameBoard(int[] array) {
        int[][] problem = new int[3][3];
        int index = 0;

        for (int i = 0; i < problem.length; i++) {
            for (int j = 0; j < problem[i].length; j++) {
                problem[i][j] = array[index];
                index++;
            }
        }

        return problem;
    }

    private static List<Node> getPath(Node node) {
        List<Node> path = new LinkedList<>();

        while (node.getParent() != null) {
            path.add(0, node);
            node = node.getParent();
        }

        return path;
    }

    private static int[][] generateProblemWithDepth(int depth) {
        int[][] problem = new int[3][3];
        int[][] tempProblem = new int[3][3];
        List<String> states = new LinkedList<>();

        // initialize problem state to goal state
        int count = 0;
        int row = 0;
        int col = 0;
        for (int i = 0; i < problem.length; i++) {
            for (int j = 0; j < problem[i].length; j++){
                problem[i][j] = count;
                tempProblem[i][j] = count;
                count++;
            }
        }
        states.add(toString(problem));

        Random random = new Random();
        for (int i = 0; i < depth; i++) {
            /*  0 - Move left
            *   1 - Move up
            *   2 - Move right
            *   3 - Move down
            */
            boolean move = false;

            do {
                int randomMove = random.nextInt(4);
                int tempRow = 0;
                int tempCol = 0;

                if (randomMove == 0) {
                    tempRow = row;
                    tempCol = col - 1;
                } else if (randomMove == 1) {
                    tempRow = row - 1;
                    tempCol = col;
                } else if (randomMove == 2) {
                    tempRow = row;
                    tempCol = col + 1;
                } else if (randomMove == 3) {
                    tempRow = row + 1;
                    tempCol = col;
                }

                if (!((tempRow < 0 || tempRow > 2) || (tempCol < 0 || tempCol > 2))) {
                    int tempVal = tempProblem[row][col];
                    tempProblem[row][col] = tempProblem[tempRow][tempCol];
                    tempProblem[tempRow][tempCol] = tempVal;

                    if (!states.contains(toString(tempProblem))) {
                        tempVal = problem[row][col];
                        problem[row][col] = problem[tempRow][tempCol];
                        problem[tempRow][tempCol] = tempVal;
                        states.add(toString(problem));
                        row = tempRow;
                        col = tempCol;
                        move = true;
                    }
                }
            } while (!move);
        }
        return problem;
    }

    private static String toString(int[][] state) {
        String path = "";

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                path += String.valueOf(state[i][j]);
            }
        }

        return path;
    }
}