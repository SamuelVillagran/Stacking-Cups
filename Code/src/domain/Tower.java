package domain;

import presentation.Rectangle;
import java.util.TreeMap;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.NavigableSet;

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
    
    private static ArrayList<Rectangle> ruler;
    private ArrayList<String> colors = new ArrayList<>();
    private int width;
    private int maxHeight;
    private boolean lastOK;
    private boolean isVisible;
    private int xCenter;
    private Cup lastCup;
    private int heightCups;
    private boolean isCreatedRuler;
    private ArrayList<StackingItem> stackingItems;
    private HashMap<Integer, String> tempColors = null;
    

    /**
     * Constructor for objects of class Tower
     * @throws TowerException CANT_CREATE_TOWER - When violates the invariant this is throw
     */
    public Tower(int width, int maxHeight) throws TowerException {
        if (invariant(width, maxHeight)) {
            inicializate(width, maxHeight);
            initializeColors();
            generateRuler();
            lastOK = true;
        } else if (!invariant(width, maxHeight)) {
            lastOK = false;
            if (isVisible) errorMessage(TowerException.CANT_CREATE_TOWER);
            throw new TowerException(TowerException.CANT_CREATE_TOWER);
        }
    }
    
    /**
     * Constructor 2 cicle
     * @throws TowerException CANT_CREATE_TOWER - When violates the invariant this is throw
     */
    public Tower(int numberCups) throws TowerException {
        int height = 0;
        for(int i = 1; i<= numberCups; i++){
             height += 2 * i -1;
        }
        if (invariant(height, height)) {
            inicializate(height, height);
            initializeColors();
            generateCupsInTower(numberCups);
            generateRuler();
        } else if (!invariant(width, maxHeight)) {
            lastOK = false;
            if (isVisible) errorMessage(TowerException.CANT_CREATE_TOWER);
            throw new TowerException(TowerException.CANT_CREATE_TOWER);
        }
    }
    
    /**
     * Add a cup if is possible.
     * @param number Is the n value cup.
     * @param isWorking Indicates if the method has to work.
     * @throws TowerException IS_OUT_SCREEN - When some shape is put out of screen this is throw
     */
    public final void pushCup(int number) throws TowerException{
        int newHeight = 2 * number - 1;
        int xPos = xCenter * Cup.getPixelsPerCm() - (newHeight * Cup.getPixelsPerCm()) / 2;
        int yPos;

        if(newHeight > maxHeight || stackItemInteriorExists(number)){
            lastOK = false;
            if(isVisible) errorMessage(TowerException.CANT_PUSH_CUP); // Aqui deberia retornar una excepcion
            throw new TowerException(TowerException.CANT_PUSH_CUP);
        }
        
        if(stackingItems.isEmpty()){
            yPos = (maxHeight - newHeight) * Cup.getPixelsPerCm();
            heightCups = newHeight;
        } else {
            StackingItem landingItem = findLandingPiece(newHeight);
            yPos = resolveYPos(landingItem, newHeight, newHeight);
            if(yPos < 0){
                if(isVisible) errorMessage(TowerException.IS_OUT_SCREEN);
                lastOK = false;
                throw new TowerException(TowerException.IS_OUT_SCREEN);
            }
            if(landingItem != null){
                heightCups += newHeight;
            }
        }
        
        Cup newCup = new Cup(number, xPos, yPos, getAssociatedColor(number, true));
        lastCup = newCup;
        stackingItems.add(newCup);
        lastOK = true;
        if(isVisible) newCup.makeVisible();
    }
    
    /**
     * Put in the tower different type of cups 
     * @param type type of cup that it's going to put at the tower
     * @param i i is the id of tower that it's going to put at the tower
     * @throws TowerException NOT_ALLOWED_TYPE - When string type not is allowed throws this exception
     */
    public void pushCup(String type, int i) throws TowerException {
        int newHeight = 2 * i - 1;
        int xPos = xCenter * Cup.getPixelsPerCm() - (newHeight * Cup.getPixelsPerCm()) / 2;
        int yPos;
        
        if(newHeight > maxHeight || stackItemInteriorExists(i)){
        	lastOK = false;
            if(isVisible) errorMessage(TowerException.CANT_PUSH_CUP); // Aqui deberia retornar una excepcion
            throw new TowerException(TowerException.CANT_PUSH_CUP);
        }
        
        List<String> types = List.of("normal", "opener", "hierarchical");
        boolean isTypeAllowed = types.contains(type);
        
        if (!isTypeAllowed) throw new TowerException(TowerException.NOT_ALLOWED_TYPE);
         // Si no es el tipo permitido se lanza una excepcion
        TreeMap<Integer, Cup> cups = new TreeMap<>();
        for (StackingItem s : stackingItems) {
            if (s.hasInterior()) cups.put(s.getId(), (Cup) s);
        }
        
        TreeMap<Integer, Cup> cupsOrderByPosY = new TreeMap<>();
        for (Cup c : cups.values()) {
            cupsOrderByPosY.put(c.getYPosition(), c); 
        }
        
        
        
        switch (type) {
            case "normal":
                pushCup(i);
                break;
            case "opener":
                pushOpener(i, xPos, newHeight);
                break;
            case "hierarchical": 
                pushHierarchical(i, xPos, newHeight);
                break;
        }
    }
    
    /**
     * Give items in order y position of way ascendent
     * @return A TreeMap with the item's position y and items
     */
    public TreeMap<Integer, StackingItem> getInOrderItems() {
        TreeMap<Integer, StackingItem> itemsOrderByPosY = new TreeMap<>();
        
        for (StackingItem c : stackingItems) {
            itemsOrderByPosY.put(c.getYPosition(), c); 
        } 
        return itemsOrderByPosY;
    }
    
    /*
     * Add the array colors.
     */
    private final void initializeColors() {
        colors = FigureColor.getStringColor();
    }
    
    /*
     * Inserts an Opener cup into the tower.
     * The Opener cup removes every lid that blocks iths way.
     * @param i Id of the cup.
     * @param xPos Position on x of the cup.
     * @param newHeight Height of the cup.
     * @throws TowerException IS_OUT_SCREEN - When some shape is put out of screen this is throw
     */
    private void pushOpener(int i, int xPos, int newHeight) throws TowerException {
        int yPos;
        if(stackingItems.isEmpty()) {  // Cuando no hay stackingItems se pone de primeras
            yPos = (maxHeight - newHeight) * Cup.getPixelsPerCm();
            heightCups = newHeight;
            Opener newCup = new Opener(i, xPos, yPos, getAssociatedColor(i, true));
            lastCup = newCup;
                    
            stackingItems.add(newCup);
            lastOK = true;
            if(isVisible) newCup.makeVisible();
            return;
        }
                
        Cup currentContainer = null;
        while (true) { // Elimina las lids que encuentra  a su paso
            StackingItem blocker = findLandingPiece(newHeight); // Busca el obstaculo más alto
            if (blocker == null || blocker.hasInterior()) break; 
                    
            if (currentContainer != null) {
                int interiorEnd = currentContainer.getYPosition()
                        + (currentContainer.getHeight() - 1) * Cup.PIXELS_PER_CM;
                if (blocker.getYPosition() >= interiorEnd) break;
            }
            Cup parentCup = findCupWithId(blocker.getId());
            if (parentCup != null) parentCup.removeLid();
            blocker.erase();
            stackingItems.remove(blocker);
            if (parentCup != null && parentCup.canContain(newHeight)) {
                currentContainer = parentCup;
            }
        }
                
        if (currentContainer != null) { //Aterrizaje de la pieza.
            StackingItem blockerInside = findBlockerInContainer(newHeight, currentContainer);
            if (blockerInside != null) { 
                yPos = resolveYPos(blockerInside, newHeight, newHeight);
            } else {
                StackingItem deepest = findDeepestContainer(newHeight);
                if (deepest != null) {
                    yPos = deepest.getYPosition()
                             + (deepest.getHeight() - newHeight - 1) * Cup.PIXELS_PER_CM;
                } else {
                    yPos = (maxHeight - newHeight) * Cup.PIXELS_PER_CM;
                }
            }
        } else {
            StackingItem landingItem = findLandingPiece(newHeight);
            yPos = resolveYPos(landingItem, newHeight, newHeight);
        }
        
        if(yPos < 0){
        	lastOK = false;
            if(isVisible) errorMessage(TowerException.CANT_PUSH_CUP); // Aqui deberia retornar una excepcion
            throw new TowerException(TowerException.CANT_PUSH_CUP);
        }
        
        heightCups += newHeight;
        Opener newCup = new Opener(i, xPos, yPos, getAssociatedColor(i, true));
        lastCup = newCup;
        stackingItems.add(newCup);
        lastOK = true;
        if(isVisible) newCup.makeVisible();
    }
    
    
    /*
     * This Method push a lid of type Fearful
     * @param id id integer of lid
     */
    private void pushFearful(int id) throws TowerException {
    	int lidWidth = 2 * id -1;
        int lidHeight = 1;
        int xPos = xCenter * Cup.getPixelsPerCm() - (lidWidth * Cup.getPixelsPerCm()) / 2;
    	
    	if(!stackItemInteriorExists(id)) {
            if(isVisible) errorMessage(TowerException.INTERIOR_CUP_DONT_EXISTS);
            lastOK = false;
            throw new TowerException(TowerException.INTERIOR_CUP_DONT_EXISTS);
        }
    	StackingItem landingItem = findLandingPiece(lidWidth);
        int yPos = resolveYPos(landingItem, lidWidth, lidHeight);
        
        if (yPos < 0) {
            lastOK = false;
            if (isVisible) errorMessage(TowerException.IS_OUT_SCREEN);
            throw new TowerException(TowerException.IS_OUT_SCREEN);
        }
        
        String color = getAssociatedColor(id, false);
        Fearful newLid = new Fearful(id, xPos, yPos, color);
        stackingItems.add(newLid);
        heightCups += lidHeight;
        lastOK = true;
        if(isVisible) newLid.makeVisible();
        checkAssociatedCup(id, newLid);
    }
    
    /**
     * Delete the last cup at the stackingItems list.
     * @throws TowerException DONT_EXISTS_LASTCUP - This exception happend when there isn't last cup
     */
    public void popCup() throws TowerException{
        boolean areStackingItemsEmpty = stackingItems.isEmpty();
        if(!areStackingItemsEmpty){
            
            int lastCupIdx = lastCup != null ? stackingItems.indexOf(lastCup) : -1;
            if (lastCupIdx >= 0) {
                if(lastCup.getLid() != null){
                    stackingItems.remove(lastCup.getLid());
                    lastCup.removeLid();
                }
                StackingItem removed = stackingItems.remove(stackingItems.indexOf(lastCup));
                simulateAFall(removed);
                lastCup.erase();
                lastCup = null;
                for (int i = stackingItems.size() - 1; i >= 0; i--) {
                    if (stackingItems.get(i).hasInterior()) {
                        lastCup = (Cup) stackingItems.get(i);
                        break;
                    }
                }
            } else if (lastCup == null) {
                if (isVisible) errorMessage(TowerException.DONT_EXISTS_LASTCUP);
                throw new TowerException(TowerException.DONT_EXISTS_LASTCUP);
            }
            
        } else if (areStackingItemsEmpty) {
            if (isVisible) errorMessage(TowerException.NO_ITEMS);
            throw new TowerException(TowerException.NO_ITEMS);
        }
        lastOK = false;
    }
    
    /**
     * Remove a especific cup, the cup that is at the j index of list.
     * @param j j is the index of cup that going to removed
     * @throws TowerException DONT_EXISTS_CUP - Throws when cup isn't founded
     */
    public void removeCup(int j) throws TowerException{
        int idCurrentCup, lenStack;
        StackingItem currentCup;
        lenStack = stackingItems.size();
        for(int i = 0; i< lenStack; i++) {
            currentCup = stackingItems.get(i);
            
            idCurrentCup = currentCup.getId();
            if(idCurrentCup == j && currentCup.hasInterior()){
                Cup cup = (Cup) currentCup;
                if (cup.getLid() != null) {
                    stackingItems.remove(cup.getLid());
                    cup.removeLid();
                }
                StackingItem removed =stackingItems.remove(stackingItems.indexOf(currentCup));                
                removed.erase();
                currentCup = null;
                simulateAFall(removed);
                lastOK = true;
                return;
            } 
        }
        if (isVisible) {
        	lastOK = false;
        	errorMessage(TowerException.DONT_EXISTS_CUP);
        	throw new TowerException(TowerException.DONT_EXISTS_CUP);
        }
        
    }
    /*
     * Simulate the fall of a item when this is removed of tower
     */
    private void simulateAFall(StackingItem item) throws TowerException { // Ayudado con Claude Sonnet 4.6 IA
        int yPosCup = item.getYPosition();
        int removedBottom = yPosCup + item.getHeight() * Cup.PIXELS_PER_CM;
        TreeMap<Integer, StackingItem> itemsInOrder = getInOrderItems();
        List<StackingItem> toReplace = new ArrayList<>();

        // Recoger ítems por encima del borde inferior del item eliminado.
        for (Integer posY : itemsInOrder.keySet()) {
            if (posY < removedBottom) {
                toReplace.add(itemsInOrder.get(posY));
            }
        }

        // FASE 1: borrar visual y quitar de la lista TODOS antes de re-pushear
        for (StackingItem itm : toReplace) {
            itm.erase();
            stackingItems.remove(itm);
        }

        // FASE 2: re-pushear en orden ascendente de la posicion Y
        tempColors = new HashMap<>();
        for(StackingItem itm : toReplace){
            tempColors.put(itm.getId(), itm.getColor());
        }
        for (StackingItem itm : toReplace) {
            if (itm.hasInterior()) {
                pushCup(itm.getType(), itm.getId());
            } else {
                pushLid(itm.getType(), itm.getId());
            }
        }
        tempColors = null;
    }

    
    /**
     * Put a lid of a specific cup, cup at the i index of cups list.
     * @param int Index of cup that it's going to add a lid. 
     * @throws TowerException CANT_PUSH_CUP - Throws when is invalid its push at the tower
     */
    public void pushLid(int i) throws TowerException {
        int lidWidth = 2 * i -1;
        int lidHeight = 1;
        int xPos = xCenter * Cup.PIXELS_PER_CM - (lidWidth * Cup.getPixelsPerCm()) / 2;
        if(lidWidth > maxHeight || stackItemNonInteriorExists(i)){
            if(isVisible) errorMessage(TowerException.CANT_PUSH_LID); 
            lastOK = false;
            throw new TowerException(TowerException.CANT_PUSH_LID);
        }
        
        StackingItem landingItem = findLandingPiece(lidWidth);
        int yPos = resolveYPos(landingItem, lidWidth, lidHeight);
        String color = getAssociatedColor(i, false);
        if (yPos < 0) {
            lastOK = false;
            if (isVisible) 
            	errorMessage(TowerException.IS_OUT_SCREEN);
            throw new TowerException(TowerException.IS_OUT_SCREEN);
        }
        
        
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
     * @throws TowerException 
     */
    public void pushLid(String type, int i) throws TowerException {
        if(stackItemNonInteriorExists(i)){
            if(isVisible) errorMessage(TowerException.INTERIOR_CUP_EXISTS);
            lastOK = false;
            throw new TowerException(TowerException.INTERIOR_CUP_EXISTS);
        }
        
        List<String> types = List.of("normal", "fearful", "crazy");
        boolean isTypeAllowed = types.contains(type);
        
        if (!isTypeAllowed) throw new TowerException(TowerException.NOT_ALLOWED_TYPE);
         // Si no es el tipo permitido se lanza una excepcion
        
        switch (type) {
        
	        case "normal":
	        	pushLid(i);
	        	break;
	        case "fearful":
	        	pushFearful(i);
	        	break;
	        case "crazy":
	        	pushCrazy(i);
	        	break;
        }
   
    }
    
    private void pushCrazy(int id) throws TowerException {
    	String color = getAssociatedColor(id, false);
    	int lidWidth = 2 * id -1;
        int lidHeight = 1;
        int xPos = xCenter * Cup.getPixelsPerCm() - (lidWidth * Cup.getPixelsPerCm()) / 2;
    	
    	if(!stackItemInteriorExists(id)) {
            if(isVisible) errorMessage(TowerException.INTERIOR_CUP_DONT_EXISTS);
            lastOK = false;
            throw new TowerException(TowerException.INTERIOR_CUP_DONT_EXISTS);
        }
    	StackingItem landingItem = findLandingPiece(lidWidth);
        int yPos = resolveYPos(landingItem, lidWidth, lidHeight);
        
        if (yPos < 0) {
            lastOK = false;
            if (isVisible) errorMessage(TowerException.IS_OUT_SCREEN);
            throw new TowerException(TowerException.IS_OUT_SCREEN);
        }
    	
    	yPos += lidWidth + landingItem.getSize();
        Crazy newLid = new Crazy(id, xPos, yPos, color);
        stackingItems.add(newLid);
        if(isVisible) newLid.makeVisible();
        lastOK = true;
        checkAssociatedCup(id, newLid);
	}

	public ArrayList<StackingItem> getStackingItems(){
        return stackingItems;
    }
    
    /**
     * Delete the last lid put at this tower
     */
    public void popLid() throws TowerException {
        boolean areStackingItemsEmpty = stackingItems.isEmpty();
        if(!areStackingItemsEmpty){
            for(int i = stackingItems.size() -1; i >= 0; i--){
                StackingItem currentItem = stackingItems.get(i);
                if(!currentItem.hasInterior()){
                    StackingItem lidToRemove = currentItem;
                    if(lidToRemove.getType().equals("fearful")) {
                        Cup cup = findCup(lidToRemove.getId());
                        if(cup != null && cup.getLid() != null) {
                            lastOK = false;
                            if(isVisible) errorMessage(TowerException.DONT_EXISTS_CUP);
                            throw new TowerException(TowerException.DONT_EXISTS_CUP); 
                        }
                    }
                    stackingItems.remove(lidToRemove);
                    Cup associatedCup = findCup(lidToRemove.getId());
                    if (associatedCup != null && associatedCup.getLid() != null) {
                        associatedCup.removeLid();
                    }else{
                        lidToRemove.erase();
                    }
                    simulateAFall(lidToRemove);
                    lastOK = true;
                    return;
                }
            }
        } else if (areStackingItemsEmpty) {
            if (isVisible) errorMessage(TowerException.NO_ITEMS);
            throw new TowerException(TowerException.NO_ITEMS);
        }
        lastOK = false;
    }
    
    /**
     * Remove a specific lid of a specific cup
     * if the cup selected has lid this is removed
     * otherwise don't remove anythinf
     * @param i i is index of cup that going to remove lid
     */
    public void removeLid(int i) throws TowerException {
        
        for(StackingItem currentItem : stackingItems){
            if(currentItem.getId() == i && !currentItem.hasInterior()){
                if(currentItem.getType().equals("fearful")) {
                    Cup associatedCup = findCup(i);
                    if(associatedCup != null && associatedCup.getLid() != null) {
                        lastOK = false;
                        if(isVisible) errorMessage(TowerException.DOESNT_HAVE_ASSOCIATED_CUP);
                        throw new TowerException(TowerException.DOESNT_HAVE_ASSOCIATED_CUP);
                    }
                }
                    
                if(currentItem != null) {
                    Cup foundCup = findCup(i);
                } else {
                    currentItem.erase();
                }
                simulateAFall(currentItem);
                lastOK = true;
                return;
            }
        }
        lastOk = false;
        if (isVisible) throw new TowerException(TowerException.DONT_EXISTS_LID);
    }
    
    /**
     * Order the stackin items from lowest to highest width.
     * @throws TowerException 
     */
    public void orderTower() throws TowerException {
        TreeMap<Integer, String> cupsIds = new TreeMap<>();
        TreeMap<Integer, String> lidsIds= new TreeMap<>();
        StackingItem fixedItem = null;
        
        for(StackingItem item : stackingItems){
            if(item.isFixed()){
                fixedItem = item;
                continue;
            }
            if(item.hasInterior()){
                cupsIds.put(item.getId(), item.getColor());
            }else{
                lidsIds.put(item.getId(), item.getColor());
            }
        }
        
        for(StackingItem item : stackingItems){
            if(!item.isFixed()) item.erase();
        }
        stackingItems.clear();
        
        if(fixedItem != null){
            stackingItems.add(fixedItem);
            heightCups = fixedItem.getHeight();
        } else{
            heightCups = 0;
        }
        
        for(Integer id : cupsIds.descendingKeySet()){
            pushCup(id, cupsIds.get(id));
            if(lidsIds.containsKey(id)){
                pushLid(id);
            }
        }
    }
    

    private void pushCup(int number, String color) throws TowerException {
        int newHeight = 2 * number - 1;
        int xPos = xCenter * Cup.getPixelsPerCm() - (newHeight * Cup.getPixelsPerCm()) / 2;
        int yPos;

        if(newHeight > maxHeight || stackItemInteriorExists(number)){
            lastOK = false;
            errorMessage(TowerException.CANT_PUSH_CUP); // Aqui deberia retornar una excepcion
            throw new TowerException(TowerException.CANT_PUSH_CUP);
            
        }
        
        if(stackingItems.isEmpty()){
            yPos = (maxHeight - newHeight) * Cup.getPixelsPerCm();
            heightCups = newHeight;
        } else{
            StackingItem landingItem = findLandingPiece(newHeight);
            yPos = resolveYPos(landingItem, newHeight, newHeight);
            if(yPos < 0){
                if(isVisible) errorMessage(TowerException.IS_OUT_SCREEN);
                lastOK = false;
                throw new TowerException(TowerException.IS_OUT_SCREEN);
               
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
     * @throws TowerException 
     */
    public void reverseTower() throws TowerException {
        TreeMap<Integer, String> cupsIds = new TreeMap<>();
        TreeMap<Integer, String> lidsIds= new TreeMap<>();
        StackingItem fixedItem = null;
        
        for(StackingItem item : stackingItems){
            if(item.isFixed()){
                fixedItem = item;
                continue;
            }
            if(item.hasInterior()){
                cupsIds.put(item.getId(), item.getColor());
            }else{
                lidsIds.put(item.getId(), item.getColor());
            }
        }
        
        for(StackingItem item : stackingItems){
            if(!item.isFixed())item.erase();
        }
        stackingItems.clear();
        
        if(fixedItem != null){
            stackingItems.add(fixedItem);
            heightCups = fixedItem.getHeight();
        } else{
            heightCups = 0;
        }
        
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
     * @throws TowerException 
     */
    public void swap(String [] o1, String[] o2) throws TowerException {
        ArrayList<StackingItem> itemsCopy = new ArrayList<>();
        int idO1 = Integer.parseInt(o1[1]);
        int idO2 = Integer.parseInt(o2[1]);
        
        StackingItem item1 = null,item2 = null;
        int idItem;
        for(StackingItem s : stackingItems){
        	idItem =  s.getId();
            if(idItem == idO1) item1 = s;
            if(idItem == idO2) item2 = s;
        }
        
        if(item1 == null || item2 == null){
            lastOK = false;
            if(isVisible) errorMessage(TowerException.DONT_EXISTS_OBJECTS_TO_SWAP);
            throw new TowerException(TowerException.DONT_EXISTS_OBJECTS_TO_SWAP);
        }
        
        if(item1.isFixed() || item2.isFixed()){
            lastOK = false;
            if(isVisible) errorMessage(TowerException.ITEM_IS_FIXED);
            throw new TowerException(TowerException.ITEM_IS_FIXED);

        }
        
        int indx1 = stackingItems.indexOf(item1);
        int indx2 = stackingItems.indexOf(item2);
        Collections.swap(stackingItems, indx1, indx2);
        itemsCopy.addAll(stackingItems);
        
        eraseItems();
        
        for(StackingItem s : itemsCopy){
            if(s.hasInterior()){
                String cupType = s.getType();
                if(cupType.equals("normal")) {
                    pushCup(s.getId());
                } else {
                    pushCup(cupType, s.getId());
                }
                
            }else {
               String lidType = s.getType();
               if(lidType.equals("normal")) {
                   pushLid(s.getId());
               } else {
                   pushLid(lidType, s.getId());
               }
            }
        }
        lastOK = true;
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
     * Rebuild the tower according to the insertion of elements.
     * Save the cups and lids like looks in StackingItems.
     * It deletes the StakcingItems (but not fixed pieces) and reassembled by 
     * executing pushCup for each cup in that order with its lid, if this exists.
     * @throws TowerException 
     */
    public void cover() throws TowerException {
        if(stackingItems.isEmpty()){
            lastOK = false;
            if(isVisible) errorMessage(TowerException.NO_ITEMS);
            throw new TowerException(TowerException.NO_ITEMS);
        }
        
        ArrayList<Integer> cupIdsInsert = new ArrayList<>();
        ArrayList<String> cupTypesInsert = new ArrayList<>();
        ArrayList<String> colorsInsert = new ArrayList<>();
        HashMap<Integer, String> lidTypesInsert = new HashMap<>();
        StackingItem fixedItem = null;
        
        for (StackingItem item : stackingItems) {
            if (item.isFixed()) {
                fixedItem = item;
                continue;
            }
            if (item.hasInterior()) {
                cupIdsInsert.add(item.getId());
                cupTypesInsert.add(item.getType());
                colorsInsert.add(item.getColor());
            } else {
                lidTypesInsert.put(item.getId(), item.getType());
            }
        }
        
        if (cupIdsInsert.isEmpty()) {
        	lastOK = false;
            if(isVisible) errorMessage(TowerException.NO_ITEMS_TO_COVER);
            throw new TowerException(TowerException.NO_ITEMS_TO_COVER);
        }
        
        for (StackingItem item : stackingItems) {
            if (!item.isFixed()) {
                item.erase();
            }
        }
        stackingItems.clear();
        
        if (fixedItem != null) {
            stackingItems.add(fixedItem);
            heightCups = fixedItem.getHeight();
        } else {
            heightCups = 0;
        }
        
        for (int i = 0; i < cupIdsInsert.size(); i++) {
            int id = cupIdsInsert.get(i);
            String cupType = cupTypesInsert.get(i);
            String color = colorsInsert.get(i);
            if("normal".equals(cupType)){
                pushCup(id, color);
            } else {
                pushCup(cupType, id);
            }
            if (!lastOK) {
                if (isVisible) errorMessage(TowerException.LAST_ISNT_OK);
                throw new TowerException(TowerException.LAST_ISNT_OK);
            }
            if (lidTypesInsert.containsKey(id)) {
                String lidType = lidTypesInsert.get(id);
                if("normal".equals(lidType)) {
                    pushLid(id);
                } else {
                    pushLid(lidType, id);
                }
                if (!lastOK) {
                	if (isVisible) errorMessage(TowerException.LAST_ISNT_OK);
                    throw new TowerException(TowerException.LAST_ISNT_OK);
                }
            }
        }
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
     * @throws TowerException 
     */
    public int[] lidedCups() throws TowerException {
        int i = 0, lenItems = stackingItems.size(), currentWidthItem;
        boolean isLidsEmpty = stackingItems.isEmpty();
        
        if (isLidsEmpty ) {
        	lastOK = false;
        	if (isVisible) errorMessage(TowerException.NO_ITEMS);
            throw new TowerException(TowerException.NO_ITEMS);
        } 
        
        
        List<Integer> ids = new ArrayList<>(); // Se crea una List para organizar las anchuras de las lids
        for (StackingItem item : stackingItems) {
            if (item.hasInterior() && item.getLid() != null) {
                currentWidthItem = item.getId();
                ids.add(currentWidthItem);
            }
        }
        
        Collections.sort(ids);
        
        int [] result = new int[ids.size()]; 
        for (int widthLid : ids) {
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
     * @throws TowerException 
     * 
     */
    public String[][] swapToReduce() throws TowerException {
        if (stackingItems.isEmpty()) {
            lastOK = false;
            if(isVisible) errorMessage(TowerException.NO_ITEMS);
            return new String[0][0];
        }
        
        ArrayList<Integer> cupIds = new ArrayList<>();
        ArrayList<String> colors = new ArrayList<>();
        ArrayList<Integer> lidIds = new ArrayList<>();
        StackingItem fixedItem = null;
        
        for (StackingItem item : stackingItems) {
            if (item.isFixed()) {
                fixedItem = item;
                continue;
            }
            if (item.hasInterior()) {
                cupIds.add(item.getId());
                colors.add(item.getColor());
            } else {
                lidIds.add(item.getId());
            }
        }
        
        if (cupIds.size() < 2) {
            lastOK = false;
            if (isVisible) errorMessage(TowerException.FEW_CUPS);
            return new String[0][0];
        }
        
        int originalHeight = heightUsed();
        int bestHeight = originalHeight;
        String[][] bestMove = new String[0][0];
        
        for (int i = 0; i < cupIds.size(); i++) {
            for (int j = i + 1; j < cupIds.size(); j++) {
                int id1 = cupIds.get(i);
                int id2 = cupIds.get(j);
                if (id1 == id2) continue;
                
                resetTower(fixedItem);
                for (int k = 0; k < cupIds.size(); k++) {
                    int cupId = cupIds.get(k);
                    String color = colors.get(k);
                    pushCup(cupId, color);
                    if (!lastOK) break;
                    if (lidIds.contains(cupId)) {
                        pushLid(cupId);
                        if (!lastOK) break;
                    }
                }
                if (!lastOK) continue;
                
                swap(new String[]{"cup", String.valueOf(id1)},
                     new String[]{"cup", String.valueOf(id2)});
                
                if (!lastOK) continue;
                
                int currentHeight = heightUsed();
                if (currentHeight < bestHeight) {
                    bestHeight = currentHeight;
                    bestMove = new String[][]{
                        {"cup", String.valueOf(id1)},
                        {"cup", String.valueOf(id2)}
                    };
                }
            }
        }
        resetTower(fixedItem);
        for (int i = 0; i < cupIds.size(); i++) {
            int cupId = cupIds.get(i);
            String color = colors.get(i);
            pushCup(cupId, color);
            if (!lastOK) break;
            if (lidIds.contains(cupId)) {
                pushLid(cupId);
                if (!lastOK) break;
            }
        }
        lastOK = bestHeight < originalHeight;
        return bestMove;
    }
    
    /*
     * Restart the Tower before rebuild it again.
     * Delete visually all elements tha are not fixed.
     * Clears the StackingItems.
     */
    private void resetTower(StackingItem fixedItem){
        for (StackingItem item : stackingItems) {
            if (!item.isFixed()) {
                item.erase();
            }
        }
        stackingItems.clear();
        
        if (fixedItem != null) {
            stackingItems.add(fixedItem);
            heightCups = fixedItem.getHeight();
        } else {
            heightCups = 0;
        }
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
     * @throws TowerException 
     */
    public void makeInvisible() throws TowerException {
        if (isVisible) isVisible = !isVisible;
        for (StackingItem s : stackingItems) {
            s.makeInvisible();
        }
        makeInvisibleRuler();
    }
    
    /*
     * This method makes the ruler invisible
     * @throws TowerException RULER_DONT_EXISTS - If ruler don´t exists it can't make invisible 
     */
    private void makeInvisibleRuler() throws TowerException {
		if (!isCreatedRuler) throw new TowerException(TowerException.RULER_DONT_EXISTS);
		for (Rectangle r : ruler) {
			if (!isVisible) break;
			r.makeInvisible();
		}
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
     * Finds cup with id cup.
     * @param idCup idCup is the id that going to search at staking items.
     */
    public Cup findCup(int idCup) {
        
        for (StackingItem s : stackingItems) {
            if (s.getId() == idCup && s.hasInterior()) {
                return (Cup) s;
            }
        }
        return null;
    }
    
    /*
     * Finds lid with id cup.
     * @param idCup idCup is the id that going to search at staking items.
     */
    public Lid findLid(int idLid) {
        
        for (StackingItem s : stackingItems) {
            if (s.getId() == idLid && !s.hasInterior()) {
                return (Lid) s;
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
        int randIndexColor = random.nextInt(colors.size());
        System.out.println(colors.size());
        String color = colors.get(randIndexColor);
        colors.remove(color);
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
    private void errorMessage(String messageError) {
        if (isVisible) {
            JOptionPane.showMessageDialog( 
            null, 
            messageError,  // Mensaje de la ventana
            "Invalid Action",  //Mensaje del titulo de la ventana
            JOptionPane.ERROR_MESSAGE
            );
        }
    }
    
    /*
     * Generate the ruler of StackingCups
     */
    private final void generateRuler() { // Ayudado por Gemini IA 2026 pero revisado
        
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
     * Inicializate atributes needed to build a tower 
     * @param width width is the atribute of tower to inicializate
     * @param height height is the atribute of tower going to inicializate 
     */
    private final void inicializate(int width, int height) {
        this.width = width;
        this.maxHeight = height;
        stackingItems = new ArrayList<>();
        xCenter = (int) Math.ceil((double) width / 2);
    }
    
    /*
     * Set deterninated cups at the tower 
     */
    private final void generateCupsInTower(int cupsRequeried) throws TowerException {
        for (int i = 1; i <= cupsRequeried; i++) {
            pushCup(i);
        }
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
                    && (deepestInner == null || s.getYPosition() > deepestInner.getYPosition())){
                    deepestInner = s;
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

    /*
     * Find items inside a Cup that blocks the way of a opener Cup.
     * GENERATED BY IA - GEMINI.
     */
    private StackingItem findBlockerInContainer(int fallingWidth, Cup container) {
        StackingItem blocker = null;
        int minY = Integer.MAX_VALUE;
        int containerTop = container.getYPosition();
        int containerBottom = containerTop + container.getHeight() * Cup.PIXELS_PER_CM;
        for (StackingItem s : stackingItems) {
            if (s == container) continue;
            int sBottom = s.getYPosition() + s.getHeight() * Cup.PIXELS_PER_CM;
            if (s.getYPosition() >= containerTop && sBottom <= containerBottom
                && s.blocksPassage(fallingWidth) && s.getYPosition() < minY) {
                blocker = s;
                minY = s.getYPosition();
            }
        }
        return blocker;
    }
    
    /*
     * Push a heriarichal cup in the Tower.
     * @param i Id of the cup.
     * @param xPos Horizontal position.
     * @param newHeight Height of the cup.
     */
    private void pushHierarchical(int i, int xPos, int newHeight){
        Cup container = null;
        int yPos;
        for(StackingItem item : stackingItems){
            if(item.hasInterior() && item.getId() > i){
                if(container == null || item.getId() < container.getId()){
                    container = (Cup)item;  
                }
            }
        }
        
        for(StackingItem item : stackingItems){
            if(item.hasInterior() && item.getId() < i){
                Cup cup = (Cup) item;
                cup.move(cup.getXPosition(), cup.getYPosition()- Cup.PIXELS_PER_CM);
            }
        }
                
        if(container != null) {
            yPos = container.getYPosition() + (container.getHeight() - newHeight -1) * Cup.PIXELS_PER_CM;
        } else{
            yPos = (maxHeight - newHeight) * Cup.PIXELS_PER_CM;
        }
                
        heightCups += newHeight;
                
        Hierarchical newCup = new Hierarchical(i, xPos, yPos, getAssociatedColor(i, true));
        if(container == null) newCup.setFixed(true);
        lastCup = newCup;
        stackingItems.add(newCup);
        lastOK = true;
        if (isVisible) newCup.makeVisible();
    }
    
    /*
     * Get the color for insertion item.
     * For Cup, check if there is a lid with same id.
     * For Lid, check if there is a cup with same id.
     * If has an element with same id get that color
     * otherwise, take a random color.
     */
    private String getAssociatedColor(int number, boolean insertsCup) {
        if(tempColors != null && tempColors.containsKey(number)){
            return tempColors.get(number);
        }
        for(StackingItem item : stackingItems) {
            if(item.getId() == number && item.hasInterior() != insertsCup) {
                String color = item.getColor();
                if(color != null) {
                    return color;
                }
                break;
            }
        }
        return getRandColor();
    }

}