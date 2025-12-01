package Final.POS;

public class POSSystem {
    private Inventory inventory;
    private Cart cart;
    private Scanner scanner;

    public POSSystem() {
        inventory = new Inventory();
        cart = new Cart();
        scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("~ ____   ___  ____    ____            _                 ~");
        System.out.println("~|  _ \\ / _ \\/ ___|  / ___| _   _ ___| |_ ___ _ __ ___  ~");
        System.out.println("~| |_) | | | \\___ \\  \\___ \\| | | / __| __/ _ \\ '_ ` _ \\ ~");
        System.out.println("~|  __/| |_| |___) |  ___) | |_| \\__ \\ ||  __/ | | | | |~");
        System.out.println("~|_|    \\___/|____/  |____/ \\__, |___/\\__\\___|_| |_| |_|~");
        System.out.println("~                           |___/                       ~");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        while (running) {
            DisplayUtil.displayMenu();
            System.out.print("\nEnter your choice: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        displayAllProducts();
                        break;
                    case 2:
                        addItemToCart();
                        break;
                    case 3:
                        viewCart();
                        break;
                    case 4:
                        calculateTotal();
                        break;
                    case 5:
                        setTaxRate();
                        break;
                    case 6:
                        generateReceipt();
                        break;
                    case 7:
                        running = false;
                        System.out.println("\n╔════════════════════════════════════╗");
                        System.out.println("║   Thank you for using POS System!  ║");
                        System.out.println("╚════════════════════════════════════╝");
                        break;
                    default:
                        System.out.println("\n✗ Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("\n✗ Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear invalid input
            }
        }

        scanner.close();
    }

    private void displayAllProducts() {
        List<Product> products = inventory.getAllProducts();
        int currentIndex = 0;
        int pageSize = 10;
        boolean browsing = true;

        while (browsing) {
            DisplayUtil.displayProducts(products, currentIndex, currentIndex + pageSize);

            System.out.println("\nOptions:");
            if (currentIndex > 0) {
                System.out.println("P - Previous");
            }
            if (currentIndex + pageSize < products.size()) {
                System.out.println("N - Next");
            }
            System.out.println("B - Back to Menu");
            System.out.print("\nEnter option: ");

            String option = scanner.nextLine().toUpperCase();

            switch (option) {
                case "N":
                    if (currentIndex + pageSize < products.size()) {
                        currentIndex += pageSize;
                    } else {
                        System.out.println("\n✗ Already at the last page.");
                    }
                    break;
                case "P":
                    if (currentIndex > 0) {
                        currentIndex -= pageSize;
                    } else {
                        System.out.println("\n✗ Already at the first page.");
                    }
                    break;
                case "B":
                    browsing = false;
                    break;
                default:
                    System.out.println("\n✗ Invalid option.");
            }
        }
    }

    private void addItemToCart() {
        System.out.print("\nEnter Product ID (e.g., 001): ");
        String id = scanner.nextLine();
        Product product = inventory.findProductById(id);
        if (product == null) {
            System.out.println("\n✗ Product not found!");
            return;
        }

        if (product.getQuantity() == 0) {
            System.out.println("\n✗ Product is out of stock!");
            return;
        }

        System.out.print("Enter quantity: ");
        try {
            int quantity = scanner.nextInt();
            scanner.nextLine();

            if (quantity <= 0) {
                System.out.println("\n✗ Quantity must be positive!");
                return;
            }

            if (quantity > product.getQuantity()) {
                System.out.println("\n✗ Not enough stock! Available: " + product.getQuantity());
                return;
            }

            cart.addItem(product, quantity);
            System.out.println("\n✓ Added " + quantity + "x " + product.getName() + " to cart!");
            System.out.printf("  Price after discount: $%.2f each%n", product.getPriceAfterDiscount());
        } catch (Exception e) {
            System.out.println("\n✗ Invalid quantity!");
            scanner.nextLine();
        }
    }

    private void viewCart() {
        DisplayUtil.displayCart(cart.getItems());
    }

    private void calculateTotal() {
        if (cart.isEmpty()) {
            System.out.println("\n✗ Cart is empty!");
            return;
        }

        System.out.println("\n╔════════════════════════════════════╗");
        System.out.println("║         CART SUMMARY               ║");
        System.out.println("╠════════════════════════════════════╣");
        System.out.printf("║ Subtotal:            $%12.2f ║%n", cart.getSubtotal());
        System.out.printf("║ Tax (%.1f%%):           $%12.2f ║%n", cart.getTaxRate(), cart.getTax());
        System.out.println("╠════════════════════════════════════╣");
        System.out.printf("║ TOTAL:               $%12.2f ║%n", cart.getTotal());
        System.out.println("╚════════════════════════════════════╝");
    }

    private void setTaxRate() {
        System.out.printf("\nCurrent tax rate: %.1f%%%n", cart.getTaxRate());
        System.out.print("Enter new tax rate (percentage): ");
        
        try {
            double taxRate = scanner.nextDouble();
            scanner.nextLine();

            if (taxRate < 0 || taxRate > 100) {
                System.out.println("\n✗ Tax rate must be between 0 and 100!");
                return;
            }

            cart.setTaxRate(taxRate);
            System.out.printf("\n✓ Tax rate updated to %.1f%%%n", taxRate);
        } catch (Exception e) {
            System.out.println("\n✗ Invalid tax rate!");
            scanner.nextLine();
        }
    }

    private void generateReceipt() {
        if (cart.isEmpty()) {
            System.out.println("\n✗ Cart is empty! Add items before checkout.");
            return;
        }

        System.out.print("\nConfirm checkout? (Y/N): ");
        String confirm = scanner.nextLine().toUpperCase();

        if (confirm.equals("Y")) {
            DisplayUtil.displayReceipt(cart, inventory);
            System.out.println("\n✓ Transaction completed successfully!");
        } else {
            System.out.println("\n✗ Checkout cancelled.");
        }
    }
        public static void main(String[] args) {
        POSSystem pos = new POSSystem();
        pos.start();
    }
}






