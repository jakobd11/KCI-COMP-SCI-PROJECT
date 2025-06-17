
package kci.simulator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
public class Item {
    private String name;
    private String image;

    public Item(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    
    
    
}
