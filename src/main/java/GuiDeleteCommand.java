import java.io.IOException;

public class GuiDeleteCommand implements Command {

    private QuerySet querySet;
    private Result result;

    public GuiDeleteCommand(QuerySet querySet) {
        this.querySet = querySet;
    }

    @Override
    public void execute() {
        Order order = new Order(querySet.getProductType(), querySet.getProductModels());
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
