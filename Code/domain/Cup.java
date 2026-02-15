package domain;
import java.util.ArrayList;


/**
 * Write a description of class Cup here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Cup
{
    private int id;
    private int height;
    private int width;
    private int color;
    private int xPosition;
    private int yPosition;
    private boolean visible;
    private ArrayList<Square> shape;
    
    
    /**
     * Constructor for objects of class Cup
     */
    public Cup() {
        
    }

    /**
     * 
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * 
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * 
     */
    public void move(int x, int y) {
        
    }
}