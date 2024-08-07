public class Customer1 {
    private String name;
    private String email;
    private String phoneNumber;

    public Customer1(String name, String email, String phoneNumber) {
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

    // This method should update the email and print the new email
    public void updateEmail(String newEmail) {
        this.email = newEmail;
        System.out.println("Email updated to: " + newEmail);
    }

    // This method changes the phone number and prints the new one
    public void updatePhoneNumber(String newPhoneNumber) {
        this.phoneNumber = newPhoneNumber;
        System.out.println("Phone number updated to: " + newPhoneNumber);
    }
}

