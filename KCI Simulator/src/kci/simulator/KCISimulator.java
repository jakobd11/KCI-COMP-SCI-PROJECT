package kci.simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class KCISimulator{

    public static JFrame frame;
    
    public static void main(String[] args) {        
        frame = new JFrame("Main Menu");
        frame.setSize(600,300);
        frame.setLocation(400,200);
        frame.addKeyListener(new KCIKeyListener());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
                
        frame.setVisible(true);
    } 
}