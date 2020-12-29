import java.io.IOException;

public class DeleteUserCommand implements Command {

    private String[] names;
    private Result result;

    public DeleteUserCommand(String[] names) {
        this.names = names;
    }

    @Override
    public void execute() {
        Order order = new Order(names);
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
