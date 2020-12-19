import product.ProductType;

import java.io.IOException;

public class DeleteCommand implements Command {
    @Override
    public void execute() {
        try {
            while (true) {
                ConsoleHelper.writeMessage("Укажите, товар какого типа необходимо удалить:");
                ConsoleHelper.writeMessage(String.format("%d ПК", ProductType.PC.ordinal()));
                ConsoleHelper.writeMessage(String.format("%d Ноутбук", ProductType.LAPTOP.ordinal()));
                ConsoleHelper.writeMessage(String.format("%d Принтер", ProductType.PRINTER.ordinal()));
                ConsoleHelper.writeMessage("0 Выйти в главное меню");
                int prod_type = ConsoleHelper.readInt();
                if ((prod_type >= 1) && (prod_type <= 3)) {
                    deleteProduct(prod_type);
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    private void deleteProduct(int type) throws IOException, ClassNotFoundException {
        ProductType[] types = ProductType.values();
        ConsoleHelper.writeMessage("Введите модель, которую нужно удалить:");
        String model = ConsoleHelper.readString();
        Order order = new Order(types[type], model);
        Connector connector = Client.getConnector();
        connector.clientSend(order);
        Result result = connector.clientReceive();
        ConsoleHelper.writeMessage(result.getUpdateStatus());
    }
}
