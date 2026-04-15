package domain;

 
import java.util.ArrayList;

import presentation.Rectangle;

/**
 * It represents the lid of the cup
 * 
 * The lid is not created in the Cup constructor; it only exists after calling addLid(),
 * and from then on the cup takes care of showing, hiding, and moving it along with itself
 * 
 * @author Sanchez-Villagran
 */
public class Lid extends StackingItem {
    public static final int PIXELS_PER_CM = 10;
    protected Rectangle shape;

    /**
     * Constructor for objects of class Lid
     */
    public Lid(int id, int xPos, int yPos, String color) {
        this.id = id;
        this.color = color;
        width = getSize();
        height = 1;
        xPosition = xPos;
        yPosition = yPos;
        createShape();
    }
    
    /**
     * Move the lid 'x' units horizontal and 'y' units vertical
     */
    @Override
    public void move(int x, int y) {
        int dx = x - xPosition;
        int dy = y - yPosition;
        xPosition = x;
        yPosition = y;
        shape.moveHorizontal(dx);
        shape.moveVertical(dy);
    }
    
    /**
     * Draw the lid
     */
    @Override
    public void makeVisible(){
        shape.makeVisible();
    }
    
    /**
     * Hide the lid
     */
    @Override
    public void makeInvisible(){
        shape.makeInvisible();
    }
    
    /**
     * Erase the lid's shape
     */
    @Override
    public void erase() {
        shape.erase();
    }
    
    /**
     * Get size going to be this lid
     */
    public int getSize(){
         return 2 * id - 1;
    }
    
    /**
     * A lid doesn't hace interior, nothin can land inside it.
     */
    @Override
    public boolean hasInterior(){
        return false;
    }
    
    /**
     * A Lid always blocks any falling piece
     */
    @Override
    public boolean blocksPassage(int fallingWidth){
        return true;
    }
    
    /**
     * A Lid does not have interior, it can't be a container.
     */
    @Override
    public boolean canContain(int fallingWidth){
        return false;
    }
    
    public Lid getLid() {
        return this;
    }
    
    public void removeLid() {
        shape.erase();
        shape = null;
    }
    
    /**
     * Give lid's type
     * @return String "normal"
     */
    public String getType() {
        return "normal";
    }
    
    /*
     * Create a rectangle with 
     */
    private void createShape(){
        int widthPixels = width * PIXELS_PER_CM;
        int heightPixels = height * PIXELS_PER_CM;
        shape = new Rectangle(xPosition, yPosition, widthPixels, heightPixels, color);
    }
}
