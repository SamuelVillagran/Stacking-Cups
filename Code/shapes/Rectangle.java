package shapes;

/**
 * A rectangle that can be manipulated and that draws itself on a canvas.
 * 
 * @author  Michael Kolling and David J. Barnes (Modified)
 * @version 1.0  (15 July 2000)()
 */

public class Rectangle extends Figure{

    public static final int EDGES = 4;
    
    private int height;
    private int width;

    /**
     * Create a new rectangle at default position with default color.
     */
    public Rectangle(){
        super(50, 50, "magenta");
        height = 40;
        width = 40;
        isVisible = false;
    }
    
    /**
     * Create a specific rectangle
     */
    public Rectangle(int xPos,int yPos, int width, int height, String color){
        super(xPos, yPos, color);
        this.height = height;
        this.width = width;
        isVisible = false;
    }
    
    /**
     * Draw the rectangle with current specifications on screen.
     */
    @Override
    protected void draw() {
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color,
                new java.awt.Rectangle(xPosition, yPosition, 
                                       width, height));
            canvas.wait(10);
        }
    }
    
    public void changeSize(int newHeight, int newWidth) {
        erase();
        height = newHeight;
        width = newWidth;
        draw();
    }
    
    public int getHeight(){
        return this.height;
    }
    
    public int getWidht(){
        return this.width;git
    }
}

