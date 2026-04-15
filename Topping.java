/**
 * Topping interface - defines operations for a pizza topping
 */
public interface Topping {
    /**
     * Get the name of the topping
     * @return topping name
     */
    String getToppingName();
    
    /**
     * Get the cost of the topping
     * @return cost in dollars
     */
    double getToppingCost();
}
