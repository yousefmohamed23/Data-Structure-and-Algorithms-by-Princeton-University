/* *****************************************************************************
 *  Name: Youssef Mohamed Abo El Maaty
 *  Date: 23/9/2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private int totalNumberOfMoves = 0;
    private final Stack<Board> itr = new Stack<Board>();
    private boolean isSolveByTwin = false;

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int moves;
        private final SearchNode previous;
        private final int priority;


        // constructor
        public SearchNode(Board aBoard, int aNumberOfMoves,
                          SearchNode aPreviousBoard) {
            this.board = aBoard;
            this.moves = aNumberOfMoves;
            this.previous = aPreviousBoard;
            this.priority = aBoard.manhattan();
        }

        // set number of moves for the searchNode
        /*
        public void setMoves(int aNumberOfMoves) {
            this.moves = aNumberOfMoves;
        }
         */
        // get Number Of moves

        /*
        private int getMoves() {
            return this.moves;
        }
        */


        // get Manhattan Distance of searchNode
        /*
        private int getManhattan() {
            return this.board.manhattan();
        }
        */

        // get Hamming Distance of searchNode

        /* private int getHamming() {
            return this.board.hamming();
        } */


        private int getPriorityManhatten() {
            return moves + priority;
        }

       /* private int getPriorityHamming() {
            return this.getMoves() + this.getHamming();
        }*/


        public int compareTo(SearchNode aSearchNode) {
            return Integer
                    .compare(this.getPriorityManhatten(), aSearchNode.getPriorityManhatten());

        }
    }


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        // twin board
        Board twinBoard = initial.twin();
        boolean isSolveByOriginal = false;

        Stack<SearchNode> shortestPath = new Stack<SearchNode>();

        // create Original initialSearchNode
        SearchNode initialNode = new SearchNode(initial, 0, null);

        // Create Twin SearchNode
        SearchNode twinNode = new SearchNode(twinBoard, 0, null);

        // Priority Queue of initial and twin board
        MinPQ<SearchNode> searchNodePQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinSearchNodePQ = new MinPQ<SearchNode>();

        // enqueue Initial searchNode
        searchNodePQ.insert(initialNode);
        // StdOut.println(initialNode.board.toString());
        // enqueue Twin searchNode
        twinSearchNodePQ.insert(twinNode);

        SearchNode originalDequeuedSearchNode;
        SearchNode twinDequeuedSearchNode;

        while (!isSolveByOriginal && !isSolveByTwin) {
            // dequeue SeachNode with Min Priority from original PQ
            originalDequeuedSearchNode = searchNodePQ.delMin();
            // push in queue Path
            shortestPath.push(originalDequeuedSearchNode);
            // StdOut.println(
            // "dequeued board : " + "\n" + originalDequeuedSearchNode.board.toString());
            // StdOut.println("Path Equal \n");
        /* for (SearchNode s : shortestPath) {
            StdOut.println(s.board.toString() + "\t");
        }*/
            if (originalDequeuedSearchNode.board.isGoal()) {
                isSolveByOriginal = true;
                totalNumberOfMoves = originalDequeuedSearchNode.moves;
                //  // StdOut.println("Size of path = " + shortestPath.size());
                continue;
            }
            // totalNumberOfMoves = originalDequeuedSearchNode.moves;
            // explore Neighbours of original board
            for (Board neighborBoard : originalDequeuedSearchNode.board.neighbors()) {

                // StdOut.println("Neighbor =" + i + "\n" + neighborBoard.toString());
                if (originalDequeuedSearchNode.previous != null) {
                    if (originalDequeuedSearchNode.previous.board.equals(neighborBoard)) {
                        // StdOut.println("Previous Neighbor");
                        continue;
                    }
                }
                // create searchNode (Board , No of Moves ,priority, previousNode)
                SearchNode tempSearchNode = new SearchNode(neighborBoard,
                                                           originalDequeuedSearchNode.moves + 1,
                                                           originalDequeuedSearchNode);

                /*  StdOut.println(
                "Moves = " + tempSearchNode.moves +
                        "\t priority = " + tempSearchNode.priority);*/
                // enqueue in PQ
                searchNodePQ.insert(tempSearchNode);
            }

            // operation on twin board
            // dequeue board from pq

            twinDequeuedSearchNode = twinSearchNodePQ.delMin();

            if (twinDequeuedSearchNode.board.isGoal()) {
                this.isSolveByTwin = true;
                continue;
            }
            // explore Neighbours of original board
            for (Board neighborBoard : twinDequeuedSearchNode.board.neighbors()) {
                if (twinDequeuedSearchNode.previous != null) {
                    if (twinDequeuedSearchNode.previous.board.equals(neighborBoard)) {
                        continue;
                    }
                }
                SearchNode tempSearchNode = new SearchNode(neighborBoard,
                                                           twinDequeuedSearchNode.moves + 1,
                                                           twinDequeuedSearchNode);
                // enqueue in PQ
                twinSearchNodePQ.insert(tempSearchNode);
            }
        }

        SearchNode temp = shortestPath.peek();
        while (temp.previous != null) {
            SearchNode x = temp.previous;
            itr.push(temp.board);
            temp = x;
        }
        itr.push(temp.board);
    }


    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (this.isSolveByTwin) return false;
        return true;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return totalNumberOfMoves;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        // StdOut.println("Stack Size : " + itr.size());

        return itr;
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

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
