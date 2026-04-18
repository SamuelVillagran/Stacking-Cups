package domain;


/**
 * Write a description of class Fearful here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Fearful extends Lid {
    
    /**
     * Constructor for objects of class Lid
     */
    public Fearful(int id, int xPos, int yPos, String color) {
        super(id, xPos, yPos, color);
    }
    
    // id : int, xPos : int, yPos : int, color : String
     /**
     * Give lid's type
     * @return String "fearful"
     */
    @Override
    public String getType() {
        return "fearful";
    }
    
    @Override 
    public void removeLid() {
        return;
    }
}
