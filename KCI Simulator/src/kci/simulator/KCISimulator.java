/**
 * Program Name: KCI Simulator
 * Programmers: Jakob Dombrowski, Abdur Rehman Bin Asad, Ava Elmitt
 * Date: June 17, 2025
 * Description: Complete missions as a KCI student
 */
package kci.simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class KCISimulator {
    public static JFrame gameFrame, menuFrame, endFrame;
    public static JPanel mapPanel, westMenu, eastMenu, pauseMenu, dialoguePanel, cursedPanel;
    public static JLabel character, rodney, interact, title, status, rodneyLabel, staminaBarLabel, dialogueText, nameText, inventoryLabel, end, cursedText;
    public static JButton play, quit, resume, savequit;
    public static JProgressBar staminaBar;
    public static JLayeredPane layeredPane;

    public static int stamina = 100, tileSize, mapWidth, mapHeight, currentMap = 2, charScreenX, charScreenY, mapSize, gameStage = 0, time = 2000;
    public static boolean inDialogue = false, inPauseMenu = false, eKeyHeld, canExitDialogue = true, timeStart = false;
    
    public static Set<Integer> pressedKeys = new HashSet<>();
    public static HashMap<String, JLabel> inventory = new HashMap<>();
    public static Map[] maps = new Map[6];
    
    public static ImageIcon walkingW, walkingS, walkingA, walkingD, 
            runningW, runningS, runningA, runningD,
            facingW, facingS, facingA, facingD;
    
    public static javax.swing.Timer timer;

    public static void main(String[] args) throws InterruptedException {
        //finding screen size so everything is scaled different for every computer
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        //creating frame for game
        gameFrame = new JFrame("KCI Simulator");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setForeground(Color.black);
        gameFrame.setBackground(Color.black);
        gameFrame.getContentPane().setBackground(Color.black);
        gameFrame.setLayout(new BorderLayout());

        //declaring layered pane and important variables for later code
        layeredPane = new JLayeredPane();
        tileSize = (int)screenSize.getHeight() / 10;
        mapWidth = tileSize * 10;
        mapHeight = tileSize * 10;
        charScreenX = (mapWidth / 2) - (tileSize / 2);
        charScreenY = (mapHeight / 2) - (tileSize / 2);
        mapSize = tileSize*10;
        layeredPane.setPreferredSize(new Dimension(mapWidth, mapHeight));
        gameFrame.add(layeredPane, BorderLayout.CENTER);

        menuFrame = new JFrame("Main Menu");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setBounds((int)(screenSize.getWidth()/2) - 300, (int)(screenSize.getHeight()/2) - 150, 600, 300);
        menuFrame.setForeground(Color.black);
        menuFrame.setBackground(Color.black);
        menuFrame.getContentPane().setBackground(Color.black);
        menuFrame.setLayout(null);
        
        title = new JLabel("KCI Simulator");
        title.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 45));
        title.setBounds(150, 50, 300, 50);
        title.setForeground(Color.white);
        
        play = new JButton("PLAY");
        play.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 45));
        play.setBounds(150, 100, 300, 50);
        play.setForeground(Color.black);
        play.setBackground(Color.gray);
        
        quit = new JButton("QUIT");
        quit.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 45));
        quit.setBounds(150, 150, 300, 50);
        quit.setForeground(Color.black);
        quit.setBackground(Color.gray);
        
        menuFrame.add(title);
        menuFrame.add(play);
        menuFrame.add(quit);
        
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                menuFrame.dispose();
                maps[currentMap].getMapImage().setBounds(maps[currentMap].getCharacterX(), maps[currentMap].getCharacterY(), mapSize, mapSize);
                layeredPane.add(maps[currentMap].getMapImage(), Integer.valueOf(1));
                gameFrame.setVisible(true);
            }
        });
        
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        endFrame = new JFrame("End");
        endFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        endFrame.setBounds((int)(screenSize.getWidth()/2) - 300, (int)(screenSize.getHeight()/2) - 150, 600, 300);
        endFrame.setForeground(Color.black);
        endFrame.setBackground(Color.black);
        endFrame.getContentPane().setBackground(Color.black);
        endFrame.setLayout(null);
        
        end = new JLabel("");
        end.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 45));
        end.setBounds(150, 0, 300, 150);
        end.setForeground(Color.white);
        
        endFrame.add(end);
        endFrame.add(quit);
        
        endFrame.setVisible(false);
        
        //left sided menu
        westMenu = new JPanel();
        westMenu.setPreferredSize(new Dimension((int)(screenSize.getWidth() - mapWidth) / 2, 100));
        westMenu.setLayout(new BoxLayout(westMenu, BoxLayout.Y_AXIS));
        westMenu.setBackground(Color.black);
        gameFrame.add(westMenu, BorderLayout.WEST);
        
        //right sided menu
        eastMenu = new JPanel();
        eastMenu.setPreferredSize(new Dimension((int)(screenSize.getWidth() - mapWidth) / 2, 100));
        eastMenu.setLayout(new BoxLayout(eastMenu, BoxLayout.Y_AXIS));
        eastMenu.setBackground(Color.black);
        
        inventoryLabel = new JLabel("Inventory");
        inventoryLabel.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 36));
        inventoryLabel.setAlignmentX(Component.CENTER_ALIGNMENT); //posX, posY, sizeX, sizeY
        inventoryLabel.setForeground(Color.white);
        
        inventory.put("money", new JLabel(new ImageIcon(new ImageIcon("money.png").getImage().getScaledInstance(tileSize*2, tileSize, Image.SCALE_DEFAULT))));
        inventory.get("money").setAlignmentX(Component.CENTER_ALIGNMENT);
        
        eastMenu.add(inventoryLabel);
        eastMenu.add(inventory.get("money"));
        
        gameFrame.add(eastMenu, BorderLayout.EAST);

        
        //image of rodney's face
        rodney = new JLabel(new ImageIcon(new ImageIcon("rodneycropped.png")
                .getImage().getScaledInstance(tileSize*2, tileSize*2, Image.SCALE_SMOOTH)));
        rodney.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        rodneyLabel = new JLabel("Rodney The Raider");
        rodneyLabel.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 36));
        rodneyLabel.setAlignmentX(Component.CENTER_ALIGNMENT); //posX, posY, sizeX, sizeY
        rodneyLabel.setForeground(Color.RED);
        rodneyLabel.setOpaque(true);
        rodneyLabel.setBackground(Color.BLACK);
        
        staminaBarLabel = new JLabel("Stamina Bar");
        staminaBarLabel.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 30));
        staminaBarLabel.setAlignmentX(Component.CENTER_ALIGNMENT); //posX, posY, sizeX, sizeY
        staminaBarLabel.setForeground(Color.GREEN);
        staminaBarLabel.setOpaque(true);
        staminaBarLabel.setBackground(Color.BLACK);

        //green stamina bar 
        staminaBar = new JProgressBar(0, 100);
        staminaBar.setForeground(Color.green);
        staminaBar.setBackground(Color.gray);
        staminaBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        westMenu.add(rodneyLabel);
        westMenu.add(rodney);
        westMenu.add(staminaBarLabel);
        westMenu.add(staminaBar);
        

        pauseMenu = new JPanel();
        pauseMenu.setBackground(new Color(0,0,0,50));
        pauseMenu.setBounds(tileSize*2, tileSize*1, mapWidth-tileSize*4, mapHeight-tileSize*2);
        pauseMenu.setLayout(null);
        pauseMenu.setVisible(false);
        
        status = new JLabel("PAUSED");
        status.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 45));
        status.setBounds(150, 50, 300, 50);
        status.setForeground(Color.white);
        
        resume = new JButton("RESUME");
        resume.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 45));
        resume.setBounds(150, 100, 300, 50);
        resume.setForeground(Color.black);
        resume.setBackground(Color.gray);
        
        savequit = new JButton("SAVE & QUIT");
        savequit.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 45));
        savequit.setBounds(150, 150, 300, 50);
        savequit.setForeground(Color.black);
        savequit.setBackground(Color.gray);
        
        pauseMenu.add(status);
        pauseMenu.add(resume);
        pauseMenu.add(savequit);
        
        layeredPane.add(pauseMenu, Integer.valueOf(5));
        

        resume.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inPauseMenu = false;
                pauseMenu.setVisible(false);
            }
        });
        
        savequit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        dialoguePanel = new JPanel();
        dialoguePanel.setBackground(Color.black);
        dialoguePanel.setBounds(tileSize*2, tileSize*6, mapWidth-tileSize*4, mapHeight-tileSize*6);
        dialoguePanel.setLayout(null);
        dialoguePanel.setVisible(false);
        
        dialogueText = new JLabel("");
        dialogueText.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 45));
        dialogueText.setBounds(0, 0, mapWidth-tileSize*4, mapHeight-tileSize*6);
        dialogueText.setForeground(Color.white);
        
        nameText = new JLabel("");
        nameText.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 45));
        nameText.setBounds(0, 0, mapWidth-tileSize*4, 45);
        nameText.setForeground(Color.white);
        
        dialoguePanel.add(dialogueText);
        dialoguePanel.add(nameText, BorderLayout.NORTH);
        
        layeredPane.add(dialoguePanel, Integer.valueOf(6));
        
        cursedPanel = new JPanel();
        cursedPanel.setBackground(Color.black);
        cursedPanel.setBounds(tileSize*2, 0, mapWidth-tileSize*4, mapHeight-tileSize*6);
        cursedPanel.setLayout(null);
        cursedPanel.setVisible(false);
        
        cursedText = new JLabel("");
        cursedText.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 45));
        cursedText.setBounds(0, 0, mapWidth-tileSize*4, mapHeight-tileSize*6);
        cursedText.setForeground(Color.white);
        
        cursedPanel.add(cursedText);
        
        layeredPane.add(cursedPanel, Integer.valueOf(6));
        
        //
        fillMaps();
                
        //npc images
        for (NPC npc : maps[currentMap].getNpcs()) {
            npc.getImage().setBounds(npc.getNpcX() - maps[currentMap].getCharacterX() + charScreenX, npc.getNpcY() - maps[currentMap].getCharacterY() + charScreenY, tileSize, tileSize);
            layeredPane.add(npc.getImage(), Integer.valueOf(3));
        }
        
        for (MovingNPC npc : maps[currentMap].getMovingNpcs()) {
            npc.getImage().setBounds(npc.getNpcX() - maps[currentMap].getCharacterX() + charScreenX,
                                    npc.getNpcY() - maps[currentMap].getCharacterY() + charScreenY,
                                    tileSize, tileSize);
            layeredPane.add(npc.getImage(), Integer.valueOf(3));
        }
        
        //standing images
        facingW = new ImageIcon(new ImageIcon("standfwd.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        facingS = new ImageIcon(new ImageIcon("standback.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        facingA = new ImageIcon(new ImageIcon("standleft.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        facingD = new ImageIcon(new ImageIcon("standright.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));

        //character image
        character = new JLabel(facingW);
        character.setBounds((mapWidth/2)-(tileSize/2), (mapHeight/2)-(tileSize/2), tileSize, tileSize);
        character.setDoubleBuffered(true);
        layeredPane.add(character, Integer.valueOf(2));

        //constantly updating hash set of keys
        gameFrame.addKeyListener(new KCIKeyListener() {
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
            }
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    eKeyHeld = false; 
                }
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
        
        //movement images
        walkingW = new ImageIcon(new ImageIcon("walkfwd.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        walkingS = new ImageIcon(new ImageIcon("walkback.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        walkingA = new ImageIcon(new ImageIcon("walkleft.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        walkingD = new ImageIcon(new ImageIcon("walkright.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        runningW = new ImageIcon(new ImageIcon("sprintfwd.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        runningS = new ImageIcon(new ImageIcon("sprintback.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        runningA = new ImageIcon(new ImageIcon("sprintleft.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));
        runningD = new ImageIcon(new ImageIcon("sprintright.gif").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT));

        //interact display
        interact = new JLabel("Press [E] to interact. ");
        interact.setFont(new java.awt.Font("Arial", Font.BOLD, 24));
        interact.setForeground(Color.red);
        interact.setBounds((mapWidth / 2) - 100, (int)(mapHeight * 0.8), 300, 24);
        layeredPane.add(interact, Integer.valueOf(4));
        interact.setVisible(false);
        
        //~60fps timer instead of keylistener
        timer = new javax.swing.Timer(16, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                maps[currentMap].getMapImage().setVisible(true);
                
                //chars current pos on map
                int charX = maps[currentMap].getCharacterX();
                int charY = maps[currentMap].getCharacterY();
                
                boolean showInteract = false;
                
                if (currentMap <= 1) {
                    for (MovingNPC npc : maps[currentMap].getMovingNpcs()) {
                        if (npc.getNpcX() >= npc.getMaxRight()
                                || npc.getNpcX() <= npc.getMaxLeft()) {
                            npc.setSpeed(-npc.getSpeed()); 
                        }

                        npc.setNpcX(npc.getNpcX() - npc.getSpeed());

                        int screenX = npc.getNpcX() + charX + charScreenX;
                        int screenY = npc.getNpcY() + charY + charScreenY;
                        npc.getImage().setLocation(screenX, screenY);
                    }
                }

                //checking if char is within npc
                for (NPC npc : maps[currentMap].getNpcs()) {
                    if (charX >= -npc.getNpcX() - tileSize && charX <= -npc.getNpcX() + tileSize &&
                        charY >= -npc.getNpcY() - tileSize && charY <= -npc.getNpcY() + tileSize) {
                        showInteract = true;
                        if (pressedKeys.contains(KeyEvent.VK_E)) {
                            if (currentMap == 4 && npc == maps[currentMap].getNpcs().get(1)) {
                                if (gameStage == 3) {
                                    timeStart=false;
                                    gameFrame.dispose();
                                    endFrame.setVisible(true);
                                    end.setText("<html>You returned the book in time and completed the game!</html>");
                                } else {
                                    inDialogue = true;
                                    dialogueText.setText(npc.getDialogue().get(gameStage));
                                    nameText.setText(npc.getName());
                                    dialoguePanel.setVisible(true);
                                }
                            } else {
                                inDialogue = true;
                                dialogueText.setText(npc.getDialogue().get(gameStage));
                                nameText.setText(npc.getName());
                                dialoguePanel.setVisible(true);
                            }
                        }
                    }
                }
                
                for (MovingNPC npc : maps[currentMap].getMovingNpcs()) {
                    if (charX >= -npc.getNpcX() - tileSize && charX <= -npc.getNpcX() + tileSize &&
                        charY >= -npc.getNpcY() - tileSize && charY <= -npc.getNpcY() + tileSize) {
                        inDialogue = true;
                        canExitDialogue = false;
                        switch (gameStage) {
                            case 2:
                            case 0:
                                dialogueText.setText(npc.getDialogue().get(gameStage));
                                nameText.setText(npc.getName());
                                dialoguePanel.setVisible(true);
                                new javax.swing.Timer(3000, new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        timer.stop();
                                        layeredPane.remove(maps[currentMap].getMapImage());
                                        for (NPC npc : maps[currentMap].getNpcs()) {
                                            layeredPane.remove(npc.getImage());
                                        }
                                        for (MovingNPC npc : maps[currentMap].getMovingNpcs()) {
                                            layeredPane.remove(npc.getImage());
                                        }
                                        dialoguePanel.setVisible(false);
                                        currentMap = 2;
                                        mapSize = 10*tileSize;   
                                        maps[currentMap].setCharacterX(maps[currentMap].getDoors().get(0).getDoorX());
                                        maps[currentMap].setCharacterY(maps[currentMap].getDoors().get(0).getDoorY());
                                        maps[currentMap].getMapImage().setVisible(true);
                                        maps[currentMap].getMapImage().setBounds(maps[currentMap].getCharacterX(), maps[currentMap].getCharacterY(), mapSize, mapSize);
                                        for (NPC npc : maps[currentMap].getNpcs()) {
                                            npc.getImage().setBounds(npc.getNpcX() - maps[currentMap].getCharacterX() + charScreenX, npc.getNpcY() - maps[currentMap].getCharacterY() + charScreenY, tileSize, tileSize);
                                            layeredPane.add(npc.getImage(), Integer.valueOf(3));
                                        }
                                        layeredPane.add(maps[currentMap].getMapImage(), Integer.valueOf(1));
                                        layeredPane.repaint();
                                        timer.start();
                                        
                                        inDialogue = false;
                                        canExitDialogue = true;
                                    }
                                }) {{
                                    setRepeats(false);
                                    start();
                                }};
                                break;
                            case 1:
                                dialogueText.setText(npc.getDialogue().get(gameStage));
                                nameText.setText(npc.getName());
                                dialoguePanel.setVisible(true);
                                canExitDialogue = true;
                                break;
                            case 3:
                                dialogueText.setText(npc.getDialogue().get(gameStage));
                                nameText.setText(npc.getName());
                                dialoguePanel.setVisible(true);
                                canExitDialogue = true;
                                break;     
                        }
                    }
                }

                //checking if char is within door
                for (Door door : maps[currentMap].getDoors()) {
                    if (charX >= door.getDoorX() - tileSize && charX <= door.getDoorX() + tileSize &&
                        charY >= door.getDoorY() - tileSize && charY <= door.getDoorY() + tileSize) {
                        showInteract = true;
                        if (pressedKeys.contains(KeyEvent.VK_E)  && !eKeyHeld) {
                            timer.stop();
                            layeredPane.remove(maps[currentMap].getMapImage());
                            eKeyHeld = true;
                            for (NPC npc : maps[currentMap].getNpcs()) {
                                layeredPane.remove(npc.getImage());
                            }
                            for (MovingNPC npc : maps[currentMap].getMovingNpcs()) {
                                layeredPane.remove(npc.getImage());
                            }
                            int nextDoor = door.getNextDoor();
                            currentMap = door.getNextMap();
                            switch(currentMap) {
                                case 0:
                                case 1:
                                    mapSize = 40*tileSize;
                                    break;
                                case 5:
                                case 4:
                                case 3:
                                case 2:
                                    mapSize = 10*tileSize;
                                    break;
                            }       
                            maps[currentMap].setCharacterX(maps[currentMap].getDoors().get(nextDoor).getDoorX());
                            maps[currentMap].setCharacterY(maps[currentMap].getDoors().get(nextDoor).getDoorY());
                            maps[currentMap].getMapImage().setVisible(true);
                            maps[currentMap].getMapImage().setBounds(maps[currentMap].getCharacterX(), maps[currentMap].getCharacterY(), mapSize, mapSize);
                            for (NPC npc : maps[currentMap].getNpcs()) {
                                npc.getImage().setBounds(npc.getNpcX() - maps[currentMap].getCharacterX() + charScreenX, npc.getNpcY() - maps[currentMap].getCharacterY() + charScreenY, tileSize, tileSize);
                                layeredPane.add(npc.getImage(), Integer.valueOf(3));
                            }
                            for (MovingNPC npc : maps[currentMap].getMovingNpcs()) {
                                npc.getImage().setBounds(npc.getNpcX() - maps[currentMap].getCharacterX() + charScreenX,
                                                        npc.getNpcY() - maps[currentMap].getCharacterY() + charScreenY,
                                                        tileSize, tileSize);
                                layeredPane.add(npc.getImage(), Integer.valueOf(3));
                            }
                            layeredPane.add(maps[currentMap].getMapImage(), Integer.valueOf(1));
                            layeredPane.repaint();
                            timer.start();
                        }
                    }
                }

                interact.setVisible(showInteract);
                
                //exit dialogue
                if (inDialogue && pressedKeys.contains(KeyEvent.VK_BACK_SPACE) && canExitDialogue) {
                    inDialogue = false;
                    dialoguePanel.setVisible(false);
                    switch(currentMap) {
                        case 2:
                            if (gameStage == 0) {
                                gameStage++;
                                inventory.put("attendance", new JLabel(new ImageIcon(new ImageIcon("attendance.png").getImage().getScaledInstance(tileSize*2, tileSize*2, Image.SCALE_DEFAULT))));
                                inventory.get("attendance").setAlignmentX(Component.CENTER_ALIGNMENT);
                                eastMenu.add(inventory.get("attendance"));
                            }
                            break;
                        case 5:
                            if (gameStage == 1) {
                                gameStage++;
                                eastMenu.remove(inventory.get("attendance"));
                                inventory.remove("attendance");
                                eastMenu.repaint();
                            }
                            break;
                        case 3:
                            if (gameStage == 2) {
                                gameStage++;
                                inventory.put("hallpass", new JLabel(new ImageIcon(new ImageIcon("hallpass.png").getImage().getScaledInstance(tileSize*2, tileSize*2, Image.SCALE_DEFAULT))));
                                inventory.get("hallpass").setAlignmentX(Component.CENTER_ALIGNMENT);
                                eastMenu.add(inventory.get("hallpass"));
                                eastMenu.remove(inventory.get("money"));
                                inventory.remove("money");
                                eastMenu.repaint();
                                timeStart = true;
                                inventory.put("book", new JLabel(new ImageIcon(new ImageIcon("book.png").getImage().getScaledInstance(tileSize*2, tileSize*2, Image.SCALE_DEFAULT))));
                                inventory.get("book").setAlignmentX(Component.CENTER_ALIGNMENT);
                                eastMenu.add(inventory.get("book"));
                                cursedPanel.setVisible(true);
                            }
                            break;
                    }
                }
                
                if(timeStart) {
                    time--;
                    cursedText.setText("<html>A cursed book has been placed in your inventory, you have " + time + " to return the book to the library.</html>");
                    if (time <= 0) {
                        timeStart=false;
                        gameFrame.dispose();
                        endFrame.setVisible(true);
                        end.setText("<html>You ran out of time and died.</html>");
                    }
                }
                
                //open pause menu
                if (pressedKeys.contains(KeyEvent.VK_ESCAPE) && !inDialogue) {
                    inPauseMenu = true;
                    pauseMenu.setVisible(true);
                }
                
                //no movement if in dialogue
                if (!inDialogue && !inPauseMenu)
                    movement();
            }
        });
        timer.start();

        menuFrame.setVisible(true);
        
        gameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gameFrame.setResizable(true);
        gameFrame.setFocusable(true);
        gameFrame.pack();
    }
    
    /**
     * Method Name: Movement
     * Description: Moves character if keys are being pressed and if stamina is being used
     */
    public static void movement() {
        //declarations
        boolean sprinting = pressedKeys.contains(KeyEvent.VK_SHIFT);
        boolean moving = false;
        int dx = 0, dy = 0;
        
        if (stamina == 0) {
            sprinting = false;
        }

        int speed = sprinting ? 8 : 4;

        //checking movement direction and putting it into temp dir variable
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

        //stamina system
        if (pressedKeys.contains(KeyEvent.VK_SHIFT) && moving) {
            stamina -= 1;
            if (stamina < 0) stamina = 0;
        } else {
            if (stamina < 100) stamina += 1;
        }

        int newCharX = maps[currentMap].getCharacterX() + dx;
        int newCharY = maps[currentMap].getCharacterY() + dy;
        
        int characterMapX = charScreenX - newCharX;
        int characterMapY = charScreenY - newCharY;

        //checks if next move is restricted
        if (canMoveTo(characterMapX, characterMapY, tileSize, tileSize, maps[currentMap])) {
            maps[currentMap].setCharacterX(newCharX);
            maps[currentMap].setCharacterY(newCharY);
            maps[currentMap].getMapImage().setLocation(newCharX, newCharY);
            
            //moves just npcs image
            for (NPC npc : maps[currentMap].getNpcs()) {
                int screenX = npc.getNpcX() + newCharX + charScreenX;
                int screenY = npc.getNpcY() + newCharY + charScreenY;
                npc.getImage().setLocation(screenX, screenY);
            }
            
        }
        
        staminaBar.setValue(stamina);
    }
    
    /**
     * Method Name: Fill Maps:
     * Description: Adds boundaries, NPCs, dialogue, and POIs to the map
     */
    public static void fillMaps() {
        maps[0] = new Map(0, 0, new JLabel(new ImageIcon(new ImageIcon("third.png").getImage().getScaledInstance((int)tileSize*40, (int)tileSize*40, Image.SCALE_DEFAULT))));

        maps[0].addWalkableArea(tileSize, (int)(tileSize*2.5), (int)(tileSize*20.5), (int)(tileSize*6.5));
        maps[0].addWalkableArea((int)(tileSize*9.2), (int)(tileSize*8), (int)(tileSize*4.1), (int)(tileSize*13.6));
        maps[0].addWalkableArea((int)(tileSize*6), (int)(tileSize*15), (int)(tileSize*5), (int)(tileSize*3.5));
        maps[0].addWalkableArea((int)(tileSize*10), (int)(tileSize*17.2), (int)(tileSize*11), (int)(tileSize*4.5));
        maps[0].addWalkableArea((int)(tileSize*14.17), (int)(tileSize*19), (int)(tileSize*4), (int)(tileSize*16));
        maps[0].addWalkableArea((int)(tileSize*4.6), (int)(tileSize*31.4), (int)(tileSize*17.5), (int)(tileSize*6));
        maps[0].addWalkableArea((int)(tileSize*2.5), (int)(tileSize*32), (int)(tileSize*4), (int)(tileSize*5.2)); 
        maps[0].addWalkableArea((int)(tileSize*16), (int)(tileSize*27.2), (int)(tileSize*5), (int)(tileSize*5.8));
        maps[0].addWalkableArea((int)(tileSize*18), (int)(tileSize*21.4), (int)(tileSize*4), (int)(tileSize*6));
        maps[0].addWalkableArea((int)(tileSize*18.3), (int)(tileSize*20), (int)(tileSize*1.9), (int)(tileSize*12));
   
        //stairs
        maps[0].addDoors((int)(tileSize*(-10)), (int)(tileSize*(2)),0 ,1 );
        maps[0].addDoors((int)(tileSize*(-16)), (int)(tileSize*(-19.5)),1 ,1 );
        maps[0].addDoors((int)(tileSize*(1)), (int)(tileSize*(-29.5)),2 ,1 );

        //rooms
        maps[0].addDoors((int)(tileSize*(1.8)), (int)(tileSize*2),0 ,2);
        maps[0].addDoors((int)(tileSize*(2)), (int)(tileSize*(-3)),0, 3);
        
        //hall monitors
        maps[0].addNpcs(10, (int)(tileSize), (int)(tileSize*(15)), (int)(tileSize), (int)(tileSize), "Hall Monitor", new JLabel(new ImageIcon(new ImageIcon("hallmonitor.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT))));
        maps[0].getMovingNpcs().get(0).addDialogue("<html>Why aren’t you in class? Get back!</html>");
        maps[0].getMovingNpcs().get(0).addDialogue("<html>I see you are bringing the attendence back, you're lucky this time.</html>");
        maps[0].getMovingNpcs().get(0).addDialogue("<html>Don't you know you need a hall pass now!</html>");
        maps[0].getMovingNpcs().get(0).addDialogue("<html>Where di you get that from?</html>");

        maps[0].addNpcs(10, (int)(tileSize*(-1)), (int)(tileSize*(15)), (int)(tileSize), (int)(tileSize*(29)), "Hall Monitor", new JLabel(new ImageIcon(new ImageIcon("hallmonitor.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT))));
        maps[0].getMovingNpcs().get(1).addDialogue("<html>Why aren’t you in class? Get back!</html>");
        maps[0].getMovingNpcs().get(1).addDialogue("<html>I see you are bringing the attendence back, you're lucky this time.</html>");
        maps[0].getMovingNpcs().get(1).addDialogue("<html>Don't you know you need a hall pass now!</html>");
        maps[0].getMovingNpcs().get(1).addDialogue("<html>Where di you get that from?</html>");

        
        maps[1] = new Map(0, 0, new JLabel(new ImageIcon(new ImageIcon("second.png").getImage().getScaledInstance((int)tileSize*40, (int)tileSize*40, Image.SCALE_DEFAULT))));

        maps[1].addWalkableArea(tileSize, (int)(tileSize*2.5), (int)(tileSize*20.5), (int)(tileSize*6.5));
        maps[1].addWalkableArea((int)(tileSize*9.2), (int)(tileSize*8), (int)(tileSize*4.1), (int)(tileSize*13.6));
        maps[1].addWalkableArea((int)(tileSize*6), (int)(tileSize*15), (int)(tileSize*5), (int)(tileSize*3.5));
        maps[1].addWalkableArea((int)(tileSize*10), (int)(tileSize*17.2), (int)(tileSize*11), (int)(tileSize*4.5));
        maps[1].addWalkableArea((int)(tileSize*14.17), (int)(tileSize*19), (int)(tileSize*6.5), (int)(tileSize*14));
        maps[1].addWalkableArea((int)(tileSize*4.6), (int)(tileSize*31.4), (int)(tileSize*14.67), (int)(tileSize*6));
        maps[1].addWalkableArea((int)(tileSize*2.5), (int)(tileSize*32), (int)(tileSize*4), (int)(tileSize*5.2)); 
        maps[1].addWalkableArea((int)(tileSize*16), (int)(tileSize*27.2), (int)(tileSize*3.3), (int)(tileSize*5.8));
        maps[1].addWalkableArea((int)(tileSize*18), (int)(tileSize*21.4), (int)(tileSize*4), (int)(tileSize*6));
//        maps[1].addWalkableArea((int)(tileSize*18.3), (int)(tileSize*20), (int)(tileSize*1.9), (int)(tileSize*12));
        
        //stairs
        maps[1].addDoors((int)(tileSize*(-10)), (int)(tileSize*2),0 ,0 );
        maps[1].addDoors((int)(tileSize*(-16)), (int)(tileSize*(-19.5)),1 ,0 );
        maps[1].addDoors((int)(tileSize*(1)), (int)(tileSize*(-29.5)),2 ,0 );
        
        //rooms 
        maps[1].addDoors((int)(tileSize*(-2)), (int)(tileSize*(-11.5)),0 ,4);
        maps[1].addDoors((int)(tileSize*(-13)), (int)(tileSize*(-28)),0, 5);
        
        //hall monitors
        maps[1].addNpcs(10, (int)(tileSize), (int)(tileSize*(14)), (int)(tileSize), (int)(tileSize), "Hall Monitor", new JLabel(new ImageIcon(new ImageIcon("hallmonitor.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT))));
        maps[1].getMovingNpcs().get(0).addDialogue("<html>Why aren’t you in class? Get back!</html>");
        maps[1].getMovingNpcs().get(0).addDialogue("<html>I see you are bringing the attendence back, you're lucky this time.</html>");
        maps[1].getMovingNpcs().get(0).addDialogue("<html>Don't you know you need a hall pass now!</html>");
        maps[1].getMovingNpcs().get(0).addDialogue("<html>Where di you get that from?</html>");

        maps[1].addNpcs(10, (int)(tileSize*(-1)), (int)(tileSize*(12)), (int)(tileSize), (int)(tileSize*(29)), "Hall Monitor", new JLabel(new ImageIcon(new ImageIcon("hallmonitor.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT))));
        maps[1].getMovingNpcs().get(1).addDialogue("<html>Why aren’t you in class? Get back!</html>");
        maps[1].getMovingNpcs().get(1).addDialogue("<html>I see you are bringing the attendence back, you're lucky this time.</html>");
        maps[1].getMovingNpcs().get(1).addDialogue("<html>Don't you know you need a hall pass now!</html>");
        maps[1].getMovingNpcs().get(1).addDialogue("<html>Where di you get that from?</html>");

        maps[2] = new Map(0, 0, new JLabel(new ImageIcon(new ImageIcon("csclass.png").getImage().getScaledInstance((int)tileSize*10, (int)tileSize*10, Image.SCALE_DEFAULT))));
        
        maps[2].addWalkableArea(0, 0, (int)(tileSize*7.8), (int)(tileSize*10.1));
        
        maps[2].addDoors((int)(tileSize*(-2)), (int)(tileSize*(4)),3 ,0);
        
        maps[2].addNpcs((int)(tileSize*(-1)), (int)(tileSize)*(-4), "Mr. Janicas", new JLabel(new ImageIcon(new ImageIcon("janicas.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT))));
        maps[2].getNpcs().get(0).addDialogue("<html>The attendance servers are down! Someone needs to hand-deliver this to the office—no excuses!</html>");
        maps[2].getNpcs().get(0).addDialogue("<html>You're still here? I told you to bring the attendance to the office.</html>");
        maps[2].getNpcs().get(0).addDialogue("<html>Thanks for bringing that attendance back.</html>");
        maps[2].getNpcs().get(0).addDialogue("<html>You should probably return that to the library, and fast!</html>");
        
        maps[3] = new Map(0, 0, new JLabel(new ImageIcon(new ImageIcon("bathroom.png").getImage().getScaledInstance((int)tileSize*10, (int)tileSize*10, Image.SCALE_DEFAULT))));
        
        maps[3].addWalkableArea(0, 0, (int)(tileSize*8), (int)(tileSize*10));
        
        maps[3].addDoors((int)(tileSize*(2.5)), (int)(tileSize*(-4)),4 ,0);
        
        maps[3].addNpcs((int)(tileSize*(-1)), (int)(tileSize)*(-4), "Bathroom Dweller", new JLabel(new ImageIcon(new ImageIcon("bathroomdweller.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT))));
        maps[3].getNpcs().get(0).addDialogue("<html>What do you want?</html>");
        maps[3].getNpcs().get(0).addDialogue("<html>Why are you bringing the attendance to the washroom?</html>");
        maps[3].getNpcs().get(0).addDialogue("<html>I heard you're looking for a hall pass, just give me that 20 and take mine.</html>");
        maps[3].getNpcs().get(0).addDialogue("<html>Where did that come from!</html>");
        
        maps[4] = new Map(0, 0, new JLabel(new ImageIcon(new ImageIcon("library.png").getImage().getScaledInstance((int)tileSize*10, (int)tileSize*10, Image.SCALE_DEFAULT))));
        
        maps[4].addWalkableArea(0, 0, (int)(tileSize*5.8), (int)(tileSize*10));
        
        maps[4].addDoors((int)(tileSize*(-0.2)), (int)(tileSize*(3.5)),3 ,1);
        
        maps[4].addNpcs((int)(tileSize*(-3.5)), (int)(tileSize*(-4.5)), "Librarian", new JLabel(new ImageIcon(new ImageIcon("librarian.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT))));
        maps[4].getNpcs().get(0).addDialogue("<html>Shouldn't you be in class right now?</html>");
        maps[4].getNpcs().get(0).addDialogue("<html>That attendance should probably be in the office.</html>");
        maps[4].getNpcs().get(0).addDialogue("<html>Make sure to return your books before the end of the semester.</html>");
        maps[4].getNpcs().get(0).addDialogue("<html>In all my years here I have never seen a book like that.</html>");
        
        maps[4].addNpcs((int)(tileSize*(-3.5)), (int)(tileSize*(5)), "Bookshelf", new JLabel(""));
        maps[4].getNpcs().get(1).addDialogue("<html>...</html>");
        maps[4].getNpcs().get(1).addDialogue("<html>...</html>");
        maps[4].getNpcs().get(1).addDialogue("<html>...</html>");
        maps[4].getNpcs().get(1).addDialogue("<html>...</html>");

        
        maps[5] = new Map(0, 0, new JLabel(new ImageIcon(new ImageIcon("office.png").getImage().getScaledInstance((int)tileSize*10, (int)tileSize*10, Image.SCALE_DEFAULT))));
        
        maps[5].addWalkableArea((int)(tileSize*4), 0, (int)(tileSize*6), (int)(tileSize*10));
        
        maps[5].addDoors((int)(tileSize*(0.5)), (int)(tileSize*(4)),4 ,1);
        
        maps[5].addNpcs((int)(tileSize*(2.5)), (int)(tileSize)*(-4), "Office Receptionist", new JLabel(new ImageIcon(new ImageIcon("officerecep.png").getImage().getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT))));
        maps[5].getNpcs().get(0).addDialogue("<html>Shouldn't you be in class right now?</html>");
        maps[5].getNpcs().get(0).addDialogue("<html>Thank you for bringing the attendance back, just so you know hall passes are now mandatory.</html>");
        maps[5].getNpcs().get(0).addDialogue("<html>Unfortunately, we are all out of hall passes, you will just have to find one.</html>");
        maps[4].getNpcs().get(0).addDialogue("<html>Where did you get that book from?</html>");
        
    }
    /**
     * Method Name: Can Move To
     * Description: Ensures player only moves within set boundaries
     * @param x
     * @param y
     * @param width
     * @param height
     * @param map
     * @return - boolean that if true, the player can walk there, if false they are blocked
     */
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