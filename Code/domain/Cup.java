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
public class Cup extends StackingItem {
    public static final int PIXELS_PER_CM = 10;
    private ArrayList<Rectangle> shape;
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
        color = color;
        xPosition = 0;
        yPosition = 0;
        makeCup();
    }
    
    /**
     * Constructor to create a Cup on a specific position
     * @param id The rectangle's number, this number will determinate the height and the width
     * @param xPos The position of cup at the X coordenade
     * @param yPos The position of cup at the Y coordenade
     * @param color Color wished to put at the cup
     */
    public Cup(int id, int xPos, int yPos, String color) {
        this.id = id;
        height = getSize();
        width = getSize();
        this.color = color;
        xPosition = xPos;
        yPosition = yPos;
        visible = true;
        makeCup();
    }
    
    public static int getPixelsPerCm() {
        return PIXELS_PER_CM;
    }
    
    /**
     * Draw the cup
     */
    @Override
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
    @Override
    public void makeInvisible(){
        for(Rectangle r : shape){
            r.makeInvisible();
        }
        
        if(lid != null){
            lid.makeInvisible();
        }
    }
    
    /**
     * A Cup has an interior where other pieces can land.
     */
    @Override
    public boolean hasInterior(){
        return true;
    }
    
    /**
     * A cup can blocks a falling piece if the Cup's wisth is less than or equals to the falling piece's width.
     */
    @Override
    public boolean blocksPassage(int fallingWidth){
        return this.width <= fallingWidth;
    }
    
    /**
     * A cup can contain a falling piece if the Cup's width is exactly greater than the fallings piece's width.
     */
    @Override
    public boolean canContain(int fallingWidth){
        return this.width > fallingWidth;
    }
    
    /**
     * Add a lip in the top of the cup
     */
    public void addLid(){
        if(lid == null){
            lid = new Lid(id, xPosition, yPosition - PIXELS_PER_CM, color);
        }
    }
    
    /**
     * Make null the cup's lid if this have it
     * And erase the shape of lid from canvas
     */
    public void removeLid() {
        if(lid != null){
            lid.erase();
            lid = null;
        } 
    }

    public int getSize(){
         return (2 * id) -1;
    }
    
    @Override
    public Lid getLid() {
        return lid;
    }
    
    @Override
    public StackingItem.PieceType getType(){
        return StackingItem.PieceType.CUP;
    }
    
    /**
     * Move the cup newX pixels horizontal and newY vertical
     */
    @Override
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
    
    /**
     * Erase all cup's shape from Canvas
     */
    @Override
    public void erase() {
        for (Rectangle s : shape) {
            s.erase();
        }
    }
    
    public void setPosition(int xPos, int yPos) {
        xPosition = xPos;
        yPosition = yPos;
    }
    
    public void setHeight(int yPos){
        yPosition = yPos;
    }
    
    public String toString() {
        return "cup";
    }
    
    
    /*
     * Build the cup with Rectangles
     * To take way cup it makes three Rectangles
     * 
     */
    private void makeCup(){
        shape = new ArrayList<>();
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
