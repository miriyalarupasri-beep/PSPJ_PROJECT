```java
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class AuctionSystem {

    static Scanner scanner = new Scanner(System.in);
    static final String DATA_FILE = "auction_data.txt";
    static int nextProductId = 1;

    static List<User> users = new ArrayList<>();
    static List<Product> products = new ArrayList<>();
    static Set<String> categories = new HashSet<>();
    static Map<Integer, Product> productMap = new HashMap<>();

    static abstract class AuctionParticipant {

        private final String username;

        public AuctionParticipant(String username) {
            if (username == null || username.isBlank()) {
                throw new IllegalArgumentException("Username cannot be empty.");
            }

            this.username = username;
        }

        public String getUsername() {
            return username;
        }

        public abstract String getRole();
    }

    static class User extends AuctionParticipant {

        private final String password;

        public User(String username, String password) {
            super(username);

            if (password == null || password.isBlank()) {
                throw new IllegalArgumentException("Password cannot be empty.");
            }

            this.password = password;
        }

        public boolean checkPassword(String password) {
            return this.password.equals(password);
        }

        @Override
        public String getRole() {
            return "Auction User";
        }

        @Override
        public String toString() {
            return getUsername() + " (" + getRole() + ")";
        }
    }

    static class Product {

        private final int id;
        private String name;
        private String description;
        private String category;
        private String seller;
        private double startingPrice;
        private double highestBid;
        private String highestBidder;
        private boolean auctionOpen;

        public Product(
                int id,
                String name,
                String description,
                String category,
                String seller,
                double startingPrice
        ) {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Product name cannot be empty.");
            }

            if (category == null || category.isBlank()) {
                throw new IllegalArgumentException("Category cannot be empty.");
            }

            if (seller == null || seller.isBlank()) {
                throw new IllegalArgumentException("Seller cannot be empty.");
            }

            if (startingPrice <= 0) {
                throw new IllegalArgumentException("Starting price must be greater than zero.");
            }

            this.id = id;
            this.name = name;
            this.description = description;
            this.category = category;
            this.seller = seller;
            this.startingPrice = startingPrice;
            this.highestBid = startingPrice;
            this.auctionOpen = true;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getCategory() {
            return category;
        }

        public String getSeller() {
            return seller;
        }

        public double getStartingPrice() {
            return startingPrice;
        }

        public double getHighestBid() {
            return highestBid;
        }

        public String getHighestBidder() {
            return highestBidder;
        }

        public boolean isAuctionOpen() {
            return auctionOpen;
        }

        public void placeBid(String bidder, double amount) {
            if (!auctionOpen) {
                throw new IllegalStateException("Auction is already closed.");
            }

            if (seller.equalsIgnoreCase(bidder)) {
                throw new IllegalArgumentException("Seller cannot bid on their own product.");
            }

            if (amount <= highestBid) {
                throw new IllegalArgumentException("Bid must be greater than the current highest bid.");
            }

            highestBid = amount;
            highestBidder = bidder;
        }

        public void closeAuction(String username) {
            if (!seller.equalsIgnoreCase(username)) {
                throw new IllegalArgumentException("Only the seller can close this auction.");
            }

            if (!auctionOpen) {
                throw new IllegalStateException("Auction is already closed.");
            }

            auctionOpen = false;
        }

        public String getWinner() {
            if (highestBidder == null) {
                return "No bids";
            }

            return highestBidder;
        }

        public String toFileString() {
            StringBuilder sb = new StringBuilder();

            sb.append(id).append("|");
            sb.append(name).append("|");
            sb.append(description).append("|");
            sb.append(category).append("|");
            sb.append(seller).append("|");
            sb.append(startingPrice).append("|");
            sb.append(highestBid).append("|");

            if (highestBidder != null) {
                sb.append(highestBidder);
            }

            return sb.toString();
        }

        @Override
        public String toString() {
            String status;

            if (auctionOpen) {
                status = "OPEN";
            } else {
                status = "CLOSED";
            }

            return String.format(
                    "%d | %-15s | %-12s | Seller: %-10s | Bid: Rs.%.2f | %s",
                    id,
                    name,
                    category,
                    seller,
                    highestBid,
                    status
            );
        }
    }

    static void addProduct(Product product) {
        products.add(product);
        productMap.put(product.getId(), product);
        categories.add(product.getCategory());
    }

    public static void main(String[] args) {
        loadData();

        if (users.isEmpty()) {
            createDefaultData();
        }

        System.out.println();
        System.out.println("==============================================");
        System.out.println("       ONLINE AUCTION MANAGEMENT SYSTEM");
        System.out.println("==============================================");
        System.out.println("CO1 -> CO6 Integrated Java Project");
        System.out.println();

        boolean running = true;

        while (running) {
            displayMenu();

            int choice = readInt("Enter your choice: ");

            try {
                switch (choice) {
                    case 1:
                        registerUser();
                        break;

                    case 2:
                        loginUser();
                        break;

                    case 3:
                        addNewProduct();
                        break;

                    case 4:
                        displayProducts();
                        break;

                    case 5:
                        searchProduct();
                        break;

                    case 6:
                        sortProducts();
                        break;

                    case 7:
                        placeBid();
                        break;

                    case 8:
                        closeAuction();
                        break;

                    case 9:
                        showStatistics();
                        break;

                    case 10:
                        demonstrateRecursion();
                        break;

                    case 11:
                        saveData();
                        break;

                    case 12:
                        showCategories();
                        break;

                    case 13:
                        running = false;
                        System.out.println("\nThank you for using the Auction System!");
                        break;

                    default:
                        System.out.println("Invalid choice. Please select 1-13.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("\nError: " + e.getMessage());
            }
        }

        scanner.close();
    }

    static void displayMenu() {
        System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println("                 MAIN MENU");
        System.out.println("----------------------------------------------");

        System.out.println("1.  Register User");
        System.out.println("2.  Login");
        System.out.println("3.  Add Product");
        System.out.println("4.  Display Products");
        System.out.println("5.  Search Product");
        System.out.println("6.  Sort Products");
        System.out.println("7.  Place Bid");
        System.out.println("8.  Close Auction");
        System.out.println("9.  Auction Statistics");
        System.out.println("10. Recursion Demonstration");
        System.out.println("11. Save Data");
        System.out.println("12. Display Categories");
        System.out.println("13. Exit");

        System.out.println("----------------------------------------------");
    }

    static void registerUser() {
        System.out.println("\n=== USER REGISTRATION ===");

        String username = readLine("Enter username: ");
        String password = readLine("Enter password: ");

        boolean exists = users.stream()
                .anyMatch(u -> u.getUsername().equalsIgnoreCase(username));

        if (exists) {
            throw new IllegalArgumentException("Username already exists.");
        }

        User user = new User(username, password);
        users.add(user);

        System.out.println("Registration successful.");
    }

    static User loginUser() {
        System.out.println("\n=== LOGIN ===");

        String username = readLine("Username: ");
        String password = readLine("Password: ");

        Optional<User> user = users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username)
                        && u.checkPassword(password))
                .findFirst();

        if (user.isEmpty()) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        System.out.println("Login successful. Welcome " + user.get().getUsername() + "!");
        return user.get();
    }

    static void addNewProduct() {
        System.out.println("\n=== ADD PRODUCT ===");

        String seller = readLine("Enter seller username: ");
        String password = readLine("Enter seller password: ");

        User user = authenticate(seller, password);

        String productName = readLine("Enter product name: ");
        String description = readLine("Enter description: ");
        String category = readLine("Enter category: ");
        double startingPrice = readDouble("Enter starting price: Rs.");

        Product product = new Product(
                nextProductId++,
                productName,
                description,
                category,
                user.getUsername(),
                startingPrice
        );

        addProduct(product);
        saveData();

        System.out.println("Product added successfully.");
    }

    static User authenticate(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username)
                        && u.checkPassword(password))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid username or password."
                ));
    }

    static void displayProducts() {
        System.out.println("\n=== ALL PRODUCTS ===");

        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        products.forEach(System.out::println);
    }

    static void searchProduct() {
        System.out.println("\n=== SEARCH PRODUCT ===");

        String keyword = readLine("Enter product name/category: ").toLowerCase();

        List<Product> result = products.stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword)
                        || p.getCategory().toLowerCase().contains(keyword))
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            System.out.println("No matching product found.");
        } else {
            System.out.println("\nMatching Products:");
            result.forEach(System.out::println);
        }
    }

    static void sortProducts() {
        System.out.println("\n=== SORT PRODUCTS ===");
        System.out.println("1. Sort by Name");
        System.out.println("2. Sort by Highest Bid");
        System.out.println("3. Sort by Category");

        int choice = readInt("Enter choice: ");
        List<Product> sorted = new ArrayList<>(products);

        switch (choice) {
            case 1:
                sorted.sort(Comparator.comparing(
                        Product::getName,
                        String.CASE_INSENSITIVE_ORDER
                ));
                break;

            case 2:
                sorted.sort(Comparator.comparingDouble(
                        Product::getHighestBid
                ).reversed());
                break;

            case 3:
                sorted.sort(Comparator.comparing(
                        Product::getCategory,
                        String.CASE_INSENSITIVE_ORDER
                ));
                break;

            default:
                System.out.println("Invalid sorting choice.");
                return;
        }

        sorted.forEach(System.out::println);
    }

    static void placeBid() {
        System.out.println("\n=== PLACE BID ===");

        String username = readLine("Username: ");
        String password = readLine("Password: ");

        User bidder = authenticate(username, password);

        displayProducts();

        int id = readInt("Enter product ID: ");
        Product product = productMap.get(id);

        if (product == null) {
            throw new IllegalArgumentException("Product not found.");
        }

        double amount = readDouble("Enter bid amount: Rs.");

        product.placeBid(bidder.getUsername(), amount);
        saveData();

        System.out.println("Bid placed successfully.");
    }

    static void closeAuction() {
        System.out.println("\n=== CLOSE AUCTION ===");

        String username = readLine("Seller username: ");
        String password = readLine("Password: ");

        User seller = authenticate(username, password);

        displayProducts();

        int id = readInt("Enter product ID: ");
        Product product = productMap.get(id);

        if (product == null) {
            throw new IllegalArgumentException("Product not found.");
        }

        product.closeAuction(seller.getUsername());
        saveData();

        System.out.println("Auction closed successfully.");
        System.out.println("Winner: " + product.getWinner());

        System.out.printf(
                "Winning Bid: Rs.%.2f%n",
                product.getHighestBid()
        );
    }

    static void showStatistics() {
        System.out.println("\n=== AUCTION STATISTICS ===");

        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        long totalProducts = products.size();

        long openAuctions = products.stream()
                .filter(Product::isAuctionOpen)
                .count();

        long closedAuctions = products.stream()
                .filter(p -> !p.isAuctionOpen())
                .count();

        double averageBid = products.stream()
                .mapToDouble(Product::getHighestBid)
                .average()
                .orElse(0);

        Optional<Product> highest = products.stream()
                .max(Comparator.comparingDouble(Product::getHighestBid));

        System.out.println("Total products : " + totalProducts);
        System.out.println("Open auctions  : " + openAuctions);
        System.out.println("Closed auctions: " + closedAuctions);

        System.out.printf("Average bid    : Rs.%.2f%n", averageBid);

        if (highest.isPresent()) {
            System.out.println("Highest auction: " + highest.get().getName());

            System.out.printf(
                    "Highest bid    : Rs.%.2f%n",
                    highest.get().getHighestBid()
            );
        }

        Map<String, Long> categoryCount = products.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.counting()
                ));

        System.out.println("\nProducts by Category:");

        categoryCount.forEach((category, count) ->
                System.out.println(category + " : " + count)
        );
    }

    static void demonstrateRecursion() {
        System.out.println("\n=== RECURSION DEMONSTRATION ===");

        int number = readInt("Enter a positive integer: ");

        if (number < 0) {
            System.out.println("Enter a non-negative number.");
            return;
        }

        System.out.println("Factorial of " + number + " = " + factorial(number));
    }

    static long factorial(int n) {
        if (n <= 1) {
            return 1;
        }

        return n * factorial(n - 1);
    }

    static void showCategories() {
        System.out.println("\n=== PRODUCT CATEGORIES ===");

        if (categories.isEmpty()) {
            System.out.println("No categories available.");
            return;
        }

        categories.stream()
                .sorted()
                .forEach(category -> System.out.println("- " + category));
    }

    static void saveData() {
        try {
            List<String> lines = products.stream()
                    .map(Product::toFileString)
                    .collect(Collectors.toList());

            Files.write(Paths.get(DATA_FILE), lines);
        } catch (IOException e) {
            System.out.println("Unable to save data: " + e.getMessage());
        }
    }

    static void loadData() {
        Path path = Paths.get(DATA_FILE);

        if (!Files.exists(path)) {
            return;
        }

        try {
            List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                try {
                    String[] data = line.split("\\|", -1);

                    if (data.length < 8) {
                        continue;
                    }

                    int id = Integer.parseInt(data[0]);
                    String name = data[1];
                    String description = data[2];
                    String category = data[3];
                    String seller = data[4];
                    double startingPrice = Double.parseDouble(data[5]);
                    double highestBid = Double.parseDouble(data[6]);
                    String highestBidder = data[7];

                    Product product = new Product(
                            id,
                            name,
                            description,
                            category,
                            seller,
                            startingPrice
                    );

                    if (highestBid > startingPrice) {
                        product.highestBid = highestBid;

                        if (!highestBidder.isBlank()) {
                            product.highestBidder = highestBidder;
                        }
                    }

                    addProduct(product);

                    if (id >= nextProductId) {
                        nextProductId = id + 1;
                    }
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Skipped invalid data record.");
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to read saved data.");
        }
    }

    static void createDefaultData() {
        users.add(new User("seller1", "1234"));
        users.add(new User("buyer1", "1234"));

        addProduct(new Product(
                nextProductId++,
                "Laptop",
                "Gaming Laptop",
                "Electronics",
                "seller1",
                25000
        ));

        addProduct(new Product(
                nextProductId++,
                "Phone",
                "Smartphone",
                "Electronics",
                "seller1",
                15000
        ));

        addProduct(new Product(
                nextProductId++,
                "Headphones",
                "Wireless Headphones",
                "Accessories",
                "seller1",
                3000
        ));

        saveData();
    }

    static String readLine(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    static int readInt(String message) {
        while (true) {
            try {
                return Integer.parseInt(readLine(message));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    static double readDouble(String message) {
        while (true) {
            try {
                double value = Double.parseDouble(readLine(message));

                if (value <= 0) {
                    System.out.println("Value must be greater than zero.");
                    continue;
                }

                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
```