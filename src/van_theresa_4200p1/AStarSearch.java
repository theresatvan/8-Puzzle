import java.util.*;

public class AStarSearch {
    public Node graphSearch(int[][] problem, int heuristic) {
        Node node = new Node(problem, null, 0, 0);
        if (heuristic == 1)
            node.setEst_cost(h1(node.getState()));
        else if (heuristic == 2)
            node.setEst_cost(h2(node.getState()));
        PriorityQueue<Node> frontier = new PriorityQueue<>();
        frontier.add(node);
        Set<String> explored = new HashSet<>();
        explored.add(toString(problem));

        while (!frontier.isEmpty()) {
            node = frontier.poll();

            if (goalTest(node.getState()))
                return node;

            explored.add(toString(node.getState()));

            node.generateChild();

            for (Node actions : node.getChild()) {
                if (!explored.contains(toString(actions.getState()))) {
                    if (heuristic == 1)
                        actions.setEst_cost(actions.getPath_cost() + h1(actions.getState()));
                     else if (heuristic == 2)
                        actions.setEst_cost(actions.getPath_cost() + h2(actions.getState()));

                    explored.add(toString(actions.getState()));
                    frontier.add(actions);
                }
            }
        }

        return node;
    }

    public Node treeSearch(int[][] problem, int heuristic) {
        Node node = new Node(problem, null, 0, 0);
        if (heuristic == 1)
            node.setEst_cost(h1(node.getState()));
        else if (heuristic == 2)
            node.setEst_cost(h2(node.getState()));
        PriorityQueue<Node> frontier = new PriorityQueue<>();
        frontier.add(node);

        while (!frontier.isEmpty()) {
            node = frontier.poll();

            if (goalTest(node.getState()))
                return node;

            node.generateChild();

            for (Node actions : node.getChild()) {
                if (heuristic == 1)
                    actions.setEst_cost(actions.getPath_cost() + h1(actions.getState()));
                else if (heuristic == 2)
                    actions.setEst_cost(actions.getPath_cost() + h2(actions.getState()));

                frontier.add(actions);
            }
        }

        return node;
    }

    private boolean goalTest(int[][] state) {
        int count = 0;
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                if (state[i][j] != count)
                    return false;
                count++;
            }
        }

        return true;
    }

    private int h1(int[][] state) {
        int itr = 0;
        int misplaced_tiles = 0;

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                if (state[i][j] != itr) {
                    itr++;
                    misplaced_tiles++;
                }
            }
        }

        return misplaced_tiles;
    }

    private int h2(int[][] state) {
        int itr = 0;
        int count = 0;

        for (int i = 0; i <state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                if (state[i][j] != itr) {
                    int expectedCol = state[i][j] % 3;
                    int expectedRow = (state[i][j] * 3) + expectedCol;

                    count += Math.abs(expectedRow - i);
                    count += Math.abs(expectedCol - j);
                    itr++;
                }
            }
        }

        return count;
    }

    public String toString(int[][] state) {
        String path = "";

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                path += String.valueOf(state[i][j]);
            }
        }

        return path;
    }
}
