
package dronefinding;

import java.awt.*;
import javax.swing.*;


public class Grid {
    private JFrame gameScreen;
    private JLabel[][] grid;
    private images img;
    private int rows, columns, frameWidth, frameHeight;
    private String response;
    private Post post;
    private boolean[][] visited; // Grid to keep track of visited positions

    public Grid(JFrame frame, Post r, String Response) {
    this.gameScreen = frame;
    this.post =r;
    this.img = new images();
    this.frameWidth = 1500;
    this.frameHeight = 800;
    frameCreation(r, frameWidth, frameHeight);
    this.response = Response;
    visited = new boolean[rows][columns]; // Initialize the visited grid

    setstartIcon(r.extractPositionXFromResponse(Response), r.extractPositionYFromResponse(Response)); 
    setwallIcon(r.extractPositionXFromResponse(Response), r.extractPositionYFromResponse(Response)); 
    setroadIcon(r.extractPositionXFromResponse(Response), r.extractPositionYFromResponse(Response)); 
    }

    public void frameCreation(Post r, int screenWidth, int screenHeight)
    {
        gameScreen.setBackground(Color.BLACK);
        gameScreen.setSize(frameWidth, frameHeight);
        createGrid(r);
        addedTitle();
        gameScreen.setVisible(true);
    }
    
  

    private void createGrid(Post r) {
        int l = 30;
        rows = r.extractedHeightFromResponse(response) ;
        columns = r.extractedWidthFromResponse(response) ;
        JPanel panel = new JPanel(new GridLayout(rows   , columns, 0, 0));
        int panelHeight = rows * l;
        int panelWidth = columns * l;
        
        grid = new JLabel[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {  // Inverti l'ordine delle colonne
                grid[i][j] = new JLabel();
                grid[i][j].setIcon(img.getImages(-2));
                panel.add(grid[i][j]);
            }
        }

        panel.setBounds(l, l, panelWidth, panelHeight);
        externalWalls(l, 0, 0, panelWidth, panelHeight);
        gameScreen.add(panel);
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
    



    public void addedTitle(){
        String nm = new String("#0e0152");
        JLabel title = new JLabel();
        title.setFont(new Font("Showcard Gothic", Font.BOLD, 54));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(50, 50, 50, 50);
        constraints.anchor = GridBagConstraints.EAST;
        title.setBounds(100, 100, 20, 20);
        title.setForeground(Color.decode(nm));
        gameScreen.add(title);   
    }
    

  
    
    public void externalWalls(int l, int x, int y, int panelWidth, int panelHeight) 
    {
        topBottomWalls(l, x, y, panelHeight);
        rightLeftWalls(l, x, y + l, panelWidth);
    }

    public void createWall(int x, int y, int width, int height, ImageIcon img) {
        JLabel wall = new JLabel();
        wall.setBounds(x, y, width, height);
        wall.setIcon(img);
        gameScreen.add(wall);
    }

    public void topBottomWalls(int l, int x, int y, int panelHeight) {
        int dim;
        if(columns >= rows){
           dim = columns + 2;
        }
        else{
            dim = columns;
        }
        
        for(int j = 0; j < 2; j++){
            for (int i = 0; i < dim; i++) {
                if( i < columns + 2){
                    createWall(x + i * l, y, l, l, img.getImages(-1));
                }
            }
            y = y + panelHeight + l;
        }
    }

    public void rightLeftWalls(int l, int x, int y, int panelWidth) 
    {
        int dim;
        if(rows > columns){
           dim = rows + 2;
        }
        else{
            dim = rows;
        }
        
        for(int j = 0; j < 2; j++){
            for (int i = 0; i < rows; i++) {
                if( i < columns + 2){
                    createWall(x, y + i * l, l, l, img.getImages(-1));
                }
            }
            x = x + panelWidth + l;
        }
    }

    public JFrame getGameScreen() {
        return gameScreen;
    }

    public void setDroneIcon(int startX, int startY) {
    grid[startY][startX].setIcon(img.getImages(4)); // Assuming 4 represents the drone image
    }
    public void markVisited(int x, int y) {
        visited[y][x] = true;
    }

    // Method to check if a position has been visited
    public boolean isVisited(int x, int y) {
        return visited[y][x];
    }
    public void setstartIcon(int startX, int startY) {
    grid[startY][startX].setIcon(img.getImages(0)); // Assuming 4 represents the drone image
    }
    
    

   public void setwallIcon(int startX, int startY) {
    String look = post.look();
    System.out.println(look);
    int[] neighbours = post.extractNeighborsFromResponse(look);
    
    for (int i = 0; i < neighbours.length; i++) {
        if (neighbours[i] == 1) {
            switch (i) {
                case 0:
                    grid[startY - 1][startX].setIcon(img.getImages(1));
                    break;
                case 1:
                    grid[startY][startX + 1].setIcon(img.getImages(1));
                    break;
                case 2:
                    grid[startY + 1][startX].setIcon(img.getImages(1));
                    break;
                case 3:
                    grid[startY][startX - 1].setIcon(img.getImages(1));
                    break;
                // Add more cases if needed for additional neighbors
            }
        }
    }
}
    public void setroadIcon(int startX, int startY) {
    String look = post.look();
    System.out.println(look);
    int[] neighbours = post.extractNeighborsFromResponse(look);
    
    for (int i = 0; i < neighbours.length; i++) {
        if (neighbours[i] == 0) {
            switch (i) {
                case 0:
                    grid[startY - 1][startX].setIcon(img.getImages(0));
                    break;
                case 1:
                    grid[startY][startX + 1].setIcon(img.getImages(0));
                    break;
                case 2:
                    grid[startY + 1][startX].setIcon(img.getImages(0));
                    break;
                case 3:
                    grid[startY][startX - 1].setIcon(img.getImages(0));
                    break;
            }
        }
    }
  }
}

