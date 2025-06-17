
package kci.simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Door {
    private int doorX, doorY, nextDoor, nextMap;

    public Door(int doorX, int doorY, int nextDoor, int nextMap) {
        this.doorX = doorX;
        this.doorY = doorY;
        this.nextDoor = nextDoor;
        this.nextMap = nextMap;
    }

    public int getDoorX() {
        return doorX;
    }

    public void setDoorX(int doorX) {
        this.doorX = doorX;
    }

    public int getDoorY() {
        return doorY;
    }

    public void setDoorY(int doorY) {
        this.doorY = doorY;
    }

    public int getNextDoor() {
        return nextDoor;
    }

    public void setNextDoor(int nextDoor) {
        this.nextDoor = nextDoor;
    }

    public int getNextMap() {
        return nextMap;
    }

    public void setNextMap(int nextMap) {
        this.nextMap = nextMap;
    }


    
    
}
