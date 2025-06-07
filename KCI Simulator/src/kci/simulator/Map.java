
package kci.simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Map {
    
    protected int characterX, characterY;
    protected JLabel mapImage;
    protected ArrayList<NPC> npcs = new ArrayList<>();
    protected ArrayList<Door> doors = new ArrayList<>();

    public Map() {}
    
    public Map(int characterX, int characterY, JLabel mapImage, ArrayList<NPC> npcs, ArrayList<Door> doors) {
        this.characterX = characterX;
        this.characterY = characterY;
        this.mapImage = mapImage;
        this.npcs = npcs;
        this.doors = doors;
    }

    public int getCharacterX() {
        return characterX;
    }

    public void setCharacterX(int characterX) {
        this.characterX = characterX;
    }

    public int getCharacterY() {
        return characterY;
    }

    public void setCharacterY(int characterY) {
        this.characterY = characterY;
    }

    public JLabel getMapImage() {
        return mapImage;
    }

    public void setMapImage(JLabel mapImage) {
        this.mapImage = mapImage;
    }

    public void addNpcs (int npcX, int npcY, String name, JLabel image, ArrayList<String> dialogue) {
        npcs.add(new NPC(npcX,npcY,name,image,dialogue));
    }
    
    public void addDoors (int doorsX, int doorsY) {
        doors.add(new Door(doorsX,doorsY));
    }
    
//    public ArrayList<NPC> getNpcs() {
//        return npcs;
//    }
//
//    public void setNpcs(ArrayList<NPC> npcs) {
//        this.npcs = npcs;
//    }
//
//    public ArrayList<Door> getDoors() {
//        return doors;
//    }
//
//    public void setDoors(ArrayList<Door> doors) {
//        this.doors = doors;
//    }
    
    
}
