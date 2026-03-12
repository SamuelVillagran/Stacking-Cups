package domain;


/**
 * Represents the idea of elements in Tower.
 *
 * @ Sanchez - Villagrán
 */
public abstract class StackingItem
{
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
    
    public String getColor(){
        return color;
    }
    
    public int getSize(){
         return 2 * id - 1;
    }
    
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
    
    /**
     * Draw the stack item.
     */
    public abstract void makeVisible();
    
    /**
     * Hide the stack item.
     */
    public abstract void makeInvisible();
    
    /**
     * Erase all stak item's shape from Canvas.
     */
    public abstract void erase();
    
    /**
     * Move the lid 'x' units horizontal and 'y' units vertical.
     */
    public abstract void move(int x, int y);
}