import java.io.IOException;

public class GuiDeleteCommand {

    private ClientGuiModel model = new ClientGuiModel();
    private Result result;

    public void execute(QuerySet querySet) {
        Order order = new Order(querySet.getProductType(), querySet.getProductModels());
        Connector connector = Client.getConnector();
        try {
            connector.clientSend(order);
            result = connector.clientReceive();
        } catch (IOException | ClassNotFoundException e) {
            ExceptionHandler.log(e);
        }
        model.setNewResult(result);
    }

    public ClientGuiModel getModel() {
        return model;
    }
}
