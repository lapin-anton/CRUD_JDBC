import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Server {

    private static Settings settings;
    private static DBManager dbManager;

    static {
        try {
            settings = new Settings();
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    public static void main(String[] args) {
        if (settings.isNotExists()) {
            new SetDBConnectionDialog();
            return;
        }
        Server server = new Server();
        server.run();
    }

    private void run() {
        try {
            int port = settings.getPort();
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                try (Connection connection = DriverManager.getConnection(settings.getDbURL(), settings.getUsername(),
                        settings.getPassword())) {
                    if (connection == null) return;
                    dbManager = new DBManager(connection);
                    System.out.println("Сервер запущен.");
                    ConsoleHelper.writeMessage("Соединение с базой данных установлено.");
                    while (true) {
                        Socket s = serverSocket.accept();
                        Handler handler = new Handler(s);
                        handler.start();
                    }
                } catch (SQLException e) {
                    ExceptionHandler.log(e);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            ConsoleHelper.writeMessage(String.format("Установлено соединение с удаленным адресом %s",
                    socket.getRemoteSocketAddress().toString()));
            try (Connector connector = new Connector(socket)){
                serverMainLoop(connector);
            } catch (Exception e) {
                ConsoleHelper.writeMessage(String.format("Ошибка обмена данными с удаленным адресом %s",
                        socket.getRemoteSocketAddress()));
            } finally {
                ConsoleHelper.writeMessage(String.format("Соединение с удаленным адресом %s закрыто",
                        socket.getRemoteSocketAddress().toString()));
            }
        }

        private void serverMainLoop(Connector connector) throws IOException, ClassNotFoundException {
            while (true) {
                Order order = connector.serverReceive();
                // сервер что-то делает с запросом от клиента в зависимости от характера запроса
                Result result = handleOrder(order);
                // сервер отправляет результат обработки запроса клиенту
                connector.serverSend(result);
            }
        }
    }

    private static Result handleOrder(Order order) {
        Result result = null;
        switch (order.getCommandType()) {
            case SINGLE_READ: result = dbManager.extractProductByModel(order.getProductType(), order.getModel());
                break;
            case CREATE: result = dbManager.createProduct(order);
                break;
            case READ: result = dbManager.extractAllProductsByType(order.getProductType());
                break;
            case UPDATE: result = dbManager.updateProduct(order);
                break;
            case DELETE: result = dbManager.deleteProduct(order);
                break;
            case MULTIPLE_DELETE: result = dbManager.deleteProducts(order);
                break;
            case MULTIPLE_UPDATE: result = dbManager.updateProducts(order);
                break;
            case AUTHORIZATION: result = dbManager.checkUser(order);
                break;
            case ADD_USER: result = dbManager.addUser(order);
                break;
            case READ_USERS: result = dbManager.extractAllUsers();
                break;
            case UPDATE_USERS: result = dbManager.updateUsers(order);
                break;
            case DELETE_USERS: result = dbManager.deleteUsers(order);
        }
        return result;
    }
}
