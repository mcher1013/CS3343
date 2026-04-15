import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

/**
 * Test class for MenuPizza
 * Verifies that a pizza created from a name + price stores and returns the correct values.
 */
public class MenuPizzaTest {

    private MenuPizza pizza;

    // Setup & Teardown

    @Before
    public void setUp() {
        pizza = new MenuPizza("Margherita", 12.99);
    }

    @After
    public void tearDown() {
        pizza = null;
    }

    // getName()

    /** Normal case: name is stored exactly as provided */
    @Test
    public void testGetName_returnsCorrectName() {
        assertEquals("Margherita", pizza.getName());
    }

    /** Name should be case-sensitive */
    @Test
    public void testGetName_caseSensitive() {
        MenuPizza p = new MenuPizza("pepperoni", 14.99);
        assertEquals("pepperoni", p.getName());
    }

    /** Name with spaces and special chars */
    @Test
    public void testGetName_withSpaces() {
        MenuPizza p = new MenuPizza("Seafood Special", 19.99);
        assertEquals("Seafood Special", p.getName());
    }

    // getCost()

    /** Normal case: price is stored correctly */
    @Test
    public void testGetCost_returnsCorrectPrice() {
        assertEquals(12.99, pizza.getCost(), 0.001);
    }

    /** Zero price edge case */
    @Test
    public void testGetCost_zeroPrize() {
        MenuPizza free = new MenuPizza("Free Pizza", 0.00);
        assertEquals(0.00, free.getCost(), 0.001);
    }

    /** Large price */
    @Test
    public void testGetCost_largePrice() {
        MenuPizza expensive = new MenuPizza("Luxury Pizza", 999.99);
        assertEquals(999.99, expensive.getCost(), 0.001);
    }

    // toString()

    /** toString should include the name and price */
    @Test
    public void testToString_containsNameAndPrice() {
        String result = pizza.toString();
        assertTrue("toString should contain pizza name", result.contains("Margherita"));
        assertTrue("toString should contain price", result.contains("12.99"));
    }
}
