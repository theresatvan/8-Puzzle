import java.util.*;

public class Node implements Comparable<Node>{
    private int[][] state;
    private Node parent;
    private int depth;
    private int path_cost;
    private int est_cost;
    private List<Node> child;

    public Node(int[][] state, Node parent, int depth, int path_cost) {
        this.state = state;
        this.parent = parent;
        this.depth = depth;
        this.path_cost = path_cost;
    }

    public void setState(int[][] state) {
        this.state = state;
    }

    public int[][] getState() {
        return this.state;
    }

    public void setParent(Node parent) { this.parent = parent; }

    public Node getParent() {
        return this.parent;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getDepth() {
        return this.depth;
    }

    public void setPath_cost(int path_cost) { this.path_cost = path_cost; }

    public int getPath_cost() { return this.path_cost; }

    public void setEst_cost(int est_cost) {
        this.est_cost = est_cost;
    }

    public int getEst_cost() {
        return this.est_cost;
    }

    public void generateChild() {
        List<Node> childNodes = new ArrayList<>(1);

        //location of 0
        int row = 0;
        int col = 0;

        // find location of 0
        for (int i = 0; i < this.state.length; i++) {
            for (int j = 0; j < this.state[i].length; j++) {
                if (this.state[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }


        for (int i = 0; i < 4; i++){
            int[][] temp = new int[3][3];
            for (int k = 0; k < this.state.length; k++) {
                for (int l = 0; l < this.state[k].length; l++) {
                    temp[k][l] = this.state[k][l];
                }
            }

            // Generate children
            int tempRow = 0;
            int tempCol = 0;

            if (i == 0) {
                tempRow = row - 1;
                tempCol = col;
            }
            else if (i == 1) {
                tempRow = row;
                tempCol = col - 1;
            }
            else if (i == 2) {
                tempRow = row + 1;
                tempCol = col;
            }
            else if (i == 3) {
                tempRow = row;
                tempCol = col + 1;
            }

            if (!((tempRow < 0 || tempRow > 2) || (tempCol < 0 || tempCol > 2))) {
                int tempVal = temp[row][col];
                temp[row][ col] = temp[tempRow][tempCol];
                temp[tempRow][tempCol] = tempVal;

                if (!childNodes.contains(new Node(temp, this, this.depth + 1, this.path_cost + 1))) {
                    childNodes.add(new Node(temp, this, this.depth + 1, this.path_cost + 1));

                }
            }
        }

        this.child = childNodes;
    }

    public List<Node> getChild() { return this.child;}

    public int compareTo(Node node) {
        if (this.est_cost > node.getEst_cost())
            return 1;
        if (this.est_cost < node.getEst_cost())
            return -1;
        return 0;
    }
}
