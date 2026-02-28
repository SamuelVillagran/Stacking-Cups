package domain;

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
    private int width;
    private int maxHeight;
    private boolean lastOK;
    private ArrayList<Cup> cups;
    private ArrayList<Rectangle> ruler;
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
        } else if (!invariant(width, maxHeight)) {
            errorMessage();
        }
    }

    /**
     * Add a cup if is possible.
     * @param int Integer is the id of cup where this going to push at the list cups.
     */
    public void pushCup(int number) {
        Cup newCup = new Cup(number, getRandColor());
        int towerHeight = heightUsed();
        int cupHeight = newCup.getHeight();
        if(towerHeight + cupHeight < maxHeight){
            cups.add(newCup);
        } 
        newCup = null;
        
        return;
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
                removed.makeInvisible();
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
            
            cups.get()
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
     */
    private boolean invariant(int weigth, int height) {
        return (height > 0 && weigth > 0) ? true : false;
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
    
}
