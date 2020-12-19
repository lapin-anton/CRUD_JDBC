import product.Product;
import product.ProductType;

import java.io.IOException;
import java.util.ArrayList;

public class ReadCommand implements Command {

    private ArrayList<Product> products = new ArrayList<>();

    @Override
    public void execute() {
        try {
            while (true) {
                ConsoleHelper.writeMessage("Информацию по какому товару вы хотите найти? (выберите тип товара)");
                ConsoleHelper.writeMessage(String.format("%s ПК", ProductType.PC.ordinal()));
                ConsoleHelper.writeMessage(String.format("%s Ноутбуки", ProductType.LAPTOP.ordinal()));
                ConsoleHelper.writeMessage(String.format("%s Принтеры", ProductType.PRINTER.ordinal()));
                ConsoleHelper.writeMessage("0. Выход в главное меню");
                int prod_type = ConsoleHelper.readInt();
                if((prod_type == 0) || (prod_type > 3)) break;
                extractAllProductsByType(prod_type);
                ConsoleHelper.writeMessage("Все товары данной категории:");
                for (Product p : products) {
                    ConsoleHelper.writeMessage(p.toString());
                }
                ConsoleHelper.writeMessage("============================");
                ConsoleHelper.writeMessage("Выберите критерий поиска:");
                ConsoleHelper.writeMessage("1. Поиск по модели");
                ConsoleHelper.writeMessage("2. Поиск по производителю");
                ConsoleHelper.writeMessage("3. Поиск по цене");
                int search_criteria = ConsoleHelper.readInt();
                ConsoleHelper.writeMessage("Товары по выбранным критериям поиска:");
                switch (search_criteria) {
                    case 1:
                        getProductByModel();
                        break;
                    case 2:
                        getProductByMaker();
                        break;
                    case 3:
                        getProductByPrice();
                }
                ConsoleHelper.writeMessage("==========================");
                products.clear();
            }
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }

    private void extractAllProductsByType(int prod_type) throws IOException, ClassNotFoundException {
        ProductType[] types = ProductType.values();
        ProductType productType = types[prod_type];
        Order order = new Order(productType);
        Connector connector = Client.getConnector();
        connector.clientSend(order);
        Result result = connector.clientReceive();
        products = result.getProductList();
    }

    private void getProductByModel() {
        ConsoleHelper.writeMessage("Введите модель выбранной категории товара");
        String model = ConsoleHelper.readString();
        for (Product p: products) {
            if(p.getModel().equals(model)) {
                ConsoleHelper.writeMessage(p.toString());
            }
        }
    }

    private void getProductByMaker() {
        ConsoleHelper.writeMessage("Введите производителя выбранной категории товара");
        String maker = ConsoleHelper.readString();
        for (Product p: products) {
            if(p.getMaker().equals(maker)) {
                ConsoleHelper.writeMessage(p.toString());
            }
        }
    }

    private void getProductByPrice() {
        ConsoleHelper.writeMessage("Укажите ценовой диапазон (первое значение - \"от включительно\", " +
                "второе - \"до включительно\")");
        int from = ConsoleHelper.readInt();
        int to = ConsoleHelper.readInt();
        for (Product p: products) {
            if((p.getPrice() >= from) && (p.getPrice() <= to)) {
                ConsoleHelper.writeMessage(p.toString());
            }
        }
    }

}
