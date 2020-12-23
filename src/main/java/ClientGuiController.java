public class ClientGuiController extends Client {
    private ClientGuiView view = new ClientGuiView(this);
    private ClientGuiModel model = new ClientGuiModel();
    private boolean isExit = false;

    public static void main(String[] args) {
        ClientGuiController controller = new ClientGuiController();
        controller.run();
    }

    @Override
    protected void run() {
        SocketThread socketThread = new GuiSocketThread();
        socketThread.start();
    }

    private class GuiSocketThread extends SocketThread {

        @Override
        protected void clientMainLoop() {
            while (true);
        }

        @Override
        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            view.notifyConnectionStatusChanged(clientConnected);
        }
    }

    public ClientGuiView getView() {
        return view;
    }

    public void setView(ClientGuiView view) {
        this.view = view;
    }

    public ClientGuiModel getModel() {
        return model;
    }

    public void setModel(ClientGuiModel model) {
        this.model = model;
    }

    public void sendQuery(CommandType commandType, QuerySet querySet) {
        Command command = null;
        switch (commandType) {
            case CREATE: command = new GuiCreateCommand(querySet);
                break;
            case READ: command = new GuiReadCommand(querySet);
                break;
            case UPDATE: command = new GuiUpdateCommand(querySet);
                break;
            case DELETE: command = new GuiDeleteCommand(querySet);
        }
        command.execute();
        model.setNewResult(command.getResult());
        view.refreshTable();
    }
}
