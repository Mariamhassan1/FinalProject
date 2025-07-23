package Customer;
public class Customer {
    private String id;
    private String name;
    private String email;
    private int familySize;
    private int emergencyLevel;
    private String location;
    private String notes;

    //----------------------constructor with ID--------------------------
    //we used this constructor to copy the customer from the file to the hashtable and also to give it the same id(in customermanager)
//where?in function loaddatafromfile in customermanager
    public Customer(String id, String name, String email, int familySize,
                    int emergencyLevel, String location, String notes) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.familySize = familySize;
        this.emergencyLevel = emergencyLevel;
        this.location = location;
        this.notes = notes != null ? notes : "";
    }

    //----------------------constructor without ID (auto-generate)--------------------------
    public Customer(String name, String email, int familySize, int emergencyLevel, String location, String notes) {
        this.id = generateId();
        this.name = name;
        this.email = email;
        this.familySize = familySize;
        this.emergencyLevel = emergencyLevel;
        this.location = location;
        this.notes = notes != null ? notes : "";         /* condition ? value_if_true : value_if_false;
                                                       this is a ternary operator,(3 operations in one code line)*/
    }

    //--------method to generate unique ID-------------------------------
    private String generateId() {             //we chosed this way to generate an id cause its the best way in java cause any other functions may be limited with specific num or very complex
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

    //------------------------utility methods--------------------------------------------
    //@Override
   /*To write a function from a father function (a function that has the same name) in order to use it to related operations.
      In addition, the function in the father's class shouldn't be private or final to be re-definable*/


    //---------------------- For repetition cases -------------------------------

    /**
     * Get emergency level description
     */
    public String getEmergencyLevelDescription() {
        switch (emergencyLevel) {
            case 1:
                return "Displacement/Asylum";
            case 2:
                return "Disabled people-can't work";
            case 3:
                return "Elderly";
            case 4:
                return "Family without breadwinner/Unemploymen";

            default:
                return "Unknown level";
        }
    }
}