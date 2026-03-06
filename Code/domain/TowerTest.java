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
    public void shouldRemoveLid() {
        Tower proofTower = new Tower(5, 15);
        proofTower.pushCup(0);
        proofTower.pushLid(0);
        proofTower.removeLid(0);
        
        Lid lidOfCup = proofTower.getCups().get(0).getLid();
        assertNull(lidOfCup);
    }
    
    @Test
    public void shouldMessageErrorAppearInPopLid() {
        Tower proofTower = new Tower(5, 15);
        proofTower.pushCup(0);
        proofTower.popLid();
        
    }
    
    @Test
    public void shouldPopLid() {
        Tower proofTower = new Tower(5, 15);
        proofTower.pushCup(0);
        proofTower.pushLid(0);
        proofTower.popLid();
        
        Lid lidOfCup = proofTower.getCups().get(0).getLid();
        assertNull(lidOfCup);
    }
    
    // =========================================================
    // CASO 1: Sin cups ni lids → resultado vacío, no null, no explota
    // =========================================================

    @Test
    public void shouldGiveSomethingStickingItemsInZero() {
        // Con 0 cups y 0 lids, el método no debe lanzar excepción
        // y debe retornar una matriz no nula
        Tower tower = new Tower(5, 15);

        String[][] result = tower.stackingItems();

        assertNotNull(result);
    }

    // =========================================================
    // CASO 2: Un solo cup, sin lid
    // =========================================================

    @Test
    public void shouldStackingItemsExecute() { // Generado con Caulde IA Sonet 4.6 2026 pero corregido
        Tower tower = new Tower(5, 15);
        tower.pushCup(1); // Cup con id=1 → height = 2*1-1 = 1

        String[][] result = tower.stackingItems();

        assertEquals(1, result.length);
        // Debe haber al menos 1 fila con "Cup"
        assertEquals("Cup", result[0][0]);
        assertEquals("1", result[0][1]);
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
            if ("Cup".equals(row[0])) hasCup = true;
            if ("Lid".equals(row[0])) hasLid = true;
        }
        assertTrue(hasCup, "Debe haber al menos un Cup en el resultado");
        assertTrue(hasLid, "Debe haber al menos un Lid en el resultado");
    }

    // =========================================================
    // CASO 4: Dos cups, sin lids → solo cups en resultado
    // =========================================================

    @Test
    public void stackingItemsTwoCupsNoLidsReturnsOnlyCups() { // Generado con Claude IA Sonnet 4.6 2026 pero corregido
        Tower tower = new Tower(10, 20);
        tower.pushCup(3); // height = 5
        tower.pushCup(2); // height = 3

        String[][] result = tower.stackingItems();

        assertNotNull(result);
        assertEquals(2, result.length);
        for (String[] row : result) {
            assertEquals("Cup", row[0], "Sin lids, todas las filas deben ser Cup");
        }
    }
    
    //=============================================================
    //Caso 1: Debería ejecutarse el constructor
    //=============================================================
    @Test
    public void shouldGenerateATowerWithDeterminatedCups() {
        Tower proofTower = new Tower(5, 15, 3);
        assertNotNull(proofTower.getCups());
        assertEquals(3, proofTower.getCups().size());
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