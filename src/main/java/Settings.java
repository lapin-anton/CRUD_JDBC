import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class Settings {

    private final static String CONFIG_PATH = "\\settings\\config.ini";
    private final static String DB_URL_PROPERTY = "dbURL";
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private final static String HOST = "host";
    private final static String PORT = "port";
    private final static String DB_URL = "jdbc:mysql://localhost:%s/%s?useUnicode=true&serverTimezone=UTC";

    // настройки для подключения к БД
    private String dbURL;
    private String username;
    private String password;
    // настройки для подключения к клиента к серверу
    private int port;
    private String hostName;
    // флаг изменения настроек
    private boolean isEdited = false;
    private boolean isNotExists = false;

    public Settings() throws Exception {
        Properties props = new Properties();
        File f = new File(CONFIG_PATH).getParentFile();
        if(f.exists()) {
            props.load(new FileInputStream(f));
            this.dbURL = props.getProperty(DB_URL_PROPERTY);
            this.username = props.getProperty(USERNAME);
            this.password = props.getProperty(PASSWORD);
            this.hostName = props.getProperty(HOST);
            this.port = Integer.parseInt(props.getProperty(PORT));
        } else {
            isNotExists = true;
            ConsoleHelper.writeMessage("Файл с настройками приложения отсутствует. \n" +
                    "Пожалуйста, запустите клиент приложения. В диалоговом окне настроек \n" +
                    "внесите все необходимые данные. Затем закройте клиент, запустите сервер \n" +
                    " и затем снова запустите клиент.");
        }
    }

    public Settings(String dbName, String dbPort, String username, String password, int port, String hostName)
            throws Exception {
        isEdited = true;
        this.dbURL = String.format(DB_URL, dbPort, dbName);
        this.username = username;
        this.password = password;
        this.port = port;
        this.hostName = hostName;
        saveNewSettings();
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

    public void setPort(int port) {
        isEdited = true;
        this.port = port;
    }

    public void setHostName(String hostName) {
        isEdited = true;
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public String getHostName() {
        return hostName;
    }

    public void saveNewSettings() throws Exception {
        if(isEdited) {
            Properties properties = new Properties();
            properties.setProperty(DB_URL_PROPERTY, this.dbURL);
            properties.setProperty(USERNAME, this.username);
            properties.setProperty(PASSWORD, this.password);
            properties.setProperty(HOST, this.hostName);
            properties.setProperty(PORT, String.valueOf(this.port));
            File f = new File(CONFIG_PATH).getParentFile();
            FileOutputStream fos = new FileOutputStream(f);
            properties.store(fos, "");
        }
    }

    public boolean isNotExists() {
        return isNotExists;
    }
}
