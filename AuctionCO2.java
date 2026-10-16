import java.util.Scanner;
public class AuctionCO2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String product = "Laptop";
        double highestBid = 25000.0;
        boolean auctionOpen = true;
        int choice;

        System.out.println("=== ONLINE AUCTION SYSTEM - CO2 ===");
        while (auctionOpen) {
            System.out.println("\n1. View Product\n2. Place Bid\n3. Close Auction\n4. Exit");
            System.out.print("Enter choice: ");
            choice = readInt(sc);

            switch (choice) {
                case 1 -> System.out.printf("Product: %s | Highest Bid: Rs.%.2f%n", product, highestBid);
                case 2 -> {
                    System.out.print("Enter bid amount: Rs.");
                    double bid = readDouble(sc);
                    if (bid > highestBid) {
                        highestBid = bid;
                        System.out.printf("Bid accepted. New highest bid: Rs.%.2f%n", highestBid);
                    } else {
                        System.out.printf("Bid rejected. Enter more than Rs.%.2f%n", highestBid);
                    }
                }
                case 3 -> {
                    auctionOpen = false;
                    System.out.println("Auction closed.");
                    System.out.printf("Final bid: Rs.%.2f%n", highestBid);
                }
                case 4 -> auctionOpen = false;
                default -> System.out.println("Invalid choice.");
            }
        }
        sc.close();
    }

    private static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Enter a valid integer: ");
            sc.next();
        }
        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    private static double readDouble(Scanner sc) {
        while (!sc.hasNextDouble()) {
            System.out.print("Enter a valid number: ");
            sc.next();
        }
        double value = sc.nextDouble();
        sc.nextLine();
        return value;
    }
}
