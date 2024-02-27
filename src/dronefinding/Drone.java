package dronefinding;

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
    private Stack<Integer> prevDirectionsStack;
    private boolean returning = false; 

    public Drone(Post p, Grid g) {
    this.post = p;
    this.grid = g;
    timer = new Timer();
    timer.scheduleAtFixedRate(new MoveTask(), 0, 100);
    prevDirectionsStack = new Stack<>();
    prevDirection = -1; 

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
        grid.updateProgressBar(post.extractEnergyFromResponse(response));
        return neighbours;
    }

    public int Direction() {
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

        

        int[] prioritizedDirections = {4, 6, 2, 0};
        for (int i = 0; i < prioritizedDirections.length; i++) {
            for (int j = 0; j < count; j++) {
                if (availableDirections[j] == prioritizedDirections[i]) {
                    return availableDirections[j];
                }

            }
        }
        return -1; // No available directions
    }

    public void moveDrone() {
    int d = -1; // Default direction

    if (!returning) {
        looking();
        d = Direction();
    }

    if (d != -1) {
        String r = post.move(d);
        prevposx = newposx;
        prevposy = newposy;

        prevDirection = d; // Update prevDirection after determining the move

        newposx = post.extractPositionXFromResponse(r);
        newposy = post.extractPositionYFromResponse(r);

        // Store the direction in the path stack only if it leads towards the destination '3'
        

        grid.setDroneIcon(newposx, newposy);
        grid.setroadIcon(newposx, newposy);
        grid.setwallIcon(newposx, newposy);
        grid.settrophyIcon(newposx, newposy);

        // Set the position x and position y after every move
        grid.setPosx(newposx);
        grid.setPosy(newposy);

        if (!returning && prevDirection != -1) {
        prevDirectionsStack.push(prevDirection);
        System.out.println(prevDirectionsStack);
        }


        if (neighbours[d / 2] == 3) {
            grid.setDroneTrophyIcon(newposx, newposy);
            post.load();
            startReturnJourney(); 
            returning = true; 
        }
    } else if (!returning && !prevDirectionsStack.isEmpty()) {
        // If dead end is encountered, backtrack using the previous directions stored in the stack
        int previousDirection = prevDirectionsStack.pop();
        System.out.println(prevDirectionsStack);
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
    } else {
        timer.cancel(); // Stop the timer if there are no more directions to go back
    }
}

    public void startReturnJourney() {
        timer.cancel(); // Cancel the original movement timer
        timer = new Timer();
        timer.scheduleAtFixedRate(new ReturnMoveTask(), 0, 100);
    }

    private class ReturnMoveTask extends TimerTask {
        @Override
        public void run() {
            returnJourney();
        }
    }

    public void returnJourney() {
    if (!prevDirectionsStack.isEmpty()) {
        int previousDirection = prevDirectionsStack.pop();
        int oppositeDirection = getOppositeDirection(previousDirection);
        String response = post.move(oppositeDirection);
        newposx = post.extractPositionXFromResponse(response);
        newposy = post.extractPositionYFromResponse(response);
        grid.setDroneTrophyIcon(newposx, newposy);
        grid.setroadIcon(newposx, newposy);
        grid.setwallIcon(newposx, newposy);
        prevDirection = oppositeDirection;

        // Set the position x and position y after every move
        grid.setPosx(newposx);
        grid.setPosy(newposy);
    } else {
        timer.cancel(); // Stop the timer if there are no more directions to go back
        post.unload(); // Call unload from post
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
