import java.io.*;
import java.util.Hashtable;
import java.util.Scanner;

public class CustomerManager {


    private Hashtable<String, Customer> customerTable;
    private final String FILE_NAME = "\"C:\\Users\\User\\Desktop\\customer (2).txt\"";

    public CustomerManager() {
        customerTable = new Hashtable<>();
        loadCustomersFromFile();
    }

    public Hashtable<String, Customer> getCustomerTable() {
        return customerTable;
    }

    // تحميل البيانات من الملف
    private void loadCustomersFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split(",\\s*");
                if (parts.length >= 7) {
                    String id = parts[0];
                    String name = parts[1];
                    String email = parts[2];
                    int familySize = Integer.parseInt(parts[3]);
                    int emergencyLevel = Integer.parseInt(parts[4]);
                    String location = parts[5];
                    String notes = parts[6];
                    Customer c = new Customer(id, name, email, familySize, emergencyLevel, location, notes);
                    customerTable.put(id, c);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading file: " + e.getMessage());
        }
    }

    // حفظ كل العملاء في الملف
    private void saveAllCustomersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Customer c : customerTable.values()) {
                writer.write(c.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }

    // تحقق إذا كان الاسم والإيميل مكررين
    public boolean isDuplicate(String name, String email) {
        for (Customer c : customerTable.values()) {
            if (c.getName().equalsIgnoreCase(name) && c.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    // إضافة عميل جديد
    public boolean addCustomer(Customer customer) {
        if (isDuplicate(customer.getName(), customer.getEmail())) {
            return false;
        }
        customerTable.put(customer.getId(), customer);
        saveAllCustomersToFile();
        return true;
    }

    // حذف عميل حسب ID
    public boolean deleteCustomer(String id) {
        if (customerTable.containsKey(id)) {
            customerTable.remove(id);
            saveAllCustomersToFile();
            return true;
        }
        return false;
    }

    // search a customer using name and id
    public Customer searchByNameAndId(String name, String id) {
        for (Customer c : customerTable.values()) {
            if (c.getName().equalsIgnoreCase(name) && c.getId().equalsIgnoreCase(id)) {
                return c;
            }
        }
        return null;
    }


}
