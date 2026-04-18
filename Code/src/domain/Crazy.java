package domain;


/**
 * Write a description of class Crazy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Crazy extends Lid {
            
    /**
     * Constructor for objects of class Lid
     */
    public Crazy(int id, int xPos, int yPos, String color) {
        super(id, xPos, yPos, color);
    }
    
     /**
     * Give lid's type
     * @return String "crazy"
     */
    public String getType() {
        return "lid/crazy";
    }
}
