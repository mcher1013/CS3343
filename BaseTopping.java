/**
 * BaseTopping - Abstract base for Topping implementations
 * Implements Pizza interface to allow stacking
 */
public abstract class BaseTopping implements Pizza, Topping {
    protected Pizza pizza;
    
    /**
     * Get the wrapped pizza
     * @return the underlying pizza
     */
    public Pizza getPizza() {
        return pizza;
    }
}
