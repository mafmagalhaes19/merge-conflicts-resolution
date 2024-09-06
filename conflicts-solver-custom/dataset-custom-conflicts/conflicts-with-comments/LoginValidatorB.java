public class LoginValidator {
    //The username should have more or equal to 5 tokens 
    public boolean isValidUsername(String username) {
        return username.length() >= 5;
    }
}

