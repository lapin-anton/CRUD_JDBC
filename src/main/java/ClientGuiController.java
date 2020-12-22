public class ClientGuiController extends Client {
    private ClientGuiView view = new ClientGuiView(this);
    private ClientGuiModel model = new ClientGuiModel();

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
            while(true) {

            }
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

    public void sendSearchQuery(QuerySet querySet) {
        GuiReadCommand guiReadCommand = new GuiReadCommand();
        guiReadCommand.execute(querySet);
        model = guiReadCommand.getModel();
        view.refreshTable();
    }

    public void sendCreateQuery(QuerySet querySet) {
        GuiCreateCommand createCommand = new GuiCreateCommand();
        createCommand.execute(querySet);
        model = createCommand.getModel();
        view.refreshTable();
    }

    public void sendDeleteQuery(QuerySet querySet) {
        GuiDeleteCommand deleteCommand = new GuiDeleteCommand();
        deleteCommand.execute(querySet);
        model = deleteCommand.getModel();
        view.refreshTable();
    }
}
