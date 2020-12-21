import java.io.IOException;

public class GuiCreateCommand {

    private ClientGuiModel model = new ClientGuiModel();
    private Result result;

    public void execute(QuerySet querySet) {
        Order order = new Order(querySet.getProductType(), querySet.getProduct());
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
