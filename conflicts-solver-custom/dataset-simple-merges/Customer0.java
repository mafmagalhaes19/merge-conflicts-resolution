public class Customer0 {
    private String name;
    private String email;
    private String phoneNumber;

    public Customer0(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void updateEmail(String newEmail) {
        this.email = newEmail;
        System.out.println("Email updated to: " + newEmail);
    }

    public void updatePhoneNumber(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
        System.out.println("Phone number updated to: " + newPhoneNumber);
    }
}
