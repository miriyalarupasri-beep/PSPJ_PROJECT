import java.util.Scanner;

public class AuctionCO3 {
    static final int MAX_PRODUCTS = 20;
    static String[] productNames = new String[MAX_PRODUCTS];
    static double[] highestBids = new double[MAX_PRODUCTS];
    static int productCount = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        addProduct("Laptop", 25000);
        addProduct("Phone", 15000);
        addProduct("Headphones", 3000);

        System.out.println("=== ONLINE AUCTION SYSTEM - CO3 ===");
        int choice;
        do {
            System.out.println("\n1. Display Products\n2. Search Product\n3. Place Bid\n4. Average Bid\n5. Exit");
            choice = readInt(sc, "Enter choice: ");
            switch (choice) {
                case 1 -> displayProducts();
                case 2 -> {
                    System.out.print("Enter product name: ");
                    int index = searchProduct(sc.nextLine().trim());
                    System.out.println(index >= 0 ? "Found at index " + index : "Product not found.");
                }
                case 3 -> placeBid(sc);
                case 4 -> System.out.printf("Average current bid: Rs.%.2f%n", averageBid());
                case 5 -> System.out.println("Exiting...");
                default -> System.out.println("Invalid choice.");
            }
        } while (choice != 5);
        sc.close();
    }

    static void addProduct(String name, double bid) {
        if (productCount < MAX_PRODUCTS) {
            productNames[productCount] = name;
            highestBids[productCount] = bid;
            productCount++;
        }
    }

    static void displayProducts() {
        System.out.println("\n--- Products ---");
        for (int i = 0; i < productCount; i++) {
            System.out.printf("%d. %s - Rs.%.2f%n", i + 1, productNames[i], highestBids[i]);
        }
    }

    static int searchProduct(String name) {
        for (int i = 0; i < productCount; i++) {
            if (productNames[i].equalsIgnoreCase(name)) return i;
        }
        return -1;
    }

    static void placeBid(Scanner sc) {
        displayProducts();
        int index = readInt(sc, "Enter product number: ") - 1;
        if (index < 0 || index >= productCount) {
            System.out.println("Invalid product number.");
            return;
        }
        double bid = readDouble(sc, "Enter new bid: Rs.");
        if (bid > highestBids[index]) {
            highestBids[index] = bid;
            System.out.println("Bid accepted.");
        } else {
            System.out.println("Bid must be higher than the current bid.");
        }
    }

    static double averageBid() {
        if (productCount == 0) return 0;
        double total = 0;
        for (int i = 0; i < productCount; i++) total += highestBids[i];
        return total / productCount;
    }

    static int readInt(Scanner sc, String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) { System.out.print("Enter a valid integer: "); sc.next(); }
        int value = sc.nextInt(); sc.nextLine(); return value;
    }

    static double readDouble(Scanner sc, String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextDouble()) { System.out.print("Enter a valid number: "); sc.next(); }
        double value = sc.nextDouble(); sc.nextLine(); return value;
    }
}
