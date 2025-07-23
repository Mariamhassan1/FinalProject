import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadCustomerData {
    public static void main(String[] args) throws IOException {
        String filePath = "Customer.txt";

        //----------------------- Read and Print File Content -----------------------//
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            System.out.println("Customer Data:");
            System.out.println("---------------------");

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
            e.printStackTrace();
        }

        //------------------- Parse File Lines to Customer Objects ------------------//
        List<Customer> customerList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(", ");

                if (parts.length < 7 ) {
                    if(Customer.getNotes() == null){continue;}
                    else{System.out.println("Incomplete line, skipped: " + line);
                    continue;}
                }

                // Create customer object without passing ID to constructor
                Customer c = new Customer(
                        parts[1],                       // name
                        parts[2],                       // email
                        Integer.parseInt(parts[3]),     // family size
                        Integer.parseInt(parts[4]),     // emergency level
                        parts[5],                       // location
                        parts[6]                        // notes
                );

                // Set the ID directly after construction
                c.id = parts[0];

                customerList.add(c);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        //------------------------ Save the updated list back to file ------------------------//
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Customer c : customerList) {
                writer.write(c.id + ", " + c.name + ", " + c.email + ", " + c.familySize + ", " + c.emergencyLevel + ", " + c.location + ", " + c.notes);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println(" Error writing to file: " + e.getMessage());
        }
    }
}
