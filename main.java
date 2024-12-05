/*
Jinjutha Yousirivat 6581053
Trinnaya Damrongpatharawat 6581147
Kyaw Zin Thant 6581178
Rapeepat Tongchai 6380869
Norawat Gajaseni 6480566
 */

package java.knightmoves;

import java.io.*;
import java.util.*;
import java.util.Scanner;

class Board {
    private final int size;
    private final char[] grid;

    public Board(int size) {
        this.size = size;
        this.grid = new char[size * size];
        Arrays.fill(grid, '.');
    }

    public void placeKnight(Position pos) {
        grid[pos.id] = 'K';
    }

    public void placeCastle(Position pos) {
        grid[pos.id] = 'C';
    }

    public void placeBomb(Position pos) {
        grid[pos.id] = 'B';
    }

    public boolean isValidPosition(Position pos) {
        return pos.id >= 0 && pos.id < size * size && grid[pos.id] == '.';
    }

    public void print() {
        for (int i = 0; i < size * size; i++) {
            System.out.print(grid[i] + " ");
            if ((i + 1) % size == 0) System.out.println();
        }
    }

    public int getSize() {
        return size;
    }
}


class Position {
    int id;

    public Position(int id) {
        this.id = id;
    }

    public int getRow(int size) {
        return id / size;
    }

    public int getCol(int size) {
        return id % size;
    }

    @Override
    public String toString() {
        return "ID: " + id;
    }
}


class PathFinder {
    private final Board board;
    private final int[][] directions = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};

    public PathFinder(Board board) {
        this.board = board;
    }



public class Knightmoves {
    
    /*static class cell{
        int x,y,dis;
        
        public cell(int x, int y, int dis)
        {
            this.x = x;
            this.y = y;
            this.dis = dis;
        }
    }
    
    static int minsteptotarget(int knightpos[], int castlepos[], int N){
        int dx[] = { -2, -1, 1, 2, -2, -1, 1, 2 };
        int dy[] = { -1, -2, -2, -1, 1, 2, 2, 1 };
        
        Queue<cell> q= new LinkedList();
        
        q int
    }*/

    public static void main(String[] args) { 
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the knight moves puzzle!");
        
        while (true) {
            System.out.print("Enter board size (N, at least 5): ");
            int N = getValidInteger(scanner, 5, Integer.MAX_VALUE);

            Board board = new Board(N);
            System.out.println("Enter Knight's position (row col): ");
            Position knight = getValidPosition(scanner, N, board);

            System.out.println("Enter Castle's position (row col): ");
            Position castle = getValidPosition(scanner, N, board);

            board.placeKnight(knight);
            board.placeCastle(castle);
            
            System.out.println("Enter Bomb position (row col): ");
            Position bomb = getValidPosition(scanner, N, board);
            board.placeBomb(bomb);
            }
    }
    private static Position getValidPosition(Scanner scanner, int N, Board board) {
    while (true) {
        try {
            System.out.print("Enter position ID (0 to " + (N * N - 1) + "): ");
            int id = scanner.nextInt();
            Position pos = new Position(id);
            if (board.isValidPosition(pos)) return pos;
            System.out.println("Invalid position! Try again.");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! Enter an integer.");
            scanner.next(); // Clear invalid input
        }
    }
}

    
    
   
      
}

