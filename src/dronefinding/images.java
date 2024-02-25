/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dronefinding;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

public class images {
    private Map<Integer, ImageIcon> images;

    public images() {
        images = new HashMap<>(); 
        creationOfImages();
    }
    
    public void creationOfImages(){
       images.put(-2, new ImageIcon("src/images/nebbia.jpg"));
       images.put(-1, new ImageIcon("src/images/esterno.jpg"));
       images.put(0, new ImageIcon("src/images/strada.png"));
       images.put(1, new ImageIcon("src/images/muro.png")); 
       images.put(3, new ImageIcon("src/images/cup.png")); 
       images.put(4, new ImageIcon("src/images/drone.jpg")); 
       images.put(5, new ImageIcon("src/images/dronecup.png"));
       images.put(6, new ImageIcon("src/images/back.jpg"));
       images.put(7, new ImageIcon("src/images/finalBack.jpg"));
    }
     public ImageIcon getImages(int key)
    {
        return images.get(key);
    }
     
    public ImageIcon ridimensionImage(int key, int width, int height)
    {
        ImageIcon originalIcon = images.get(key);
        if (originalIcon != null) {
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            return new ImageIcon(resizedImage);
        } else {
            return null; 
        }
    } 
}
