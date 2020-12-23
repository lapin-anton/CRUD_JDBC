import java.io.IOException;

public class GuiUpdateCommand implements Command {

    private Result result;
    private QuerySet querySet;

    public GuiUpdateCommand(QuerySet querySet) {
        this.querySet = querySet;
    }

    @Override
    public void execute() {
        Order order = new Order(querySet.getProductType(), querySet.getProducts());
        Connector connector = Client.getConnector();
        try {
            connector.clientSend(order);
            result = connector.clientReceive();
        } catch (IOException | ClassNotFoundException e) {
            ExceptionHandler.log(e);
        }
    }

    public Result getResult() {
        return result;
    }
}
