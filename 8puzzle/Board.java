/* *****************************************************************************
 *  Name: Youssef Mohamed Abo El Maaty
 *  Date: 20/9/2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private static final int BLANK = 0;
    private final int boardDimension;
    private final int[][] tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)

    public Board(int[][] tiles) {
        this.boardDimension = tiles.length;
        if (boardDimension < 2 || boardDimension >= 128)
            throw new IllegalArgumentException("Board Size Must be  2 <= n <128");
        this.tiles = new int[boardDimension][boardDimension];

        for (int row = 0; row < boardDimension; row++) {
            for (int col = 0; col < boardDimension; col++) {
                this.tiles[row][col] = tiles[row][col];
                if (this.tiles[row][col] < 0
                        || this.tiles[row][col] > boardDimension * boardDimension - 1)
                    throw new IllegalArgumentException();

            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(boardDimension);
        buf.append("\n");
        for (int row = 0; row < boardDimension; row++) {
            for (int col = 0; col < boardDimension; col++) {
                buf.append(tiles[row][col]);
                if (col == boardDimension - 1 && row != boardDimension - 1)
                    buf.append("\n");
                else
                    buf.append("\t");
            }

        }
        return buf.toString();
    }

    // board dimension n
    public int dimension() {
        return boardDimension;
    }

    // number of tiles out of place
    public int hamming() {
        int hammingDistance = 0;
        int counter = 1;
        for (int row = 0; row < boardDimension; row++) {
            for (int col = 0; col < boardDimension; col++) {
                if (tiles[row][col] != BLANK && tiles[row][col] != counter)
                    hammingDistance++;
                counter++;
            }
        }
        return hammingDistance;
    }

    private int getColInGoal(int tile) {
        if (tile % boardDimension == 0)
            return boardDimension - 1;
        else
            return (tile % boardDimension) - 1;
    }

    private int getRowInGoal(int tile) {
        if (tile % boardDimension == 0)
            return (tile / boardDimension) - 1;
        else
            return tile / boardDimension;
    }

    private int[] getBlankPosition() {
        int[] blankPosition = new int[2];
        for (int row = 0; row < boardDimension; row++) {
            for (int col = 0; col < boardDimension; col++) {
                if (tiles[row][col] == BLANK) {
                    blankPosition[0] = row;
                    blankPosition[1] = col;
                    return blankPosition;
                }

            }
        }
        return blankPosition;
    }

    // sum of Manhattan distances between tiles and goal
    // Manhanttan distance of each tile = Abs(rowNumber in board - rowNumberInGoal )+
    //            Abs (colNumber in board - ColNUmberInGoal)
    public int manhattan() {
        int manhattanDistance = 0;
        int counter = 1;
        for (int row = 0; row < boardDimension; row++) {
            for (int col = 0; col < boardDimension; col++) {
                if (tiles[row][col] != BLANK && !(tiles[row][col] == counter)) {
                    int goalRow = getRowInGoal(tiles[row][col]);
                    int goalCol = getColInGoal(tiles[row][col]);
                    int steps = Math.abs(row - goalRow) + Math.abs(col - goalCol);
                    manhattanDistance += steps;
                }
                counter++;
            }
        }
        return manhattanDistance;
    }

    // is this board the goal board? wrong
    public boolean isGoal() {
        int counter = 1;
        for (int row = 0; row < boardDimension; row++) {
            for (int col = 0; col < boardDimension; col++) {
                if (tiles[row][col] != counter)
                    return false;
                counter++;
                if (counter == boardDimension * boardDimension)
                    counter = BLANK;
            }
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this.tiles) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass())
            return false;
        Board that = (Board) y;
        // check for dimension equality

        if (that.dimension() != this.boardDimension)
            return false;

        // check each element in the 2 boards
        for (int row = 0; row < boardDimension; row++) {
            for (int col = 0; col < boardDimension; col++) {
                if (this.tiles[row][col] != that.tiles[row][col])
                    return false;
            }

        }
        return true;
    }

    // pass array by value
    private void swap(int[][] copy, int row1, int col1, int row2, int col2) {

        int temp = copy[row1][col1];
        copy[row1][col1] = copy[row2][col2];
        copy[row2][col2] = temp;
    }

    private int[][] deepCopy(int[][] arr, int len) {
        int[][] copy = new int[len][len];
        for (int row = 0; row < boardDimension; row++) {
            for (int col = 0; col < boardDimension; col++) {
                copy[row][col] = arr[row][col];
            }
        }
        return copy;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighboursQueue = new Queue<Board>();
        int[] blankPosition = getBlankPosition();
        int blankRow = blankPosition[0];
        int blankCol = blankPosition[1];
        int[][] copy;
        // deep copy
        copy = deepCopy(tiles, boardDimension);

        // down neighbor
        if (blankRow + 1 <= boardDimension - 1) {
            swap(copy, blankRow, blankCol, blankRow + 1, blankCol);
                /* copy[blankRow][blankCol] = copy[blankRow + 1][blankCol];
                copy[blankRow + 1][blankCol] = blank;
                */
            neighboursQueue.enqueue(new Board(copy));
            swap(copy, blankRow, blankCol, blankRow + 1, blankCol);
        }
        // up neighbor
        if (blankRow - 1 >= 0) {
            swap(copy, blankRow, blankCol, blankRow - 1, blankCol);
            neighboursQueue.enqueue(new Board(copy));
            swap(copy, blankRow, blankCol, blankRow - 1, blankCol);
        }

        // right neighbor
        if (blankCol + 1 <= boardDimension - 1) {
            swap(copy, blankRow, blankCol, blankRow, blankCol + 1);
            neighboursQueue.enqueue(new Board(copy));
            swap(copy, blankRow, blankCol, blankRow, blankCol + 1);
        }
        // left neighbor
        if (blankCol - 1 >= 0) {
            swap(copy, blankRow, blankCol, blankRow, blankCol - 1);
            neighboursQueue.enqueue(new Board(copy));
            swap(copy, blankRow, blankCol, blankRow, blankCol - 1);
        }
        return neighboursQueue;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] copy;
        // deep copy
        copy = deepCopy(tiles, boardDimension);
        int[] blankPosition = getBlankPosition();
        int blankRow = blankPosition[0];
        int blankCol = blankPosition[1];
        // Test right and left
        if (blankCol + 1 <= boardDimension - 1 && blankCol - 1 >= 0) {
            swap(copy, blankRow, blankCol + 1, blankRow, blankCol - 1);
        }
        // Test down and up
        else if (blankRow + 1 <= boardDimension - 1 && blankRow - 1 >= 0) {
            swap(copy, blankRow + 1, blankCol, blankRow - 1, blankCol);
        }
        // Test Right and up
        else if (blankCol + 1 <= boardDimension - 1 && blankRow - 1 >= 0) {
            swap(copy, blankRow, blankCol + 1, blankRow - 1, blankCol);
        }
        // Test Right and Down
        else if (blankCol + 1 <= boardDimension - 1 && blankRow + 1 <= boardDimension - 1) {
            swap(copy, blankRow, blankCol + 1, blankRow + 1, blankCol);
        }
        // Test down and up
        else if (blankCol - 1 >= 0 && blankRow - 1 >= 0) {
            swap(copy, blankRow, blankCol - 1, blankRow - 1, blankCol);
        }
        // Test Right and Down
        else if (blankCol - 1 >= 0 && blankRow + 1 <= boardDimension - 1) {
            swap(copy, blankRow, blankCol - 1, blankRow + 1, blankCol);
        }
        return new Board(copy);

    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        StdOut.println(initial.toString());
        // StdOut.println(initial.twin());
        StdOut.println(initial.hamming());
        StdOut.println(initial.manhattan());

        Board[] arr = new Board[3];
        int i = 0;
        for (Board e :
                initial.neighbors()) {
            arr[i] = e;
            StdOut.println("Neighbor" + i + "\n" + e.toString());
            StdOut.println("Hamming = " + e.hamming());
            StdOut.println("Manh = " + e.manhattan());
            i++;
        }

        StdOut.println("Neighbors of arr[0]");
        for (Board t : arr[0].neighbors()) {
            StdOut.println(t);
        }
    }


}
