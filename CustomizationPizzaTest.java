import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

/**
 * Test class for CustomizationPizza
 *
 * CustomizationPizza is the base "blank canvas" pizza.
 * It has a fixed base name and price that the decorator chain builds on.
 */
public class CustomizationPizzaTest {

    private CustomizationPizza pizza;

    @Before
    public void setUp() {
        pizza = new CustomizationPizza();
    }

    @After
    public void tearDown() {
        pizza = null;
    }

    /** Base name is "Custom Pizza" */
    @Test
    public void testGetName_returnsCustomPizza() {
        assertEquals("Custom Pizza", pizza.getName());
    }

    /** Base price is $10.99 */
    @Test
    public void testGetCost_returnsBaseCost() {
        assertEquals(10.99, pizza.getCost(), 0.001);
    }

    /** Two instances are independent (separate objects) */
    @Test
    public void testTwoInstances_areIndependent() {
        CustomizationPizza other = new CustomizationPizza();
        assertNotSame(pizza, other);
        assertEquals(pizza.getCost(), other.getCost(), 0.001);
    }

    /** CustomizationPizza implements Pizza interface */
    @Test
    public void testImplementsPizzaInterface() {
        assertTrue(pizza instanceof Pizza);
    }

    /** toString contains name and price */
    @Test
    public void testToString_containsNameAndPrice() {
        String s = pizza.toString();
        assertTrue(s.contains("Custom Pizza"));
        assertTrue(s.contains("10.99"));
    }
}
