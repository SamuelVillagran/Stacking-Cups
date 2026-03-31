package tower;



import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class TowerC3Test.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TowerC3Test {
    

    @Test
    public void shouldBeImpossibleToSolve() {
        Tower t = new Tower(100, 100);
        
        assertEquals(t.icpcProblem("4 100"), "impossible");
        
    }
    
    @Test 
    public void shouldBeGiveSolution() {
        Tower t = new Tower(100, 100);
        
        assertEquals(t.icpcProblem("4 9"), "7 3 5 1");
        
    }
}
