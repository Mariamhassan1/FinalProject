package Sorting;

import Customer.Customer;

import java.io.*;
import java.util.*;

/**
 * Sorting.CustomerSorter class handles sorting customers based on priority system:
 * 1. Priority level (1 > 2 > 3 > 4)
 * 2. Family size (larger families first)
 * 3. Location (Jerusalem > West Jerusalem > East Jerusalem)
 */
public class CustomerSorter {

    // Location priority mapping - lower number = higher priority
    private static final Map<String, Integer> LOCATION_PRIORITY = new HashMap<>(); //an map that starts empty
    //here we used the hashMap it is one from the java collections(arraylist/hashset)
    //without these <>"generics " the map will be (raw type:dont give the data a specific type so we should do cast bypuerselves)

    static {        //our map is static final(static blockثابتة) we made these 3 lines to prepare the map with a first template when the first class running
        LOCATION_PRIORITY.put("Jerusalem", 1);           // Highest priority
        LOCATION_PRIORITY.put("West Jerusalem", 2);      // Medium priority
        LOCATION_PRIORITY.put("East Jerusalem", 3);      // Lower priority
    }

    /**
     * Sort customers based on the priority system and return sorted list
     */
    public static List<Customer> sortCustomers(List<Customer> customers) {
        List<Customer> sortedList = new ArrayList<>(customers);

        sortedList.sort(Comparator          //::   method reference
                .comparingInt(Customer::getEmergencyLevel)    //customer -> customer.getEmergencyLevel()// Primary: Priority (1,2,3,4)
                .thenComparing((Customer c) -> -c.getFamilySize())           // Secondary: Family size (descending because of that we added "-" cause java sorts from low to high)
                .thenComparing(c -> LOCATION_PRIORITY.getOrDefault(          // Tertiary: Location priority
                        c.getLocation(), Integer.MAX_VALUE))        //first search about the key in the hashmap to get the location by number,if not found will do(Integer.MAX_VALUE):that puts max number which means lowest priority
        );

        return sortedList;
    }

    /**
     * Sort and save customers to file, then update hashtable
     */
    public static boolean sortAndSaveToFile(String filename, Hashtable<String, Customer> customerTable) {
        try {
            // Get all customers from hashtable
            List<Customer> customers = new ArrayList<>(customerTable.values());

            // Sort the customers
            List<Customer> sortedCustomers = sortCustomers(customers);

            // Create backup of existing file
            File originalFile = new File(filename);
            if (originalFile.exists()) {
                File backupFile = new File(filename + ".backup");
                try (FileInputStream fis = new FileInputStream(originalFile);
                     FileOutputStream fos = new FileOutputStream(backupFile)) {
                    byte[] buffer = new byte[1024];    //we will use it to read a parts from the file
                    int length;
                    while ((length = fis.read(buffer)) > 0) {       //We read from the original file part by part (1024 bytes each time), and write the same part to the new file, and repeat this process until we finish the entire file.
                        fos.write(buffer, 0, length);
                    }
                }
            }

            // Write sorted data to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (Customer customer : sortedCustomers) {
                    writer.write(formatCustomerForFile(customer));   //to take customer customer and made the code on it
                    writer.newLine();
                }
            }

            // Update hashtable to maintain order (clear and repopulate)
            //we do that Because the Hashtable doesn't save the order, so we empty it and then add customers according to the sorted list .
            customerTable.clear();
            for (Customer customer : sortedCustomers) {   //to take customer customer and made the code on it
                customerTable.put(customer.getId(), customer);
            }

            System.out.println("Database sorted and saved successfully. Total customers: " + sortedCustomers.size());
            return true;

        } catch (IOException e) {
            System.err.println("Error sorting and saving customers: " + e.getMessage());
            return false;
        }
    }

    /**
     * Insert customer in correct sorted position
     */
    public static boolean insertCustomerInSortedOrder(Customer newCustomer, String filename,
                                                      Hashtable<String, Customer> customerTable) {
        try {
            // Add customer to hashtable first
            customerTable.put(newCustomer.getId(), newCustomer);

            // Get all customers and sort them
            List<Customer> allCustomers = new ArrayList<>(customerTable.values());
            List<Customer> sortedCustomers = sortCustomers(allCustomers);

            // Write sorted list back to file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
                for (Customer customer : sortedCustomers) {
                    writer.write(formatCustomerForFile(customer));
                    writer.newLine();
                }
            }

            // Update hashtable with sorted order
            customerTable.clear();
            for (Customer customer : sortedCustomers) {
                customerTable.put(customer.getId(), customer);
            }

            System.out.println("Customer '" + newCustomer.getName() + "' inserted in sorted position.");
            return true;

        } catch (IOException e) {
            System.err.println("Error inserting customer in sorted order: " + e.getMessage());
            // Rollback hashtable if file operation failed
            customerTable.remove(newCustomer.getId());
            return false;
        }
    }

    /**
     * Format customer data for file storage (matching your current format)
     */
    private static String formatCustomerForFile(Customer customer) {               // تحوّل بيانات العميل (Customer) من كائن (object) إلى سطر نصي منسق، عشان تكتبيه داخل ملف نصي
        return customer.getId() + ", " +
                customer.getName() + ", " +
                customer.getEmail() + ", " +
                customer.getFamilySize() + ", " +
                customer.getEmergencyLevel() + ", " +
                customer.getLocation() + ", " +
                customer.getNotes();
    }
}