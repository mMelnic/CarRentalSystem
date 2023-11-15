package carrental.models;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User implements Serializable{
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String fullName;
    private String email;

    // Constructors
    public User(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // For security reasons, avoid exposing the password directly
    // Getter for password should not be used in normal circumstances
    // Setter for password should only be used during user creation or password update
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isValidUser() {
        return isValidUsername(this.getUsername()) && isValidPassword(this.getPassword())
                && isValidFullName(this.getFullName()) && isValidEmail(this.getEmail());
    }

    private boolean isValidUsername(String username) {
        return username != null && !username.isEmpty() && username.length() >= 3;
    }

    private boolean isValidPassword(String password) {
        return password != null && !password.isEmpty() && password.length() >= 8;
    }

    private boolean isValidFullName(String fullName) {
        return fullName != null && !fullName.isEmpty() && fullName.matches("[a-zA-Z\\s']+");
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }

        String[] validEmailExtensions = {"com", "net", "org", "edu"}; // Add more extensions as needed
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+(" +
                String.join("|", validEmailExtensions) + ")$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }
}
