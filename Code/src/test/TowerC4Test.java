package test;

 



import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.Hierarchical;
import domain.Opener;
import domain.Tower;
import domain.TowerException;

import java.util.ArrayList;

/**
 * The test class TowerC4Test.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TowerC4Test {
    Tower t;
    
    @BeforeEach
    public void setUp(){
        t = new Tower(100, 100);
    }
    
    @Test
    public void shouldBeCreateCupOpener() {
        t.pushCup("opener", 3);
        assertEquals( Opener.class, t.getStackingItems().get(0).getClass());
        assertEquals( Opener.class, t.getStackingItems().get(0).getClass());
        
        
    }
    
    @Test
    public void shouldBePutCorretlyCupOpener() {
        t.pushCup(6);
        t.pushCup(5);
        t.pushCup(4);
        t.pushLid(4);
        t.pushLid(5);
        t.pushLid(6);
            
            
        t.pushCup("opener", 3); // Should destroy the cup to entry
        
        String[][] expected = {
        {"cup", "6"},
        {"cup", "5"},
        {"cup", "4"},
        {"cup", "3"}};
        
        String[][] stackingItems = t.stackingItems();
        assertEquals(4, stackingItems.length);
        assertArrayEquals(expected, stackingItems);
    }
    
    @Test
    public void shouldBePutCorretlyCupOpenerWithCupsInDesorder() throws TowerException {
        t.pushCup(3);
        t.pushLid(3);
        t.pushCup(4);
        t.pushLid(4);
            
            
        t.pushCup("opener", 2); // Should destroy the cup to entry
        
        String[][] expected = {
        {"cup", "3"},
        {"lid", "3"},
        {"cup", "4"},
        {"cup", "2"}};
        
        String[][] stackingItems = t.stackingItems();
        assertEquals(4, stackingItems.length);
        assertArrayEquals(expected, stackingItems);
    }
    
    @Test
    public void shouldBePutCorretlyCupOpenerWithCupsInDesorder2() throws TowerException{
        t.pushCup(7);
        t.pushCup(1);
        t.pushCup(2);
        t.pushCup(3);
        t.pushLid(7);
        t.pushLid(2);
        t.pushLid(3);
        t.pushCup("opener", 5);
        
        String[][] expected = {
        {"cup", "7"},
        {"cup", "1"},
        {"cup", "2"},
        {"cup", "3"},
        {"cup", "5"}};
        String[][] stackingItems = t.stackingItems();
        assertArrayEquals(expected, stackingItems);
    }
    
    @Test
    public void shouldPushAHierarchichalCup() throws TowerException{
        t.pushCup("hierarchical", 6);
        assertEquals(Hierarchical.class, t.getStackingItems().get(0).getClass());
    }
    
    @Test
    public void shouldInsertHierarchical() throws TowerException{
        t.pushCup(6);
        t.pushCup(4);
        t.pushCup(3);
        t.pushCup(2);
        t.pushCup(1);
            
        t.pushCup("hierarchical", 5);
            
        String[][] stackingItems = t.stackingItems();
        assertArrayEquals(new String[]{"cup", "6"}, stackingItems[0]);
        assertArrayEquals(new String[]{"cup", "5"}, stackingItems[1]);
        assertArrayEquals(new String[]{"cup", "4"}, stackingItems[2]);
        assertArrayEquals(new String[]{"cup", "3"}, stackingItems[3]);
        assertArrayEquals(new String[]{"cup", "2"}, stackingItems[4]);
        assertArrayEquals(new String[]{"cup", "1"}, stackingItems[5]);
    }
    
    @Test
    public void shouldntMoveHierarchicalOnceBottomAfterSwap() throws TowerException{
        t.pushCup(4);
        t.pushCup(3);
        t.pushCup(2);
        t.pushCup(1);
        t.pushCup("hierarchical", 5);
            
        t.swap(new String[]{"cup", "5"}, new String[]{"cup", "1"});
            
        String[][] stackingItems = t.stackingItems();
        String[][] expected = {
        {"cup", "5"},
        {"cup", "4"},
        {"cup", "3"},
        {"cup", "2"},
        {"cup", "1"}};
            
        assertArrayEquals(expected, stackingItems);
    }
    
    @Test
    public void shouldntMoveHierarchichalOnceBottomAfterReverseTower(){
        t.pushCup(4);
        t.pushCup(3);
        t.pushCup(2);
        t.pushCup(1);
        t.pushCup("hierarchical", 5);
        t.reverseTower();
        
        String[][] expected = {
        {"cup", "5"},
        {"cup", "1"},
        {"cup", "2"},
        {"cup", "3"},
        {"cup", "4"}};
        
        assertArrayEquals(expected, t.stackingItems());
    }
    
    @Test
    public void shouldntMoveHierarchichalOnceBottomAfterOrder(){
        t.pushCup(1);
        t.pushCup(2);
        t.pushCup(3);
        t.pushCup(4);
        t.pushCup("hierarchical", 5);
        
        t.orderTower();
        
        String[][] expected = {
        {"cup", "5"},
        {"cup", "4"},
        {"cup", "3"},
        {"cup", "2"},
        {"cup", "1"}};

        assertArrayEquals(expected, t.stackingItems());
    }
    
    @Test
    public void shouldnIsertFearfulLidWithHisCup() {
        t.pushCup(2);
        t.pushLid("fearful", 3);
        
        int totalItems = t.stackingItems().length;
        int expectedItems = 1;
        boolean lastOperationOk = t.ok();
        
        assertFalse(lastOperationOk);
        assertEquals(expectedItems, totalItems);
    }
    
    @Test
    public void shouldInsertFearfulLid() throws TowerException{
        t.pushCup(6);
        t.pushCup(4);
        t.pushCup(3);
            
        t.pushLid("fearful", 4);
            
        String[][] stackingItems = t.stackingItems();
        assertTrue(t.ok());
        assertArrayEquals(new String[]{"cup", "6"}, stackingItems[0]);
        assertArrayEquals(new String[]{"cup", "4"}, stackingItems[1]);
        assertArrayEquals(new String[]{"cup", "3"}, stackingItems[2]);
        assertArrayEquals(new String[]{"lid", "4"}, stackingItems[3]);
    }
    
    @Test
    public void shouldntRemoveFearfulLidIfItIsOnHisCup() {
        t.pushCup(2);
        t.pushCup(3);
        t.pushLid("fearful",3);
        
        t.removeLid(3);
        assertFalse(t.ok());
        
        int totalItems = t.stackingItems().length;
        int expectedItems = 3;
        assertEquals(expectedItems, totalItems);
    }
    
    @Test
    public void shouldPopFearuflLidIfItIsOnHisCup() {
        t.pushCup(2);
        t.pushCup(3);
        t.pushLid("fearful",3);
        
        t.popLid();
        assertFalse(t.ok());
        int totalItems = t.stackingItems().length;
        int expectedItems = 3;
        assertEquals(expectedItems, totalItems);
    }
    
    @Test
    public void shoudlSwapHierarchicalWithNormalCup() {
        t.pushCup(6);
        t.pushCup(5);
        t.pushCup("hierarchical", 4);
        // A este punto el orden seria 6, 5, 4
        t.swap(new String[] {"cup", "5"}, new String[]{"cup", "4"});
        // Orden despues de swap 6, 4, 5
        
        String[][] expected = {
                {"cup", "6"},
                {"cup", "4"},
                {"cup", "5"}};
        assertTrue(t.ok());
        assertArrayEquals(expected, t.stackingItems());
        
    }
    
    @Test
    public void shouldSwapFearfulLidWithCup() {
        t.pushCup(3);
        t.pushCup(4);
        t.pushLid("fearful", 3);
        
        t.swap(new String[] {"cup", "4"}, new String[]{"lid", "3"});
        
        String[][] expected = {
                {"cup", "3"},
                {"lid", "3"},
                {"cup", "4"}};
        
        assertTrue(t.ok());
        assertArrayEquals(expected, t.stackingItems());
    }
}
