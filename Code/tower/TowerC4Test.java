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
    Tower t;
    
    @BeforeEach
    public void setUp(){
        t = new Tower(100, 100);
    }
    
    @Test
    public void shouldBeCreateCupOpener() {
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
    
    @Test
    public void shouldPushAHierarchichalCup() throws tower.TowerException{
        t.pushCup("Hierarchical", 6);
        assertEquals(Hierarchical.class, t.getStackingItems().get(0).getClass());
    }
    
    @Test
    public void shouldInsertHierarchical() throws tower.TowerException{
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
    public void shouldntMoveHierarchicalOnceBottom() throws tower.TowerException{
        t.pushCup(4);
        t.pushCup(3);
        t.pushCup(2);
        t.pushCup(1);
        t.pushCup("hierarchical", 5);
            
        t.swap(new String[]{"cup", "4"}, new String[]{"cup", "1"});
            
        String[][] stackingItems = t.stackingItems();
        String[][] expected = {
        {"cup", "5"},
        {"cup", "4"},
        {"cup", "3"},
        {"cup", "2"},
        {"cup", "1"}};
            
        assertArrayEquals(expected, stackingItems);
    }
}
