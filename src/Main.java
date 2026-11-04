import java.util.*;

public class Main {
    public static void main(String[] args) {
        InventoryShop inventory = new InventoryShop();
        ShopService service = new ShopService(inventory);

        Rose redRose = new Rose(1, "redRose", "red", "60cm", 2, true);
        Rose whiteRose = new Rose(2, "whiteRose", "white", "55cm", 2.2, true);
        Rose pinkRose = new Rose(3, "pinkRose", "pink", "65cm", 2.5, true);
        Rose yellowRose = new Rose(4, "yellowRose", "yellow", "50cm", 2.3, false);
        Peony peony = new Peony(5, "peony", "light pink", "40cm", 5, BudStage.FULL_BLOOM);
        Lilie lilie = new Lilie(6, "lilie", "pink", "50cm", 3, true);


        service.addFlower(redRose, 10);
        service.addFlower(whiteRose, 23);
        service.addFlower(lilie, 90);
        service.addFlower(pinkRose, 23);
        service.addFlower(peony, 78);
        service.addFlower(yellowRose, 33);

        service.addCustomer(new Customer(1, "Ayana", "0221013030", 20));
        service.addCustomer(new Customer(2, "Dariia", "0998213333", 30));

        service.addSupplier(new Supplier(1, "Flora Company"));
        service.addSupplier(new Supplier(2, "Green field"));

        System.out.println("Flower with id 6: " + service.findFlowerById(6).getName());

        System.out.println("Peony's id: " + service.findFlowerByName("peony").getFlowerId());

        peony.displayInfo();
        service.updateFlowerColor(5, "Orange");
        peony.displayInfo();

        System.out.println("Pink flowers: " + namesF(service.getFlowersByColor("Pink")));

        service.linkSupplierToFlower(1, 4);
        service.linkSupplierToFlower(2, 1);
        service.linkSupplierToFlower(1, 1);
        System.out.println("Suppliers of red rose: " + namesS(service.getSuppliersForFlower(1)));

        System.out.println("Quantity of red rose before ordering it from suppliers (Flora Company): " + inventory.getQuantity(redRose));
        service.orderFromSupplier(1, 1, 10);
        System.out.println("Quantity if red rose after ordering it from suppliers (Flora Company): " + inventory.getQuantity(redRose));

        service.orderFromSupplier(4, 2, 1);


        Order orderAyana = service.createOrder(1, Priority.REGULAR);
        Order orderDariia = service.createOrder(2, Priority.URGENT);

        Scanner scanner = new Scanner(System.in);
        createBouquet(service, inventory, orderAyana, scanner);
        createBouquet(service, inventory, orderDariia, scanner);
        scanner.close();

        System.out.println("Expected poll order: Dariia, Ayana");
        service.submitOrder(orderAyana);
        service.submitOrder(orderDariia);

        service.addEmployee(new Employee(1, "Sabina"));
        Employee sabina = service.findEmployeeById(1);
        int sabinasAllDoneOrders = sabina.processAll(service);
        System.out.println("Processed by " + sabina.getName() + " : " + sabinasAllDoneOrders + " queue size now : " + service.getQueueSize());


        //sorting
        service.printSortedReport("Natural order (by compatable)", null);
//        service.printSortedReport("By price desc: ", Flower.BY_PRICE_DESC);

        //searching & filtering
        System.out.println("Roses: " + namesF(service.filterByType(Rose.class)));
        System.out.println("Flowers with min price 2.0: " + namesF(service.filterByMinPrice(2.0)));
        System.out.println("Confirmed orders: " + service.getOrdersByStatus(OrderStatus.CONFIRMED).size());

        //synchronized crud
        service.removeFlower(4);
        System.out.println("find flower by id 4: " + service.findFlowerById(4));
        System.out.println("find flower by name yellowRose: " + service.findFlowerByName("yellowRose"));
        System.out.println("pink flowers: " + namesF(service.getFlowersByColor("pink")));
        System.out.println("yellow flowers: " + service.getFlowersByColor("yellow"));
        System.out.println("suppliers of yellow flowers: " + service.getSuppliersForFlower(4));
        service.findSupplierById(1).displayInfo();
        System.out.println("In stock: " + inventory.getQuantity(yellowRose));


        //
        service.printSummaryReport();
    }


    private static List<String> namesF(List<Flower> flowers) {
        List<String> result = new ArrayList<>();
        for (Flower f : flowers) {
            result.add(f.getName());
        }
        return result;
    }

    private static List<String> namesS(List<Supplier> suppliers) {
        List<String> result = new ArrayList<>();
        for (Supplier f : suppliers) {
            result.add(f.getCompanyName());
        }
        return result;
    }

    private static void createBouquet(ShopService service, InventoryShop inventory, Order order, Scanner scanner) {
        System.out.println("--------------------------------");
        System.out.println("Lets create bouquet for " + order.getCustomer().getName() + "!");
        System.out.println("Chose from our catalog: ");
        inventory.displayFlowers();
        System.out.println("Enter in format 'flower - quantity': ");
        String[] pairs = scanner.nextLine().split(",");
        for (String pair : pairs) {
            String[] part = pair.trim().split("-");
            if (part.length != 2) {
                System.out.println("Error! Wrong format for '" + pair.trim() + "'. Use 'flower - quantity'");
                continue;
            }

            String flowerName = part[0].trim();
            Flower matchedFlower = service.findFlowerByName(flowerName);

            if (matchedFlower == null) {
                System.out.println("Error: Flower '" + flowerName + "' not found in catalog!");
                continue;
            }

            if (inventory.getQuantity(matchedFlower) == 0) {
                System.out.println("Sorry, " + matchedFlower.getName() + " is out of stock!");
                continue;
            }

            int quantity = 0;
            String currentQuantityInput = part[1].trim();
            while (true) {
                if (currentQuantityInput.equalsIgnoreCase("skip")) {
                    quantity = 0;
                    break;
                }

                if (currentQuantityInput.contains("-")) {
                    System.out.println("Error: You must enter only a positive number! Enter again a number for " + flowerName + " \n(or enter skip if you don't want to add this flower anymore) : ");
                    currentQuantityInput = scanner.nextLine().trim();
                    continue;
                }

                try {
                    quantity = Integer.parseInt(currentQuantityInput);

                    if (quantity <= 0) {
                        System.out.println("Quantity must be greater than 0! Enter again for " + flowerName + ": ");
                        currentQuantityInput = scanner.nextLine().trim();
                        continue;
                    }

                    if (!inventory.checkStock(matchedFlower, quantity)) {
                        System.out.println("Not enough in stock! Available: " + inventory.getQuantity(matchedFlower) + " pcs for " + flowerName + ": ");
                        currentQuantityInput = scanner.nextLine().trim();
                        continue;
                    }

                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Quantity must be a number! Enter again a number for " + flowerName + ": ");
                    currentQuantityInput = scanner.nextLine().trim();
                }
            }

            if (quantity == 0) {
                continue;
            }

            if (!service.addToBouquet(order, matchedFlower.getName(), quantity)) {
                continue;
            }

            System.out.println("Successfully added " + quantity + " " + matchedFlower.getName() + "(s) in current bouquet!");
        }
    }
}