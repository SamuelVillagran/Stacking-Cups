package domain;
import java.util.ArrayList;
import shapes.Rectangle;


/**
 * It represents a cup drawn with rectangles
 * It is constructed with an arraylist of three triangles;
 * one left, one right and one bottom, forming a “U” shape
 * 
 * @author Sanchez-Villagran 
 * 
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
    private Lid lid;
    
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
        makeVisible();
    }
    
    /**
     * Draw the cup
     */
    public void makeVisible(){
        for(Rectangle r : shape){
            r.makeVisible();
        }
        if(lid != null){
            lid.makeVisible();
        }
    }
    
    /**
     * Hide the cup
     */
    public void makeInvisible(){
        for(Rectangle r : shape){
            r.makeInvisible();
        }
        
        if(lid != null){
            lid.makeInvisible();
        }
    }
    
    /**
     * Add a lip in the top of the cup
     */
    public void addLid(){
        if(lid == null){
            lid = new Lid(id, xPosition, yPosition, color);
        }
    }

    public int getSize(){
         return 2 * (id-1);
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
    public String getColor(){
        return color;
    }
    
    /**
     * 
     */
    public int getXPosition(){
        return xPosition;
    }
    
    /**
     * 
     */
    public int getYPosition(){
        return yPosition;
    }
    
    /**
     * Move the cup newX pixels horizontal and newY vertical
     */
    public void move(int newX, int newY) {
        int dx = newX - xPosition;
        int dy = newY - yPosition;
        xPosition = newX;
        yPosition = newY;
        for(Rectangle r : shape){
            r.moveHorizontal(dx);
            r.moveVertical(dy);
        }
        
        if(lid != null){
            lid.move(newX, newY);
        }
    }
    
    public int getId(){
        return id;
    }
    
    /**
     * Build the cup with Rectangles
     * To take way cup it makes three Rectangles
     * 
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
    
    /**
     * Erase all cup's shape from Canvas
     */
    public void erase() {
        for (Rectangle s : shape) {
            s.erase();
        }
    }
}