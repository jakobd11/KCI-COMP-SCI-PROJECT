package kci.simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class KCISimulator{

    public static JFrame frame;
    
    public static void main(String[] args) {        
        frame = new JFrame("Main Menu");
        frame.setSize(700,700);
        frame.setLocation(400,200);
        frame.setLayout(new GridLayout(7,7));
        frame.addKeyListener(new KCIKeyListener());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        int[][] mapMatrix = new int[7][7];
        ImageIcon pic;
        
        for (int i = 0; i < mapMatrix.length; i++) {
            for (int j = 0; j < mapMatrix[i].length; j++) {
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
                frame.add(label);
            }
        }
          
        frame.setVisible(true);
    } 
}
