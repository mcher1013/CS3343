import java.util.List;

/**
 * PizzaMenu - Manages the pizza menu dynamically
 * Allows easy addition of new pizza types without modifying existing code
 */
public class PizzaMenu {
    /**
     * Get the number of available pizza types
     * @return number of pizzas
     */
    public static int getPizzaCount() {
        return PizzaManagement.getItems().size();
    }
    
    /**
     * Display the menu with all available pizzas
     */
    public static void displayMenu() {
        System.out.println("========================================");
        System.out.println("           OUR MENU          ");
        System.out.println("========================================");
        System.out.println();
        
        List<PizzaManagement.PizzaItem> items = PizzaManagement.getItems();
        for (int i = 0; i < items.size(); i++) {
            Pizza pizza = createPizza(i);
            System.out.println("  " + (i + 1) + ". " + pizza.getName() + "     - $" + String.format("%.2f", pizza.getCost()));
        }
        
        System.out.println();
        System.out.println("========================================");
    }
    
    /**
     * Create a pizza instance by index
     * @param index pizza index (0-based)
     * @return Pizza instance or null if invalid index
     */
    public static Pizza createPizza(int index) {
        List<PizzaManagement.PizzaItem> items = PizzaManagement.getItems();
        if (index < 0 || index >= items.size()) {
            return null;
        }
        
        PizzaManagement.PizzaItem item = items.get(index);
        return new MenuPizza(item.name, item.price);
    }
    
    /**
     * Get pizza name by index
     * @param index pizza index
     * @return pizza name
     */
    public static String getPizzaName(int index) {
        Pizza pizza = createPizza(index);
        return pizza != null ? pizza.getName() : "Unknown";
    }
    
    /**
     * Check if the pizza is a CustomizationPizza
     * @param index pizza index
     * @return true if it's CustomizationPizza
     */
    public static boolean isCustomizationPizza(int index) {
        return false;
    }

    public static List<PizzaManagement.PizzaItem> getItems() {
        return PizzaManagement.getItems();
    }

    public static void addPizza(String name, double price, boolean customization) {
        PizzaManagement.addPizza(name, price, customization);
    }

    public static void updatePizza(int id, String newName, Double newPrice, Boolean customization) {
        PizzaManagement.updatePizza(id, newName, newPrice, customization);
    }

    public static void deletePizza(int id) {
        PizzaManagement.deletePizza(id);
    }
}
