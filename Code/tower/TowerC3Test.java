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
public class TowerC3Test
{
    /**
     * Default constructor for test class TowerC3Test
     */
    public TowerC3Test() {
        
    }

    
    
    
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
    }

    @Test
    public void shouldBeImpossibleToSolve() {
        Tower t = new Tower(100, 100);
        
        assertEquals(t.icpcProblem("4 100"), "impossible");
        
    }
    
    
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
    }
}
