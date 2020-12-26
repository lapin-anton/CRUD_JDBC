import java.io.Serializable;

public class User implements Serializable {
    private String login;
    private String password;
    private UserMode mode;

    public User() {
        this.login = "";
        this.mode = UserMode.NONE;
    }

    public User(String login, String password, UserMode mode) {
        this.login = login;
        this.password = password;
        this.mode = mode;
    }

    public User(String login) {
        this.login = login;
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public UserMode getMode() {
        return mode;
    }
}
