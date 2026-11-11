package com.pluralsight;
import java.io.*;
import java.util.*;

public class Store {

    public static void main(String[] args) {

        // Create lists for inventory and the shopping cart
        ArrayList<Product> inventory = new ArrayList<>();
        ArrayList<Product> cart = new ArrayList<>();

        // Load inventory from the data file (pipe-delimited: SKU|Product Name|Price|Department)
        loadInventory("products.csv", inventory);

        // Main menu loop
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice != 3) {
            System.out.println("\nWelcome to the Online Store!");
            System.out.println("1. Show Products");
            System.out.println("2. Show Cart");
            System.out.println("3. Exit");
            System.out.print("Your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter 1, 2, or 3.");
                scanner.nextLine();                 // discard bad input
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine();                     // clear newline

            switch (choice) {
                case 1 -> {
                    displayProducts(inventory, cart, scanner);
                }
                case 2 -> displayCart(cart, scanner);
                case 3 -> System.out.println("Thank you for shopping with us!");
                default -> System.out.println("Invalid choice!");
            }
        }
        scanner.close();
    }

    /**
     * Reads product data from a file and populates the inventory list.
     * File format (pipe-delimited), the first line is a header row:
     * SKU|Product Name|Price|Department
     * <p>
     * Example line:
     * AV1051|JBL Bluetooth Speaker|89.95|Audio Video
     */
    public static void loadInventory(String fileName, ArrayList<Product> inventory) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("products.csv"));

            // The first line of the file is the column names, not a city.
            // Read it once before the loop so the loop never sees it
            // (parseInt("population") would throw NumberFormatException).
            bufferedReader.readLine();                                  // first line is the header: read it and drop it

            String csvLine;
            while ((csvLine = bufferedReader.readLine()) != null) {
                String[] tokens = csvLine.split("\\|");
                String sku = tokens[0];
                String productName = tokens[1];
                Double price = Double.parseDouble(tokens[2]);
                String department = tokens[3];


                inventory.add(new Product(sku, productName, price, department));
            }

            bufferedReader.close();

        } catch (Exception ex) {
            System.out.println("Something went wrong!");
        }
    }

    /**
     * Displays all products and lets the user add one to the cart.
     * Typing X returns to the main menu.
     */
    public static void displayProducts(ArrayList<Product> inventory,
                                       ArrayList<Product> cart,
                                       Scanner scanner) {
        String input;
        while (true) {
            System.out.println("Press V to view all products or S to search by sku number.");
            input = scanner.nextLine();
            if (input.equalsIgnoreCase("v")) {
                System.out.printf("%-10s|%-40s|%-10s|%-20s\n", "SKU Number", "Product Name", "Price", "Department");
                for (Product product : inventory) {
                    System.out.println(product);
                }
                break;
            } else if (input.equalsIgnoreCase("s")) {
                System.out.println("Enter sku number");
                String id = scanner.nextLine();
                Product foundProduct = findProductById(id, inventory);
                System.out.printf("%-10s|%-40s|%-10s|%-20s\n", "SKU Number", "Product Name", "Price", "Department");
                if (foundProduct != null) {
                    System.out.println(foundProduct);
                } else
                    System.out.println("No match found");
                break;
            }

            System.out.println("Please enter a valid option");
        }

        System.out.println("Enter a product sku number to add it to the cart or press x to return home");
        input = scanner.nextLine();
        if (input.equalsIgnoreCase("x")) {
            return;
        }
        for (Product product : inventory) {
            if (input.equalsIgnoreCase(product.getSku())) {
                cart.add(product);
                System.out.println("Item added to cart!");
            }
        }
    }

    /**
     * Shows the contents of the cart, calculates the total,
     * and offers the option to check out.
     */
    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {
        double totalAmount = 0;
        System.out.printf("%-10s|%-40s|%-10s|%-20s\n", "SKU Number", "Product Name", "Price", "Department");
        for (Product product : cart) {
            System.out.println(product);
            totalAmount += product.getPrice();
        }
        System.out.println();

        while (true) {
            System.out.println("Your total is: " + totalAmount + ". Press C to checkout or X to return home.");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("c")) {
                checkOut(cart, totalAmount, scanner);
            } else if (input.equalsIgnoreCase("x")) {
                return;
            } else {
                System.out.println("Please enter a valid selection");
            }
        }
    }

    /**
     * Handles the checkout process:
     * 1. Confirm that the user wants to buy.
     * 2. Accept payment and calculate change.
     * 3. Display a simple receipt.
     * 4. Clear the cart.
     */
    public static void checkOut(ArrayList<Product> cart,
                                double totalAmount,
                                Scanner scanner) {
        Double payment;
        Double change;
        System.out.println("Press Y to continue with your purchase");
        String input = scanner.nextLine();
        if (!input.equalsIgnoreCase("y")) {
            return;
        }
        System.out.println("Enter your payment amount (ex. 120.00");
        while (true) {
            payment = scanner.nextDouble();
            if (payment >= totalAmount && !payment.isNaN()) {
                change = payment - totalAmount;
                break;
            }
            System.out.println("Please enter a valid amount: ");
        }
        scanner.nextLine();
        System.out.println("Thank you for your purchase, here is your receipt");
        System.out.printf(
                "%s%nTotal: %.2f%nPayment: %.2f%nChange: %.2f%nThank you for shopping with us%n",
                cart,
                totalAmount,
                payment,
                change
        );
        cart.clear();
        scanner.close();
    }

    /**
     * Searches a list for a product by its id.
     *
     * @return the matching Product, or null if not found
     */
    public static Product findProductById(String id, ArrayList<Product> inventory) {
        for (Product product : inventory) {
            if (id.equalsIgnoreCase(product.getSku())) {
                return product;
            }
        }
        return null;
    }

}
