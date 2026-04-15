import java.util.List;
import java.util.Scanner;

public class AdminConsole {
    private static final int SPACING_LINES = 5;
    private static final int PAGE_SIZE = 9;

    public static void run(Scanner scanner) {
        while (true) {
            printBlankLines(SPACING_LINES);
            System.out.println("========================================");
            System.out.println("              ADMIN PANEL              ");
            System.out.println("========================================");
            System.out.println("  1. Add pizza/topping");
            System.out.println("  2. Update pizza/topping");
            System.out.println("  3. Delete pizza/topping");
            System.out.println("  4. View current menu data");
            System.out.println("  5. Exit");
            System.out.print("Please enter your choice (1-5): ");

            String choice = scanner.nextLine().trim();
            if (choice.equals("5")) {
                printBlankLines(SPACING_LINES);
                return;
            }
            switch (choice) {
                case "1":
                    addFlow(scanner);
                    break;
                case "2":
                    updateFlow(scanner);
                    break;
                case "3":
                    deleteFlow(scanner);
                    break;
                case "4":
                    viewMenuDataFlow(scanner);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }

    private static void addFlow(Scanner scanner) {
        String target = askTarget(scanner);
        if (target == null) {
            System.out.println("Returned to admin panel.");
            return;
        }

        if (target.equals("pizza")) {
            System.out.print("Pizza name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Pizza price: ");
            Double price = readDouble(scanner);
            if (price != null && !name.isEmpty()) {
                PizzaMenu.addPizza(name, price, false);
                System.out.println("Pizza added.");
                pauseAfterAdd(scanner);
            } else {
                System.out.println("Add cancelled.");
            }
            return;
        }

        System.out.print("Topping name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Topping price: ");
        Double price = readDouble(scanner);
        if (price != null && !name.isEmpty()) {
            ToppingMenu.addTopping(name, price);
            System.out.println("Topping added.");
            pauseAfterAdd(scanner);
        } else {
            System.out.println("Add cancelled.");
        }
    }

    /** Only after a successful add (pizza or topping). */
    private static void pauseAfterAdd(Scanner scanner) {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }

    private static void updateFlow(Scanner scanner) {
        String target = askTarget(scanner);
        if (target == null) {
            System.out.println("Returned to admin panel.");
            return;
        }

        if (target.equals("pizza")) {
            Integer id = browsePizzasSelectId(scanner, "update");
            if (id == null) {
                System.out.println("Update cancelled.");
                return;
            }
            System.out.print("New name (leave blank to keep): ");
            String newName = scanner.nextLine().trim();
            System.out.print("New price (leave blank to keep): ");
            String priceStr = scanner.nextLine().trim();
            Double newPrice = priceStr.isEmpty() ? null : tryParseDouble(priceStr);
            PizzaMenu.updatePizza(id, newName.isEmpty() ? null : newName, newPrice, null);
            System.out.println("Pizza updated.");
            return;
        }

        Integer id = browseToppingsSelectId(scanner, "update");
        if (id == null) {
            System.out.println("Update cancelled.");
            return;
        }
        System.out.print("New name (leave blank to keep): ");
        String newName = scanner.nextLine().trim();
        System.out.print("New price (leave blank to keep): ");
        String priceStr = scanner.nextLine().trim();
        Double newPrice = priceStr.isEmpty() ? null : tryParseDouble(priceStr);
        ToppingMenu.updateTopping(id, newName.isEmpty() ? null : newName, newPrice);
        System.out.println("Topping updated.");
    }

    private static void deleteFlow(Scanner scanner) {
        String target = askTarget(scanner);
        if (target == null) {
            System.out.println("Returned to admin panel.");
            return;
        }

        if (target.equals("pizza")) {
            Integer id = browsePizzasSelectId(scanner, "delete");
            if (id == null) {
                System.out.println("Delete cancelled.");
                return;
            }
            PizzaMenu.deletePizza(id);
            System.out.println("Pizza deleted.");
            return;
        }

        Integer id = browseToppingsSelectId(scanner, "delete");
        if (id == null) {
            System.out.println("Delete cancelled.");
            return;
        }
        ToppingMenu.deleteTopping(id);
        System.out.println("Topping deleted.");
    }

    private static void viewMenuDataFlow(Scanner scanner) {
        while (true) {
            printBlankLines(SPACING_LINES);
            System.out.println("========== VIEW MENU DATA ==========");
            System.out.println("  1. View pizzas (pizzas.csv)");
            System.out.println("  2. View toppings (toppings.csv)");
            System.out.println("  3. Back to admin panel");
            System.out.print("Your choice (1-3): ");
            String input = scanner.nextLine().trim();
            if (input.equals("3")) {
                return;
            }
            if (input.equals("1")) {
                browsePizzasView(scanner);
                continue;
            }
            if (input.equals("2")) {
                browseToppingsView(scanner);
                continue;
            }
            System.out.println("Invalid choice.");
        }
    }

    /** View only: n/p/q, max 9 per page. */
    private static void browsePizzasView(Scanner scanner) {
        List<PizzaManagement.PizzaItem> items = PizzaMenu.getItems();
        browsePizzaListView(scanner, items);
    }

    /**
     * Update/Delete: browse with n/p, type numeric id to select, or q to cancel.
     */
    private static Integer browsePizzasSelectId(Scanner scanner, String verb) {
        List<PizzaManagement.PizzaItem> items = PizzaMenu.getItems();
        return browsePizzaListSelectId(scanner, items, verb);
    }

    private static void browsePizzaListView(Scanner scanner, List<PizzaManagement.PizzaItem> items) {
        int totalPages = Math.max(1, (items.size() + PAGE_SIZE - 1) / PAGE_SIZE);
        int page = 1;

        while (true) {
            printBlankLines(SPACING_LINES);
            System.out.println("========== PIZZAS (pizzas.csv) ==========");
            if (items.isEmpty()) {
                System.out.println("(none)");
                System.out.println("[q] back");
                System.out.print("Your choice: ");
                String in = scanner.nextLine().trim();
                if (in.equalsIgnoreCase("q")) {
                    return;
                }
                continue;
            }

            if (page < 1) page = 1;
            if (page > totalPages) page = totalPages;

            System.out.println("Page " + page + " / " + totalPages + " (max " + PAGE_SIZE + " per page)");
            System.out.println("----------------------------------------");
            int start = (page - 1) * PAGE_SIZE;
            int end = Math.min(start + PAGE_SIZE, items.size());
            for (int i = start; i < end; i++) {
                PizzaManagement.PizzaItem p = items.get(i);
                System.out.println("id=" + p.id + " | " + p.name + " | $" + String.format("%.2f", p.price));
            }
            System.out.println("----------------------------------------");
            if (totalPages > 1) {
                System.out.println("[n] next  [p] prev  [q] back");
            } else {
                System.out.println("[q] back");
            }
            System.out.print("Your choice: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) {
                return;
            }
            if (input.equalsIgnoreCase("n") && page < totalPages) {
                page++;
            } else if (input.equalsIgnoreCase("p") && page > 1) {
                page--;
            } else if (!input.isEmpty()) {
                System.out.println("Unknown command. Use n, p, or q.");
            }
        }
    }

    private static Integer browsePizzaListSelectId(Scanner scanner, List<PizzaManagement.PizzaItem> items, String verb) {
        if (items.isEmpty()) {
            System.out.println("No pizzas to " + verb + ".");
            return null;
        }

        int totalPages = Math.max(1, (items.size() + PAGE_SIZE - 1) / PAGE_SIZE);
        int page = 1;

        while (true) {
            printBlankLines(SPACING_LINES);
            System.out.println("========== PIZZAS (pizzas.csv) ==========");
            if (page < 1) page = 1;
            if (page > totalPages) page = totalPages;

            System.out.println("Page " + page + " / " + totalPages + " (max " + PAGE_SIZE + " per page)");
            System.out.println("----------------------------------------");
            int start = (page - 1) * PAGE_SIZE;
            int end = Math.min(start + PAGE_SIZE, items.size());
            for (int i = start; i < end; i++) {
                PizzaManagement.PizzaItem p = items.get(i);
                System.out.println("id=" + p.id + " | " + p.name + " | $" + String.format("%.2f", p.price));
            }
            System.out.println("----------------------------------------");
            if (totalPages > 1) {
                System.out.println("[n] next  [p] prev  [q] cancel  |  Or type pizza id to " + verb + ":");
            } else {
                System.out.println("[q] cancel  |  Or type pizza id to " + verb + ":");
            }
            System.out.print("Your choice: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                return null;
            }
            if (input.equalsIgnoreCase("n") && page < totalPages) {
                page++;
                continue;
            }
            if (input.equalsIgnoreCase("p") && page > 1) {
                page--;
                continue;
            }

            Integer id = tryParseInt(input);
            if (id != null) {
                for (PizzaManagement.PizzaItem p : items) {
                    if (p.id == id) {
                        return id;
                    }
                }
                System.out.println("No pizza with id " + id + ".");
            } else if (!input.isEmpty()) {
                if (totalPages > 1) {
                    System.out.println("Invalid input. Use n, p, q, or a numeric id.");
                } else {
                    System.out.println("Invalid input. Use q or a numeric id.");
                }
            }
        }
    }

    private static void browseToppingsView(Scanner scanner) {
        List<ToppingManagement.ToppingItem> items = ToppingMenu.getItems();
        browseToppingListView(scanner, items);
    }

    private static Integer browseToppingsSelectId(Scanner scanner, String verb) {
        List<ToppingManagement.ToppingItem> items = ToppingMenu.getItems();
        return browseToppingListSelectId(scanner, items, verb);
    }

    private static void browseToppingListView(Scanner scanner, List<ToppingManagement.ToppingItem> items) {
        int totalPages = Math.max(1, (items.size() + PAGE_SIZE - 1) / PAGE_SIZE);
        int page = 1;

        while (true) {
            printBlankLines(SPACING_LINES);
            System.out.println("========== TOPPINGS (toppings.csv) ==========");
            if (items.isEmpty()) {
                System.out.println("(none)");
                System.out.println("[q] back");
                System.out.print("Your choice: ");
                String in = scanner.nextLine().trim();
                if (in.equalsIgnoreCase("q")) {
                    return;
                }
                continue;
            }

            if (page < 1) page = 1;
            if (page > totalPages) page = totalPages;

            System.out.println("Page " + page + " / " + totalPages + " (max " + PAGE_SIZE + " per page)");
            System.out.println("----------------------------------------");
            int start = (page - 1) * PAGE_SIZE;
            int end = Math.min(start + PAGE_SIZE, items.size());
            for (int i = start; i < end; i++) {
                ToppingManagement.ToppingItem t = items.get(i);
                System.out.println("id=" + t.id + " | " + t.name + " | +$" + String.format("%.2f", t.price));
            }
            System.out.println("----------------------------------------");
            if (totalPages > 1) {
                System.out.println("[n] next  [p] prev  [q] back");
            } else {
                System.out.println("[q] back");
            }
            System.out.print("Your choice: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) {
                return;
            }
            if (input.equalsIgnoreCase("n") && page < totalPages) {
                page++;
            } else if (input.equalsIgnoreCase("p") && page > 1) {
                page--;
            } else if (!input.isEmpty()) {
                System.out.println("Unknown command. Use n, p, or q.");
            }
        }
    }

    private static Integer browseToppingListSelectId(Scanner scanner, List<ToppingManagement.ToppingItem> items, String verb) {
        if (items.isEmpty()) {
            System.out.println("No toppings to " + verb + ".");
            return null;
        }

        int totalPages = Math.max(1, (items.size() + PAGE_SIZE - 1) / PAGE_SIZE);
        int page = 1;

        while (true) {
            printBlankLines(SPACING_LINES);
            System.out.println("========== TOPPINGS (toppings.csv) ==========");
            if (page < 1) page = 1;
            if (page > totalPages) page = totalPages;

            System.out.println("Page " + page + " / " + totalPages + " (max " + PAGE_SIZE + " per page)");
            System.out.println("----------------------------------------");
            int start = (page - 1) * PAGE_SIZE;
            int end = Math.min(start + PAGE_SIZE, items.size());
            for (int i = start; i < end; i++) {
                ToppingManagement.ToppingItem t = items.get(i);
                System.out.println("id=" + t.id + " | " + t.name + " | +$" + String.format("%.2f", t.price));
            }
            System.out.println("----------------------------------------");
            if (totalPages > 1) {
                System.out.println("[n] next  [p] prev  [q] cancel  |  Or type topping id to " + verb + ":");
            } else {
                System.out.println("[q] cancel  |  Or type topping id to " + verb + ":");
            }
            System.out.print("Your choice: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("q")) {
                return null;
            }
            if (input.equalsIgnoreCase("n") && page < totalPages) {
                page++;
                continue;
            }
            if (input.equalsIgnoreCase("p") && page > 1) {
                page--;
                continue;
            }

            Integer id = tryParseInt(input);
            if (id != null) {
                for (ToppingManagement.ToppingItem t : items) {
                    if (t.id == id) {
                        return id;
                    }
                }
                System.out.println("No topping with id " + id + ".");
            } else if (!input.isEmpty()) {
                if (totalPages > 1) {
                    System.out.println("Invalid input. Use n, p, q, or a numeric id.");
                } else {
                    System.out.println("Invalid input. Use q or a numeric id.");
                }
            }
        }
    }

    private static String askTarget(Scanner scanner) {
        printBlankLines(SPACING_LINES);
        System.out.println("Manage which data?");
        System.out.println("  1. Pizza");
        System.out.println("  2. Topping");
        System.out.println("  3. Back");
        System.out.print("Your choice (1-3): ");
        String input = scanner.nextLine().trim();
        if (input.equals("1")) return "pizza";
        if (input.equals("2")) return "topping";
        return null;
    }

    private static Double readDouble(Scanner scanner) {
        String s = scanner.nextLine().trim();
        if (s.isEmpty()) return null;
        return tryParseDouble(s);
    }

    private static Integer tryParseInt(String s) {
        if (s == null || s.isEmpty()) return null;
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Double tryParseDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static void printBlankLines(int count) {
        for (int i = 0; i < count; i++) {
            System.out.println();
        }
    }
}
