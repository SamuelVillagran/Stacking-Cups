package tower;

import shapes.Rectangle;
import java.util.TreeMap;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import java.util.Random;

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
    private boolean isVisible;
    private int xCenter;
    private Cup lastCup;
    private int heightCups;
    private boolean isCreatedRuler;
    private ArrayList<StackingItem> stackingItems;
    
    static {
        for (FigureColor fc : FigureColor.values()) {
            COLORS.add(fc.name()); // ayudado con IA
        }
    }

    /**
     * Constructor for objects of class Tower
     */
    public Tower(int width, int maxHeight) {
        if (invariant(width, maxHeight)) {
            inicializate(width, maxHeight);
            generateRuler();
            lastOK = true;
        } else if (!invariant(width, maxHeight) && isVisible) {
            lastOK = false;
            errorMessage();
        }
    }
    
    /**
     * Constructor 2 cicle
     */
    public Tower(int numberCups) {
        int height = 0;
        for(int i = 1; i<= numberCups; i++){
             height += 2 * i -1;
        }
        if (invariant(height, height)) {
            inicializate(height, height);
            generateCupsInTower(numberCups);
            generateRuler();
        } else if (!invariant(width, maxHeight) && isVisible) {
            lastOK = false;
            errorMessage();
        }
    }
    
    /**
     * Add a cup if is possible.
     * @param number Is the n value cup.
     * @param isWorking Indicates if the method has to work.
     */
    public void pushCup(int number){
        int newHeight = 2 * number - 1;
        int xPos = xCenter * Cup.getPixelsPerCm() - (newHeight * Cup.getPixelsPerCm()) / 2;
        int yPos;

        if(newHeight > maxHeight || stackItemInteriorExists(number)){
            lastOK = false;
            if(isVisible) errorMessage(); // Aqui deberia retornar una excepcion
            return;
        }
        
        if(stackingItems.isEmpty()){
            yPos = (maxHeight - newHeight) * Cup.getPixelsPerCm();
            heightCups = newHeight;
        } else {
            StackingItem landingItem = findLandingPiece(newHeight);
            yPos = resolveYPos(landingItem, newHeight, newHeight);
            if(yPos < 0){
                if(isVisible) errorMessage();
                lastOK = false;
                return;
            }
            if(landingItem != null){
                heightCups += newHeight;
            }
        }
        
        Cup newCup = new Cup(number, xPos, yPos, getRandColor());
        lastCup = newCup;
        stackingItems.add(newCup);
        lastOK = true;
        if(isVisible) newCup.makeVisible();
    }
    
    /**
     * Put in the tower diferent type of cups 
     * @param type type of cup that it's going to put at the tower
     * @param i i is the id of tower that it's going to put at the tower
     */
    public void pushCup(String type, int i) {
        int newHeight = 2 * i - 1;
        int xPos = xCenter * Cup.getPixelsPerCm() - (newHeight * Cup.getPixelsPerCm()) / 2;
        int yPos;
        
        if(newHeight > maxHeight || stackItemInteriorExists(i)){
            lastOK = false;
            if(isVisible) errorMessage(); // Aqui deberia retornar una excepcion
            return;
        }
        
        List<String> types = List.of("normal", "opener", "hierarchical");
        boolean isTypeAllowed = types.contains(type);
         // Si no es el tipo permitido se lanza una excepcion
        TreeMap<Integer, Cup> cups = new TreeMap<>();
        for (StackingItem s : stackingItems) {
            if (s.hasInterior()) cups.put(s.getId(), (Cup) s);
        }
        
        TreeMap<Integer, Cup> cupsOrderByPosY = new TreeMap<>();
        for (Cup c : cups.values()) {
            cupsOrderByPosY.put(c.getYPos(), c); 
        } 
        
        if (isTypeAllowed) {
            if (type.equals("normal")) pushCup(i);
            
            if (type.equals("opener")) {
                
                if(stackingItems.isEmpty()) {  // Cuando no hay stackingItems se pone de primeras
                    yPos = (maxHeight - newHeight) * Cup.getPixelsPerCm();
                    heightCups = newHeight;
                    Opener newCup = new Opener(i, xPos, yPos, getRandColor());
                    lastCup = newCup;
                    
                    stackingItems.add(newCup);
                    lastOK = true;
                    if(isVisible) newCup.makeVisible();
                    return;
                }
                
                int yPosLastCup = Integer.MIN_VALUE, sizeLastCup = 0; // Está parte de la lógica fue mejorada por Claude Sonnet 4.6 IA 2026
                int yPosTopCup = Integer.MIN_VALUE, sizeTopCup = 0;
                List<StackingItem> lidsToRemove = new ArrayList<>(); // ← acumula
                
                for (Integer yPosCups : cupsOrderByPosY.descendingKeySet()) {
                    Cup currentCup = cupsOrderByPosY.get(yPosCups);
                    Lid lidOfCup = currentCup.getLid();
                    if (currentCup.getId() > i && lidOfCup != null) {
                        lidsToRemove.add(lidOfCup); // ← guarda referencia antes de borrarla
                        currentCup.removeLid();
                    }
                    if (yPosTopCup == Integer.MIN_VALUE) {
                        yPosTopCup = yPosCups;
                        sizeTopCup = currentCup.getSize();
                    }
                    if (currentCup.getId() <= i) {
                        yPosLastCup = yPosCups;
                        sizeLastCup = currentCup.getSize();
                        break;
                    }
                }
                
                stackingItems.removeAll(lidsToRemove); // ← elimínalas de la lista
                
                if (yPosLastCup == Integer.MIN_VALUE) {
                    yPosLastCup = yPosTopCup;
                    sizeLastCup = sizeTopCup;
                }
                    
                yPos = yPosLastCup + sizeLastCup; // Put the new yPos cup at (0,0) where should be stay
                Opener newCup = new Opener(i, xPos, yPos, getRandColor());
                stackingItems.add(newCup);    
                if(isVisible) newCup.makeVisible();
            }
            
            if (type.equals("hierarchical")) {
                Hierarchical newCup = new Hierarchical(i, getRandColor());
                stackingItems.add(newCup);
            }
            
            
        }
    }
    
    /**
     * Delete the last cup at the stackingItems list.
     */
    public void popCup(){
        boolean areStackingItemsEmpty = stackingItems.isEmpty();
        if(!areStackingItemsEmpty){
            for(int i = stackingItems.size() -1; i >= 0; i--){
                if(stackingItems.get(i).hasInterior()){
                    stackingItems.remove(i).erase();
                    lastOK = true;
                    return;
                }
            }
        } else if (areStackingItemsEmpty && isVisible) {
            errorMessage();
        }
        lastOK = false;
    }
    
    /**
     * Remove a especific cup, the cup that is at the j index of list.
     * @param j j is the index of cup that going to removed
     */
    public void removeCup(int j){
        int idCurrentCup, lenStack;
        StackingItem currentCup;
        lenStack = stackingItems.size();
        for(int i = 0; i< lenStack; i++){
            currentCup = stackingItems.get(i);
            idCurrentCup = currentCup.getId();
            if(idCurrentCup == j && currentCup.hasInterior()){
                StackingItem removed = stackingItems.remove(i);
                removed.erase();
                currentCup = null;
                lastOK = true;
                return;
            } 
        }
        if (isVisible) errorMessage();
        lastOK = false;
    }
    
    /**
     * Put a lid of a specific cup, cup at the i index of cups list.
     * @param int Index of cup that it's going to add a lid. 
     */
    public void pushLid(int i) {
        int lidWidth = 2 * i -1;
        int lidHeight = 1;
        int xPos = xCenter * Cup.PIXELS_PER_CM - (lidWidth * Cup.getPixelsPerCm()) / 2;
        
        if(stackingItems.isEmpty() || (stackItemNonInteriorExists(i) && stackItemInteriorExists(i))){
            if(isVisible) errorMessage();
            lastOK = false;
            return;
        }
        
        StackingItem landingItem = findLandingPiece(lidWidth);
        int yPos = resolveYPos(landingItem, lidWidth, lidHeight);
        if (yPos < 0) {
            lastOK = false;
            if (isVisible) errorMessage();
            return;
        }
        if(landingItem == null) {
            if(isVisible){
                lastOK = false;
                errorMessage();
                return;
            }else{
                lastOK = false;
                return;
            }
        }
        String color = landingItem.getColor();
        Lid newLid = new Lid(i, xPos, yPos, color);
        stackingItems.add(newLid);
        if(isVisible) newLid.makeVisible();
        heightCups += lidHeight;
        lastOK = true;
        
        checkAssociatedCup(i, newLid);
    }
    
    /**
     * Put at the tower diferents lid's types 
     * @param type type es the type of lid that want to put at tower ("normal", "fearful", "crazy")
     * @param i i it's the id of lid that wants to insert at the tower 
     */
    public void pushLid(String type, int i) {
        int lidWidth = 2 * i -1;
        int lidHeight = 1;
        int xPos = xCenter * Cup.PIXELS_PER_CM - (lidWidth * Cup.getPixelsPerCm()) / 2;
        
        if(stackingItems.isEmpty() || (stackItemNonInteriorExists(i) && stackItemInteriorExists(i))){
            if(isVisible) errorMessage();
            lastOK = false;
            return;
        }
        
        StackingItem landingItem = findLandingPiece(lidWidth);
        int yPos = resolveYPos(landingItem, lidWidth, lidHeight);
        if (yPos < 0) {
            lastOK = false;
            if (isVisible) errorMessage();
            return;
        }
        if(landingItem == null) {
            if(isVisible){
                lastOK = false;
                errorMessage();
                return;
            }else{
                lastOK = false;
                return;
            }
        }
        String color = landingItem.getColor();
        if (type.equals("normal")) pushLid(i);
        
        if (type.equals("crazy")) {
            yPos += lidWidth + landingItem.getSize();
            Crazy newLid = new Crazy(i, xPos, yPos, color);
            stackingItems.add(newLid);
            if(isVisible) newLid.makeVisible();
            lastOK = true;
            checkAssociatedCup(i, newLid);
        } else if (type.equals("fearful")) {
            for (StackingItem s : stackingItems) {
                if (s.getId() == i && s.hasInterior() && s.getLid() == null) {
                    Fearful newLid = new Fearful(i, xPos, yPos, color);
                }
            }
        }
        
        
    }
    
    public ArrayList<StackingItem> getStackingItems(){
        return stackingItems;
    }
    
    /**
     * Delete the last lid put at this tower
     */
    public void popLid(){
        boolean areStackingItemsEmpty = stackingItems.isEmpty();;
        if(!areStackingItemsEmpty){
            for(int i = stackingItems.size() -1; i >= 0; i--){
                StackingItem currentItem = stackingItems.get(i);
                if(!currentItem.hasInterior()){
                    StackingItem itemRemoved = stackingItems.remove(i);
                    itemRemoved.erase();
                    lastOK = true;
                    return;
                }
            }
        } else if (areStackingItemsEmpty && isVisible) {
            errorMessage();
        }
        lastOK = false;
    }
    
    /**
     * Remove a specific lid of a specific cup
     * if the cup selected has lid this is removed
     * otherwise don't remove anythinf
     * @param i i is index of cup that going to remove lid
     */
    public void removeLid(int i) {
        
        for(StackingItem currentItem : stackingItems){
            if(currentItem.getId() == i && !currentItem.hasInterior()){
                Lid lid = currentItem.getLid();
                if(currentItem != null) {
                    
                    
                    Cup foundCup = findCup(i);
                    if (foundCup != null) {
                        foundCup.removeLid();
                    }
                    
                    stackingItems.remove(currentItem);
                    
                    lastOK = true;
                    return;
                }
                if (isVisible) errorMessage();
            }
        }
        lastOK = false;
    }
    
    /**f
     * Order the stackin items from lowest to highest width.
     */
    public void orderTower() {
        TreeMap<Integer, String> cupsIds = new TreeMap<>();
        TreeMap<Integer, String> lidsIds= new TreeMap<>();
        
        for(StackingItem item : stackingItems){
            if(item.hasInterior()){
                cupsIds.put(item.getId(), item.getColor());
            }else{
                lidsIds.put(item.getId(), item.getColor());
            }
        }
        
        for(StackingItem item : stackingItems){
            item.erase();
        }
        heightCups = 0;
        stackingItems.clear();
        
        
        for(Integer id : cupsIds.descendingKeySet()){
            pushCup(id, cupsIds.get(id));
            if(lidsIds.containsKey(id)){
                pushLid(id);
            }
        }
    }
    
    /**
     * Add a cup if is possible in order.
     * @param int Integer is the id of cup where this going to push at the list cups.
     */
    private void pushCupInOrder(int number) {
        boolean isItemEmpty = stackingItems.size() == 0 || stackingItems == null;
        Cup newCup = null;
        int newHeight = 2 * number - 1;
        int xPos = xCenter * Cup.getPixelsPerCm() - (newHeight * Cup.getPixelsPerCm()) / 2;
        int yPos;
        if (isItemEmpty) { // Si es la primera copa que se inserto
            isVisible = true;
            yPos = (maxHeight - newHeight) * Cup.getPixelsPerCm();
            newCup = new Cup(number, xPos, 30, getRandColor());
        }
        
        if (!isItemEmpty) {
            Integer lastIndex = stackingItems.size() - 1;
            StackingItem lastCup = stackingItems.get(lastIndex).hasInterior() ? stackingItems.get(lastIndex) : null;
            
            StackingItem landingItem = findLandingPiece(newHeight);
            yPos = resolveYPos(landingItem, newHeight, newHeight);
            
            
            if (lastCup == null) return;
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

            int cupHeight = newCup.getHeight();
            if (!invariant2(cupHeight)) {
                newCup.erase();
                if (isVisible) errorMessage();

                return;
            }
        }

        stackingItems.add(newCup);

        if (isVisible) {
            makeVisibleRuler();
        }

    }

    private void pushCup(int number, String color) {
        int newHeight = 2 * number - 1;
        int xPos = xCenter * Cup.getPixelsPerCm() - (newHeight * Cup.getPixelsPerCm()) / 2;
        int yPos;

        if(newHeight > maxHeight || stackItemInteriorExists(number)){
            lastOK = false;
            if(isVisible) errorMessage();
            return;
        }
        
        if(stackingItems.isEmpty()){
            yPos = (maxHeight - newHeight) * Cup.getPixelsPerCm();
            heightCups = newHeight;
        } else{
            StackingItem landingItem = findLandingPiece(newHeight);
            yPos = resolveYPos(landingItem, newHeight, newHeight);
            if(yPos < 0){
                if(isVisible) errorMessage();
                lastOK = false;
                return;
            }
            if(landingItem != null){
                heightCups += newHeight;
            }
        }
        
        Cup newCup = new Cup(number, xPos, yPos, color);
        lastCup = newCup;
        stackingItems.add(newCup);
        lastOK = true;
        if(isVisible) newCup.makeVisible();
    }
    
    /**
     * Order the stackin items from highest to lowest width.
     */
    public void reverseTower() {
        TreeMap<Integer, String> cupsIds = new TreeMap<>();
        TreeMap<Integer, String> lidsIds= new TreeMap<>();
        
        for(StackingItem item : stackingItems){
            if(item.hasInterior()){
                cupsIds.put(item.getId(), item.getColor());
            }else{
                lidsIds.put(item.getId(), item.getColor());
            }
        }
        
        for(StackingItem item : stackingItems){
            item.erase();
        }
        heightCups = 0;
        stackingItems.clear();
        
        for(Integer id : cupsIds.keySet()){
            pushCup(id, cupsIds.get(id));
            if(lidsIds.containsKey(id)){
                pushLid(id);
            }
        }
    }
    
    /**
     * Swap two objects of position
     * @param o1 o1 is the first object to swap positions with o2's position
     * @param o2 o2 is the second object to swap position with o1's position
     */
    public void swap(String [] o1, String[] o2) {
        ArrayList<StackingItem> itemsCopy = new ArrayList<>();
        int idO1 = Integer.parseInt(o1[1]);
        int idO2 = Integer.parseInt(o2[1]);
        
        StackingItem item1 = null,item2 = null;
        for(StackingItem s : stackingItems){
            if(s.getId() == idO1) item1 = s;
            if(s.getId() == idO2) item2 = s;
        }
        
        if(item1 == null || item2 == null){
            lastOK = false;
            if(isVisible) errorMessage();
            return;
        }
        
        int indx1 = stackingItems.indexOf(item1);
        int indx2 = stackingItems.indexOf(item2);
        Collections.swap(stackingItems, indx1, indx2);
        itemsCopy.addAll(stackingItems);
        
        eraseItems();
        
        for(StackingItem s : itemsCopy){
            if(s.hasInterior()){
                pushCup(s.getId());
            }else {
               pushLid(s.getId()); 
            }
        }
    }
    
    /*
     * Delete the items of the container.
     */
    private void eraseItems(){
        for(StackingItem item : stackingItems){
            item.erase();
        }
        heightCups = 0;
        stackingItems.clear();
    }
    
    /**
     * 
     */
    public void cover() {
        
    }
    
    /**
     * Get the max height of tower
     * @return int this is the max height of tower
     */
    public int height() {
        return maxHeight;
    }
    
    /**
     * Get a list of lid widths in ascending order.
     * @return int[] Sorted lid widths.
     */
    public int[] lidedCups() {
        int i = 0, lenItems = stackingItems.size(), currentWidthItem;
        boolean isLidsEmpty = stackingItems.isEmpty();
        
        if (isLidsEmpty && isVisible) {
            errorMessage();
            lastOK = false;
            return null;
        } 
        
        if (isLidsEmpty) return null;
        
        List<Integer> widths = new ArrayList<>(); // Se crea una List para organizar las anchuras de las lids
        for (StackingItem item : stackingItems) {
            currentWidthItem = item.getWidth();
            widths.add(currentWidthItem);
        }
        
        Collections.sort(widths);
        
        int [] result = new int[widths.size()]; 
        for (int widthLid : widths) {
            result[i] = widthLid;
            i++;
        }
        
        lastOK = true;
        return result;
    }
    
    /**
     * Give items order from base since top
     * @return A matrix-array of two columns where first column
     *      contain name item and second column contain its width
     */
    public String[][] stackingItems() {
        int lenItems, i = 0;
        lenItems = stackingItems.size();
        String[][] result =  new String[lenItems][2];
        Collection<StackingItem> items = stackingItems; // A+Esta linea fue ayudada a extraer con Gemini IA
        System.out.println(items.size());
        TreeMap<Integer, StackingItem> itemsWithPosY = new TreeMap<>();
        
        for (StackingItem item : items) {
            itemsWithPosY.put(item.getYPosition() + (item.getHeight() * Cup.PIXELS_PER_CM), item);
        }
        
        Set<Integer> itemsWithPosYKey = itemsWithPosY.descendingKeySet();
        for (Integer itemPosY : itemsWithPosYKey) {
            StackingItem currentItem = itemsWithPosY.get(itemPosY);
            boolean isCup = currentItem.hasInterior();
            if (isCup) {
                result[i][0] = "cup";
                result[i][1] = currentItem.getId()+"";
            } else {
                result[i][0] = "lid";
                result[i][1] = currentItem.getId()+"";
            }
            i++;
        }
        
        return result;
    }
    
    /**
     * 
     */
    public String[][] swapToReduce() {
        return new String[0][0];
    }
    
    /**
     * Make all items of StackingItems visible
     * and make ruler visible
     */
    public void makeVisible() {
        if (!isVisible) isVisible = !isVisible;
        for (StackingItem s : stackingItems) {
            s.makeVisible();
        }
        makeVisibleRuler();
    }
    
    /**
     * Make the stacking items invisible.
     */
    public void makeInvisible() {
        if (isVisible) isVisible = !isVisible;
        for (StackingItem s : stackingItems) {
            s.makeInvisible();
        }
        makeInvisibleRuler();
    }
    
    /**
     * Close the game's window
     */
    public void exit() {
        System.exit(0);
    }
    
    /**
     * Verify with the invariant if Its last movement was valid
     * @return true if last cup's height was menor than height of tower;
     *          false otherwise.
     */
    public boolean ok() {
        int heightCup;
        heightCup = lastCup.getHeight();
        return invariant2(heightCup) && lastOK;
    }

    public int getHeightCups(){
        return heightCups;
    }
    
    public String icpcProblem(String numberAndHeight) {
        String result = "";
        
        String[] nAndHeight = numberAndHeight.split(" ");
        
        try {
            int[] nAndHeightInteger = new int[2];
            int i = 0;
            for (String part : nAndHeight) {
                nAndHeightInteger[i++] = Integer.parseInt(part);
            }
            boolean isPosible = isPosible(nAndHeightInteger);
            
            if (isPosible) {
                
            }
            
            if (!isPosible) {
                return "impossible";    
            }
            
        } catch (Exception e) {
            System.err.println("""
                Look that any parameter given will 
                be more than two numbers \nOr This be numbers""");
            e.printStackTrace();
        }
        
        return result;
    }
    
    /*
     * Finds cup with id cup
     * @param idCup idCup is the id that going to search at staking items
     */
    private Cup findCup(int idCup) {
        
        for (StackingItem s : stackingItems) {
            if (s.getId() == idCup) {
                return (Cup) s;
            }
        }
        return null;
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
    public int heightUsed(){
        int min = Integer.MAX_VALUE;
        for(StackingItem s : stackingItems) {
            if(s.getYPosition() < min){
                min = s.getYPosition();
            }
        }
        return maxHeight - (min / Cup.PIXELS_PER_CM);
    }
    
    /*
     * Invariant of Stacking Cups
     * Comprove if height and weigth are positive integers
     * @param width It's the width of tower that it's going to comprove if this integer is more than 0
     * @param height It's the height of tower that it's going to comprove if this integer is more than 0
     */
    private boolean invariant(int width, int height) {
        return (height > 0 && width > 0);
    }
    
    /*
     * Second invariant of game
     */
    private boolean invariant2(int cupHeight) {
        return heightUsed() + cupHeight <= maxHeight;
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
    private void generateRuler() { // Ayudado por Gemini IA 2026 pero revisado
        
        if (isCreatedRuler) return;
        
        int DISTANCE, NITERATIONS, width, height, currentPosX, currentPosY;
        DISTANCE = 10;
        NITERATIONS = 40;
        currentPosX = 0;
        currentPosY = 0;
        width = 10;
        height = 3;
        ruler = new ArrayList<>();
        for (int i = 0; i < NITERATIONS; i++) {
            // To create a rectangle this have:
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
     * Make invisible the ruler of Stacking Cups
     */
    private void makeInvisibleRuler() {
        for (Rectangle r : ruler) {
            r.makeInvisible();
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
        stackingItems = new ArrayList<>();
        xCenter = (int) Math.ceil((double) width / 2);
    }
    
    /*
     * Set deterninated cups at the tower 
     */
    private void generateCupsInTower(int cupsRequeried) {
        for (int i = 1; i <= cupsRequeried; i++) {
            pushCup(i);
        }
    }
    
    /*
     * Put Cups in Tower
     */
    private void putCupInTower(int xPos, int yPos, int idCup, Cup newCup) {
        if (yPos > 0 && xPos > 0) {
                newCup = new Cup(idCup, xPos,
                    yPos, getRandColor());
            } else {
                yPos = lastCup.getYPosition()-Cup.getPixelsPerCm();
                xPos = lastCup.getXPosition()-Cup.getPixelsPerCm();
                newCup = new Cup(idCup, xPos,
                    yPos, getRandColor());
            } 
    }

    /*
     * Inicializate attributes of second constructor
     */
    private void inicializateAttributes() {
        stackingItems = new ArrayList<>();
        isVisible =  false;
        width = 30;
        maxHeight = 30;
        isCreatedRuler = false;
        xCenter = (int) Math.ceil((double) width / 2);
    }
    
    private String getColorCup(int number){
        String result = null;
        if(stackingItems.isEmpty()){
            if(isVisible) errorMessage();
        } else{
            for(int i = 0; i< stackingItems.size(); i++){
                if(stackingItems.get(i).getId() == number){
                    result = stackingItems.get(i).getColor();
                    return result;
                }
            }
        }
        return result;
    }
    
    /*
     * Calculates the final yPosition of the falling piece.
     * If landing == null, landing piece falls through all and it nests in the deepest container.
     * If landing.hasInterior() search the effective container above the blocker.
     * If landing is a null, lands directly on top.
    */
    private int resolveYPos(StackingItem landing, int fallingWidth, int fallingHeight){ // Generado con IA, modificado.
        if (landing == null){
            StackingItem deepest = findDeepestContainer(fallingWidth);
            if(deepest == null){
                return (maxHeight - fallingHeight) * Cup.PIXELS_PER_CM;
            }
            return deepest.getYPosition() + (deepest.getHeight() - fallingHeight -1) * Cup.PIXELS_PER_CM;
        }
        if(landing.hasInterior()){
            StackingItem landingContainer = findLandingContainer(landing, fallingWidth);
            if(landingContainer != landing){
                return landingContainer.getYPosition() + (landingContainer.getHeight() -fallingHeight -1) * Cup.PIXELS_PER_CM;
            }
        }
        return landing.getYPosition() - fallingHeight * Cup.PIXELS_PER_CM;
    }
    
    /*
     * Returns the deepest piece that can contain a piece of fallingWidth.
     * This is used when the falling piece is more smaller than everything,
     * like no blocker found.
     */
    private StackingItem findDeepestContainer(int fallingWidth){
        StackingItem deepest = null;
        for(StackingItem s : stackingItems){
            if(s.canContain(fallingWidth) && !isEntranceBlocked(s)){
                if(deepest == null || s.getYPosition() > deepest.getYPosition()){
                    deepest = s;
                }
            }
        }
        return deepest;
    }
    
    /*
     * Find the effective container where the falling piece will actually land.
     * Climbs up through containers whose bottom aligns with landing's top.
     * It could land into the deepest nested container.
     */
    private StackingItem findLandingContainer(StackingItem landing, int fallingWidth){ //Generado con IA
        StackingItem container = landing;
        boolean found = true;
        while(found){
            found = false;
            for(StackingItem s : stackingItems){
                if(s.canContain(fallingWidth) && s.getYPosition() + s.getHeight() * Cup.PIXELS_PER_CM == container.getYPosition()){
                    container = s;
                    found = true;
                    break;
                }
            }
        }
        if(container != landing){
            container = findInnermostContainer(container, fallingWidth);
        }
        return container;
    }
    
    /*
     * Given a container iteratively finds the deepest piece nested inside it
     * that can still hold the falling piece.
     */
    private StackingItem findInnermostContainer(StackingItem container, int fallingWidth){
        boolean foundInner = true;
        while(foundInner){
            foundInner = false;
            StackingItem deepestInner = null;
            int containerBottom = container.getYPosition() + container.getHeight() * Cup.PIXELS_PER_CM;
            for(StackingItem s : stackingItems){
                if(s.canContain(fallingWidth) && s.getYPosition() > container.getYPosition() 
                    && s.getYPosition() > deepestInner.getYPosition()){
                    if (deepestInner == null || s.getYPosition() > deepestInner.getYPosition()) {
                        deepestInner = s;
                    }
                }
            } if(deepestInner != null){
                container = deepestInner;
                foundInner = true;
            }
        }
        return container;
    }
    
    /*
     * Returns the upper piece that blocks a fallinge piece of the giving width.
     * Returns null when no piece blocks - the falling piece is smaller than everything
     * and will nest in the deepest avilable "container".
     */
    private StackingItem findLandingPiece(int fallingWidth){
        StackingItem landing = null;
        int minY = Integer.MAX_VALUE;
        for(StackingItem s : stackingItems) {
            if(s.blocksPassage(fallingWidth) && s.getYPosition() < minY){
                landing = s;
                minY = s.getYPosition();
            }
        }
        return landing;
    }
    
    /*
     * Check if the stack item Cup exists.
     */
    private boolean stackItemInteriorExists(int number){
        for(StackingItem element : stackingItems){
            if(element.getId() ==  number && element.hasInterior()) return true;
        }
        return false;
    }
    
    /*
     * Check if the stack item Lid exists.
     */
    private boolean stackItemNonInteriorExists(int number){
        for(StackingItem element : stackingItems){
            if( element.getId() ==  number && !element.hasInterior()) return true;
        }
        return false;
    }
    
    private int getEffectiveTop(Cup cup){
        int lidExtra = cup.getLid() != null ? cup.getLid().getHeight() * Cup.PIXELS_PER_CM : 0;
        return cup.getYPosition() - lidExtra;
    }
    
    /*
     * Find the Cup whose id matches with the given number.
     */
    private Cup findCupWithId(int number){
        for(StackingItem item : stackingItems){
            if(item.getId() == number && item.hasInterior()){
                return (Cup)item;
            }
        }
        return null;
    }
    
    /*
     * Match a Lid with the respective Cup.
     */
    private void checkAssociatedCup(int number, Lid lid){
        Cup matchingCup = findCupWithId(number);
        if(matchingCup !=null){
            int expectedLidPos =  matchingCup.getYPosition() - (1 * Cup.PIXELS_PER_CM);
            if(lid.getYPosition() == expectedLidPos){
                matchingCup.addLid(lid);
            }
        }
    }
    
    private boolean isPosible(int[] numberAndHeight) {
        int number = numberAndHeight[0], heightWillBeOcupped = numberAndHeight[1];
        int CONDITION = (2*number) - 1;
        
        if ((number*number - 2) == heightWillBeOcupped) return false;
        
        for (int i = 1; i <= numberAndHeight[0]; i++) {
            if (number >= 3) {
                heightWillBeOcupped -= CONDITION;
                if (heightWillBeOcupped == 0) {
                    return true;
                }
            }
            
            if (number < 3 && number > 0) {
                
            }
            
            if (number <= 0 || number > heightWillBeOcupped) return false;
        }
        
        return false;
    }
    
    /*
     * Checks whether the entrance of a container is physically blocked
     * by another item resting directly on top of it.
     * @param container the sackingItem which entances is to be checked.
     * GENERATED BY IA - CHAT GPT.
     */
    private boolean isEntranceBlocked(StackingItem container) {
        int entranceY = container.getYPosition();
        for (StackingItem s : stackingItems) {
            if (s != container && s.getYPosition() + 
            s.getHeight() * Cup.PIXELS_PER_CM == entranceY) {
                return true;
            }
        }
        return false;
    }
}
