import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

/**
 * Test class for MenuTopping
 *
 * MenuTopping is a DECORATOR - it wraps a Pizza and adds a topping.
 * Key things to test:
 *   1. getCost() = wrapped pizza cost + topping cost
 *   2. getName() = wrapped pizza name + " + " + topping name
 *   3. Stacking multiple toppings (decorator chain)
 */
public class MenuToppingTest {

    private Pizza basePizza;

    // Setup & Teardown

    @Before
    public void setUp() {
        basePizza = new MenuPizza("Margherita", 12.99);
    }

    @After
    public void tearDown() {
        basePizza = null;
    }

    // getToppingName()

    /** The topping name is stored and returned correctly */
    @Test
    public void testGetToppingName() {
        MenuTopping t = new MenuTopping(basePizza, "Cheese", 1.50);
        assertEquals("Cheese", t.getToppingName());
    }

    // getToppingCost()

    /** The topping's own price is returned correctly */
    @Test
    public void testGetToppingCost() {
        MenuTopping t = new MenuTopping(basePizza, "Cheese", 1.50);
        assertEquals(1.50, t.getToppingCost(), 0.001);
    }

    // getCost() - total cost includes base + topping

    /** Total cost = base pizza + one topping */
    @Test
    public void testGetCost_baseAndOneTopping() {
        MenuTopping t = new MenuTopping(basePizza, "Cheese", 1.50);
        // 12.99 + 1.50 = 14.49
        assertEquals(14.49, t.getCost(), 0.001);
    }

    /** Zero-price topping should not change the total */
    @Test
    public void testGetCost_zeroCostTopping() {
        MenuTopping t = new MenuTopping(basePizza, "FreeTopping", 0.00);
        assertEquals(12.99, t.getCost(), 0.001);
    }

    /** Stacking 2 toppings - decorator chain */
    @Test
    public void testGetCost_twoToppingsStacked() {
        Pizza withCheese = new MenuTopping(basePizza, "Cheese", 1.50); // 14.49
        Pizza withOlives = new MenuTopping(withCheese, "Olives", 1.00); // 15.49
        assertEquals(15.49, withOlives.getCost(), 0.001);
    }

    /** Stacking 3 toppings */
    @Test
    public void testGetCost_threeToppingsStacked() {
        Pizza p = new MenuTopping(basePizza, "Cheese", 1.50); // 14.49
        p = new MenuTopping(p, "Olives", 1.00); // 15.49
        p = new MenuTopping(p, "Mushroom", 1.25); // 16.74
        assertEquals(16.74, p.getCost(), 0.001);
    }

    // getName() - name accumulates through the decorator chain

    /** Name of single-topping pizza */
    @Test
    public void testGetName_singleTopping() {
        MenuTopping t = new MenuTopping(basePizza, "Cheese", 1.50);
        assertEquals("Margherita + Cheese", t.getName());
    }

    /** Name when two toppings are stacked */
    @Test
    public void testGetName_twoToppingsStacked() {
        Pizza withCheese = new MenuTopping(basePizza, "Cheese", 1.50);
        Pizza withOlives = new MenuTopping(withCheese, "Olives", 1.00);
        assertEquals("Margherita + Cheese + Olives", withOlives.getName());
    }

    // getPizza() - wrapped pizza is accessible

    /** getPizza() returns the original wrapped pizza */
    @Test
    public void testGetPizza_returnsWrappedPizza() {
        MenuTopping t = new MenuTopping(basePizza, "Cheese", 1.50);
        assertSame(basePizza, t.getPizza());
    }
}
