
package dronefinding;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class graphics {
 private JFrame frame;
    private JPanel panel;
    private JLabel label;
    private JTextField textField;
    private JButton startButton;
    private String initResponse;
    Post p = new Post();

    
    public graphics() {
        createFirstScreen();
        
    }

    private void Frame(){
         this.frame = new JFrame("Trophy Finder");        
         frame.setSize(1500, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }
    
   private void createFirstScreen() {
       Frame();
       panel = new JPanel();

        label = new JLabel("Seed:");
        textField = new JTextField(10);
        startButton = new JButton("Start");

        panel.add(label);
        panel.add(textField);
        panel.add(startButton);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initResponse= p.init();
                frame.getContentPane().removeAll(); 
                createSecondScreen();
            }
        });

        frame.add(panel);
        frame.setVisible(true);
    }
   
   private void createSecondScreen() {
       Grid grid = new Grid(frame,p , initResponse);
       images img;
       Drone d = new Drone();
}
}