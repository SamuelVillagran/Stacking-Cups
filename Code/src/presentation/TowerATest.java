package presentation;



import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.Lid;
import domain.StackingItem;
import domain.Tower;

import java.util.ArrayList;

/**
 * The test class TowerATest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TowerATest {
    
    @Test
    public void shouldBeRemovedCorrectlyCup() {
        Tower t = new Tower(30, 30);
        t.makeVisible();
        try {
            t.pushCup(6);
            t.pushCup(5);
            t.pushCup(4);
            t.pushLid(4);
            t.pushLid(5);
            t.pushLid(6);
            t.pushLid(3);
                 
            t.removeCup(5);
            assertTrue(t.ok());
        } 
        catch (Exception te) {
            te.printStackTrace();
        }
    }
    
    @Test
    public void shouldBePutCorretlyCupOpener() {
        Tower t = new Tower(30, 30);
        t.makeVisible();
        try {
            t.pushCup(6);
            t.pushCup(5);
            t.pushCup(4);
            t.pushLid(4);
            t.pushLid(5);
            t.pushLid(6);
            
            
            t.pushCup("opener", 3); // Should destroy the cup to entry
            
            ArrayList<StackingItem> stackingItems = t.getStackingItems();
            Lid lidOfCup6 = stackingItems.get(0).getLid(); // Lid of cup with id = 6
            Lid lidOfCup5 = stackingItems.get(1).getLid(); // Lid of cup with id = 5
            Lid lidOfCup4 = stackingItems.get(2).getLid(); // Lid of cup with id = 4
            
            t.removeCup(5);
            t.removeCup(6);
            assertTrue(t.ok());
        } 
        catch (Exception te) {
            te.printStackTrace();
        }
    }
}
