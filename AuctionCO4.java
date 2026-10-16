import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AuctionCO4 {
    static class User {
        private final String name;
        private final String username;
        private final String password;
        User(String name, String username, String password) {
            this.name = name; this.username = username; this.password = password;
        }
        String getName() { return name; }
        String getUsername() { return username; }
        boolean checkPassword(String input) { return password.equals(input); }
    }

    static class Product {
        private static int nextId = 1;
        private final int id;
        private final String name;
        private final double startingPrice;
        private final User seller;
        private double highestBid;
        private User highestBidder;
        private boolean open = true;

        Product(String name, double startingPrice, User seller) {
            if (name == null || name.isBlank() || startingPrice <= 0 || seller == null)
                throw new IllegalArgumentException("Invalid product details.");
            this.id = nextId++; this.name = name; this.startingPrice = startingPrice;
            this.highestBid = startingPrice; this.seller = seller;
        }
        int getId() { return id; }
        String getName() { return name; }
        double getHighestBid() { return highestBid; }
        boolean isOpen() { return open; }
        User getSeller() { return seller; }
        void placeBid(User bidder, double amount) {
            if (!open) throw new IllegalStateException("Auction is closed.");
            if (bidder == seller) throw new IllegalArgumentException("Seller cannot bid on own product.");
            if (amount <= highestBid) throw new IllegalArgumentException("Bid must exceed current highest bid.");
            highestBid = amount; highestBidder = bidder;
        }
        void close() { open = false; }
        String winner() { return highestBidder == null ? "No bids" : highestBidder.getUsername(); }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<User> users = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        User current = null;
        boolean running = true;

        System.out.println("=== ONLINE AUCTION SYSTEM - CO4 ===");
        while (running) {
            try {
                if (current == null) {
                    System.out.println("\n1.Register  2.Login  3.View Products  4.Exit");
                    int c = readInt(sc);
                    switch (c) {
                        case 1 -> register(sc, users);
                        case 2 -> current = login(sc, users);
                        case 3 -> displayProducts(products);
                        case 4 -> running = false;
                        default -> System.out.println("Invalid choice.");
                    }
                } else {
                    System.out.println("\nLogged in as: " + current.getUsername());
                    System.out.println("1.Add Product  2.View Products  3.Place Bid  4.Close Auction  5.Logout");
                    int c = readInt(sc);
                    switch (c) {
                        case 1 -> addProduct(sc, products, current);
                        case 2 -> displayProducts(products);
                        case 3 -> placeBid(sc, products, current);
                        case 4 -> closeAuction(sc, products, current);
                        case 5 -> current = null;
                        default -> System.out.println("Invalid choice.");
                    }
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        sc.close();
    }

    static void register(Scanner sc, List<User> users) {
        System.out.print("Name: "); String name = sc.nextLine().trim();
        System.out.print("Username: "); String username = sc.nextLine().trim();
        if (users.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username))) throw new IllegalArgumentException("Username already exists.");
        System.out.print("Password: "); String password = sc.nextLine();
        users.add(new User(name, username, password)); System.out.println("Registration successful.");
    }

    static User login(Scanner sc, List<User> users) {
        System.out.print("Username: "); String username = sc.nextLine().trim();
        System.out.print("Password: "); String password = sc.nextLine();
        for (User u : users) if (u.getUsername().equalsIgnoreCase(username) && u.checkPassword(password)) { System.out.println("Login successful."); return u; }
        System.out.println("Invalid credentials."); return null;
    }

    static void addProduct(Scanner sc, List<Product> products, User seller) {
        System.out.print("Product name: "); String name = sc.nextLine().trim();
        System.out.print("Starting price: "); double price = readDouble(sc);
        products.add(new Product(name, price, seller)); System.out.println("Product added.");
    }

    static void displayProducts(List<Product> products) {
        if (products.isEmpty()) { System.out.println("No products available."); return; }
        for (Product p : products) System.out.printf("ID:%d | %s | Rs.%.2f | %s | Seller:%s | Winner:%s%n", p.getId(), p.getName(), p.getHighestBid(), p.isOpen() ? "OPEN" : "CLOSED", p.getSeller().getUsername(), p.winner());
    }

    static void placeBid(Scanner sc, List<Product> products, User bidder) {
        displayProducts(products); System.out.print("Product ID: "); int id = readInt(sc);
        Product p = findProduct(products, id); if (p == null) throw new IllegalArgumentException("Product not found.");
        System.out.print("Bid amount: "); p.placeBid(bidder, readDouble(sc)); System.out.println("Bid accepted.");
    }

    static void closeAuction(Scanner sc, List<Product> products, User seller) {
        displayProducts(products); System.out.print("Product ID: "); Product p = findProduct(products, readInt(sc));
        if (p == null) throw new IllegalArgumentException("Product not found.");
        if (p.getSeller() != seller) throw new IllegalArgumentException("Only the seller can close the auction.");
        p.close(); System.out.println("Auction closed. Winner: " + p.winner());
    }

    static Product findProduct(List<Product> products, int id) { for (Product p : products) if (p.getId() == id) return p; return null; }
    static int readInt(Scanner sc) { while (!sc.hasNextInt()) { System.out.print("Enter a valid integer: "); sc.next(); } int v = sc.nextInt(); sc.nextLine(); return v; }
    static double readDouble(Scanner sc) { while (!sc.hasNextDouble()) { System.out.print("Enter a valid number: "); sc.next(); } double v = sc.nextDouble(); sc.nextLine(); return v; }
}
