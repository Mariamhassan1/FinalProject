public class Customer {
    String id;
    String name;
    String email;
    int familySize;
    int emergencyLevel;
    String location;
    String notes;

    //----------------------constructor--------------------------
    public Customer(String id, String name, String email, int familySize,
                    int emergencyLevel, String location, String notes) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.familySize = familySize;
        this.emergencyLevel = emergencyLevel;
        this.location = location;
        this.notes = notes;
    }


    public Customer(String name, String email, int familySize, int emergencyLevel, String location, String notes) {
        this.id = generateId();  // randomly given
        this.name = name;
        this.email = email;
        this.familySize = familySize;
        this.emergencyLevel = emergencyLevel;
        this.location = location;
        this.notes = notes;
    }

    //--------method to generate unique ID-------------------------------
    private String generateId() {
        return "C" + System.currentTimeMillis();
    }
    //------------------------getters--------------------------------------------


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
}