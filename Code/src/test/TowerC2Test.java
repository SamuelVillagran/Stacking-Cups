package test;



import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class TowerC2Test.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TowerC2Test {
    //======================================================
    // Caso 1: Se crea el constructor correctamente
    //======================================================
    @Test
    public void shouldCreateTowerWiTthNumCups() {
        /*
        Tower proof = new Tower(3);
        int numCupsInTower = proof.getCups().size();
        assertEquals(3, numCupsInTower);
        */
    }
    
    @Test
    //Testing new Constructor.
    public void shouldCreateTowersIncrementally(){
        Tower proofTower = new Tower(4);
        String[][] result = proofTower.stackingItems();
        
        assertArrayEquals(new String[]{"cup", "1"}, result[0]);
        assertArrayEquals(new String[]{"cup", "2"}, result[1]);
        assertArrayEquals(new String[]{"cup", "3"}, result[2]);
        assertArrayEquals(new String[]{"cup", "4"}, result[3]);
    }
    
    @Test
    public void shouldSwapLidWithHisCup(){
        Tower proofTower = new Tower(13, 13);        
        proofTower.pushCup(2);
        proofTower.pushCup(3);
        
        //Verificamos el primer swap
        proofTower.swap(new String[]{"cup", "3"}, new String[]{"cup", "2"});
        String[][] result = proofTower.stackingItems();
        assertArrayEquals(new String[]{"cup", "3"}, result[0]);
        assertArrayEquals(new String[]{"cup", "2"}, result[1]);
        
        //verificamos el segundo swap con los mismo elelmentos
        proofTower.swap(new String[]{"cup", "3"}, new String[]{"cup", "2"});
        result = proofTower.stackingItems();
        assertArrayEquals(new String[]{"cup", "2"}, result[0]);
        assertArrayEquals(new String[]{"cup", "3"}, result[1]);
    }
    
    @Test
    public void shouldSwapTwoCups(){
        Tower proofTower = new Tower(13, 13);
        proofTower.pushCup(4);
        proofTower.pushCup(3);
        proofTower.pushCup(2);
        proofTower.pushCup(1);
        
        proofTower.swap(new String[]{"cup", "1"}, new String[]{"cup", "4"});
        String[][] result = proofTower.stackingItems();
        String[][] expected = new String[][]{
            {"cup","1"},
            {"cup","3"},
            {"cup", "2"},
            {"cup", "4"}};
            
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void shouldCoverACup(){
        Tower proofTower = new Tower(15, 15);
        proofTower.pushCup(1);
        proofTower.pushCup(2);
        proofTower.pushCup(3);
        proofTower.pushLid(2);
        
        proofTower.cover();
        String[][] result = proofTower.stackingItems();
        String[][] expected = new String[][]{
            {"cup", "1"},
            {"cup", "2"},
            {"lid", "2"},
            {"cup", "3"}};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void shouldCoverACupInALongStack(){
        Tower proofTower = new Tower(15, 15);
        proofTower.pushCup(1);
        proofTower.pushCup(2);
        proofTower.pushCup(3);
        proofTower.pushLid(2);
    }
    
    @Test
    public void shouldSuggestASwapToReduceHeight(){
        Tower proofTower = new Tower(15, 15);
        proofTower.pushCup(1);
        proofTower.pushCup(2);
        proofTower.pushCup(3);
        proofTower.pushCup(4);
        
        int initialHeight = proofTower.heightUsed();
        String[][] resultSwap = proofTower.swapToReduce();
        
        assertEquals(2, resultSwap.length);
        assertEquals("cup", resultSwap[0][0]);
        assertEquals("cup", resultSwap[1][0]);
        
        proofTower.swap(resultSwap[0], resultSwap[1]);
        int reduceHeight = proofTower.heightUsed();
        assertTrue(reduceHeight < initialHeight);
    }
}
