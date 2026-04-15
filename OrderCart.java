import java.util.ArrayList;
import java.util.List;

/**
 * OrderCart - Manages the customer's pizza order
 */
public class OrderCart {
    private List<Pizza> pizzas;
    private int orderNumber;
    private static int orderCounter = 1000;
    private static boolean orderCounterInitialized = false;
    
    /**
     * Constructor - creates a new empty order cart
     */
    public OrderCart() {
        this.pizzas = new ArrayList<>();
        if (!orderCounterInitialized) {
            orderCounter = OrderHistory.getLastOrderNumberOrDefault();
            orderCounterInitialized = true;
        }
        this.orderNumber = ++orderCounter;
    }
    
    /**
     * Add a pizza to the order cart
     * @param pizza the pizza to add
     */
    public void addPizza(Pizza pizza) {
        if (pizza != null) {
            pizzas.add(pizza);
            System.out.println(pizza.getName() + " has been added to your order cart!");
        }
    }
    
    /**
     * Get the total number of pizzas in the cart
     * @return number of pizzas
     */
    public int getPizzaCount() {
        return pizzas.size();
    }
    
    /**
     * Get all pizzas in the cart
     * @return list of pizzas
     */
    public List<Pizza> getPizzas() {
        return new ArrayList<>(pizzas); // Return a copy to prevent external modification
    }
    
    /**
     * Get the order number
     * @return order number
     */
    public int getOrderNumber() {
        return orderNumber;
    }
    
    /**
     * Calculate the total cost of all pizzas in the cart
     * @return total cost
     */
    public double getTotalCost() {
        double total = 0;
        for (Pizza pizza : pizzas) {
            total += pizza.getCost();
        }
        return total;
    }
    
    /**
     * Check if the cart is empty
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return pizzas.isEmpty();
    }
    
    /**
     * Display the current order cart contents
     */
    public void displayCart() {
        if (pizzas.isEmpty()) {
            System.out.println("\nYour order cart is empty.");
            return;
        }
        
        System.out.println("\n========== ORDER CART ==========");
        System.out.println("Order #: " + orderNumber);
        System.out.println("--------------------------------");
        
        int index = 1;
        for (Pizza pizza : pizzas) {
            System.out.printf("%d. %s - $%.2f%n", index++, pizza.getName(), pizza.getCost());
        }
        
        System.out.println("--------------------------------");
        System.out.printf("Total: $%.2f%n", getTotalCost());
        System.out.println("==================================\n");
    }
    
    /**
     * Clear all items from the cart
     */
    public void clear() {
        pizzas.clear();
    }
    
    /**
     * Generate a receipt for the order
     * @return formatted receipt string
     */
    public String generateReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("\n====================================\n");
        receipt.append("        PIZZA ORDER RECEIPT        \n");
        receipt.append("====================================\n");
        receipt.append("Order #: " + orderNumber + "\n");
        receipt.append("------------------------------------\n");
        
        int index = 1;
        for (Pizza pizza : pizzas) {
            receipt.append(String.format("%d. %-15s $%.2f%n", 
                index++, pizza.getName(), pizza.getCost()));
        }
        
        receipt.append("------------------------------------\n");
        receipt.append(String.format("TOTAL:        $%.2f%n", getTotalCost()));
        receipt.append("====================================\n");
        receipt.append("     Thank you for your order!     \n");
        receipt.append("====================================\n");
        
        return receipt.toString();
    }
}
