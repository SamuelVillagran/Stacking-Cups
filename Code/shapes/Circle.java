package shapes;

import java.awt.*;
import java.awt.geom.*;

/**
 * A circle that can be manipulated and that draws itself on a canvas.
 * 
 * @author  Michael Kolling and David J. Barnes
 * @version 1.0.  (15 July 2000) 
 */

public class Circle extends Figure{

    public static final double PI=3.1416;
    private int diameter;

    public Circle() {
        super(50, 50, "blue");
        diameter = 30;
        isVisible = false;
    }

    @Override
    protected void draw(){
        if(isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.draw(this, color, 
                new Ellipse2D.Double(xPosition, yPosition, 
                diameter, diameter));
            canvas.wait(10);
        }
    }
    
    public void changeSize(int newDiameter) {
        erase();
        diameter = newDiameter;
        draw();
    }
    
}
