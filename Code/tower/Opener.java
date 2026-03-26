package tower;


/**
 * Write a description of class Opener here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Opener extends Cup {
    
    /**
     * Constructor for objects of class Cup Opener
     * @param id The rectangle's number, this number will determinate the height and the width
     * @param color Color wished
     */
    public Opener(int id, String color) {
        super(id, color);
        this.lid = null;
    }
    
    /**
     * Constructor to create a Cup Opener on a specific position
     * @param id The rectangle's number, this number will determinate the height and the width
     * @param xPos The position of cup at the X coordenade
     * @param yPos The position of cup at the Y coordenade
     * @param color Color wished to put at the cup
     */
    public Opener(int id, int xPos, int yPos, String color) {
        super(id, xPos, yPos, color);
    }
    
    
}