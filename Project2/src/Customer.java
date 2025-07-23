public class Customer {
    String id;             // يتم توليده تلقائيًا داخل الكلاس
    String name;
    String email;
    int familySize;
//-------------getters-------------------------------
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getFamilySize() {
        return familySize;
    }

    public String getEmail() {
        return email;
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

    int emergencyLevel;
    String location;
    String notes;

    //--------constructor: لا يأخذ id من الخارج----------------------------
    public Customer(String name, String email, int familySize, int emergencyLevel, String location, String notes) {
        this.id = generateId();  // يتم إنشاؤه تلقائيًا
        this.name = name;
        this.email = email;
        this.familySize = familySize;
        this.emergencyLevel = emergencyLevel;
        this.location = location;
        this.notes = notes;
    }

    //--------toString: لطباعة بيانات المستخدم بشكل مرتب----------------
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Email: " + email;
    }

    //--------method to generate unique ID-------------------------------
    private String generateId() {
        return "C" + System.currentTimeMillis();
    }
}
