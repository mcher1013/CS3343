import java.util.List;

/**
 * ToppingMenu - Manages the toppings menu dynamically
 * Allows easy addition of new topping types without modifying existing code
 */
public class ToppingMenu {
    /**
     * Get the number of available topping types
     * @return number of toppings
     */
    public static int getToppingCount() {
        return ToppingManagement.getItems().size();
    }
    
    /**
     * Display the topping menu
     */
    public static void displayMenu() {
        System.out.println("========================================");
        System.out.println("         TOPPINGS MENU        ");
        System.out.println("========================================");
        System.out.println();
        
        List<ToppingManagement.ToppingItem> items = ToppingManagement.getItems();
        for (int i = 0; i < items.size(); i++) {
            ToppingManagement.ToppingItem t = items.get(i);
            System.out.println("  " + (i + 1) + ". " + t.name + "        +$"
                    + String.format("%.2f", t.price));
        }
        
        System.out.println();
        System.out.println("========================================");
    }
    
    /**
     * Create a topping instance by index
     * @param index topping index (0-based)
     * @param pizza the pizza to wrap
     * @return Topping instance or null if invalid index
     */
    public static BaseTopping createTopping(int index, Pizza pizza) {
        List<ToppingManagement.ToppingItem> items = ToppingManagement.getItems();
        if (index < 0 || index >= items.size()) {
            return null;
        }
        ToppingManagement.ToppingItem item = items.get(index);
        return new MenuTopping(pizza, item.name, item.price);
    }
    
    /**
     * Get topping name by index
     * @param index topping index
     * @return topping name
     */
    public static String getToppingName(int index) {
        List<ToppingManagement.ToppingItem> items = ToppingManagement.getItems();
        if (index < 0 || index >= items.size()) {
            return "Unknown";
        }
        return items.get(index).name;
    }
    
    /**
     * Get topping cost by index
     * @param index topping index
     * @return topping cost
     */
    public static double getToppingCost(int index) {
        List<ToppingManagement.ToppingItem> items = ToppingManagement.getItems();
        if (index < 0 || index >= items.size()) {
            return 0;
        }
        return items.get(index).price;
    }

    public static void addTopping(String name, double price) {
        ToppingManagement.addTopping(name, price);
    }

    public static void updateTopping(int id, String newName, Double newPrice) {
        ToppingManagement.updateTopping(id, newName, newPrice);
    }

    public static void deleteTopping(int id) {
        ToppingManagement.deleteTopping(id);
    }

    public static List<ToppingManagement.ToppingItem> getItems() {
        return ToppingManagement.getItems();
    }
}
