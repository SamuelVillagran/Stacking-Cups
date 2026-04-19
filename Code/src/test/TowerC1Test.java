package test;


import domain.Cup;
import domain.Lid;
import domain.StackingItem;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.Tower;

import java.util.ArrayList;

/**
 * The test class TowerTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class TowerC1Test {

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp() {
    }
    
    @Test
    public void shouldCreateAnEmptyTower(){
        Tower proofTower = new Tower(13, 13);
        
        int totalElements = proofTower.stackingItems().length;
        assertEquals(0, totalElements);
    }
    
    @Test
    public void shouldPushCupsInOrder(){
        Tower proofTower = new Tower(13,13);
        proofTower.pushCup(4); //Cup grande, va primero
        proofTower.pushCup(3); //Cup va sobre Cup4
        proofTower.pushCup(2); //Cup va sobre Cup3
        proofTower.pushCup(1); //Cup va sobre Cup2
        
        String[][] result = proofTower.stackingItems();
        assertArrayEquals(new String[]{"cup", "4"}, result[0]);
        assertArrayEquals(new String[]{"cup", "3"}, result[1]);
        assertArrayEquals(new String[]{"cup", "2"}, result[2]);
        assertArrayEquals(new String[]{"cup", "1"}, result[3]);
        assertTrue(proofTower.ok());
    }
    
    @Test
    public void shouldPushCupInDisorder(){
        Tower proofTower = new Tower(13, 13);
        proofTower.pushCup(1);
        proofTower.pushCup(2);
        proofTower.pushCup(3);
        
        String[][] result = proofTower.stackingItems();
        String[][] expected = {
        {"cup", "1"},
        {"cup", "2"},
        {"cup", "3"}};
        
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void shouldStoreACupInsideAnother(){
        Tower proofTower = new Tower(13, 13);
        proofTower.pushCup(2);
        proofTower.pushCup(3);
        proofTower.pushCup(1);
        
        String[][] result = proofTower.stackingItems();
        String[][] expected = {
        {"cup", "2"},
        {"cup", "3"},
        {"cup", "1"}};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void shouldIgnoreDuplicatedCup(){
        Tower proofTower = new Tower(13,13);
        proofTower.pushCup(4);
        proofTower.pushCup(4);
        
        String[][] result = proofTower.stackingItems();
        
        assertEquals(1, result.length);
        assertFalse(proofTower.ok());
    }
    
    @Test
    public void shouldIgnoreDuplicatedLid(){
        Tower proofTower = new Tower(13, 13);
        proofTower.pushCup(3);
        proofTower.pushLid(3);
        proofTower.pushLid(3);
        
        String[][] result = proofTower.stackingItems();
        assertEquals(2, result.length);
        assertFalse(proofTower.ok());
    }
    
    @Test
    public void shouldPushLidWithCup(){
        Tower proofTower = new Tower(13,13);
        proofTower.pushCup(4);
        proofTower.pushLid(4);
        
        String[][] result = proofTower.stackingItems();
        
        assertArrayEquals(new String[]{"cup", "4"}, result[0]);
        assertArrayEquals(new String[]{"lid", "4"}, result[1]);
    }
    
    @Test
    public void shouldNotAddCupsMoreThanTheHeightTowerAllow(){
        Tower proofTower = new Tower(13,13);
        proofTower.pushCup(1);
        proofTower.pushCup(2);
        proofTower.pushCup(3);
        proofTower.pushCup(4);
        
        String[][] result = proofTower.stackingItems();
        
        assertFalse(proofTower.ok());
        assertEquals(3, result.length);
    }
    
    @Test
    public void shouldOrderTower(){
        Tower proofTower = new Tower(13,13);
        proofTower.pushCup(1);
        proofTower.pushCup(2);
        proofTower.pushCup(3);
        
        proofTower.orderTower();
        String[][] result = proofTower.stackingItems();
        
        assertArrayEquals(new String[]{"cup", "3"}, result[0]);
        assertArrayEquals(new String[]{"cup", "2"}, result[1]);
        assertArrayEquals(new String[]{"cup", "1"}, result[2]);
    }
    
    @Test
    public void shouldReverseTower(){
        Tower proofTower = new Tower(13,13);
        proofTower.pushCup(3);
        proofTower.pushCup(2);
        proofTower.pushCup(1);
        
        proofTower.reverseTower();
        String[][] result = proofTower.stackingItems();
        
        assertArrayEquals(new String[]{"cup", "1"}, result[0]);
        assertArrayEquals(new String[]{"cup", "2"}, result[1]);
        assertArrayEquals(new String[]{"cup", "3"}, result[2]);
    }
    
    @Test
    public void shouldRemoveLid() {
        Tower proofTower = new Tower(2, 15);
        proofTower.pushCup(0);
        proofTower.pushLid(0);
        proofTower.removeLid(0);
        
        StackingItem lidOfCup = proofTower.getStackingItems().get(0).getLid();
        assertNull(lidOfCup);
    }
    
    @Test
    public void shouldPopCup(){
        Tower proofTower = new Tower(13,13);
        proofTower.pushCup(4);
        proofTower.pushCup(3);
        proofTower.pushCup(2);
        proofTower.pushCup(1);
        
        proofTower.popCup();
        String[][] result = proofTower.stackingItems();
        String[][] expected = {
        {"cup", "4"},
        {"cup", "3"},
        {"cup", "2"}};
        assertTrue(proofTower.ok());
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void shouldPopLid() {
        Tower proofTower = new Tower(5, 15);
        //proofTower.pushCup(0);
        proofTower.pushLid(0);
        proofTower.popLid();
        
        //Lid lidOfCup = proofTower.getCups().get(0).getLid();
        //assertNull(lidOfCup);
    }
    
    @Test
    public void shouldLidedCupsCorrectly() {
        Tower proofTower = new Tower(50, 50);
        assertNull(proofTower.lidedCups());
        
        proofTower.pushCup(1);
        proofTower.pushCup(2);
        proofTower.pushCup(5);
        proofTower.pushCup(3);
        proofTower.pushCup(9);
        proofTower.pushCup(7);
        
        proofTower.pushLid(2); // indice 0
        proofTower.pushLid(5); // indice 1
        proofTower.pushLid(3); // indice 2
        proofTower.pushLid(9); // indice 3
        proofTower.pushLid(7); // indice 4
        
        int[] lidsSorted = proofTower.lidedCups();
        
        ArrayList<StackingItem> lids = proofTower.getStackingItems();
        assertEquals(lids.get(0).getWidth(), lidsSorted[0]);
        assertEquals(lids.get(1).getWidth(), lidsSorted[1]);
        assertEquals(lids.get(3).getWidth(), lidsSorted[2]); // Se alternan, va primero el indice 3
        assertEquals(lids.get(2).getWidth(), lidsSorted[3]);
        assertEquals(lids.get(5).getWidth(), lidsSorted[4]); // Se alternan, va primero el indice 5
        assertEquals(lids.get(4).getWidth(), lidsSorted[5]);
        
    } //  Incluir el caso en el que este la taza pero no la tapa (id - 1)
    
    // =========================================================
    // CASO 1: Sin cups ni lids → resultado vacío, no null, no explota
    // =========================================================

    @Test
    public void shouldGiveSomethingStickingItemsInZero() { // Generado con Caulde IA Sonet 4.6 2026 pero corregido
        // Con 0 cups y 0 lids, el método no debe lanzar excepción
        // y debe retornar una matriz no nula
        Tower tower = new Tower(5, 15);

        String[][] result = tower.stackingItems();

        assertEquals(0, result.length);
    }

    // =========================================================
    // CASO 2: Un solo cup, sin lid
    // =========================================================
    @Test
    public void shouldStackingItemsExecute() { // Generado con Caulde IA Sonet 4.6 2026 pero corregido
        Tower tower = new Tower(5, 15);
        //tower.pushCup(1); // Cup con id=1 → height = 2*1-1 = 1
        tower.pushCup(2);
        String[][] result = tower.stackingItems();
        

        assertEquals(1, result.length);
        // Debe haber al menos 1 fila con "Cup"
        assertEquals("cup", result[0][0]);
        assertEquals("2", result[0][1]);
    }

    // =========================================================
    // CASO 3: Un cup con su lid → deben aparecer ambos
    // =========================================================

    @Test
    public void stackingItems_oneCupWithLid_returnsCupThenLid() { // Generado con Claude IA Sonnet 4.6 2026 pero corregido
        Tower tower = new Tower(5, 15);
        tower.pushCup(1); // Cup id=1, height=1
        tower.pushLid(1);       // Lid para cup id=1, height = 2*(1-1) = 0 → ajustar según Lid.getSize()

        String[][] result = tower.stackingItems();

        assertNotNull(result);
        // La matriz debe tener 2 filas: Cup y Lid
        assertEquals(2, result.length);

        // Verificar que ambos tipos están presentes
        boolean hasCup = false, hasLid = false;
        for (String[] row : result) {
            if ("cup".equals(row[0])) hasCup = true;
            if ("lid".equals(row[0])) hasLid = true;
        }
        assertTrue(hasCup, "Debe haber al menos un Cup en el resultado");
        assertTrue(hasLid, "Debe haber al menos un Lid en el resultado");
    }
    
    //=============================================================
    //Caso 1: Debería ejecutarse el constructor
    //=============================================================
    @Test
    public void shouldGenerateATowerWithDeterminatedCups() {
        /*
        Tower proofTower = new Tower(3);
        assertNotNull(proofTower.getCups());
        assertEquals(3, proofTower.getCups().size());
        */
    }
    
    public void shouldGiveCorrectlyHeight(){
        Tower tower = new Tower(30, 30);
        tower.pushCup(4);
        tower.pushCup(1);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.pushCup(6);
        
        int expectedHeight = 21;
        int resultHeight = tower.heightUsed();
        assertEquals(expectedHeight, resultHeight);
    }
    
    @Test
    public void cupAndLidShouldHaveSameColor(){
        Tower tower = new Tower(30, 30);
        tower.pushCup(7);
        tower.pushCup(2);
        tower.pushCup(3);
        tower.pushLid(7);
        tower.pushLid(2);
        tower.pushLid(3);
        
        Cup cupBottom = tower.findCup(2);
        Lid lidBottom = tower.findLid(2);
        Cup cupUpper = tower.findCup(3);
        Lid lidUpper = tower.findLid(3);
        
        assertEquals(cupBottom.getColor(), lidBottom.getColor(), "La lid debe tener el mismo color que su Cup");
        assertEquals(cupUpper.getColor(), lidUpper.getColor(), "La lid debe tener el mismo color que su Cup");
    }
}
