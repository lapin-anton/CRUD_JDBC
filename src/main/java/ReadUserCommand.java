import java.io.IOException;

public class ReadUserCommand implements Command {
    private Result result;

    @Override
    public void execute() {
        Order order = new Order(CommandType.READ_USERS);
        Connector connector = Client.getConnector();
        try {
            connector.clientSend(order);
            result = connector.clientReceive();
        } catch (IOException | ClassNotFoundException e) {
            ExceptionHandler.log(e);
        }
    }

    @Override
    public Result getResult() {
        return result;
    }
}
