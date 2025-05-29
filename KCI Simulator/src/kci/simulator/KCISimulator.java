package kci.simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class KCISimulator{

    public static JFrame frame;
    public static JLabel label;
    public static JTextField txt;
    
    public static void main(String[] args) {        
        txt = new JTextField();
        frame = new JFrame("Main Menu");
        frame.setSize(600,300);
        frame.setLocation(400,200);
        frame.addKeyListener(new KCIKeyListener());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        
        label = new JLabel("null");
        label.setFont(new java.awt.Font("Arial", Font.BOLD, 24));
        label.setBounds(200, 50, 200, 24);
        label.setForeground(Color.red);
        frame.add(label);
        
        frame.setVisible(true);
    } 
}
