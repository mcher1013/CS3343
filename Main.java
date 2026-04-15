import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main class - Entry point for the Pizza Ordering System
 * Provides a CLI interface for customers to order pizzas
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static OrderCart cart = new OrderCart();
    private static final int VIEW_HISTORY_OPTION_OFFSET = 1;
    private static final int EXIT_OPTION_OFFSET = 2;
    private static final int SECTION_SPACING_LINES = 5;
    private static final String CUSTOM_PIZZA_TOKEN = "c";
    private static final int PAGE_SIZE = 5;
    
    /**
     * Main method - Entry point of the application
     */
    public static void main(String[] args) {
        selectMode();

        // Display welcome message
        displayWelcome();
        
        // Main ordering loop
        boolean continueOrdering = true;
        
        while (continueOrdering) {
            // Display menu and get user selection
            String choiceToken = displayMenuAndGetChoiceToken();
            if (choiceToken != null && handleTopLevelAction(choiceToken)) {
                continue;
            }

            Integer choice = tryParseInt(choiceToken);

            Pizza selectedPizza = null;
            if (choiceToken.equalsIgnoreCase(CUSTOM_PIZZA_TOKEN)) {
                selectedPizza = handleCustomizationPizza();
                if (selectedPizza == null) {
                    continue;
                }
            } else if (choice != null) {
                // Process user selection
                selectedPizza = createPizzaByChoice(choice);
            }
            
            if (selectedPizza != null) {
                // Add to cart
                cart.addPizza(selectedPizza);
                
                // Show cart contents
                cart.displayCart();
                
                // Ask for next action
                continueOrdering = askNextAction();
            } else {
                System.out.println("Invalid selection. Please try again.\n");
                printBlankLines(SECTION_SPACING_LINES);
            }
        }
        
        // Generate receipt and end
        endOrder();
    }

    private static void selectMode() {
        printBlankLines(SECTION_SPACING_LINES);
        System.out.println("========================================");
        System.out.println("Please select mode:");
        System.out.println("  1. Customer");
        System.out.println("  2. Admin");
        System.out.print("Enter your choice (1-2): ");

        int mode = readIntWithSpacing();
        if (mode == 2) {
            AdminConsole.run(scanner);
            System.out.println("Goodbye!");
            System.exit(0);
        }
    }
    
    /**
     * Display welcome message
     */
    private static void displayWelcome() {
        System.out.println("========================================");
        System.out.println("                                    ");
        System.out.println("       WELCOME TO PIZZA PALACE        ");
        System.out.println("                                    ");
        System.out.println("     The Best Pizza in Town           ");
        System.out.println("========================================");
        System.out.println();
        System.out.println("We are excited to serve you today!");
        System.out.println("Please select a pizza from our menu below.\n");
    }
    
    /**
     * Display the pizza menu and get user choice
     * @return user selection
     */
    private static String displayMenuAndGetChoiceToken() {
        int page = 1;
        while (true) {
            List<PizzaManagement.PizzaItem> items = PizzaMenu.getItems();
            int totalPages = Math.max(1, (items.size() + PAGE_SIZE - 1) / PAGE_SIZE);
            if (page < 1) page = 1;
            if (page > totalPages) page = totalPages;

            System.out.println("========================================");
            System.out.println("           OUR MENU          ");
            System.out.println("========================================");
            System.out.println();
            System.out.println("Page " + page + " / " + totalPages);
            System.out.println("----------------------------------------");

            int start = (page - 1) * PAGE_SIZE;
            int end = Math.min(start + PAGE_SIZE, items.size());
            int shown = 0;
            for (int i = start; i < end; i++) {
                PizzaManagement.PizzaItem item = items.get(i);
                System.out.println("  " + (++shown) + ". " + item.name + "     - $" + String.format("%.2f", item.price));
            }

            System.out.println("----------------------------------------");
            if (page > 1) System.out.println("  p. Previous page");
            if (page < totalPages) System.out.println("  n. Next page");
            System.out.println("  " + CUSTOM_PIZZA_TOKEN + ". Custom Pizza (build your own)");
            System.out.println("  v. View past orders");
            System.out.println("  x. Exit");
            System.out.print("Please enter your choice (1-" + shown + ", n/p, 6/7 or '" + CUSTOM_PIZZA_TOKEN + "'): ");

            String token = readTokenWithSpacing();
            if (token.equalsIgnoreCase("n") && page < totalPages) {
                page++;
                continue;
            }
            if (token.equalsIgnoreCase("p") && page > 1) {
                page--;
                continue;
            }
            Integer localChoice = tryParseInt(token);
            if (localChoice != null && localChoice >= 1 && localChoice <= shown) {
                int globalIndex = start + (localChoice - 1);
                return String.valueOf(globalIndex + 1); // back to 1-based for createPizzaByChoice
            }
            if (token.equalsIgnoreCase(CUSTOM_PIZZA_TOKEN)) {
                return token;
            }
            if (token.equals("v") || token.equals("x")) {
                return token;
            }
            System.out.println("Invalid selection. Please try again.\n");
            printBlankLines(SECTION_SPACING_LINES);
        }
    }

    private static boolean handleTopLevelAction(String choice) {
        if (choice.equals("v")) {
            OrderHistory.displayHistoryPaginated(scanner);
            printBlankLines(SECTION_SPACING_LINES);
            return true;
        }
        if (choice.equals("x")) {
            System.out.println("\nThank you for visiting Pizza Palace!");
            System.exit(0);
            return true;
        }
        return false;
    }

    private static void printBlankLines(int count) {
        for (int i = 0; i < count; i++) {
            System.out.println();
        }
    }

    private static int readIntWithSpacing() {
        int value = scanner.nextInt();
        scanner.nextLine(); // consume newline after int input
        printBlankLines(SECTION_SPACING_LINES);
        return value;
    }

    private static String readTokenWithSpacing() {
        String value = scanner.nextLine().trim();
        printBlankLines(SECTION_SPACING_LINES);
        return value;
    }

    private static Integer tryParseInt(String s) {
        if (s == null) return null;
        String t = s.trim();
        if (t.isEmpty()) return null;
        try {
            return Integer.parseInt(t);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Create pizza object based on user choice
     * @param choice user selection
     * @return Pizza object or null if invalid
     */
    private static Pizza createPizzaByChoice(int choice) {
        // Use PizzaMenu to create pizza (convert 1-based to 0-based index)
        return PizzaMenu.createPizza(choice - 1);
    }
    
    /**
     * Handle CustomizationPizza - allows user to add toppings
     * @return customized pizza or null if user cancelled
     */
    private static Pizza handleCustomizationPizza() {
        PizzaBuilder builder = new CustomPizzaBuilder();
        List<String> selectedToppings = new ArrayList<>();
        int page = 1;
        
        while (true) {
            List<ToppingManagement.ToppingItem> toppings = ToppingMenu.getItems();
            int totalPages = Math.max(1, (toppings.size() + PAGE_SIZE - 1) / PAGE_SIZE);
            if (page < 1) page = 1;
            if (page > totalPages) page = totalPages;

            Pizza currentPreview = builder.preview();
            displayToppingMenu(selectedToppings, currentPreview, toppings, page, totalPages);

            int start = (page - 1) * PAGE_SIZE;
            int end = Math.min(start + PAGE_SIZE, toppings.size());
            int shown = Math.max(0, end - start);

            System.out.println("Commands: [n] next, [p] prev, [r] reset, [d] done, [e] exit");
            System.out.print("Select topping (1-" + shown + " or command): ");

            String token = readTokenWithSpacing();
            if (token.equalsIgnoreCase("n") && page < totalPages) {
                page++;
                continue;
            }
            if (token.equalsIgnoreCase("p") && page > 1) {
                page--;
                continue;
            }
            if (token.equalsIgnoreCase("r")) {
                selectedToppings.clear();
                builder.reset();
                System.out.println("All toppings have been cleared.");
                continue;
            }
            if (token.equalsIgnoreCase("e")) {
                System.out.println("Returning to pizza menu.");
                return null;
            }
            if (token.equalsIgnoreCase("d")) {
                if (selectedToppings.isEmpty()) {
                    System.out.println("Please select at least one topping.");
                } else {
                    System.out.println("Customization confirmed!");
                    return builder.build();
                }
                continue;
            }

            Integer local = tryParseInt(token);
            if (local == null || local < 1 || local > shown) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            int globalIndex = start + (local - 1);
            String toppingName = ToppingMenu.getToppingName(globalIndex);
            if (selectedToppings.contains(toppingName)) {
                System.out.println(toppingName + " is already selected.");
            } else {
                selectedToppings.add(toppingName);
                double toppingPrice = ToppingMenu.getToppingCost(globalIndex);
                builder.addTopping(toppingName, toppingPrice);
                System.out.println(toppingName + " has been added.");
            }
        }
    }
    
    /**
     * Display the topping menu
     * @param selectedToppings list of already selected toppings
     * @param currentPizza current pizza with toppings
     */
    private static void displayToppingMenu(List<String> selectedToppings, Pizza currentPizza,
                                          List<ToppingManagement.ToppingItem> allToppings,
                                          int page, int totalPages) {
        // Use ToppingMenu for dynamic menu display
        System.out.println("========================================");
        System.out.println("         TOPPINGS MENU        ");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Page " + page + " / " + totalPages);
        System.out.println("----------------------------------------");

        int start = (page - 1) * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, allToppings.size());
        int shown = 0;
        for (int i = start; i < end; i++) {
            ToppingManagement.ToppingItem t = allToppings.get(i);
            System.out.println("  " + (++shown) + ". " + t.name + "        +$" + String.format("%.2f", t.price));
        }
        
        System.out.println("----------------------------------------");
        System.out.println("Current selections:");
        if (selectedToppings.isEmpty()) {
            System.out.println("  No toppings selected");
        } else {
            for (String topping : selectedToppings) {
                System.out.println("  - " + topping);
            }
        }
        System.out.println("----------------------------------------");
        System.out.println("Current total: $" + String.format("%.2f", currentPizza.getCost()));
    }
    
    /**
     * Ask user what to do next - continue shopping or view cart or checkout
     * @return true to continue, false to checkout
     */
    private static boolean askNextAction() {
        while (true) {
            System.out.println("What would you like to do next?");
            System.out.println("  1. Order another pizza");
            System.out.println("  2. View cart");
            System.out.println("  3. Checkout");
            System.out.println("  4. Cancel order");
            System.out.print("Please enter your choice (1-4): ");

            String token = readTokenWithSpacing();
            Integer choice = tryParseInt(token);
            if (choice == null) {
                System.out.println("Invalid choice. Please enter a number (1-4).\n");
                continue;
            }
            
            switch (choice) {
                case 1:
                    System.out.println("Great! Let's add another pizza to your order.\n");
                    return true;
                case 2:
                    cart.displayCart();
                    break;
                case 3:
                    if (cart.isEmpty()) {
                        System.out.println("Your cart is empty. Please add a pizza first.\n");
                        break;
                    }
                    return false;
                case 4:
                    System.out.println("Your order has been cancelled.");
                    System.out.println("Thank you for visiting Pizza Palace!");
                    System.exit(0);
                    return false;
                default:
                    System.out.println("Invalid choice. Please try again.\n");
                    break;
            }
        }
    }
    
    /**
     * Complete the order and display receipt
     */
    private static void endOrder() {
        System.out.println("\nProcessing your order...\n");
        System.out.println(cart.generateReceipt());
        OrderHistory.appendOrder(cart);
        System.out.println("Your order has been placed successfully!");
        System.out.println("Thank you for choosing Pizza Palace!");
        System.out.println("We hope to see you again soon!");
        
        scanner.close();
    }
}
