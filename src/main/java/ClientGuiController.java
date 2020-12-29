import javax.swing.*;
import java.util.HashMap;

public class ClientGuiController extends Client {
    private AuthUserDialog auth = new AuthUserDialog(this);
    private ClientGuiView view;
    private ClientGuiModel model = new ClientGuiModel();

    public static void main(String[] args) {
        if (Client.getSettings().isNotExists()) {
            new SetDBConnectionDialog();
            return;
        }
        ClientGuiController controller = new ClientGuiController();
        controller.run();
    }

    @Override
    protected void run() {
        SocketThread socketThread = new GuiSocketThread();
        socketThread.start();
    }

    public void initAuth() throws Exception {
        Order order = new Order(auth.getUser());
        connector.clientSend(order);
        Result result = connector.clientReceive();
        if(result.isUserExists()) {
            auth.dispose();
            initView(result.getUser());
        } else {
            JOptionPane.showMessageDialog(auth, "Неправильный логин или пароль", "Информация",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void initDemo() {
        auth.dispose();
        initView(new User());
    }

    public String sendNewUserInfo(User user) {
        AddUserCommand command = new AddUserCommand(user);
        command.execute();
        return command.getResult().getUpdateStatus();
    }

    public Object[][] sendForGetUserInfo() {
        ReadUserCommand command = new ReadUserCommand();
        command.execute();
        return command.getResult().getData();
    }

    public void sendForDeleteUsers(String[] names) {
        DeleteUserCommand command = new DeleteUserCommand(names);
        command.execute();
        model.setNewResult(command.getResult());
    }

    public void sendForUpdateUsers(HashMap<String,User> map) {
        UpdateUserCommand command = new UpdateUserCommand(map);
        command.execute();
        model.setNewResult(command.getResult());
    }

    private class GuiSocketThread extends SocketThread {

        @Override
        protected void clientMainLoop() {
            while (true) {

            }
        }

        @Override
        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            view.notifyConnectionStatusChanged(clientConnected);
        }
    }

    public void initView(User user) {
        view = new ClientGuiView(this, user);
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
