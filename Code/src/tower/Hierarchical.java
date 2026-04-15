package tower;


/**
 * Write a description of class Hierarchical here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Hierarchical extends Cup {
    private boolean fixed;
    
    /**
     * Constructor for objects of class Cup Hierarchical
     * @param id The rectangle's number, this number will determinate the height and the width
     * @param color Color wished
     */
    public Hierarchical(int id, String color) {
        super(id, color);
        this.fixed = false;
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
        this.fixed = false;
    }
    
    public void setFixed(boolean fixed){
        this.fixed = fixed;
    }
    
    /**
     * Check if the element es fixed.
     * return true if it is in bottom tower, otherwise returns false.
     */
    @Override
    public boolean isFixed(){
        return fixed;
    }
    
     /**
     * Give lid's type
     * @return String "hierarchical"
     */
    @Override
    public String getType() {
        return "hierarchical";
    }
}