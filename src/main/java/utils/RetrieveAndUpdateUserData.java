package utils;

public class RetrieveAndUpdateUserData {
    private String email;
    private String password;
    private String name;
    private String authorization;

    public RetrieveAndUpdateUserData(String email, String password, String authorization, String name) {
        this.email = email;
        this.password = password;
        this.authorization = authorization;
        this.name = name;
    }

    public RetrieveAndUpdateUserData(String authorization) {
        this.authorization = authorization;
    }

    public RetrieveAndUpdateUserData() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
