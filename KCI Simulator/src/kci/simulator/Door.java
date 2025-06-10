
package kci.simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Door {
    private int doorX, doorY, next;

    public Door(int doorX, int doorY, int next) {
        this.doorX = doorX;
        this.doorY = doorY;
        this.next = next;
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

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }
    
    
    
}
