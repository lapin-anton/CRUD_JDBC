import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class Client {

    protected static Connector connector;
    private volatile boolean clientConnected = false;
    private static Settings settings;
    private Command currentCommand;

    static {
        try {
            settings = new Settings();
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    private static HashMap<Integer, Command> commands = new HashMap<>();

    static {
        commands.put(CommandType.CREATE.ordinal(), new CreateCommand());
        commands.put(CommandType.READ.ordinal(), new ReadCommand());
        commands.put(CommandType.UPDATE.ordinal(), new UpdateCommand());
        commands.put(CommandType.DELETE.ordinal(), new DeleteCommand());
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    protected void run() {
        SocketThread socketThread = new SocketThread();
        socketThread.start();
        try {
            synchronized (this) {
                this.wait();
                notify();
            }
        } catch (InterruptedException e) {
            ConsoleHelper.writeMessage("Ошибка подключения клиента");
        }
        if(clientConnected) {
            ConsoleHelper.writeMessage("Соединение установлено.");
        } else {
            ConsoleHelper.writeMessage("Произошла ошибка во время работы клиента.");
        }
    }

    public class SocketThread extends Thread {

        @Override
        public void run() {
            String host = settings.getHostName();
            int port = settings.getPort();
            try(Socket socket = new Socket(host, port)) {
                connector = new Connector(socket);
                clientMainLoop();
            } catch (IOException e) {
                notifyConnectionStatusChanged(false);
            }
        }

        protected void clientMainLoop() {
            ConsoleHelper.writeMessage("Вас приветствует CRUD-приложение (Компания оргтехники)");
            while(true) {
                ConsoleHelper.writeMessage("Пожалуйста, выберите действие (укажите его порядковый номер):");
                ConsoleHelper.writeMessage(String.format("%d Создать новую запись", CommandType.CREATE.ordinal()));
                ConsoleHelper.writeMessage(String.format("%d Найти запись", CommandType.READ.ordinal()));
                ConsoleHelper.writeMessage(String.format("%d Обновить существующую запись", CommandType.UPDATE.ordinal()));
                ConsoleHelper.writeMessage(String.format("%d Удалить запись", CommandType.DELETE.ordinal()));
                ConsoleHelper.writeMessage(String.format("%d Выйти", CommandType.NONE.ordinal()));
                int answer = ConsoleHelper.readInt();
                if(answer == CommandType.NONE.ordinal()) {
                    ConsoleHelper.sayGoodbye();
                    break;
                }
                if (commands.containsKey(answer)) {
                    currentCommand = commands.get(answer);
                    currentCommand.execute();
                    Result result = currentCommand.getResult();
                    currentCommand = null;
                }
            }
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            Client.this.clientConnected = clientConnected;
            synchronized (Client.this) {
                Client.this.notify();
            }
        }
    }

    public static Connector getConnector() {
        return connector;
    }
}
