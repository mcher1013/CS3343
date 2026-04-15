/**
 * Simple manual test runner: {@code javac *.java && java -ea PizzaPricingTest}
 */
public final class PizzaPricingTest {

    private static final double EPS = 1e-9;

    /** Fixed-price stand-in for menu or custom pizzas in tests. */
    private static final class MockPizza implements Pizza {
        private final String name;
        private final double cost;

        MockPizza(String name, double cost) {
            this.name = name;
            this.cost = cost;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public double getCost() {
            return cost;
        }
    }

    private static void assertPrice(String label, double expected, double actual) {
        if (Math.abs(expected - actual) > EPS) {
            throw new AssertionError(label + ": expected " + expected + " but was " + actual);
        }
    }

    public static void main(String[] args) {
        testOrderCartTotalWithMocks();
        testMenuToppingDecorator();
        testCustomPizzaBuilderTotal();
        System.out.println("PizzaPricingTest: all passed");
    }

    private static void testOrderCartTotalWithMocks() { // test total cost of cart
        OrderCart cart = new OrderCart();
        cart.addPizza(new MockPizza("A", 12.5));
        cart.addPizza(new MockPizza("B", 3.25));
        assertPrice("cart total", 15.75, cart.getTotalCost());
    }

    private static void testMenuToppingDecorator() { // test toppings increase price of pizza
        Pizza base = new MenuPizza("Cheese", 9.0);
        Pizza decorated = new MenuTopping(base, "Pepperoni", 1.5);
        assertPrice("decorated cost", 10.5, decorated.getCost());
    }

    private static void testCustomPizzaBuilderTotal() { // test custom pizza builder total cost
        PizzaBuilder b = new CustomPizzaBuilder();
        b.addTopping("Mushroom", 0.75);
        b.addTopping("Olives", 0.5);
        Pizza built = b.build();
        // CustomizationPizza base is 10.99 in this codebase
        assertPrice("custom build", 10.99 + 0.75 + 0.5, built.getCost());
    }
}
