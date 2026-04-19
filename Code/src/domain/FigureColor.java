package domain;
import java.util.ArrayList;


/**
 * This are the diferent figure's color that can have.
 * 
 * @author Sanchez - Villagrán
 * @version 1.0.0
 */
public enum FigureColor {
    RED, BLACK, BLUE, YELLOW, MAGENTA,  ORANGE, PINK, CYAN, GRAY, 
    LIGHTGRAY, DARKGRAY, BROWN, MAROON, GOLD, 
    DARKYELLOW, GREENTINT, SALMON, DARKRED, HARDGRAY, 
    SOFTGRAY, VIOLET, NAVY, OLIVE, PURPLE, 
    SILVER, LIME, TEAL, AQUA, SKYBLUE, 
    CHOCOLATE, BEIGE; // Some colors are generated with IA
    
    public static ArrayList<String> getStringColor() {
        ArrayList<String> colorsInString = new ArrayList<>();
        for (FigureColor fc : FigureColor.values()) {
            colorsInString.add(fc.name()); // ayudado con IA
        }
        return colorsInString;
    }
}
