/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dronefinding;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Dell
 */
public class Grid {
    private JFrame gameScreen;
    private JLabel[][] grid;
    private boolean controllo;
    private images img;
    private JButton connection;
    private int rows, columns, frameWidth, frameHeight;
    private String response;
    public Grid(JFrame frame,Post r, String Response)
    {
        this.gameScreen = frame;
        this.img = new images();
        this.frameWidth = 1500;
        this.frameHeight = 800;
        frameCreation(r, frameWidth, frameHeight);
        this.response = Response;
        
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
    
}

