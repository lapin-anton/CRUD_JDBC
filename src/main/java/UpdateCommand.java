import product.*;

import java.io.IOException;

public class UpdateCommand implements Command {

    private Result result;

    @Override
    public void execute() {
        try {
            while (true) {
                ConsoleHelper.writeMessage("Укажите, данные товара какого типа необходимо изменить:");
                ConsoleHelper.writeMessage(String.format("%d ПК", ProductType.PC.ordinal()));
                ConsoleHelper.writeMessage(String.format("%d Ноутбук", ProductType.LAPTOP.ordinal()));
                ConsoleHelper.writeMessage(String.format("%d Принтер", ProductType.PRINTER.ordinal()));
                ConsoleHelper.writeMessage("0 Выйти в главное меню");
                int prod_type = ConsoleHelper.readInt();
                if((prod_type >= 1) && (prod_type <= 3)) {
                    updateProduct(prod_type);
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    protected void updateProduct(int prod_type) throws IOException, ClassNotFoundException {
        Product product = null;
        ProductType[] types = ProductType.values();
        String modelForUpdate = getModelForUpdate();
        Order order = new Order(CommandType.SINGLE_READ, types[prod_type], modelForUpdate);
        Connector connector = Client.getConnector();
        connector.clientSend(order);
        result = connector.clientReceive();
        if (result.getUpdateStatus().equals("OK")) {
            product = result.getProductList().get(0);
        } else {
            ConsoleHelper.writeMessage(result.getUpdateStatus());
            return;
        }
        switch (types[prod_type]) {
            case PC: product = getPCForUpdate((PC) product);
                break;
            case LAPTOP: product = getLaptopForUpdate((Laptop) product);
                break;
            case PRINTER: product = getPrinterForUpdate((Printer) product);
        }
        order = new Order(types[prod_type], modelForUpdate, product);
        connector.clientSend(order);
        result = connector.clientReceive();
        ConsoleHelper.writeMessage(result.getUpdateStatus());
    }

    private Product getPrinterForUpdate(Printer printer) {
        ConsoleHelper.writeMessage(String.format("Изменить модель '%s' на:", printer.getModel()));
        String model = ConsoleHelper.readString();
        ConsoleHelper.writeMessage(String.format("Изменить производителя '%s' на:", printer.getMaker()));
        String maker = ConsoleHelper.readString();
        ConsoleHelper.writeMessage(String.format("Изменить значение цены '%d' на:", printer.getPrice()));
        int price = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage(String.format("Изменить тип печати '%s' на:", printer.getType()));
        String type = ConsoleHelper.readString();
        ConsoleHelper.writeMessage(String.format("Изменить статус цветной печати '%s' на:", printer.getColor()));
        String color = ConsoleHelper.readString();
        return new Printer(0, model, maker, price, type, color);
    }

    private Product getLaptopForUpdate(Laptop laptop) {
        ConsoleHelper.writeMessage(String.format("Изменить модель '%s' на:", laptop.getModel()));
        String model = ConsoleHelper.readString();
        ConsoleHelper.writeMessage(String.format("Изменить производителя '%s' на:", laptop.getMaker()));
        String maker = ConsoleHelper.readString();
        ConsoleHelper.writeMessage(String.format("Изменить значение цены '%d' на:", laptop.getPrice()));
        int price = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage(String.format("Изменить значение скорости процессора '%d' на:", laptop.getSpeed()));
        int speed = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage(String.format("Изменить объем памяти жесткого диска '%d' на:", laptop.getHd()));
        int hd = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage(String.format("Изменить объем оперативной памяти '%d' на:", laptop.getRam()));
        int ram = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage(String.format("Изменить размер экрана монитора '%d' на:", laptop.getScreen()));
        int screen = ConsoleHelper.readInt();
        return new Laptop(0, model, maker, price, speed, hd, ram, screen);
    }

    private Product getPCForUpdate(PC pc) {
        ConsoleHelper.writeMessage(String.format("Изменить модель '%s' на:", pc.getModel()));
        String model = ConsoleHelper.readString();
        ConsoleHelper.writeMessage(String.format("Изменить производителя '%s' на:", pc.getMaker()));
        String maker = ConsoleHelper.readString();
        ConsoleHelper.writeMessage(String.format("Изменить значение цены '%d' на:", pc.getPrice()));
        int price = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage(String.format("Изменить значение скорости процессора '%d' на:", pc.getSpeed()));
        int speed = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage(String.format("Изменить объем памяти жесткого диска '%d' на:", pc.getHd()));
        int hd = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage(String.format("Изменить объем оперативной памяти '%d' на:", pc.getRam()));
        int ram = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage(String.format("Изменить скорость привода CD-дисков '%s' на:", pc.getCd()));
        String cd = ConsoleHelper.readString();
        return new PC(0, model, maker, price, speed, hd, ram, cd);
    }

    private String getModelForUpdate() {
        ConsoleHelper.writeMessage("Укажите модель товара, данные которой необходимо изменить:");
        return ConsoleHelper.readString();
    }

    @Override
    public Result getResult() {
        return result;
    }
}
