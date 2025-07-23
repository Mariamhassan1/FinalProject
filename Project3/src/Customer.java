import java.util.UUID;

public class Customer {
    private String id;
    private String name;
    private String email;
    private int familySize;
    private int emergencyLevel;
    private String location;
    private String notes;

    // Constructor with auto-generated ID
    public Customer(String name, String email, int familySize, int emergencyLevel, String location, String notes) {
        this.id = UUID.randomUUID().toString();  // generate unique ID
        this.name = name;
        this.email = email;
        this.familySize = familySize;
        this.emergencyLevel = emergencyLevel;
        this.location = location;
        this.notes = notes;
    }

    // Constructor with existing ID (for loading from file)
    public Customer(String id, String name, String email, int familySize, int emergencyLevel, String location, String notes) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.familySize = familySize;
        this.emergencyLevel = emergencyLevel;
        this.location = location;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getFamilySize() {
        return familySize;
    }

    public int getEmergencyLevel() {
        return emergencyLevel;
    }

    public String getLocation() {
        return location;
    }

    public String getNotes() {
        return notes;
    }

    public String toFileString() {
        return String.join(", ", id, name, email, String.valueOf(familySize),
                String.valueOf(emergencyLevel), location, notes == null ? "" : notes);
    }

    @Override
    public String toString() {
        return "ID: " + id + "\nName: " + name + "\nEmail: " + email + "\nFamily Size: " + familySize +
                "\nEmergency Level: " + emergencyLevel + "\nLocation: " + location + "\nNotes: " + notes;
    }
}
