package kci.simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class KCISimulator {

    public static JFrame frame;
    public static JPanel mapPanel, sideMenu;
    public static JLabel character, nameLabel, rodney, interact, mapImage;
    public static JProgressBar staminaBar;
    public static JLayeredPane layeredPane;

    public static int characterX, characterY, stamina = 100, tileSize, mapWidth, mapHeight, mapX, mapY;
    public static double screenX, screenY;
    public static boolean inDialogue = false;

    public static Set<Integer> pressedKeys = new HashSet<>();
    
    public static ImageIcon walkingW , walkingS, walkingA, walkingD, 
            runningW, runningS, runningA, runningD,
            facingW, facingS, facingA, facingD;
    
    public static NPC janicas = new NPC(300, 300, "janicas", new JLabel());

    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenX = screenSize.getWidth();
        screenY = screenSize.getHeight();

        frame = new JFrame("KCI Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setForeground(Color.black);
        frame.setBackground(Color.black);
        frame.getContentPane().setBackground(Color.black);
        frame.setLayout(new BorderLayout());

        layeredPane = new JLayeredPane();
        tileSize = (int)screenY / 10;
        mapWidth = tileSize * 10;
        mapHeight = tileSize * 10;
        characterX = (mapWidth/2)-(tileSize/2);
        characterY = mapHeight/2;
        mapX = tileSize;
        mapY = -15*tileSize;
        layeredPane.setPreferredSize(new Dimension(mapWidth, mapHeight));
        frame.add(layeredPane, BorderLayout.CENTER);

        sideMenu = new JPanel();
        sideMenu.setPreferredSize(new Dimension((int)(screenX-mapWidth)/2, 100));
        sideMenu.setLayout(new BoxLayout(sideMenu, BoxLayout.Y_AXIS));
        sideMenu.setBackground(Color.DARK_GRAY);
        frame.add(sideMenu, BorderLayout.WEST);
        
        nameLabel = new JLabel("Rodney the Raider");
        nameLabel.setFont(new java.awt.Font("Arial", Font.BOLD, 24));
        nameLabel.setForeground(Color.red);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        rodney = new JLabel(new ImageIcon(new ImageIcon("rodneycropped.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH)));
        rodney.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        janicas.setImage(new JLabel(new ImageIcon(new ImageIcon("janicas.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT))));
        janicas.getImage().setBounds(janicas.getNpcX(), janicas.getNpcY(), tileSize, tileSize);
        layeredPane.add(janicas.getImage(), Integer.valueOf(3));
        
        staminaBar = new JProgressBar(0, 100);
        staminaBar.setForeground(Color.green);
        staminaBar.setBackground(Color.gray);
        staminaBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        sideMenu.add(nameLabel);
        sideMenu.add(rodney);
        sideMenu.add(staminaBar);
        
        mapImage = new JLabel(new ImageIcon(new ImageIcon("Map1template.jpg").getImage().getScaledInstance(mapWidth*3, mapHeight*3, Image.SCALE_DEFAULT)));
        mapImage.setBounds(mapX, mapY, mapWidth*3, mapHeight*3);
        layeredPane.add(mapImage, Integer.valueOf(0));

//        mapPanel.setLayout(new GridLayout(7, 7));
//        mapPanel.setBounds(0, 0, mapWidth, mapHeight);
//
//        int[][] mapMatrix = new int[7][7];
//        for (int i = 0; i < mapMatrix.length; i++) {
//            for (int j = 0; j < mapMatrix[i].length; j++) {
//                ImageIcon pic;
//                if (i == 0 || i == 6) {
//                    mapMatrix[i][j] = 0;
//                    pic = new ImageIcon(new ImageIcon("smoke.jpg").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
//                } else if (i == 1 || i == 5) {
//                    mapMatrix[i][j] = 1;
//                    pic = new ImageIcon(new ImageIcon("wall.jpeg").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
//                } else {
//                    mapMatrix[i][j] = 2;
//                    pic = new ImageIcon(new ImageIcon("floor.jpg").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
//                }
//                JLabel label = new JLabel(pic);
//                mapPanel.add(label);
//            }
//        }

        facingW = new ImageIcon(new ImageIcon("standfwd.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        facingS = new ImageIcon(new ImageIcon("standback.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        facingA = new ImageIcon(new ImageIcon("standleft.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        facingD = new ImageIcon(new ImageIcon("standright.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));

        character = new JLabel(facingS);
        character.setBounds(characterX, characterY, tileSize, tileSize);
        character.setDoubleBuffered(true);
        layeredPane.add(character, Integer.valueOf(1));

        frame.addKeyListener(new KCIKeyListener() {
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
            }

            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        character.setIcon(facingW);
                        break;
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        character.setIcon(facingS);
                        break;
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        character.setIcon(facingA);
                        break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        character.setIcon(facingD);
                        break;
                }
            }
        });
        
        walkingW = new ImageIcon(new ImageIcon("walkfwd.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        walkingS = new ImageIcon(new ImageIcon("walkback.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        walkingA = new ImageIcon(new ImageIcon("walkleft.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        walkingD = new ImageIcon(new ImageIcon("walkright.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        runningW = new ImageIcon(new ImageIcon("sprintfwd.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        runningS = new ImageIcon(new ImageIcon("sprintback.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        runningA = new ImageIcon(new ImageIcon("sprintleft.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        runningD = new ImageIcon(new ImageIcon("sprintright.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));

        interact = new JLabel("Press [E] to interact. ");
        interact.setFont(new java.awt.Font("Arial", Font.BOLD, 24));
        interact.setForeground(Color.red);
        interact.setBounds((mapWidth/2)-100, (int)(mapHeight*0.8), 300, 24);
        layeredPane.add(interact, Integer.valueOf(2));
        interact.setVisible(false);
        
        javax.swing.Timer timer = new javax.swing.Timer(16, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                if (characterX >= janicas.getNpcX() - 100 && characterX <= janicas.getNpcX() + 100 &&
                    characterY >= janicas.getNpcY() - 100 && characterY <= janicas.getNpcY() + 100) {
                    interact.setVisible(true);
                    if (pressedKeys.contains(KeyEvent.VK_E)) {
                        inDialogue = true;
                    }
                } else {
                    interact.setVisible(false);
                }
                
                if (inDialogue && pressedKeys.contains(KeyEvent.VK_ESCAPE)) {
                    inDialogue = false;
                }
                
                if (!inDialogue)
                    movement();
                
            }
        });
        timer.start();

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(true);
        frame.setFocusable(true);
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void movement() {
        boolean sprinting = pressedKeys.contains(KeyEvent.VK_SHIFT);
        boolean moving = false;

        if (stamina == 0) {
            sprinting = false;
        }

        int speed = sprinting ? 7 : 4;

        if (pressedKeys.contains(KeyEvent.VK_W) || pressedKeys.contains(KeyEvent.VK_UP)) {
//            characterY -= speed;
            mapY += speed;
            janicas.setNpcY(janicas.getNpcY() + speed);
            character.setIcon(sprinting ? runningW : walkingW);
            moving = true;
        } 
        if (pressedKeys.contains(KeyEvent.VK_S) || pressedKeys.contains(KeyEvent.VK_DOWN)) {
//            characterY += speed;
            mapY -= speed;
            janicas.setNpcY(janicas.getNpcY() - speed);
            character.setIcon(sprinting ? runningS : walkingS);
            moving = true;
        }
        if (pressedKeys.contains(KeyEvent.VK_A) || pressedKeys.contains(KeyEvent.VK_LEFT)) {
//            characterX -= speed;
            mapX += speed;
            janicas.setNpcX(janicas.getNpcX() + speed);
            character.setIcon(sprinting ? runningA : walkingA);
            moving = true;
        }
        if (pressedKeys.contains(KeyEvent.VK_D) || pressedKeys.contains(KeyEvent.VK_RIGHT)) {
//            characterX += speed;
            mapX -= speed;
            janicas.setNpcX(janicas.getNpcX() - speed);
            character.setIcon(sprinting ? runningD : walkingD);
            moving = true;
        }

        if (pressedKeys.contains(KeyEvent.VK_SHIFT) && moving) {
            stamina -= 1;
            if (stamina <= 0) stamina = 0;
        } else {
            if (stamina < 100) stamina += 1;
        }

//        if (characterY < 0 + 2*tileSize) characterY = 2*tileSize;
//        if (characterY > mapHeight - 3*tileSize) characterY = mapHeight - 3*tileSize;
//        if (characterX < 0) characterX = 0;
//        if (characterX > mapWidth - tileSize) characterX = mapWidth - tileSize;

        mapImage.setLocation(mapX, mapY);
        janicas.getImage().setLocation(janicas.getNpcX(), janicas.getNpcY());
//        character.setLocation(characterX, characterY);
        staminaBar.setValue(stamina);
    }
}