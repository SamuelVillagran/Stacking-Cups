package tower;


/**
 * Write a description of class TowerException here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TowerException extends Exception {
    private String message;
    public static final String DONT_EXISTS_CUP = "Don't exists a cup where put a new type of cup";
    
    public TowerException(String message) {
        super(message);
    }
}