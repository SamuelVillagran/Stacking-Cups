 
import java.util.ArrayList;
import shapes.Rectangle;

/**
 * It represents the lid of the cup
 * 
 * The lid is not created in the Cup constructor; it only exists after calling addLid(),
 * and from then on the cup takes care of showing, hiding, and moving it along with itself
 * 
 * @author Sanchez-Villagran
 */
public class Lid
{
    public static final int PIXELS_PER_CM = 10;
    private int id;
    private int height;
    private int width;
    private String color;
    private int xPosition;
    private int yPosition;
    private Rectangle shape;

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
    public void makeVisible(){
        shape.makeVisible();
    }
    
    /**
     * Hide the lid
     */
    public void makeInvisible(){
        shape.makeInvisible();
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
    
    public int getSize(){
         return 2 * (id - 1);
    }
    
    /**
     * Create a rectangle with 
     */
    private void createShape(){
        int widthPixels = width * PIXELS_PER_CM;
        int heightPixels = height * PIXELS_PER_CM;
        shape = new Rectangle(xPosition, yPosition, widthPixels, heightPixels, color);
    }
}
