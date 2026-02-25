package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

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
    
    static {
        for (FigureColor fc : FigureColor.values()) {
            COLORS.add(fc.name()); // ayudado con IA
        }
    }

    /**
     * Constructor for objects of class Tower
     */
    public Tower(int width, int maxHeight) {
        this.width = width;
        this.maxHeight = maxHeight;
        cups = new ArrayList<>();
    }

    /**
     * Add a cup if is possible
     */
    public void pushCup(int number) {
        Cup newCup = new Cup(number, getRandColor());
        int towerHeight = heightUsed();
        int cupHeight = newCup.getHeight();
        if(towerHeight + cupHeight < maxHeight){
            cups.add(newCup);
        } 
        return;
    }
    
    /**
     *
     */
    public void popCup(){
        if(!cups.isEmpty()){
            Cup removed = cups.remove(cups.size() - 1);
            removed.erase();
        }
    }
    
    /**
     * 
     */
    public void removeCup(int j){
        for(int i = 0; i< cups.size(); i++){
            if(cups.get(i).getId() == j){
                Cup removed = cups.remove(i);
                removed.makeInvisible();
            }
        }
    }
    
    private String getRandColor(){
        Random random = new Random();
        int randIndexColor = random.nextInt(COLORS.size());
        String color = COLORS.get(randIndexColor);
        System.out.println(COLORS);
        System.out.println(color);
        COLORS.remove(color);
        return color;
    }
    
    /**
     * Get the height used
     */
    public int heightUsed(){
        int total = 0;
        for(Cup c : cups){
            total += c.getHeight();
        }
        return total;
    }
    
    /**
     * 
     */
    public void pushLid(int i) {
        for(int j = 0; j< cups.size(); j++){
            if(cups.get(j).getId() == i){
                cups.get(j).addLid();
            }
        }
    }
    
    public void popLid(){
        if(!cups.isEmpty()){
            
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
     * 
     * @return
     */
    public int height() {
        return 0;
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
     * 
     */
    public void makeVisible() {
        for (Cup c : cups) {
            c.makeVisible();
        }
    }
    
    /**
     * 
     */
    public void makeInvisible() {
        
    }
    
    /**
     *
     */
    public void exit() {
        
    }
    
    /**
     * 
     */
    public boolean ok() {
        return true;
    }
}
