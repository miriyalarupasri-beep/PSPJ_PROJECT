import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AuctionCO6 {
    record Product(int id, String name, String category, double price, boolean open) {}
    record Bid(String bidder, int productId, double amount) {}

    public static void main(String[] args) {
        List<Product> products = new ArrayList<>(List.of(
                new Product(1, "Laptop", "Electronics", 25000, true),
                new Product(2, "Phone", "Electronics", 15000, true),
                new Product(3, "Watch", "Accessories", 5000, false),
                new Product(4, "Headphones", "Electronics", 3000, true)
        ));
        List<Bid> bids = new ArrayList<>(List.of(
                new Bid("rahul", 1, 27000), new Bid("anita", 1, 28000),
                new Bid("rahul", 2, 16000), new Bid("vijay", 4, 3500)
        ));

        System.out.println("=== ONLINE AUCTION SYSTEM - CO6 ===");
        Predicate<Product> openAuction = Product::open;
        System.out.println("\nOpen auctions:");
        products.stream().filter(openAuction).forEach(AuctionCO6::printProduct);

        System.out.println("\nProducts sorted by price:");
        products.stream().sorted(Comparator.comparingDouble(Product::price)).forEach(AuctionCO6::printProduct);

        System.out.println("\nElectronics count: " + products.stream().filter(p -> p.category().equalsIgnoreCase("Electronics")).count());

        Map<Integer, Double> highestBidByProduct = bids.stream()
                .collect(Collectors.groupingBy(Bid::productId, Collectors.collectingAndThen(
                        Collectors.maxBy(Comparator.comparingDouble(Bid::amount)),
                        opt -> opt.map(Bid::amount).orElse(0.0))));
        System.out.println("\nHighest bids by product:");
        highestBidByProduct.forEach((id, amount) -> System.out.printf("Product %d -> Rs.%.2f%n", id, amount));

        Map<String, Long> bidsByBidder = bids.stream().collect(Collectors.groupingBy(Bid::bidder, Collectors.counting()));
        System.out.println("\nBids per bidder:");
        bidsByBidder.forEach((bidder, count) -> System.out.println(bidder + " -> " + count));

        double totalBidValue = bids.stream().mapToDouble(Bid::amount).sum();
        System.out.printf("\nTotal bid value: Rs.%.2f%n", totalBidValue);

        Map<String, List<Product>> byCategory = products.stream().collect(Collectors.groupingBy(Product::category));
        System.out.println("\nProducts grouped by category:");
        byCategory.forEach((category, list) -> System.out.println(category + " -> " + list.stream().map(Product::name).collect(Collectors.joining(", "))));
    }

    static void printProduct(Product p) {
        System.out.printf("ID:%d | %s | %s | Rs.%.2f | %s%n", p.id(), p.name(), p.category(), p.price(), p.open() ? "OPEN" : "CLOSED");
    }
}
