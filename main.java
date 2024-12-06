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

    public boolean isBomb(Position pos) {
        return grid[pos.id] == 'B'; // Check if the position contains a Bomb
    }

    public int getSize() {
        return size; // Get the size of the board
    }

    public void print() {
        for (int i = 0; i < size * size; i++) {
            char symbol = (grid[i] == '.') ? ' ' : grid[i]; 
            System.out.printf("%2d %c  ", i, symbol); 
            if ((i + 1) % size == 0) {
                System.out.println();
            }
        }
    }

    public void clearKnight() {
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] == 'K') grid[i] = '.'; // Clear the Knight's position
        }
    }

    public void printWithIDs() {
        System.out.println("Board with IDs\n");
        for (int i = 0; i < size * size; i++) {
            System.out.printf("%6d", i); // Print ID with 4-character width for alignment
            if ((i + 1) % size == 0) {
                System.out.println(); 
            }
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

                if (isValidMove(newRow, newCol, size, newId) && !visited[newId]) {
                    visited[newId] = true;
                    queue.add(new Position(newId));
                    parentMap.put(newId, current.id);
                }
            }
        }

        return false; // No path found
    }

    private boolean isValidMove(int row, int col, int size, int newId) {
        return row >= 0 && row < size && col >= 0 && col < size && !board.isBomb(new Position(newId));
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
            System.out.println("\nMove " + (step + 1) + ":");
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
            System.out.println();
            Board board = new Board(N);

            board.printWithIDs();
            System.out.println();

            // Place the Knight
            System.out.println("Placing the Knight (K) on the board");
            Position knight = getValidPosition(scanner, N, board, "Knight");
            board.placeKnight(knight);
            board.print(); // Show board after placing Knight
            System.out.println();

            // Place the Castle
            System.out.println("\nPlacing the Castle (C) on the board");
            Position castle = getValidPosition(scanner, N, board, "Castle");
            board.placeCastle(castle);
            board.print(); // Show board after placing Castle
            System.out.println();

            // Place Bombs
            System.out.println("\nPlacing Bombs (B) on the board:");
            placeBombs(scanner, N, board);

            // Show the final board setup
            System.out.println("\nFinal Board Setup:");
            System.out.println();
            board.print();
            System.out.println();

            // Find and display the shortest path
            PathFinder pathFinder = new PathFinder(board);
            if (pathFinder.findShortestPath(knight, castle)) {
                System.out.println("Solution found in " + (pathFinder.getPath().size() - 1) + " moves.");
                pathFinder.showSteps();
                System.out.println();
                System.out.println("Knight has reached the Castle!");
            } else {
                System.out.println("No solution exists for this configuration.");
            }
            System.out.println();
            // Option to try another configuration
            System.out.println("New game? (yes/no): ");
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
                System.out.println("Enter " + pieceName + " ID");
                int id = scanner.nextInt();
                System.out.println();

                Position pos = new Position(id);
                if (board.isValidPosition(pos)) return pos;
                System.out.println("Invalid position! Try again.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Enter integers for position ID.");
                scanner.next(); // Clear invalid input
            }
        }
    }

    private static void placeBombs(Scanner scanner, int N, Board board) {
        System.out.println("Enter the Bomb positions (comma-separated, e.g., 0,11,23):");
        scanner.nextLine(); // Consume newline
        String input = scanner.nextLine();
        String[] ids = input.split(",");

        for (String idStr : ids) {
            try {
                int id = Integer.parseInt(idStr.trim());
                Position bomb = new Position(id);

                if (board.isValidPosition(bomb)) {
                    board.placeBomb(bomb);
                    System.out.println("Placed Bomb at position " + id);
                } else {
                    System.out.println("Invalid Bomb position: " + id + ". Skipping.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + idStr + ". Skipping.");
            }
        }
        System.out.println();
        board.print(); // Show the updated board after placing all Bombs
    }
}
