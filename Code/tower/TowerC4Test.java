package tower;



import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

/**
 * The test class TowerC4Test.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TowerC4Test {
    
    @Test
    public void shouldBeCreateCupOpener() {
        Tower t = new Tower(100, 100);
        
        try {
            t.pushCup("opener", 3);
            
            assertEquals( Opener.class, t.getStackingItems().get(0).getClass());
        } 
        catch (tower.TowerException te) {
            te.printStackTrace();
        }
        
    }
    
    @Test
    public void shouldBePutCorretlyCupOpener() {
        Tower t = new Tower(100, 100);
        
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
            assertEquals(4, stackingItems.size());
            assertNull(lidOfCup6);
            assertNull(lidOfCup5);
            assertNull(lidOfCup4);
        } 
        catch (tower.TowerException te) {
            te.printStackTrace();
        }
    }
    
}