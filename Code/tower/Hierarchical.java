package tower;


/**
 * Write a description of class Hierarchical here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Hierarchical extends Cup {
    
    /**
     * Constructor for objects of class Cup Hierarchical
     * @param id The rectangle's number, this number will determinate the height and the width
     * @param color Color wished
     */
    public Hierarchical(int id, String color) {
        super(id, color);
    }
    
    /**
     * Constructor to create a Cup Hierarchical on a specific position
     * @param id The rectangle's number, this number will determinate the height and the width
     * @param xPos The position of cup at the X coordenade
     * @param yPos The position of cup at the Y coordenade
     * @param color Color wished to put at the cup
     */
    public Hierarchical(int id, int xPos, int yPos, String color) {
        super(id, xPos, yPos, color);
    }
    
    
}
