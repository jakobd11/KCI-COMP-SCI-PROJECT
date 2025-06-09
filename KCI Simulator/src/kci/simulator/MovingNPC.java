
package kci.simulator;

import java.util.ArrayList;
import javax.swing.JLabel;

public class MovingNPC extends NPC {
    private int speed, maxX, maxY;

    public MovingNPC(int speed, int maxX, int maxY, int npcX, int npcY, String name, JLabel image) {
        super(npcX, npcY, name, image);
        this.speed = speed;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getNpcX() {
        return npcX;
    }

    public void setNpcX(int npcX) {
        this.npcX = npcX;
    }

    public int getNpcY() {
        return npcY;
    }

    public void setNpcY(int npcY) {
        this.npcY = npcY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JLabel getImage() {
        return image;
    }

    public void setImage(JLabel image) {
        this.image = image;
    }
    
    public void addDialogue(String lines) {
        dialogue.add(lines);
    }

    public ArrayList<String> getDialogue() {
        return dialogue;
    }

    public void setDialogue(ArrayList<String> dialogue) {
        this.dialogue = dialogue;
    }
    
    
}
