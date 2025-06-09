


package kci.simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class NPC{
    protected int npcX, npcY;
    protected String name;
    protected JLabel image;
    protected ArrayList<String> dialogue = new ArrayList<>();

    public NPC(int npcX, int npcY, String name, JLabel image, ArrayList<String> dialogue) {
        this.npcX = npcX;
        this.npcY = npcY;
        this.name = name;
        this.image = image;
        this.dialogue = dialogue;
    }

    public int getNpcX() {
        return npcX;
    }

    public int getNpcY() {
        return npcY;
    }

    public String getName() {
        return name;
    }

    public JLabel getImage() {
        return image;
    }
    
    public void setNpcX(int npcX) {
        this.npcX = npcX;
    }

    public void setNpcY(int npcY) {
        this.npcY = npcY;
    }

    public void setName(String name) {
        this.name = name;
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
