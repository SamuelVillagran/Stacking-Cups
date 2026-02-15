package domain;
import java.util.ArrayList;
import shapes.Rectangle;


/**
 * Write a description of class Cup here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Cup
{
    public static final int PIXELS_PER_CM = 10;
    private ArrayList<Rectangle> shape;
    private int id;
    private int height;
    private int width;
    private String color;
    private int xPosition; //pixels
    private int yPosition; //pixels
    private boolean visible;
    
    /**
     * Constructor for objects of class Cup
     * @param id The rectangle's number, this number will determinate the height and the width
     * @param color Color wished
     */
    public Cup(int id, String color) {
        this.id = id;
        height = getSize();
        width = getSize();
        this.color = color;
        xPosition = 0;
        yPosition = 0;
        visible = true;
        shape = new ArrayList<>();
        makeCup();
    }
    
    /**
     * Draw the cup in canvas
     */
    public void makeVisible(){
        for(Rectangle r : shape){
            r.makeVisible();
        }
    }
    
    /**
     * Erase the cup in canvas
     */
    public void makeInvisible(){
        for(Rectangle r : shape){
            r.makeInvisible();
        }
    }
    
    public int getSize(){
        return 2 * (id - 1);
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
     * Move the cup newX pixels horizontal and newY vertical
     */
    public void move(int newX, int newY) {
        int dx = newX - xPosition;
        int dy = newY - yPosition;
        for(Rectangle r : shape){
            r.moveHorizontal(dx);
            r.moveVertical(dy);
        }
    }
    
    /**
     * Build the cup with Rectangles
     */
    private void makeCup(){
        int heightPixels = PIXELS_PER_CM * height;
        int widthPixels = PIXELS_PER_CM * width;
        
        Rectangle left = new Rectangle(xPosition, yPosition, PIXELS_PER_CM, heightPixels, color);
        Rectangle right = new Rectangle(xPosition + widthPixels - PIXELS_PER_CM, yPosition,
        PIXELS_PER_CM, heightPixels, color);
        Rectangle down = new Rectangle(xPosition, yPosition + heightPixels - PIXELS_PER_CM,
        widthPixels , PIXELS_PER_CM, color);
        shape.add(left);
        shape.add(right);
        shape.add(down);
    }
}