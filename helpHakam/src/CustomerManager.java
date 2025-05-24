import java.io.*;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;

/**
 * CustomerManager class to handle all operations related to customers
 * Uses a Hashtable for in-memory operations and syncs with file storage
 */
public class CustomerManager {
    private static final String FILE_PATH = "C:\\Users\\User\\IdeaProjects\\project1\\src\\customer.txt";
    private Hashtable<String, Customer> customerTable;

    // Singleton instance
    private static CustomerManager instance;

    /**
     * Get singleton instance of CustomerManager
     */
    public static CustomerManager getInstance() {
        if (instance == null) {
            instance = new CustomerManager();
        }
        return instance;
    }

    /**
     * Private constructor - loads data from file into hashtable
     */
    private CustomerManager() {
        customerTable = new Hashtable<>();
        loadDataFromFile();
    }

    /**
     * Load customer data from file into hashtable
     */
    private void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(", ");

                if (parts.length >= 7) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String email = parts[2].trim();
                    int familySize = Integer.parseInt(parts[3].trim());
                    int emergencyLevel = Integer.parseInt(parts[4].trim());
                    String location = parts[5].trim();
                    String notes = parts[6].trim();

                    Customer customer = new Customer(id, name, email, familySize,
                            emergencyLevel, location, notes);
                    customerTable.put(id, customer);
                }
            }

            System.out.println("Loaded " + customerTable.size() + " customers into hash table.");

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number: " + e.getMessage());
        }
    }

    /**
     * Save all customer data from hashtable back to file
     */
    public void saveDataToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Customer customer : customerTable.values()) {
                writer.write(customer.getId() + ", " +
                        customer.getName() + ", " +
                        customer.getEmail() + ", " +
                        customer.getFamilySize() + ", " +
                        customer.getEmergencyLevel() + ", " +
                        customer.getLocation() + ", " +
                        customer.getNotes());
                writer.newLine();
            }
            System.out.println("Data saved to file successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /**
     * Add a new customer to the hashtable
     */
    public boolean addCustomer(Customer customer) {
        if (customerTable.containsKey(customer.getId())) {
            return false; // Customer with this ID already exists
        }

        customerTable.put(customer.getId(), customer);
        saveDataToFile();
        return true;
    }

    /**
     * Find customer by ID
     */
    public Customer findCustomerById(String id) {
        return customerTable.get(id);
    }

    /**
     * Find customer by name (returns first match)
     */
    public Customer findCustomerByName(String name) {
        for (Customer customer : customerTable.values()) {
            if (customer.getName().equalsIgnoreCase(name)) {
                return customer;
            }
        }
        return null;
    }

    /**
     * Find customer by ID and name
     */
    public Customer findCustomer(String id, String name) {
        Customer customer = customerTable.get(id);
        if (customer != null && customer.getName().equalsIgnoreCase(name)) {
            return customer;
        }
        return null;
    }

    /**
     * Delete customer by id
     */
    public boolean deleteCustomer(String id) {
        if (!customerTable.containsKey(id)) {
            return false;
        }

        customerTable.remove(id);
        saveDataToFile();
        return true;
    }

    /**
     * Print all customers to console
     */
    public void printAllCustomers() {
        System.out.println("\nCustomer Details:");
        for (String key : customerTable.keySet()) {
            Customer customer = customerTable.get(key);
            System.out.println("ID: " + key +
                    " | Name: " + customer.getName() +
                    " | Emergency Level: " + customer.getEmergencyLevel() +
                    " | Location: " + customer.getLocation());
        }
    }

    /**
     * Get all customers
     */
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customerTable.values());
    }

    /**
     * Get customer count
     */
    public int getCustomerCount() {
        return customerTable.size();
    }
    /**
     * check if customer already exists
     */
    public boolean findCustomerByNameAndEmail(String name, String email) {
        for (Customer customer : customerTable.values()) {
            if (customer.getName().equalsIgnoreCase(name) && customer.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

}