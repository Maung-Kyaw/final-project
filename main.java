/*
Jinjutha Yousirivat 6581053
Trinnaya Damrongpatharawat 6581147
Kyaw Zin Thant 6581178
Rapeepat Tongchai 6380869
Norawat Gajaseni 6480566
 */

import java.util.*;

class Board {
    private final int size;
    private final char[] grid;

    public Board(int size) {
        this.size = size;
        this.grid = new char[size * size];
        Arrays.fill(grid, '.'); // Fill the board with empty spaces
    }

    public void placeKnight(Position pos) {
        grid[pos.id] = 'K'; // Place the Knight on the board
    }

    public void placeCastle(Position pos) {
        grid[pos.id] = 'C'; // Place the Castle on the board
    }

    public void placeBomb(Position pos) {
        grid[pos.id] = 'B'; // Place a Bomb on the board
    }

    public boolean isValidPosition(Position pos) {
        return pos.id >= 0 && pos.id < size * size && grid[pos.id] == '.'; // Check if the position is valid
    }

    public int getSize() {
        return size; // Get the size of the board
    }

    public void print() {
        for (int i = 0; i < size * size; i++) {
            System.out.print(grid[i] + " "); // Print each cell
            if ((i + 1) % size == 0) System.out.println(); // Move to the next row
        }
    }

    public void clearKnight() {
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] == 'K') grid[i] = '.'; // Clear the Knight's position
        }
    }
}

class Position {
    int id;

    public Position(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ID: " + id;
    }
}

class PathFinder {
    private final Board board;
    private final int[][] directions = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };
    private final List<Position> path;

    public PathFinder(Board board) {
        this.board = board;
        this.path = new ArrayList<>();
    }

    public boolean findShortestPath(Position knight, Position castle) {
        int size = board.getSize();
        Queue<Position> queue = new LinkedList<>();
        Map<Integer, Integer> parentMap = new HashMap<>();
        boolean[] visited = new boolean[size * size];

        queue.add(knight); // Start BFS with the Knight's position
        visited[knight.id] = true;
        parentMap.put(knight.id, null); // Mark the starting position

        while (!queue.isEmpty()) {
            Position current = queue.poll();

            if (current.id == castle.id) { // If the Castle is reached
                reconstructPath(parentMap, castle.id);
                return true;
            }

            for (int[] move : directions) {
                int newRow = current.id / size + move[0];
                int newCol = current.id % size + move[1];
                int newId = newRow * size + newCol;

                if (isValidMove(newRow, newCol, size) && !visited[newId]) {
                    visited[newId] = true;
                    queue.add(new Position(newId));
                    parentMap.put(newId, current.id);
                }
            }
        }

        return false; // No path found
    }

    private boolean isValidMove(int row, int col, int size) {
        return row >= 0 && row < size && col >= 0 && col < size; // Check if the move is within bounds
    }

    private void reconstructPath(Map<Integer, Integer> parentMap, int endId) {
        Integer current = endId;
        while (current != null) {
            path.add(0, new Position(current)); // Reconstruct the path in reverse order
            current = parentMap.get(current);
        }
    }

    public List<Position> getPath() {
        return path;
    }

    public void showSteps() {
        for (int step = 0; step < path.size(); step++) {
            System.out.println("\nStep " + (step + 1) + ":");
            Position current = path.get(step);
            board.clearKnight(); // Clear previous Knight position
            board.placeKnight(current); // Place the Knight at the new position
            board.print(); // Print the updated board
        }
    }
}

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Knight Moves Puzzle!");

        while (true) {
            System.out.print("Enter board size (N, at least 5): ");
            int N = getValidInteger(scanner, 5, Integer.MAX_VALUE);

            Board board = new Board(N);

            // Place the Knight
            System.out.println("Placing the Knight (K) on the board:");
            Position knight = getValidPosition(scanner, N, board, "Knight");
            board.placeKnight(knight);
            board.print(); // Show board after placing Knight

            // Place the Castle
            System.out.println("\nPlacing the Castle (C) on the board:");
            Position castle = getValidPosition(scanner, N, board, "Castle");
            board.placeCastle(castle);
            board.print(); // Show board after placing Castle

            // Place Bombs
            System.out.println("\nPlacing Bombs (B) on the board:");
            System.out.print("How many Bombs would you like to place? ");
            int bombCount = scanner.nextInt();
            for (int i = 0; i < bombCount; i++) {
                System.out.println("Placing Bomb " + (i + 1) + ":");
                Position bomb = getValidPosition(scanner, N, board, "Bomb");
                board.placeBomb(bomb);
                board.print(); // Show board after placing each Bomb
            }

            // Show the final board setup
            System.out.println("\nFinal Board Setup:");
            board.print();

            // Find and display the shortest path
            PathFinder pathFinder = new PathFinder(board);
            if (pathFinder.findShortestPath(knight, castle)) {
                System.out.println("Solution found in " + (pathFinder.getPath().size() - 1) + " moves.");
                pathFinder.showSteps();
            } else {
                System.out.println("No solution exists for this configuration.");
            }

            // Option to try another configuration
            System.out.println("Try another configuration? (yes/no): ");
            if (!scanner.next().equalsIgnoreCase("yes")) {
                break;
            }
        }
    }

    private static int getValidInteger(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int value = scanner.nextInt();
                if (value >= min && value <= max) return value;
            } catch (InputMismatchException e) {
                scanner.next(); // Clear invalid input
            }
            System.out.println("Invalid input! Enter a value between " + min + " and " + max + ".");
        }
    }

    private static Position getValidPosition(Scanner scanner, int N, Board board, String pieceName) {
        while (true) {
            try {
                System.out.println("Enter the position for the " + pieceName + ":");
                System.out.print("Enter position ID (0 to " + (N * N - 1) + "): ");
                int id = scanner.nextInt();

                Position pos = new Position(id);
                if (board.isValidPosition(pos)) return pos;
                System.out.println("Invalid position! Try again.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Enter integers for position ID.");
                scanner.next(); // Clear invalid input
            }
        }
    }
}
