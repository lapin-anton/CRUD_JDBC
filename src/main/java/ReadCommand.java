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

                ConsoleHelper.writeMessage("Информацию по какому товару вы хотите найти? (выберите номер типа товара)");
                ConsoleHelper.writeMessage("1. ПК");
                ConsoleHelper.writeMessage("2. Ноутбуки");
                ConsoleHelper.writeMessage("3. Принтеры");
                int prod_type = ConsoleHelper.readInt();
                switch (prod_type) {
                    case 1: extractAllAboutPC(connection);
                        break;
                    case 2: extractAllAboutLaptop(connection);
                        break;
                    case 3: extractAllAboutPrinter(connection);
                }
                for (Product p: products) {
                    ConsoleHelper.writeMessage(p.toString());
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            ExceptionHandler.log(e);
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
