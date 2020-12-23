import product.*;

import java.io.IOException;

public class CreateCommand implements Command {

    private Result result;

    @Override
    public void execute() {
        try {
            while (true) {
                ConsoleHelper.writeMessage("Укажите, какой тип товара необходимо добавить:");
                ConsoleHelper.writeMessage(String.format("%d ПК", ProductType.PC.ordinal()));
                ConsoleHelper.writeMessage(String.format("%d Ноутбук", ProductType.LAPTOP.ordinal()));
                ConsoleHelper.writeMessage(String.format("%d Принтер", ProductType.PRINTER.ordinal()));
                ConsoleHelper.writeMessage("0 Выйти в главное меню");
                int prod_type = ConsoleHelper.readInt();
                if ((prod_type >= 1) && (prod_type <= 3)) {
                    addProduct(prod_type);
                } else
                    break;
            }
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    private void addProduct(int prod_type) throws IOException, ClassNotFoundException {
        Product product = null;
        ProductType[] types = ProductType.values();
        switch (types[prod_type]) {
            case PC: product = createPC();
                    break;
            case LAPTOP: product = createLaptop();
                    break;
            case PRINTER: product = createPrinter();
        }
        Order order = new Order(types[prod_type], product);
        Connector connector = Client.getConnector();
        connector.clientSend(order);
        result = connector.clientReceive();
        ConsoleHelper.writeMessage(result.getUpdateStatus());
    }

    private Product createPrinter() {
        ConsoleHelper.writeMessage("Модель:");
        String model = ConsoleHelper.readString();
        ConsoleHelper.writeMessage("Производитель:");
        String maker = ConsoleHelper.readString();
        ConsoleHelper.writeMessage("Цена:");
        int price = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage("Тип печати:");
        String type = ConsoleHelper.readString();
        ConsoleHelper.writeMessage("Цветной (y/n):");
        String color = ConsoleHelper.readString();
        return new Printer(0, model, maker, price, type, color);
    }

    private Product createLaptop() {
        ConsoleHelper.writeMessage("Модель:");
        String model = ConsoleHelper.readString();
        ConsoleHelper.writeMessage("Производитель:");
        String maker = ConsoleHelper.readString();
        ConsoleHelper.writeMessage("Цена:");
        int price = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage("Скорость процессора:");
        int speed = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage("Объем памяти жесткого диска:");
        int hd = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage("Объем оперативной памяти:");
        int ram = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage("Размер экрана монитора:");
        int screen = ConsoleHelper.readInt();
        return new Laptop(0, model, maker, price, speed, hd, ram, screen);
    }

    private Product createPC() {
        ConsoleHelper.writeMessage("Модель:");
        String model = ConsoleHelper.readString();
        ConsoleHelper.writeMessage("Производитель:");
        String maker = ConsoleHelper.readString();
        ConsoleHelper.writeMessage("Цена:");
        int price = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage("Скорость процессора:");
        int speed = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage("Объем памяти жесткого диска:");
        int hd = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage("Объем оперативной памяти:");
        int ram = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage("Скорость привода CD-дисков:");
        String cd = ConsoleHelper.readString();
        return new PC(0, model, maker, price,speed, hd, ram, cd);
    }

    @Override
    public Result getResult() {
        return result;
    }
}
