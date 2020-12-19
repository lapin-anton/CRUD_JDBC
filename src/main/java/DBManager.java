import product.*;

import java.sql.*;
import java.util.ArrayList;

public class DBManager {

    private Connection connection;

    public DBManager(Connection connection) {
        this.connection = connection;
    }

    // для запросов на чтение
    private Result extractAllAboutPC() throws SQLException {
        ArrayList<Product> pcList = new ArrayList<>();
        String sql = "SELECT * FROM pc";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("id");
            String maker = rs.getString("maker");
            String model = rs.getString("model");
            int speed = rs.getInt("speed");
            int hd = rs.getInt("hd");
            int ram = rs.getInt("ram");
            String cd = rs.getString("cd");
            int price = rs.getInt("price");
            PC pc = new PC(id, model, maker, price, speed, hd, ram, cd);
            pcList.add(pc);
        }
        return new Result(pcList);
    }

    private Result extractAllAboutLaptop() throws SQLException {
        ArrayList<Product> laptopList = new ArrayList<>();
        String sql = "SELECT * FROM laptop";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("id");
            String maker = rs.getString("maker");
            String model = rs.getString("model");
            int speed = rs.getInt("speed");
            int hd = rs.getInt("hd");
            int ram = rs.getInt("ram");
            int screen = rs.getInt("screen");
            int price = rs.getInt("price");
            Laptop lt = new Laptop(id, model, maker, price, speed, hd, ram, screen);
            laptopList.add(lt);
        }
        return new Result(laptopList);
    }

    private Result extractAllAboutPrinter() throws SQLException {
        ArrayList<Product> printerList = new ArrayList<>();
        String sql = "SELECT * FROM printer";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int id = rs.getInt("id");
            String maker = rs.getString("maker");
            String model = rs.getString("model");
            String color = rs.getString("color");
            String type = rs.getString("type");
            int price = rs.getInt("price");
            Printer pr = new Printer(id, model, maker, price, type, color);
            printerList.add(pr);
        }
        return new Result(printerList);
    }

    public Result extractAllProductsByType(ProductType productType) {
        Result result = null;
        try {
            switch (productType) {
                case PC:
                    result = extractAllAboutPC();
                    break;
                case LAPTOP:
                    result = extractAllAboutLaptop();
                    break;
                case PRINTER:
                    result = extractAllAboutPrinter();
            }
        } catch (SQLException e) {
            ExceptionHandler.log(e);
        }
        return result;
    }

    // для запросов на добавление
    public Result createProduct(Order order) {
        String message = null;
        try {
            switch (order.getProductType()) {
                case PC: message = createPC(order.getProduct());
                    break;
                case LAPTOP: message = createLaptop(order.getProduct());
                    break;
                case PRINTER: message = createPrinter(order.getProduct());
            }
        } catch (SQLException e) {
            ExceptionHandler.log(e);
        }
        return new Result(message);
    }

    private String createPrinter(Product product) throws SQLException {
        String result = null;
        Printer printer = (Printer) product;
        String sql = "INSERT INTO printer (model, maker, price, type, color) " +
                "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, printer.getModel());
        statement.setString(2, printer.getMaker());
        statement.setInt(3, printer.getPrice());
        statement.setString(4, printer.getType());
        statement.setString(5, printer.getColor());

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            result = String.format("Модель притера '%s' успешно добавлена в базу!", printer.getModel());
        } else  {
            result = String.format("Модель притера '%s' не удалось добавить в базу!", printer.getModel());
        }
        return result;
    }

    private String createLaptop(Product product) throws SQLException {
        String result = null;
        Laptop laptop = (Laptop) product;
        String sql = "INSERT INTO laptop (model, maker, price, speed, hd, ram, screen) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, laptop.getModel());
        statement.setString(2, laptop.getMaker());
        statement.setInt(3, laptop.getPrice());
        statement.setInt(4, laptop.getSpeed());
        statement.setInt(5, laptop.getHd());
        statement.setInt(6, laptop.getRam());
        statement.setInt(7, laptop.getScreen());

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            result = String.format("Модель ноутбука '%s' успешно добавлена в базу!", laptop.getModel());
        } else {
            result = String.format("Модель ноутбука '%s' не удалось добавить в базу!", laptop.getModel());
        }
        return result;
    }

    private String createPC(Product product) throws SQLException {
        String result = null;
        PC pc = (PC) product;
        String sql = "INSERT INTO pc (model, maker, price, speed, hd, ram, cd) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, pc.getModel());
        statement.setString(2, pc.getMaker());
        statement.setInt(3, pc.getPrice());
        statement.setInt(4, pc.getSpeed());
        statement.setInt(5, pc.getHd());
        statement.setInt(6, pc.getRam());
        statement.setString(7, pc.getCd());

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            result = String.format("Модель ПК '%s' успешно добавлена в базу!", pc.getModel());
        } else {
            result = String.format("Модель ПК '%s' не удалось добавить в базу!", pc.getModel());
        }
        return result;
    }

    // для запросов на обноление
    // для запросов на удаление
}
