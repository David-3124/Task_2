package utils;

public class AuthorizationUser {

    private String email;
    private String password;

    public AuthorizationUser(String password, String name) {
        this.email = password;
        this.password = name;
    }

    public AuthorizationUser() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
