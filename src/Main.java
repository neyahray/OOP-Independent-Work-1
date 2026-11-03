import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Rose redRose = new Rose(1, "redRose", "red", "60cm", 2, true);
        Rose whiteRose = new Rose(2, "whiteRose", "white", "55cm", 2.2, true);
        Rose pinkRose = new Rose(3, "pinkRose", "pink", "65cm", 2.5, true);
        Rose yellowRose = new Rose(4, "yellowRose", "yellow", "50cm", 2.3, false);
        Peony peony = new Peony(5, "Peony", "light pink", "40cm", 5, BudStage.FULL_BLOOM);
        Lilie lilie = new Lilie(6, "Lilie", "pink", "50cm", 3, true);

        Map<Flower, Integer> initialStock = new HashMap<>();
        initialStock.put(redRose, 12);
        initialStock.put(whiteRose, 16);
        initialStock.put(pinkRose, 22);
        initialStock.put(yellowRose,7);
        initialStock.put(peony, 10);
        initialStock.put(lilie, 9);


        InventoryShop inventory = new InventoryShop();
        inventory.setStock(initialStock);


        //3) User role workflows:
        //Customers (creating bouquets)
        Customer customer = new Customer(1, "Ayana", "0221013030", 198);
        System.out.println("Customer bouquet creation:");
        Bouquet bouquet = customer.createBouquet(inventory, scanner);
        Order order = new Order(1, customer, bouquet, OrderStatus.PENDING, customer.getBudget(), Priority.REGULAR);


        Customer vipCustomer = new Customer(2, "Dariia", "0998452367", 3678);
        Bouquet vipBouquet = vipCustomer.createBouquet(inventory, scanner);
        Order urgentOrder = new Order(2, vipCustomer, vipBouquet, OrderStatus.PENDING, vipCustomer.getBudget(), Priority.URGENT);


        //2) Block D
        PriorityQueue<Order> orderQueue = new PriorityQueue<>();
        orderQueue.add(order);
        orderQueue.add(urgentOrder);


        //Workers (handling with orders)
        System.out.println("Urgent orders will automatically be prioritized, regardless of the order in which they were added!");

        while (!orderQueue.isEmpty()) {
            Order currentOrder = orderQueue.poll();
            currentOrder.processOrder(inventory);
            currentOrder.printReceipt();
        }

        //2) Block B
        System.out.println("------------------------------------------------------");
        System.out.println("BLOCK B:");
        Map<String, List<Flower>> groupedFlowers = inventory.groupFlowersByColor();

        for (Map.Entry<String, List<Flower>> e : groupedFlowers.entrySet()) {
            String color = e.getKey();
            List<Flower> flowerList = e.getValue();

            System.out.println("color: " + color.toUpperCase());

            for (Flower f: flowerList) {
                System.out.println(" - " + f.getName() );
            }
        }



        //2) Block C
        System.out.println("-------------------------------------------------------");
        System.out.println("BLOCK C:");
        List<Flower> flowersHit = new ArrayList<>(List.of(peony, lilie, yellowRose, redRose, whiteRose));
        flowersHit.sort(Flower.BY_PRICE_DESC);
        for (Flower f : flowersHit) {
            f.displayInfo();
        }



        System.out.println("-------------------------------------------------------");
        List<Flower> filteredFlowers = inventory.filterByType("Lilie");
        for (Flower f : filteredFlowers) {
            f.displayInfo();
        }


        InventoryShop testInventory = new InventoryShop();
        ShopService service = new ShopService(testInventory);

        service.addFlower(redRose, 12);
        service.addFlower(pinkRose, 22);
        service.addFlower(lilie, 9);

        System.out.println(service.findFlowerById(3).getName());          // pinkRose
        System.out.println(service.findFlowerByName("LILIE").getName());  // Lilie
        System.out.println(service.getFlowersByColor("pink").size());     // 2

        service.updateFlowerColor(3, "red");
        System.out.println(service.getFlowersByColor("pink").size());     // 1
        System.out.println(service.getFlowersByColor("red").size());      // 2

        service.removeFlower(1);
        System.out.println(service.findFlowerById(1));                    // null
        System.out.println(testInventory.getQuantity(redRose));           // 0
        System.out.println(service.getFlowersByColor("red").size());      // 1

        service.addFlower(lilie, 5);   // ошибка: id уже существует
    }
}