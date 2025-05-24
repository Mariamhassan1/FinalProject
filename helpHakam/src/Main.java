//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
// Main application entry point
public class Main {
    public static void main(String[] args) {
        // Get the CustomerManager instance
        CustomerManager manager = CustomerManager.getInstance();

        // Print loaded customer information
        System.out.println("Customer Management System");
        System.out.println("==========================");
        System.out.println("Total customers: " + manager.getCustomerCount());

        // Print all customers
        manager.printAllCustomers();
    }
}