import product.Laptop;
import product.PC;
import product.Printer;
import product.Product;

import java.sql.*;
import java.util.ArrayList;

public class ReadCommand implements Command {

    private ArrayList<Product> products = new ArrayList<>();

    @Override
    public void execute() {
        try {
            Settings s = new Settings();
            try (Connection connection = DriverManager.getConnection(s.getDbURL(), s.getUsername(), s.getPassword())) {
                if (connection == null) return;
                ConsoleHelper.writeMessage("Соединение с базой данных установлено");
                boolean flag = false;
                while (true) {
                    ConsoleHelper.writeMessage("Информацию по какому товару вы хотите найти? (выберите тип товара)");
                    ConsoleHelper.writeMessage("1. ПК");
                    ConsoleHelper.writeMessage("2. Ноутбуки");
                    ConsoleHelper.writeMessage("3. Принтеры");
                    ConsoleHelper.writeMessage("0. Выход в главное меню");
                    int prod_type = ConsoleHelper.readInt();
                    switch (prod_type) {
                        case 1:
                            extractAllAboutPC(connection);
                            break;
                        case 2:
                            extractAllAboutLaptop(connection);
                            break;
                        case 3:
                            extractAllAboutPrinter(connection);
                            break;
                        default:
                            flag = true; // exit from the loop
                    }
                    if (flag) break;
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
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

    private void extractAllAboutPC(Connection connection) throws SQLException {
        String sql = String.format("SELECT * FROM common_pc");
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String maker = rs.getString("maker");
            String model = rs.getString("model");
            int speed = rs.getInt("speed");
            int hd = rs.getInt("hd");
            int ram = rs.getInt("ram");
            String cd = rs.getString("cd");
            int price = rs.getInt("price");
            PC pc = new PC(model, maker, price, speed, hd, ram, cd);
            products.add(pc);
        }
    }

    private void extractAllAboutLaptop(Connection connection) throws SQLException {
        String sql = String.format("SELECT * FROM common_laptop");
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String maker = rs.getString("maker");
            String model = rs.getString("model");
            int speed = rs.getInt("speed");
            int hd = rs.getInt("hd");
            int ram = rs.getInt("ram");
            int price = rs.getInt("price");
            Laptop lt = new Laptop(model, maker, price, speed, hd, ram);
            products.add(lt);
        }
    }

    private void extractAllAboutPrinter(Connection connection) throws SQLException {
        String sql = String.format("SELECT * FROM common_printer");
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String maker = rs.getString("maker");
            String model = rs.getString("model");
            String color = rs.getString("color");
            String type = rs.getString("type");
            int price = rs.getInt("price");
            Printer pr = new Printer(model, maker, price, type, color);
            products.add(pr);
        }
    }
}
