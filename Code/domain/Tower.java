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
    
    private static ArrayList<String> COLORS = new ArrayList<>();
    private static ArrayList<Rectangle> ruler;
    private int width;
    private int maxHeight;
    private boolean lastOK;
    private ArrayList<Cup> cups;
    private boolean isVisible;
    private ArrayList<Lid> lids;
    private int xCenter;
    private Cup lastCup;
    private int heightBase;
    private int heightCups;
    
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
            inicializate(width, maxHeight);
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
                if (isVisible) errorMessage();
                
                return;
            }
        }
        
        newCup.makeVisible();
        cups.add(newCup);
        
        if (isVisible) {
            makeVisibleRuler();
        }
        
    }
    
    /**
     * Add a cup if is possible.
     * @param number Is the n value cup.
     * @param isWorking Indicates if the method has to work.
     */
    public void pushCup(int number, boolean isworking){
        int newHeight = 2 * number - 1;
        int xPos = xCenter * Cup.getPixelsPerCm() - (newHeight * Cup.getPixelsPerCm()) / 2;
        int yPos;

        if(newHeight > maxHeight){
            if(isVisible) errorMessage();
            return;
        }
         
        if(cups.isEmpty()){
            yPos = (maxHeight - newHeight) * Cup.getPixelsPerCm();
            heightCups = newHeight;
            heightBase = 1;
        } else{
            int lastHeight = lastCup.getHeight();
            int lastY = lastCup.getYPosition();
            
            if(newHeight < lastHeight){
                yPos = lastY + (lastHeight - newHeight - 1) * Cup.getPixelsPerCm();
                heightBase++;
            } else {
                if(heightCups + newHeight > maxHeight){
                    if(isVisible) errorMessage();
                    return;
                }
                Cup refCup = null;
                for(int i = cups.size() - 1; i >= 0; i--){
                    if(cups.get(i).getHeight() >= newHeight){
                        refCup = cups.get(i);
                        break;
                    }
                }
                if(refCup == null){
                    refCup = cups.get(0);
                    for(Cup c : cups){
                        if(c.getYPosition() < refCup.getYPosition()){
                            refCup = c;
                        }
                    }
                }
                yPos = refCup.getYPosition() - newHeight * Cup.getPixelsPerCm();
                heightCups += newHeight;
            }
        }
        
        Cup newCup = new Cup(number, xPos, yPos, getRandColor());
        lastCup = newCup;
        cups.add(newCup);
        newCup.makeVisible();
    }
    
    /**
     * Delete the last cup at the cups list.
     */
    public void popCup(){
        boolean areCupsEmpty = cups.isEmpty();
        if(!areCupsEmpty){
            Cup removed = cups.remove(cups.size() - 1);
            removed.erase();
        } else if (areCupsEmpty && isVisible) {
            errorMessage();
        }
        
    }
    
    /**
     * Remove a especific cup, the cup that is at the j index of list.
     * @param j j is the index of cup that going to removed
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
                currentCup = null;
                return;
            } 
        }
        if (isVisible) errorMessage();
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
                lids.add(currentCup.getLid());
                // Aqui se debe agregar a la lista lids 
                // la lid que se generó
                currentCup = null;
                return;
            }
        }
        if (isVisible) errorMessage();
    }
    
    public void popLid(){
        boolean isLidsEmpty = lids.isEmpty();
        if(!isLidsEmpty){
            Lid removed = lids.remove(lids.size() - 1);
            removed.erase();
        } else if (isLidsEmpty && isVisible) {
            errorMessage();
        }
        
    }
    
    /**
     * Remove a specific lid of a specific cup
     * if the cup selected has lid this is removed
     * otherwise don't remove anythinf
     * @param i i is index of cup that going to remove lid
     */
    public void removeLid(int i) {
        
        for(int j = 0; j < cups.size(); j++){
            Cup currentCup = cups.get(j);
            if(currentCup.getID() == i){
                Lid lid = currentCup.getLid();
                if(lid != null){
                    currentCup.removeLid();
                    return;
                }
                
            }
        }
        if (isVisible) errorMessage();
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
    public String[][] stackingItems() {
        int lenCups, lenLids, j = 0, c = 0, counterAdd = 0;
        lenCups = cups.size();
        lenLids = lids.size();
        
        String[][] res =  new String[lenCups+lenLids][2];
        
        if (lenCups >= lenLids) {
            Lid currentLid = null;
            Cup currentCup = null;
            int verifyLid = -1, verifyCup;
            for (int i = 0; i < lenCups; i++) {
                
                if (j > lenLids) {
                    res = joinArrayCups(i, counterAdd, res);
                }
                
                currentCup = cups.get(i); 
                if (lenLids != 0) currentLid = lids.get(j);
                verifyCup = currentCup.getHeight();
                if (currentLid != null) {
                    verifyLid = currentLid.getHeight();
                }
                
                counterAdd++;
                if (verifyCup > verifyLid) {
                    j++;
                    i--;
                    res[counterAdd][0] = "Lid";
                    res[counterAdd][1] = verifyLid + "";
                    continue;
                } else if (verifyCup <= verifyLid) {
                    res[counterAdd][0] = "Cup";
                    res[counterAdd][1] = verifyCup + "";
                }
            }
        } else if (lenCups < lenLids) {
            Cup currentCup = null;
            Lid currentLid = null;
            int verifyLid2 = -1, verifyCup2;
            for (int l = 0; l < lenLids; l++) {
                if (c > lenCups) {
                    res = joinArrayLids(l, counterAdd, res);
                }
                currentCup = cups.get(c); 
                currentLid = lids.get(l);
                verifyCup2 = currentCup.getHeight();
                if (lenLids != 0) {
                    verifyLid2 = currentLid.getHeight();
                }
                
                if (verifyLid2 > verifyCup2) {
                    c++;
                    l--;
                    res[counterAdd][0] = "Cup";
                    res[counterAdd][1] = verifyCup2 + "";
                } else if (verifyLid2 <= verifyCup2) {
                    res[counterAdd][0] = "Lid";
                    res[counterAdd][1] = verifyLid2 + "";
                }
                counterAdd++;
            }
        }
        
        
        return res;
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
    
    public ArrayList<Lid> getLids() {
        return lids;
    }
    
    public ArrayList<Cup> getCups() {
        return cups;
    }
    
    public int getHeightCups(){
        return heightCups;
    }
    
    public int getHieghtBas(){
         return heightBase;
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
    
    /*
     * Inicializate atributes needed to build a tower 
     * @param width width is the atribute of tower to inicializate
     * @param height height is the atribute of tower going to inicializate 
     */
    private void inicializate(int width, int height) {
        this.width = width;
        this.maxHeight = height;
        cups = new ArrayList<>();
        lids = new ArrayList<>();
        xCenter = (int) Math.ceil((double) width / 2);
    }
    
    /*
     * Join two array of cups at a determinated index 
     */
    private String[][] joinArrayCups(int i, int counter, String[][] matrixString) {
        int lenCups = cups.size();
        int number;
        for (int z = i; z < lenCups; z++) {
            number = cups.get(z).getHeight();
            matrixString[counter][0] = "Cup";
            matrixString[counter][1] = number+"";
        }
        return matrixString;
    }
    
    /*
     * Join two array of lids at a determinated index to complete staking items method
     * @param i i index integer of list is missing to add to matrix of strings
     * @param counter counter is the count where matrixString is being add its data
     * @param matrixString matrixString is the matrix that going to be fulled of data
     * @return matrixString is the matrix fulled of data 
     */
    private String[][] joinArrayLids(int i, int counter, String[][] matrixString) {
        int lenLids = lids.size();
        int number;
        for (int z = i; z < lenLids; z++) {
            number = lids.get(z).getHeight();
            matrixString[counter][0] = "Lid";
            matrixString[counter][1] = number+"";
        }
        return matrixString;
    }
}