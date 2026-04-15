import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

/**
 * Test class for CustomPizzaBuilder
 *
 * CustomPizzaBuilder uses the BUILDER pattern:
 *   - addTopping()  -> adds toppings one by one
 *   - preview()     -> returns a pizza WITHOUT clearing selections
 *   - build()       -> returns the final pizza WITHOUT clearing selections
 *   - reset()       -> clears all selected toppings, returns builder (fluent)
 */
public class CustomPizzaBuilderTest {

    private CustomPizzaBuilder builder;
    private static final double BASE_PRICE = 10.99; // CustomizationPizza base price

    // Setup & Teardown

    @Before
    public void setUp() {
        builder = new CustomPizzaBuilder();
    }

    @After
    public void tearDown() {
        builder = null;
    }

    // build() with no toppings

    /** An empty build should equal the base price of CustomizationPizza */
    @Test
    public void testBuild_noToppings_returnsBasePizza() {
        Pizza result = builder.build();
        assertEquals(BASE_PRICE, result.getCost(), 0.001);
    }

    /** An empty build should be named "Custom Pizza" */
    @Test
    public void testBuild_noToppings_hasCorrectName() {
        Pizza result = builder.build();
        assertEquals("Custom Pizza", result.getName());
    }

    // addTopping()

    /** Adding one topping increases the price */
    @Test
    public void testAddTopping_oneTopping_costIncreases() {
        builder.addTopping("Cheese", 1.50);
        Pizza result = builder.build();
        assertEquals(BASE_PRICE + 1.50, result.getCost(), 0.001); // 12.49
    }

    /** The topping name is appended to the pizza name */
    @Test
    public void testAddTopping_oneTopping_nameUpdated() {
        builder.addTopping("Cheese", 1.50);
        Pizza result = builder.build();
        assertEquals("Custom Pizza + Cheese", result.getName());
    }

    /** Adding two toppings stacks both costs */
    @Test
    public void testAddTopping_twoToppings_costAccumulates() {
        builder.addTopping("Cheese", 1.50); // 10.99 + 1.50 = 12.49
        builder.addTopping("Olives", 1.00); // 12.49 + 1.00 = 13.49
        Pizza result = builder.build();
        assertEquals(BASE_PRICE + 1.50 + 1.00, result.getCost(), 0.001);
    }

    /** Adding three toppings stacks all costs */
    @Test
    public void testAddTopping_threeToppings_costAccumulates() {
        builder.addTopping("Cheese", 1.50);
        builder.addTopping("Olives", 1.00);
        builder.addTopping("Mushroom", 1.25);
        Pizza result = builder.build();
        assertEquals(BASE_PRICE + 1.50 + 1.00 + 1.25, result.getCost(), 0.001);
    }

    /** addTopping() returns the builder itself (fluent/chaining) */
    @Test
    public void testAddTopping_returnsBuilderForChaining() {
        PizzaBuilder returned = builder.addTopping("Cheese", 1.50);
        assertSame(builder, returned);
    }

    // reset()

    /** After reset(), build() returns base price again */
    @Test
    public void testReset_clearsAllToppings() {
        builder.addTopping("Cheese", 1.50);
        builder.addTopping("Olives", 1.00);
        builder.reset();
        Pizza result = builder.build();
        assertEquals(BASE_PRICE, result.getCost(), 0.001);
    }

    /** After reset(), the name goes back to just "Custom Pizza" */
    @Test
    public void testReset_clearsName() {
        builder.addTopping("Cheese", 1.50);
        builder.reset();
        assertEquals("Custom Pizza", builder.build().getName());
    }

    /** reset() returns the builder itself (fluent/chaining) */
    @Test
    public void testReset_returnsBuilderForChaining() {
        PizzaBuilder returned = builder.reset();
        assertSame(builder, returned);
    }

    // preview()

    /** preview() returns the correct current pizza state */
    @Test
    public void testPreview_returnsPizzaWithCurrentToppings() {
        builder.addTopping("Cheese", 1.50);
        Pizza preview = builder.preview();
        assertEquals(BASE_PRICE + 1.50, preview.getCost(), 0.001);
    }

    /** preview() does NOT clear selections - build() after preview() still works */
    @Test
    public void testPreview_doesNotClearSelections() {
        builder.addTopping("Cheese", 1.50);
        builder.preview(); // should NOT clear
        builder.addTopping("Olives", 1.00); // add a second topping
        Pizza result = builder.build();
        assertEquals(BASE_PRICE + 1.50 + 1.00, result.getCost(), 0.001);
    }

    // Fluent chaining

    /** Can chain reset().addTopping().addTopping().build() fluently */
    @Test
    public void testFluentChain_resetAndAddAndBuild() {
        Pizza result = builder
                .reset()
                .addTopping("Cheese", 1.50)
                .addTopping("Olives", 1.00)
                .build();
        assertEquals(BASE_PRICE + 1.50 + 1.00, result.getCost(), 0.001);
    }
}
