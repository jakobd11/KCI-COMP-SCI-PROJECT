package kci.simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class KCISimulator {

    public static JFrame frame;
    public static JPanel mapPanel, sideMenu;
    public static JLabel character, nameLabel, rodney;
    public static JProgressBar staminaBar;
    public static JLayeredPane layeredPane;

    public static int characterX = 300, characterY = 300, stamina = 100;
    public static double screenX, screenY;

    public static Set<Integer> pressedKeys = new HashSet<>();

    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenX = screenSize.getWidth();
        screenY = screenSize.getHeight();

        frame = new JFrame("KCI Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        layeredPane = new JLayeredPane();
        int tileSize = (int)screenY / 7;
        int mapWidth = tileSize * 7;
        int mapHeight = tileSize * 7;
        layeredPane.setPreferredSize(new Dimension(mapWidth, mapHeight));
        frame.add(layeredPane, BorderLayout.CENTER);

        sideMenu = new JPanel();
        sideMenu.setPreferredSize(new Dimension((int)(screenX-mapWidth)/2, 100));
        sideMenu.setLayout(new FlowLayout());
        frame.add(sideMenu, BorderLayout.WEST);
        nameLabel = new JLabel("Rodney Raider");
        nameLabel.setFont(new java.awt.Font("Arial", Font.BOLD, 24));
        nameLabel.setForeground(Color.red);
        rodney = new JLabel(new ImageIcon(new ImageIcon("rodneyorsmth.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT)));
        staminaBar = new JProgressBar(0, 100);
        staminaBar.setForeground(Color.green);
        staminaBar.setBackground(Color.gray);
        sideMenu.add(nameLabel);
        sideMenu.add(rodney);
        sideMenu.add(staminaBar);
        
        mapPanel = new JPanel();
        mapPanel.setLayout(new GridLayout(7, 7));
        mapPanel.setBounds(0, 0, mapWidth, mapHeight);

        int[][] mapMatrix = new int[7][7];
        for (int i = 0; i < mapMatrix.length; i++) {
            for (int j = 0; j < mapMatrix[i].length; j++) {
                ImageIcon pic;
                if (i == 0 || i == 6) {
                    mapMatrix[i][j] = 0;
                    pic = new ImageIcon(new ImageIcon("smoke.jpg").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
                } else if (i == 1 || i == 5) {
                    mapMatrix[i][j] = 1;
                    pic = new ImageIcon(new ImageIcon("wall.jpeg").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
                } else {
                    mapMatrix[i][j] = 2;
                    pic = new ImageIcon(new ImageIcon("floor.jpg").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
                }
                JLabel label = new JLabel(pic);
                mapPanel.add(label);
            }
        }

        layeredPane.add(mapPanel, Integer.valueOf(0));

        ImageIcon walkingW = new ImageIcon(new ImageIcon("walkfwd.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        ImageIcon walkingS = new ImageIcon(new ImageIcon("walkback.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        ImageIcon walkingA = new ImageIcon(new ImageIcon("walkleft.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        ImageIcon walkingD = new ImageIcon(new ImageIcon("walkright.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        ImageIcon facingW = new ImageIcon(new ImageIcon("standfwd.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        ImageIcon facingS = new ImageIcon(new ImageIcon("standback.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        ImageIcon facingA = new ImageIcon(new ImageIcon("standleft.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        ImageIcon facingD = new ImageIcon(new ImageIcon("standright.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        ImageIcon runningW = new ImageIcon(new ImageIcon("sprintfwd.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        ImageIcon runningS = new ImageIcon(new ImageIcon("sprintback.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        ImageIcon runningA = new ImageIcon(new ImageIcon("sprintleft.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        ImageIcon runningD = new ImageIcon(new ImageIcon("sprintright.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));

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

        javax.swing.Timer timer = new javax.swing.Timer(16, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean sprinting = pressedKeys.contains(KeyEvent.VK_SHIFT);
                boolean moving = false;

                if (stamina == 0) {
                    sprinting = false;
                }

                int speed = sprinting ? 7 : 4;

                if (pressedKeys.contains(KeyEvent.VK_W) || pressedKeys.contains(KeyEvent.VK_UP)) {
                    characterY -= speed;
                    character.setIcon(sprinting ? runningW : walkingW);
                    moving = true;
                }
                if (pressedKeys.contains(KeyEvent.VK_S) || pressedKeys.contains(KeyEvent.VK_DOWN)) {
                    characterY += speed;
                    character.setIcon(sprinting ? runningS : walkingS);
                    moving = true;
                }
                if (pressedKeys.contains(KeyEvent.VK_A) || pressedKeys.contains(KeyEvent.VK_LEFT)) {
                    characterX -= speed;
                    character.setIcon(sprinting ? runningA : walkingA);
                    moving = true;
                }
                if (pressedKeys.contains(KeyEvent.VK_D) || pressedKeys.contains(KeyEvent.VK_RIGHT)) {
                    characterX += speed;
                    character.setIcon(sprinting ? runningD : walkingD);
                    moving = true;
                }

                if (pressedKeys.contains(KeyEvent.VK_SHIFT) && moving) {
                    stamina -= 1;
                    if (stamina <= 0) stamina = 0;
                } else {
                    if (stamina < 100) stamina += 1;
                }

                if (characterY < 0 + 2*tileSize) characterY = 2*tileSize;
                if (characterY > mapHeight - 3*tileSize) characterY = mapHeight - 3*tileSize;
                if (characterX < 0) characterX = 0;
                if (characterX > mapWidth - tileSize) characterX = mapWidth - tileSize;

                character.setLocation(characterX, characterY);
                staminaBar.setValue(stamina);
            }
        });
        timer.start();

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(true);
        frame.setFocusable(true);
        frame.pack();
        frame.setVisible(true);
    }
}