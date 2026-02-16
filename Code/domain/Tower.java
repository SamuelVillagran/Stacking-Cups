package domain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
 * Write a description of class Tower here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tower
{
    public final static ArrayList<String> COLORS = new ArrayList<>(Arrays.asList("red", "black", 
        "blue", "yellow", "magenta", "white", "orange", "pink",
        "cyan", "gray", "lightGray", "darkGray", "brown", "maroon", 
        "gold", "darkYellow", "greenTint", "salmon", "darkRed",
        "hardGray", "softGray"));
    private int width;
    private int maxHeight;
    private boolean lastOK;
    private ArrayList<Cup> cups;

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
        if(heightUsed() + newCup.getHeight() < maxHeight){
            cups.add(newCup);
        } else{
            return;
        }
    }
    
    /**
     *
     */
    public void popCup(){
        if(!cups.isEmpty()){
            Cup removed = cups.remove(cups.size() - 1);
            removed.makeInvisible();
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