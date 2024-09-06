public class UserAccount {
    private String username;
    private String email;

    // The user account should be identified by username
    public UserAccount(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}

