
package dronefinding;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
    panel = new JPanel(new BorderLayout());
    
    ImageIcon backgroundImage = new ImageIcon("src/images/castle.jpg");
    JLabel backgroundLabel = new JLabel(backgroundImage);
    
    JPanel subpanel = new JPanel();
    
    subpanel.setBackground(Color.WHITE);
    subpanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    
    label = new JLabel("Seed:");
    textField = new JTextField(10);
    startButton = new JButton("Start");

    subpanel.add(label);
    subpanel.add(textField);
    subpanel.add(startButton);

    startButton.addActionListener(new ActionListenerImpl());

    backgroundLabel.setLayout(new GridBagLayout()); 
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.gridy = 0;
    backgroundLabel.add(subpanel, gbc);
    
    panel.add(backgroundLabel, BorderLayout.CENTER);
    
    frame.add(panel);
    frame.pack();
    frame.setVisible(true);
}

   
   private void createSecondScreen() {
       Grid grid = new Grid(frame,p , initResponse);        
       images img;
       Drone d = new Drone(p,grid);
    }

    private class ActionListenerImpl implements ActionListener {

        public ActionListenerImpl() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            initResponse= p.init();
            frame.getContentPane().removeAll();
            createSecondScreen();
        }
    }
}