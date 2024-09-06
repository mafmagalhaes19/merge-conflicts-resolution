public class UserAccount {
    private String username;
    private String email;

    // The user account should be identified by username
    public UserAccount(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
