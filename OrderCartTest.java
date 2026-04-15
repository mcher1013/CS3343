import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.util.List;

/**
 * Test class for OrderCart
 *
 * Tests:
 * - isEmpty(), getPizzaCount(), getTotalCost()
 * - addPizza(), getPizzas()
 * - generateReceipt()
 * - clear()
 */
public class OrderCartTest {

    private OrderCart cart;
    private Pizza margherita;
    private Pizza pepperoni;

    // Setup & Teardown

    @Before
    public void setUp() {
        cart = new OrderCart();
        margherita = new MenuPizza("Margherita", 12.99);
        pepperoni = new MenuPizza("Pepperoni", 14.99);
    }

    @After
    public void tearDown() {
        cart = null;
        margherita = null;
        pepperoni = null;
    }

    // isEmpty()

    /** A new cart should be empty */
    @Test
    public void testIsEmpty_newCart_returnsTrue() {
        assertTrue(cart.isEmpty());
    }

    /** Cart is not empty after adding a pizza */
    @Test
    public void testIsEmpty_afterAdd_returnsFalse() {
        cart.addPizza(margherita);
        assertFalse(cart.isEmpty());
    }

    // getPizzaCount()

    /** New cart has count 0 */
    @Test
    public void testGetPizzaCount_emptyCart_returnsZero() {
        assertEquals(0, cart.getPizzaCount());
    }

    /** Count increases with each pizza added */
    @Test
    public void testGetPizzaCount_afterAddingOnePizza() {
        cart.addPizza(margherita);
        assertEquals(1, cart.getPizzaCount());
    }

    @Test
    public void testGetPizzaCount_afterAddingTwoPizzas() {
        cart.addPizza(margherita);
        cart.addPizza(pepperoni);
        assertEquals(2, cart.getPizzaCount());
    }

    // getTotalCost()

    /** Empty cart total is 0 */
    @Test
    public void testGetTotalCost_emptyCart_returnsZero() {
        assertEquals(0.00, cart.getTotalCost(), 0.001);
    }

    /** Total with one pizza equals that pizza's price */
    @Test
    public void testGetTotalCost_onePizza() {
        cart.addPizza(margherita);
        assertEquals(12.99, cart.getTotalCost(), 0.001);
    }

    /** Total with two pizzas is the sum of their prices */
    @Test
    public void testGetTotalCost_twoPizzas() {
        cart.addPizza(margherita); // 12.99
        cart.addPizza(pepperoni); // 14.99
        assertEquals(27.98, cart.getTotalCost(), 0.001);
    }

    /** Total includes toppings on a custom pizza */
    @Test
    public void testGetTotalCost_customPizzaWithToppings() {
        Pizza custom = new MenuTopping(
                new MenuTopping(new CustomizationPizza(), "Cheese", 1.50),
                "Olives", 1.00);
        cart.addPizza(custom); // 10.99 + 1.50 + 1.00 = 13.49
        assertEquals(13.49, cart.getTotalCost(), 0.001);
    }

    // addPizza()

    /** Null pizza is silently ignored */
    @Test
    public void testAddPizza_nullPizza_cartStaysEmpty() {
        cart.addPizza(null);
        assertEquals(0, cart.getPizzaCount());
        assertTrue(cart.isEmpty());
    }

    // getPizzas()

    /** getPizzas() returns a copy - modifying it does not affect the cart */
    @Test
    public void testGetPizzas_returnsDefensiveCopy() {
        cart.addPizza(margherita);
        List<Pizza> copy = cart.getPizzas();
        copy.clear(); // modify the copy
        assertEquals(1, cart.getPizzaCount()); // original cart unchanged
    }

    /** All added pizzas appear in getPizzas() */
    @Test
    public void testGetPizzas_containsAddedPizzas() {
        cart.addPizza(margherita);
        cart.addPizza(pepperoni);
        List<Pizza> pizzas = cart.getPizzas();
        assertEquals(2, pizzas.size());
        assertEquals("Margherita", pizzas.get(0).getName());
        assertEquals("Pepperoni", pizzas.get(1).getName());
    }

    // clear()

    /** After clear(), cart is empty again */
    @Test
    public void testClear_removesAllPizzas() {
        cart.addPizza(margherita);
        cart.addPizza(pepperoni);
        cart.clear();
        assertTrue(cart.isEmpty());
        assertEquals(0, cart.getPizzaCount());
    }

    /** After clear(), total cost is zero */
    @Test
    public void testClear_totalCostIsZero() {
        cart.addPizza(margherita);
        cart.clear();
        assertEquals(0.00, cart.getTotalCost(), 0.001);
    }

    // generateReceipt()

    /** Receipt contains the order number */
    @Test
    public void testGenerateReceipt_containsOrderNumber() {
        cart.addPizza(margherita);
        String receipt = cart.generateReceipt();
        assertTrue("Receipt should contain order number",
                receipt.contains(String.valueOf(cart.getOrderNumber())));
    }

    /** Receipt contains each pizza name */
    @Test
    public void testGenerateReceipt_containsPizzaName() {
        cart.addPizza(margherita);
        String receipt = cart.generateReceipt();
        assertTrue(receipt.contains("Margherita"));
    }

    /** Receipt contains the total cost */
    @Test
    public void testGenerateReceipt_containsTotal() {
        cart.addPizza(margherita); // 12.99
        cart.addPizza(pepperoni); // 14.99 -> total 27.98
        String receipt = cart.generateReceipt();
        assertTrue("Receipt should contain total", receipt.contains("27.98"));
    }

    /** Receipt lists multiple pizzas */
    @Test
    public void testGenerateReceipt_listsBothPizzas() {
        cart.addPizza(margherita);
        cart.addPizza(pepperoni);
        String receipt = cart.generateReceipt();
        assertTrue(receipt.contains("Margherita"));
        assertTrue(receipt.contains("Pepperoni"));
    }
}
