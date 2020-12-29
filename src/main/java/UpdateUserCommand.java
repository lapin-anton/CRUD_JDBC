import java.io.IOException;
import java.util.HashMap;

public class UpdateUserCommand implements Command {

    private Result result;
    private HashMap<String, User> users;

    public UpdateUserCommand(HashMap<String, User> users) {
        this.users = users;
    }

    @Override
    public void execute() {
        Order order = new Order(users);
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
