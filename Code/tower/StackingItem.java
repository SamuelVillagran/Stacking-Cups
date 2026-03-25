package tower;


/**
 * Write a description of class StackingItems here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public abstract class StackingItem {
    
    protected int id;
    protected int height;
    protected int width;
    protected String color;
    protected int xPosition;
    protected int yPosition;
    
    public int getId(){
        return id;
    }
    
    public int getHeight(){
        return height;
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getYPosition(){
        return yPosition;
    }
    
    public int getXPosition() {
        return xPosition;
    }
    
    public String getColor(){
        return color;
    }
    
    public int getSize(){
         return 2 * id - 1;
    }
    
    public abstract Lid getLid();
    
    public abstract void removeLid();
    
    
    /**
     * Returns true if this piece has an interior where other pieces can land.
     * Cups return ture and Lids return false;
     */
    public abstract boolean hasInterior();
    
    /**
     * Returns true if this piece blocks a falling piece of the fiven width.
     * Lids always blocks, they are always flat figures, nothing passes trhough them.
     * Cups blocks if their width < fallingWidth. 
     */
    public abstract boolean blocksPassage(int fallingWidth);
    
    /**
     * Returns true if this piece can be like a container for a falling piece of the giving width.
     * A cup can be a container if its width > fallingWidth.
     * Lids never can contain.
     */
    public abstract boolean canContain(int fallingWidth);
    public abstract void makeVisible();
    public abstract void makeInvisible();
    public abstract void erase();
    public abstract void move(int x, int y);
}
