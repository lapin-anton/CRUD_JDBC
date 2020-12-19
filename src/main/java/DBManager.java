import product.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    // для запросов на обноление
    // для запросов на удаление
}
