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
    
    public static int characterX = 300, characterY = 300;
    
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
        
        ImageIcon walkingW = new ImageIcon(new ImageIcon("walkfwd.gif").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        ImageIcon walkingS = new ImageIcon(new ImageIcon("walkback.gif").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        ImageIcon walkingA = new ImageIcon(new ImageIcon("walkleft.gif").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        ImageIcon walkingD = new ImageIcon(new ImageIcon("walkright.gif").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        ImageIcon facingW = new ImageIcon(new ImageIcon("standfwd.gif").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        ImageIcon facingS = new ImageIcon(new ImageIcon("standback.gif").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        ImageIcon facingA = new ImageIcon(new ImageIcon("standleft.gif").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        ImageIcon facingD = new ImageIcon(new ImageIcon("standright.gif").getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
        character = new JLabel(facingW);
        character.setBounds(characterX, characterY, 100, 100);
        character.setDoubleBuffered(true);
        layeredPane.add(character, Integer.valueOf(1));
        
        frame.addKeyListener(new KCIKeyListener() {  
            public void keyPressed(KeyEvent e) {
                boolean sprinting = false;
                switch (e.getKeyChar()) {
                    case 'w':
                        characterY -= 15;
                        character.setIcon(walkingW);
                        break;
                    case 's':
                        characterY += 15;
                        character.setIcon(walkingS);
                        break;
                    case 'a':
                        characterX -= 15;
                        character.setIcon(walkingA);
                        break;
                    case 'd':
                        characterX += 15;
                        character.setIcon(walkingD);
                        break;
                    case 'W':
                        characterY -= 30;
                        character.setIcon(walkingW);
                        sprinting = true;
                        break;
                    case 'S':
                        characterY += 30;
                        character.setIcon(walkingS);
                        sprinting = true;
                        break;
                    case 'A':
                        characterX -= 30;
                        character.setIcon(walkingA);
                        sprinting = true;
                        break;
                    case 'D':
                        characterX += 30;
                        character.setIcon(walkingD);
                        sprinting = true;
                        break;
                    default:
                        break;
                }
                if (characterY > 160){
                    if (characterY < 440) {
                        character.setLocation(characterX, characterY);
                    } else {
                        if (sprinting) {
                            characterY -= 30;
                        } else {
                            characterY -= 15;
                        }
                    }
                } else {
                    if (sprinting) {
                        characterY += 30;
                    } else {
                        characterY += 15;
                    }
                        
                    
                }
                
            } 
            public void keyReleased (KeyEvent e) {
                switch (e.getKeyChar()) {
                    case 'w':
                        character.setIcon(facingW);
                        break;
                    case 's':
                        character.setIcon(facingS);
                        break;
                    case 'a':
                        character.setIcon(facingA);
                        break;
                    case 'd':
                        character.setIcon(facingD);
                        break;
                    case 'W':
                        character.setIcon(facingW);
                        break;
                    case 'S':
                        character.setIcon(facingS);
                        break;
                    case 'A':
                        character.setIcon(facingA);
                        break;
                    case 'D':
                        character.setIcon(walkingD);
                        break;
                    default:
                        break;
                }
            }
        });
        
        frame.setFocusable(true);
        frame.setVisible(true);
    }
}
