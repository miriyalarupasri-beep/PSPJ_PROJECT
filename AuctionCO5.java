import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class AuctionCO5 {
    record Product(int id, String name, double price) {}

    static final Path FILE = Path.of("auction_products.txt");
    static final List<Product> products = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        loadFromFile();
        if (products.isEmpty()) {
            products.add(new Product(1, "Laptop", 25000));
            products.add(new Product(2, "Phone", 15000));
            saveToFile();
        }

        System.out.println("=== ONLINE AUCTION SYSTEM - CO5 ===");
        System.out.print("Search keyword (blank = all): ");
        String keyword = sc.nextLine().trim();
        List<Product> result = products.stream()
                .filter(p -> keyword.isBlank() || p.name().toLowerCase().contains(keyword.toLowerCase()))
                .sorted(Comparator.comparingDouble(Product::price))
                .toList();

        if (result.isEmpty()) System.out.println("No matching products.");
        else result.forEach(p -> System.out.printf("ID:%d | %s | Rs.%.2f%n", p.id(), p.name(), p.price()));

        System.out.println("\nData is stored in: " + FILE.toAbsolutePath());
        sc.close();
    }

    static void loadFromFile() {
        if (!Files.exists(FILE)) return;
        try {
            for (String line : Files.readAllLines(FILE)) {
                if (line.isBlank()) continue;
                String[] parts = line.split("\\|", -1);
                if (parts.length != 3) continue;
                try { products.add(new Product(Integer.parseInt(parts[0]), parts[1], Double.parseDouble(parts[2]))); }
                catch (NumberFormatException ignored) { System.out.println("Skipped malformed record: " + line); }
            }
        } catch (IOException e) { System.out.println("Could not read file: " + e.getMessage()); }
    }

    static void saveToFile() {
        List<String> lines = products.stream().map(p -> p.id() + "|" + p.name().replace("|", " ") + "|" + p.price()).toList();
        try { Files.write(FILE, lines); }
        catch (IOException e) { System.out.println("Could not write file: " + e.getMessage()); }
    }
}
