package dronefinding;

import java.util.Random;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

public class Drone {
    private Post post;
    private int[] neighbours;
    private int newposx;
    private int newposy;
    private int prevposx;
    private int prevposy;
    private int prevDirection;
    private Grid grid;
    private Timer timer;
    private Stack<Integer> prevDirectionsStack; // Stack to store previous directions
    private boolean[][] visited; // Matrix to keep track of visited cells

    public Drone(Post p, Grid g) {
        this.post = p;
        this.grid = g;
        this.visited = new boolean[g.getRows()][g.getColumns()];
        timer = new Timer();
        timer.scheduleAtFixedRate(new MoveTask(), 0, 100);
        prevDirectionsStack = new Stack<>(); // Initialize the stack
    }

    private class MoveTask extends TimerTask {
        @Override
        public void run() {
            moveDrone();
        }
    }

    public int[] looking() {
        String response = post.look();
        neighbours = post.extractNeighborsFromResponse(response);
        return neighbours;
    }

    public int Direction() {
        Random random = new Random();
        int[] availableDirections = new int[4];
        int count = 0;
        int directionTo3 = -1; // Direction towards '3', if found

        if (neighbours[0] == 3 && prevDirection != 4) {
            return 0; // Move up
        }
        if (neighbours[1] == 3 && prevDirection != 6) {
            return 2; // Move right
        }
        if (neighbours[2] == 3 && prevDirection != 0) {
            return 4; // Move down
        }
        if (neighbours[3] == 3 && prevDirection != 2) {
            return 6; // Move left
        }

        // If '3' is not directly adjacent, check for available empty spaces
        if (neighbours[0] == 0 && prevDirection != 4) {
            availableDirections[count++] = 0; // Up
        }
        if (neighbours[1] == 0 && prevDirection != 6) {
            availableDirections[count++] = 2; // Right
        }
        if (neighbours[2] == 0 && prevDirection != 0) {
            availableDirections[count++] = 4; // Down
        }
        if (neighbours[3] == 0 && prevDirection != 2) {
            availableDirections[count++] = 6; // Left
        }

        if (count > 0) {
            int randomIndex = random.nextInt(count);
            return availableDirections[randomIndex];
        } else {
            return -1; // No available directions
        }
    }

    public void moveDrone() {
        looking();
        int d = Direction();
        if (d != -1) {
            // Check if the next position is already visited
            int nextX = newposx;
            int nextY = newposy;
            switch (d) {
                case 0:
                    nextY--;
                    break; // Up
                case 2:
                    nextX++;
                    break; // Right
                case 4:
                    nextY++;
                    break; // Down
                case 6:
                    nextX--;
                    break; // Left
            }
            if (nextX >= 0 && nextX < grid.getRows() && nextY >= 0 && nextY < grid.getColumns() &&
                    visited[nextX][nextY]) {
                // Next position is visited, skip this move
                return;
            }

            String r = post.move(d);
            prevDirectionsStack.push(prevDirection); // Push the previous direction onto the stack
            prevposx = newposx;
            prevposy = newposy;

            prevDirection = d;
            newposx = post.extractPositionXFromResponse(r);
            newposy = post.extractPositionYFromResponse(r);

            visited[newposx][newposy] = true; // Mark the current cell as visited

            grid.setDroneIcon(newposx, newposy);
            grid.setroadIcon(newposx, newposy);
            grid.setwallIcon(newposx, newposy);
            grid.settrophyIcon(newposx, newposy);

            // Set the position x and position y after every move
            grid.setPosx(newposx);
            grid.setPosy(newposy);

            // Check if the destination cell contains '3', if so, stop moving
            if (neighbours[d / 2] == 3) {
                timer.cancel(); // Stop the timer
                post.load();
            }
        } else {
            // If dead end is encountered, backtrack using the previous directions stored in the stack
            if (!prevDirectionsStack.isEmpty()) {
                int previousDirection = prevDirectionsStack.pop();
                int oppositeDirection = getOppositeDirection(previousDirection);
                String response = post.move(oppositeDirection);
                newposx = post.extractPositionXFromResponse(response);
                newposy = post.extractPositionYFromResponse(response);
                grid.setDroneIcon(newposx, newposy);
                grid.setroadIcon(newposx, newposy);
                grid.setwallIcon(newposx, newposy);
                prevDirection = oppositeDirection;

                // Set the position x and position y after every move
                grid.setPosx(newposx);
                grid.setPosy(newposy);
            }
        }
    }

    private int getOppositeDirection(int direction) {
        switch (direction) {
            case 0:
                return 4;
            case 2:
                return 6;
            case 4:
                return 0;
            case 6:
                return 2;
            default:
                return -1;
        }
    }
}
