package kci.simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class KCISimulator{

    public static JFrame frame;
    public static JPanel panel;
    public static JLabel character;
    public static JLayeredPane layeredPane;
    
    public static void main(String[] args) {        
        frame = new JFrame("Main Menu");
        frame.setSize(700,700);
        frame.setLocation(400,200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(7 * 100, 7 * 100));
        frame.setContentPane(layeredPane);
        
        panel = new JPanel();
        panel.setLayout(new GridLayout(7,7));
        panel.setBounds(0, 0, 700, 700);
        
        
        
        int[][] mapMatrix = new int[7][7];
        
        for (int i = 0; i < mapMatrix.length; i++) {
            for (int j = 0; j < mapMatrix[i].length; j++) {
                ImageIcon pic;
                if (i == 0 || i == 6) {
                    mapMatrix[i][j] = 0;
                    pic = new ImageIcon(new ImageIcon("smoke.jpg").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
                } else if (i == 1 || i == 5) {
                    mapMatrix[i][j] = 1;
                    pic = new ImageIcon(new ImageIcon("wall.jpeg").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
                } else {
                    mapMatrix[i][j] = 2;
                    pic = new ImageIcon(new ImageIcon("floor.jpg").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
                }
                JLabel label = new JLabel(pic);
                label.setBounds(0, 0, 100, 100);
                panel.add(label);
            }
        }
        
        layeredPane.add(panel, Integer.valueOf(0));  
        
        ImageIcon characterIcon = new ImageIcon(new ImageIcon("smoke.jpg").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        character = new JLabel(characterIcon);
        character.setBounds(300, 300, 100, 100);
        layeredPane.add(character, Integer.valueOf(1));
        
        frame.addKeyListener(new KCIKeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'w') {
                    
                } else if (e.getKeyChar() == 'a') {
                    
                } else if (e.getKeyChar() == 's') {
                    
                } else if (e.getKeyChar() == 'd') {
                    
                }
            }
        });
        
        frame.setFocusable(true);
        frame.setVisible(true);
    } 
}
