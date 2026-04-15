import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class PizzaManagement {
    private static final Path CSV_PATH = Paths.get("pizzas.csv");
    private static final String HEADER = "id,name,price";

    public static List<PizzaItem> getItems() {
        return loadItems();
    }

    public static void addPizza(String name, double price, boolean customization) {
        ensureCsvExists();
        List<PizzaItem> items = loadItems();
        int nextId = items.stream().mapToInt(i -> i.id).max().orElse(0) + 1;
        String line = CsvUtil.toLine(String.valueOf(nextId), name, String.format("%.2f", price));
        try {
            Files.write(CSV_PATH, (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Failed to add pizza: " + e.getMessage());
        }
    }

    public static void updatePizza(int id, String newName, Double newPrice, Boolean customization) {
        ensureCsvExists();
        List<PizzaItem> items = loadItems();
        for (PizzaItem item : items) {
            if (item.id == id) {
                if (newName != null) item.name = newName;
                if (newPrice != null) item.price = newPrice;
                break;
            }
        }
        saveItems(items);
    }

    public static void deletePizza(int id) {
        ensureCsvExists();
        List<PizzaItem> items = loadItems();
        items.removeIf(i -> i.id == id);
        saveItems(items);
    }

    public static void ensureCsvExists() {
        if (Files.exists(CSV_PATH)) {
            return;
        }
        try {
            Files.write(
                    CSV_PATH,
                    (HEADER + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE);
        } catch (IOException e) {
            System.out.println("Failed to create pizzas.csv: " + e.getMessage());
        }
    }

    private static List<PizzaItem> loadItems() {
        ensureCsvExists();
        List<String> lines;
        try {
            lines = Files.readAllLines(CSV_PATH, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return new ArrayList<>();
        }

        List<PizzaItem> items = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null || line.trim().isEmpty()) continue;
            String[] cols = CsvUtil.parseLine(line);
            if (cols.length < 3) continue;
            try {
                int id = Integer.parseInt(cols[0].trim());
                String name = cols[1];
                double price = Double.parseDouble(cols[2].trim());
                items.add(new PizzaItem(id, name, price));
            } catch (NumberFormatException ignored) {
            }
        }
        return items;
    }

    private static void saveItems(List<PizzaItem> items) {
        ensureCsvExists();
        List<String> out = new ArrayList<>();
        out.add(HEADER);
        for (PizzaItem item : items) {
            out.add(CsvUtil.toLine(String.valueOf(item.id), item.name, String.format("%.2f", item.price)));
        }
        try {
            Files.write(CSV_PATH, out, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Failed to save pizzas.csv: " + e.getMessage());
        }
    }

    public static class PizzaItem {
        public final int id;
        public String name;
        public double price;

        public PizzaItem(int id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }
    }
}

