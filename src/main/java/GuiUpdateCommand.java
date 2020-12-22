import java.io.IOException;

public class GuiUpdateCommand {

    private Result result;
    private ClientGuiModel model = new ClientGuiModel();

    public void execute(QuerySet querySet) {
        Order order = new Order(querySet.getProductType(), querySet.getProducts());
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
