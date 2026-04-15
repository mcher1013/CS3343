import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * OrderHistory - Persist and display past orders in CSV format.
 */
public class OrderHistory {
    private static final Path CSV_PATH = Paths.get("orders.csv");
    private static final DateTimeFormatter TS_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String HEADER = "timestamp,orderNumber,itemCount,total,items";
    private static final int DEFAULT_ORDER_COUNTER = 1000;

    public static int getLastOrderNumberOrDefault() {
        ensureCsvExists();
        List<String> lines;
        try {
            lines = Files.readAllLines(CSV_PATH, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return DEFAULT_ORDER_COUNTER;
        }

        // find last valid record (skip header and blank lines)
        for (int i = lines.size() - 1; i >= 1; i--) {
            String line = lines.get(i);
            if (line == null || line.trim().isEmpty()) {
                continue;
            }
            String[] cols = parseCsvLine(line);
            if (cols.length < 2) {
                continue;
            }
            try {
                return Integer.parseInt(cols[1].trim());
            } catch (NumberFormatException ignored) {
                // keep scanning upward
            }
        }
        return DEFAULT_ORDER_COUNTER;
    }

    public static void appendOrder(OrderCart cart) {
        ensureCsvExists();

        String timestamp = LocalDateTime.now().format(TS_FORMAT);
        int orderNumber = cart.getOrderNumber();
        int itemCount = cart.getPizzaCount();
        double total = cart.getTotalCost();
        String items = cart.getPizzas().stream()
                .map(p -> p.getName() + " ($" + String.format("%.2f", p.getCost()) + ")")
                .collect(Collectors.joining(" | "));

        String line = toCsvLine(timestamp, String.valueOf(orderNumber), String.valueOf(itemCount),
                String.format("%.2f", total), items);

        try (BufferedWriter writer = Files.newBufferedWriter(
                CSV_PATH,
                StandardCharsets.UTF_8,
                StandardOpenOption.APPEND)) {
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Failed to save order history: " + e.getMessage());
        }
    }

    public static void displayHistory() {
        ensureCsvExists();

        List<String> lines;
        try {
            lines = Files.readAllLines(CSV_PATH, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Failed to read order history: " + e.getMessage());
            return;
        }

        if (lines.size() <= 1) {
            System.out.println("\n========== ORDER HISTORY ==========");
            System.out.println("No past orders found.");
            System.out.println("===================================\n");
            return;
        }

        System.out.println("\n========== ORDER HISTORY ==========");
        // Skip header
        for (int i = 1; i < lines.size(); i++) {
            String[] cols = parseCsvLine(lines.get(i));
            if (cols.length < 5) {
                continue;
            }
            System.out.println("-----------------------------------");
            System.out.println("Time: " + cols[0]);
            System.out.println("Order #: " + cols[1]);
            System.out.println("Items: " + cols[2]);
            System.out.println("Total: $" + cols[3]);
            System.out.println("Detail: " + cols[4]);
        }
        System.out.println("-----------------------------------");
        System.out.println("===================================\n");
    }

    public static void displayHistoryPaginated(Scanner scanner) {
        ensureCsvExists();

        List<OrderRecord> records = loadRecordsLatestFirst();
        if (records.isEmpty()) {
            System.out.println("\n========== ORDER HISTORY ==========");
            System.out.println("No past orders found.");
            System.out.println("===================================\n");
            return;
        }

        final int pageSize = 5;
        int totalPages = (records.size() + pageSize - 1) / pageSize;
        int page = 1;

        while (true) {
            printBlankLines(5);
            System.out.println("========== ORDER HISTORY ==========");
            System.out.println("Page " + page + " / " + totalPages + " (latest first)");
            System.out.println("-----------------------------------");

            int start = (page - 1) * pageSize;
            int end = Math.min(start + pageSize, records.size());
            for (int i = start; i < end; i++) {
                OrderRecord r = records.get(i);
                System.out.println("Time: " + r.timestamp);
                System.out.println("Order #: " + r.orderNumber);
                System.out.println("Items: " + r.itemCount);
                System.out.println("Total: $" + r.total);
                System.out.println("Detail: " + r.items);
                System.out.println("-----------------------------------");
            }

            System.out.println("Commands: [n] next, [p] prev, [number] jump, [q] back");
            System.out.print("Your choice: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) {
                printBlankLines(5);
                return;
            }
            if (input.equalsIgnoreCase("n")) {
                if (page < totalPages) {
                    page++;
                }
                continue;
            }
            if (input.equalsIgnoreCase("p")) {
                if (page > 1) {
                    page--;
                }
                continue;
            }
            try {
                int target = Integer.parseInt(input);
                if (target >= 1 && target <= totalPages) {
                    page = target;
                }
            } catch (NumberFormatException ignored) {
                // ignore invalid command
            }
        }
    }

    private static List<OrderRecord> loadRecordsLatestFirst() {
        List<String> lines;
        try {
            lines = Files.readAllLines(CSV_PATH, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return Collections.emptyList();
        }

        ArrayList<OrderRecord> records = new ArrayList<>();
        // Skip header
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null || line.trim().isEmpty()) {
                continue;
            }
            String[] cols = parseCsvLine(line);
            if (cols.length < 5) {
                continue;
            }
            records.add(new OrderRecord(cols[0], cols[1], cols[2], cols[3], cols[4]));
        }
        Collections.reverse(records); // latest first
        return records;
    }

    private static void printBlankLines(int count) {
        for (int i = 0; i < count; i++) {
            System.out.println();
        }
    }

    private static class OrderRecord {
        final String timestamp;
        final String orderNumber;
        final String itemCount;
        final String total;
        final String items;

        private OrderRecord(String timestamp, String orderNumber, String itemCount, String total, String items) {
            this.timestamp = timestamp;
            this.orderNumber = orderNumber;
            this.itemCount = itemCount;
            this.total = total;
            this.items = items;
        }
    }

    private static void ensureCsvExists() {
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
            System.out.println("Failed to create orders.csv: " + e.getMessage());
        }
    }

    private static String toCsvLine(String... fields) {
        return java.util.Arrays.stream(fields)
                .map(OrderHistory::escapeCsv)
                .collect(Collectors.joining(","));
    }

    // RFC4180-ish escaping: quote if contains comma, quote, CR/LF.
    private static String escapeCsv(String s) {
        if (s == null) {
            return "";
        }
        boolean needQuote = s.contains(",") || s.contains("\"") || s.contains("\n") || s.contains("\r");
        String escaped = s.replace("\"", "\"\"");
        return needQuote ? "\"" + escaped + "\"" : escaped;
    }

    // Minimal CSV parser for 1-line records with quotes.
    private static String[] parseCsvLine(String line) {
        if (line == null) {
            return new String[0];
        }
        java.util.ArrayList<String> out = new java.util.ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    cur.append(c);
                }
            } else {
                if (c == ',') {
                    out.add(cur.toString());
                    cur.setLength(0);
                } else if (c == '"') {
                    inQuotes = true;
                } else {
                    cur.append(c);
                }
            }
        }
        out.add(cur.toString());
        return out.toArray(new String[0]);
    }
}
