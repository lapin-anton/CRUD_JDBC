public class AddUserCommand implements Command {

    private Result result;
    private User user;

    public AddUserCommand(User user) {
        this.user = user;
    }

    @Override
    public void execute() {
        Order order = new Order(CommandType.ADD_USER, user);
        Connector connector = Client.getConnector();
        try {
            connector.clientSend(order);
            result = connector.clientReceive();
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    @Override
    public Result getResult() {
        return result;
    }
}
