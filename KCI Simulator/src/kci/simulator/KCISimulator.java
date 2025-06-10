package kci.simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class KCISimulator {

    public static JFrame frame;
    public static JPanel mapPanel, westMenu, eastMenu;
    public static JLabel character, rodney, interact;
    public static JProgressBar staminaBar;
    public static JLayeredPane layeredPane;

    public static int stamina = 100, tileSize, mapWidth, mapHeight;
    public static boolean inDialogue = false;
    
    public static Map thirdFloor;

    public static Set<Integer> pressedKeys = new HashSet<>();
    
    public static ImageIcon walkingW, walkingS, walkingA, walkingD, 
            runningW, runningS, runningA, runningD,
            facingW, facingS, facingA, facingD;
    
    public static NPC janicas = new NPC(300, 300, "janicas", new JLabel());

    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        frame = new JFrame("KCI Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setForeground(Color.black);
        frame.setBackground(Color.black);
        frame.getContentPane().setBackground(Color.black);
        frame.setLayout(new BorderLayout());

        layeredPane = new JLayeredPane();
        tileSize = (int)screenSize.getHeight() / 10;
        mapWidth = tileSize * 10;
        mapHeight = tileSize * 10;
        layeredPane.setPreferredSize(new Dimension(mapWidth, mapHeight));
        frame.add(layeredPane, BorderLayout.CENTER);

        westMenu = new JPanel();
        westMenu.setPreferredSize(new Dimension((int)(screenSize.getWidth() - mapWidth) / 2, 100));
        westMenu.setLayout(new BoxLayout(westMenu, BoxLayout.Y_AXIS));
        westMenu.setBackground(Color.black);
        frame.add(westMenu, BorderLayout.WEST);
        
        eastMenu = new JPanel();
        eastMenu.setPreferredSize(new Dimension((int)(screenSize.getWidth() - mapWidth) / 2, 100));
        eastMenu.setLayout(new BoxLayout(eastMenu, BoxLayout.Y_AXIS));
        eastMenu.setBackground(Color.black);
        frame.add(eastMenu, BorderLayout.EAST);
        
        rodney = new JLabel(new ImageIcon(new ImageIcon("rodneycropped.png")
                .getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH)));
        rodney.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        janicas.setImage(new JLabel(new ImageIcon(new ImageIcon("janicas.png")
                .getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT))));
        janicas.getImage().setBounds(janicas.getNpcX(), janicas.getNpcY(), tileSize, tileSize);
        layeredPane.add(janicas.getImage(), Integer.valueOf(3));
        
        staminaBar = new JProgressBar(0, 100);
        staminaBar.setForeground(Color.green);
        staminaBar.setBackground(Color.gray);
        staminaBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        westMenu.add(rodney);
        westMenu.add(staminaBar);
        
        fillMaps();
        
        thirdFloor.getMapImage().setBounds(thirdFloor.getCharacterX(), thirdFloor.getCharacterY(), tileSize*40, tileSize*40);
        layeredPane.add(thirdFloor.getMapImage(), Integer.valueOf(0));

        facingW = new ImageIcon(new ImageIcon("standfwd.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        facingS = new ImageIcon(new ImageIcon("standback.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        facingA = new ImageIcon(new ImageIcon("standleft.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        facingD = new ImageIcon(new ImageIcon("standright.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));

        character = new JLabel(facingW);
        character.setBounds((mapWidth/2)-(tileSize/2), (mapHeight/2)-(tileSize/2), tileSize, tileSize);
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
        interact.setBounds((mapWidth / 2) - 100, (int)(mapHeight * 0.8), 300, 24);
        layeredPane.add(interact, Integer.valueOf(2));
        interact.setVisible(false);
        
        javax.swing.Timer timer = new javax.swing.Timer(16, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int charX = thirdFloor.getCharacterX();
                int charY = thirdFloor.getCharacterY();
                
                if (charX >= janicas.getNpcX() - 100 && charX <= janicas.getNpcX() + 100 &&
                    charY >= janicas.getNpcY() - 100 && charY <= janicas.getNpcY() + 100) {
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
        int dx = 0, dy = 0;
        
        if (stamina == 0) {
            sprinting = false;
        }

        int speed = sprinting ? 7 : 4;

        if (pressedKeys.contains(KeyEvent.VK_W) || pressedKeys.contains(KeyEvent.VK_UP)) {
            dy += speed;
            character.setIcon(sprinting ? runningW : walkingW);
            moving = true;
        } 
        if (pressedKeys.contains(KeyEvent.VK_S) || pressedKeys.contains(KeyEvent.VK_DOWN)) {
            dy -= speed;
            character.setIcon(sprinting ? runningS : walkingS);
            moving = true;
        }
        if (pressedKeys.contains(KeyEvent.VK_A) || pressedKeys.contains(KeyEvent.VK_LEFT)) {
            dx += speed;
            character.setIcon(sprinting ? runningA : walkingA);
            moving = true;
        }
        if (pressedKeys.contains(KeyEvent.VK_D) || pressedKeys.contains(KeyEvent.VK_RIGHT)) {
            dx -= speed;
            character.setIcon(sprinting ? runningD : walkingD);
            moving = true;
        }

        if (pressedKeys.contains(KeyEvent.VK_SHIFT) && moving) {
            stamina -= 1;
            if (stamina < 0) stamina = 0;
        } else {
            if (stamina < 100) stamina += 1;
        }
        
        int charScreenX = (mapWidth / 2) - (tileSize / 2);
        int charScreenY = (mapHeight / 2) - (tileSize / 2);

        int newMapX = thirdFloor.getCharacterX() + dx;
        int newMapY = thirdFloor.getCharacterY() + dy;

        int characterMapX = -newMapX + charScreenX;
        int characterMapY = -newMapY + charScreenY;

        if (canMoveTo(characterMapX, characterMapY, tileSize, tileSize, thirdFloor)) {
            thirdFloor.setCharacterX(newMapX);
            thirdFloor.setCharacterY(newMapY);
            thirdFloor.getMapImage().setLocation(newMapX, newMapY);
        }

        System.out.println("x: " + thirdFloor.getCharacterX() + "\ny: " + thirdFloor.getCharacterY());
        
        staminaBar.setValue(stamina);
    }
    
    public static void fillMaps() {
//         thirdFloor = new Map(mapX, mapY, new JLabel(new ImageIcon(new ImageIcon("3rdfloorsketch.png").getImage().getScaledInstance((int)(tileSize), (int)(tileSize), Image.SCALE_DEFAULT))));
//        
////        //class exit
////        compSci.addDoors(, );
//        
//        //janicas
//        compSci.addNpcs(0,0 ,"Mr. Janicas" , new JLabel(new ImageIcon(new ImageIcon("janicas.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT))));
        
        thirdFloor = new Map(0, 0, new JLabel(new ImageIcon(new ImageIcon("3rdfloorsketch.png").getImage().getScaledInstance((int)tileSize*40, (int)tileSize*40, Image.SCALE_DEFAULT))));
        
        thirdFloor.addWalkableArea(tileSize, (int)(tileSize*2.5), (int)(tileSize*20.5), (int)(tileSize*6.5));
        thirdFloor.addWalkableArea((int)(tileSize*9.2), (int)(tileSize*8), (int)(tileSize*4.1), (int)(tileSize*13.6));
        thirdFloor.addWalkableArea((int)(tileSize*6), (int)(tileSize*15), (int)(tileSize*5), (int)(tileSize*1.5));
        thirdFloor.addWalkableArea((int)(tileSize*10), (int)(tileSize*17.2), (int)(tileSize*11), (int)(tileSize*4.5));
        thirdFloor.addWalkableArea((int)(tileSize*14.17), (int)(tileSize*19), (int)(tileSize*4), (int)(tileSize*19));
        thirdFloor.addWalkableArea((int)(tileSize*14.17), (int)(tileSize*19), (int)(tileSize*4), (int)(tileSize*19));
//        //stairs
//        thirdFloor.addDoors(, );
//        thirdFloor.addDoors(, );
//        thirdFloor.addDoors(, );
//        
//        //rooms
//        thirdFloor.addDoors(, );
//        thirdFloor.addDoors(, );
//        
//        //hall monitors
//        thirdFloor.addNpcs(, , , );
//        thirdFloor.addNpcs(, , , );
        
//        secondFloor.setCharacterX(0);
//        secondFloor.setCharacterY(0);
//        secondFloor.setMapImage(new JLabel(new ImageIcon(new ImageIcon("image name here").getImage().getScaledInstance(mapWidth*3, mapHeight*3, Image.SCALE_DEFAULT))));
//        
//        //stairs
//        secondFloor.addDoors(, );
//        secondFloor.addDoors(, );
//        secondFloor.addDoors(, );
//        
//        //rooms
//        secondFloor.addDoors(, );
//        secondFloor.addDoors(, );
//        
//        //hall monitors
//        secondFloor.addNpcs(, , , );
//        secondFloor.addNpcs(, , , );
        
        
    }
    
    public static boolean canMoveTo(int x, int y, int width, int height, Map map) {
        Rectangle characterBounds = new Rectangle(x, y, width, height);
        for (Rectangle walkableArea : map.getWalkable()) {
            if (walkableArea.contains(characterBounds)) {
                return true;
            }
        }
        return false;
    }
    
}