package domain;



import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class TowerTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TowerTest {

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp() {
    }
    
    @Test
    public void shouldEliminateLid() {
        Tower proofTower = new Tower(5, 15);
        proofTower.pushCup(0);
        proofTower.pushLid(0);
        proofTower.removeLid(0);
        
        Lid lidOfCup = proofTower.getCups().get(0).getLid();
        assertNull(lidOfCup);
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