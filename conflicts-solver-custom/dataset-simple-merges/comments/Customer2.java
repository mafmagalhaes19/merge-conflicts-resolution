public class Customer2 {
    private String name;
    private String email;
    private String phoneNumber;

    public Customer2(String name, String email, String phoneNumber) {
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
    public void changeEmail(String email) {
        this.email = email;
        System.out.println("Email changed to: " + email);
    }

    // This method changes the phone number and prints the new one
    public void changePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        System.out.println("Phone number changed to: " + phoneNumber);
    }
}
