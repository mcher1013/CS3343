/**
 * BasePizza abstract class - provides common functionality for all pizzas
 */
public abstract class BasePizza implements Pizza {
    protected String name;
    protected double cost;
    
    /**
     * Get the cost of the pizza
     * @return price in dollars
     */
    @Override
    public double getCost() {
        return cost;
    }
    
    /**
     * Get the name of the pizza
     * @return pizza name
     */
    @Override
    public String getName() {
        return name;
    }
    
    /**
     * Get formatted string with pizza details
     * @return formatted string
     */
    public String toString() {
        return String.format("%s - $%.2f", name, cost);
    }
}
