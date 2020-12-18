import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class Settings {

    private final static String CONFIG_PATH = "\\settings\\config.ini";
    private final static String DB_URL_PROPERTY = "dbURL";
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";

    private String dbURL;
    private String username;
    private String password;
    private boolean isEdited = false;

    public Settings() throws Exception {
        Properties props = new Properties();
        File f = new File(CONFIG_PATH).getParentFile();
        if(f.exists()) {
            props.load(new FileInputStream(f));
            this.dbURL = props.getProperty(DB_URL_PROPERTY);
            this.username = props.getProperty(USERNAME);
            this.password = props.getProperty(PASSWORD);
        } else {
            isEdited = true;
            ConsoleHelper.writeMessage("Настройки подключения к БД не были сохранены.");
            ConsoleHelper.writeMessage("Пожалуйста, введите новые настройки.");
            ConsoleHelper.writeMessage("Введите тип БД:");
            String db = ConsoleHelper.readString();
            ConsoleHelper.writeMessage("Введите номер порта сервера БД:");
            int port = ConsoleHelper.readInt();
            ConsoleHelper.writeMessage("Введите имя БД:");
            String dbName = ConsoleHelper.readString();
            this.dbURL = String.format("jdbc:%s://localhost:%d/%s?useUnicode=true&serverTimezone=UTC", db, port, dbName);
            ConsoleHelper.writeMessage("Введите имя пользователя:");
            this.username = ConsoleHelper.readString();
            ConsoleHelper.writeMessage("Введите пароль:");
            this.password = ConsoleHelper.readString();
            saveNewSettings();
        }
    }

    public String getDbURL() {
        return dbURL;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setDbURL(String dbURL) {
        isEdited = true;
        this.dbURL = dbURL;
    }

    public void setUsername(String username) {
        isEdited = true;
        this.username = username;
    }

    public void setPassword(String password) {
        isEdited = true;
        this.password = password;
    }

    public void saveNewSettings() throws Exception {
        if(isEdited) {
            Properties properties = new Properties();
            properties.setProperty(DB_URL_PROPERTY, this.dbURL);
            properties.setProperty(USERNAME, this.username);
            properties.setProperty(PASSWORD, this.password);

            File f = new File(CONFIG_PATH).getParentFile();
            FileOutputStream fos = new FileOutputStream(f);
            properties.store(fos, "");
        }
    }
}
