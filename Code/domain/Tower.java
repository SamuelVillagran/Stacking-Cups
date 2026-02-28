package domain;

import shapes.Rectangle;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;


/**
 * Write a description of class Tower here.
 * 
 * @author Sanchéz - Villagrán
 * @version 1.0.0
 */
public class Tower {
    
    public static ArrayList<String> COLORS = new ArrayList<>();
    public static ArrayList<Rectangle> ruler;
    private int width;
    private int maxHeight;
    private boolean lastOK;
    private ArrayList<Cup> cups;
    private boolean isVisible;
    
    static {
        for (FigureColor fc : FigureColor.values()) {
            COLORS.add(fc.name()); // ayudado con IA
        }
    }

    /**
     * Constructor for objects of class Tower.get
     */
    public Tower(int width, int maxHeight) {
        if (invariant(width, maxHeight)) {
            this.width = width;
            this.maxHeight = maxHeight;
            cups = new ArrayList<>();
            generateRuler();
        } else if (!invariant(width, maxHeight) && isVisible) {
            errorMessage();
        }
    }

    /**
     * Add a cup if is possible.
     * @param int Integer is the id of cup where this going to push at the list cups.
     */
    public void pushCup(int number) {
        boolean isCupsEmpty = cups.size() == 0 || cups == null ? true : false;
        Cup newCup = null;
        if (isCupsEmpty) {
            isVisible = true;
            newCup = new Cup(number, 160, 20, getRandColor());
        }
        if (!isCupsEmpty) {
            int lastIndex = cups.size() - 1;
            Cup lastCup = cups.get(lastIndex);
            int nXPos = lastCup.getXPosition()-((lastCup.getWidth()*Cup.PIXELS_PER_CM)/2);
            int nYPos = lastCup.getYPosition()-((lastCup.getWidth()*Cup.PIXELS_PER_CM)/2);
            if (nYPos > 0 && nXPos > 0) {
                newCup = new Cup(number, nXPos,
                nYPos, getRandColor());
            } else {
                nYPos = lastCup.getYPosition()-Cup.getPixelsPerCm();
                nXPos = lastCup.getXPosition()-Cup.getPixelsPerCm();
                newCup = new Cup(number, nXPos,
                nYPos, getRandColor());
            } 
            
            int towerHeight = heightUsed();
            int cupHeight = newCup.getHeight();
            if (!invariant2(towerHeight, cupHeight)) {
                newCup.erase();
                errorMessage();
                return;
            }
        }
        
        
        newCup.makeVisible();
        cups.add(newCup);
        
        if (isVisible) {
            makeVisibleRuler();
        }
        newCup = null;
    }
    
    /**
     * Delete the last cup at the cups list.
     */
    public void popCup(){
        boolean cupsAreEmpty = cups.isEmpty();
        if(!cupsAreEmpty){
            Cup removed = cups.remove(cups.size() - 1);
            removed.erase();
        }
        
    }
    
    /**
     * Remove a especific cup, the cup that is at the j index of list.
     */
    public void removeCup(int j){
        int idCurrentCup, lenCups;
        Cup currentCup;
        lenCups = cups.size();
        for(int i = 0; i< lenCups; i++){
            currentCup = cups.get(i);
            idCurrentCup = currentCup.getID();
            if(idCurrentCup == j){
                Cup removed = cups.remove(i);
                removed.erase();
            }
        }
        currentCup = null;        
    }
    
    /**
     * Put a lid of a specific cup, cup at the i index of cups list.
     * @param int Index of cup that it's going to add a lid. 
     */
    public void pushLid(int i) {
        Cup currentCup;
        for(int j = 0; j< cups.size(); j++){
            currentCup = cups.get(j);
            if(currentCup.getID() == i){
                currentCup.addLid();
            }
        }
        currentCup = null;
    }
    
    public void popLid(){
        if(!cups.isEmpty()){
            
            //cups.get()
        }
    }
    
    /**
     * 
     */
    public void removeLid(int i) {
        
    }
    
    /**
     * 
     */
    public void orderTower() {
        
    }
    
    /**
     * 
     */
    public void reverseTower() {
        
    }
    
    /**
     * Get the max height of tower
     * @return int this is the max height of tower
     */
    public int height() {
        return maxHeight;
    }
    
    /**
     * 
     */
    public int[] lidedCups() {
        return new int[] {0, 0};
    }
    
    /**
     * 
     */
    public String[][] stackingitems() {
        return new String[][] {{""}};
    }
    
    /**
     * Make all cups visible
     */
    public void makeVisible() {
        if (!isVisible) isVisible = !isVisible;
        for (Cup c : cups) {
            c.makeVisible();
        }
    }
    
    /**
     * Make all cups invisible
     */
    public void makeInvisible() {
        if (isVisible) isVisible = !isVisible;
        for (Cup c : cups) {
            c.makeInvisible();
        }
    }
    
    /**
     * Close the game's window
     */
    public void exit() {
        isVisible = false;
        System.exit(0);
    }
    
    /**
     * 
     */
    public boolean ok() {
        return true;
    }
    
    /*
     * Generate a random color of list COLORS
     * 
     */
    private String getRandColor(){
        Random random = new Random();
        int randIndexColor = random.nextInt(COLORS.size());
        String color = COLORS.get(randIndexColor);
        COLORS.remove(color);
        return color;
    }
    
    /*
     * Get the height used
     * @return int this is the the height is using the cups at this tower
     */
    private int heightUsed(){
        int total = 0;
        for(Cup c : cups){
            total += c.getHeight();
        }
        return total;
    }
    
    /*
     * Invariant of Stacking Cups
     * Comprove if height and weigth are positive integers
     * @param width It's the width of tower that it's going to comprove if this integer is more than 0
     * @param height It's the height of tower that it's going to comprove if this integer is more than 0
     */
    private boolean invariant(int width, int height) {
        return (height > 0 && width > 0) ? true : false;
    }
    
    /*
     * Second invariant of game
     */
    private boolean invariant2(int towerHeight, int cupHeight) {
        return towerHeight + cupHeight < maxHeight ? true : false;
    }
    
    /*
     * Error message this going to apperear at the screen
     * This only apper if simulator is visible
     */
    private void errorMessage() {
        if (isVisible) {
            JOptionPane.showMessageDialog( 
            null, 
            "Action not allowed",  // Mensaje de la ventana
            "Invalid Action",  //Mensaje del titulo de la ventana
            JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /*
     * Generate the ruler of StackingCups
     */
    private void generateRuler() {
        int DISTANCE, NITERATIONS, width, height, currentPosX, currentPosY;
        DISTANCE = 10;
        NITERATIONS = 40;
        currentPosX = 0;
        currentPosY = 0;
        width = 10;
        height = 3;
        ruler = new ArrayList<>();
        for (int i = 0; i < NITERATIONS; i++) {
            // To create a rectangle this has:
            // int xPos,int yPos, int width, int height, String color
            Rectangle currentRectangle = new Rectangle(currentPosX, currentPosY, width,
                height, "BLACK");
            ruler.add(currentRectangle);
            currentPosY += DISTANCE;
            
        }
    }
    
    /*
     * Make visible the ruler of Stacking Cups
     */
    private void makeVisibleRuler() {
        for (Rectangle r : ruler) {
            r.makeVisible();
        }
    }
}
