package tower;



import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
            t.pushLid(6);
            t.pushCup("opener", 3); // Should destroy the cup to entry
            
            assertEquals(2, t.getStackingItems().size());
        } 
        catch (tower.TowerException te) {
            te.printStackTrace();
        }
    }
    
}