import java.util.Scanner;

public class AuctionCO1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== ONLINE AUCTION SYSTEM - CO1 ===");
        System.out.print("Enter seller name: ");
        String seller = sc.nextLine().trim();
        System.out.print("Enter product name: ");
        String product = sc.nextLine().trim();
        System.out.print("Enter description: ");
        String description = sc.nextLine().trim();
        System.out.print("Enter starting price: ");
        double price = sc.nextDouble();

        if (seller.isEmpty() || product.isEmpty() || price <= 0) {
            System.out.println("Invalid input. Seller/product cannot be empty and price must be positive.");
        } else {
            System.out.println("\n--- Product Details ---");
            System.out.println("Seller      : " + seller);
            System.out.println("Product     : " + product);
            System.out.println("Description : " + description);
            System.out.printf("Starting Price: Rs.%.2f%n", price);
        }
        sc.close();
    }
}
