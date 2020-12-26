import product.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class DBManager {

    private Connection connection;

    public DBManager(Connection connection) {
        this.connection = connection;
    }

    // для запросов на чтение
    public Result extractProductByModel(ProductType productType, String model) {
        Result result = null;
        Product product = null;
        String type = null;
        try {
            switch (productType) {
                case PC: product = extractPCByModel(model);
                    break;
                case LAPTOP: product = extractLaptopByModel(model);
                    break;
                case PRINTER: product = extractPrinterByModel(model);
            }
        } catch (SQLException e) {
            ExceptionHandler.log(e);
        }
        if(product != null) {
            result = new Result(product, "OK");
        } else {
            result = new Result("К сожалению, такая модель товара в базе не нашлась:(");
        }
        return result;
    }

    private Product extractPrinterByModel(String mdl) throws SQLException {
        Printer printer = null;
        String sql = String.format("SELECT * FROM printer WHERE model=%s", mdl);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            int id = rs.getInt("id");
            String maker = rs.getString("maker");
            String model = rs.getString("model");
            String color = rs.getString("color");
            String type = rs.getString("type");
            int price = rs.getInt("price");
            printer = new Printer(id, model, maker, price, type, color);
        }
        return printer;
    }

    private Product extractLaptopByModel(String mdl) throws SQLException {
        Laptop laptop = null;
        String sql = String.format("SELECT * FROM laptop WHERE model=%s", mdl);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            int id = rs.getInt("id");
            String maker = rs.getString("maker");
            String model = rs.getString("model");
            int speed = rs.getInt("speed");
            int hd = rs.getInt("hd");
            int ram = rs.getInt("ram");
            int screen = rs.getInt("screen");
            int price = rs.getInt("price");
            laptop = new Laptop(id, model, maker, price, speed, hd, ram, screen);
        }
        return laptop;
    }

    private Product extractPCByModel(String mdl) throws SQLException {
        PC pc = null;
        String sql = String.format("SELECT * FROM pc WHERE model=%s", mdl);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            int id = rs.getInt("id");
            String maker = rs.getString("maker");
            String model = rs.getString("model");
            int speed = rs.getInt("speed");
            int hd = rs.getInt("hd");
            int ram = rs.getInt("ram");
            String cd = rs.getString("cd");
            int price = rs.getInt("price");
            pc = new PC(id, model, maker, price, speed, hd, ram, cd);
        }
        return pc;
    }

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
    public Result updateProduct(Order order) {
        String message = null;
        try {
            switch (order.getProductType()) {
                case PC: message = updatePC(order.getProduct(), order.getModel());
                    break;
                case LAPTOP: message = updateLaptop(order.getProduct(), order.getModel());
                    break;
                case PRINTER: message = updatePrinter(order.getProduct(), order.getModel());
            }
        } catch (SQLException e) {
            ExceptionHandler.log(e);
        }
        return new Result(message);
    }

    public Result updateProducts(Order order) {
        StringBuilder message = new StringBuilder("");
        try {
            for (Map.Entry<String, Product> pair: order.getProducts().entrySet()) {
                switch (order.getProductType()) {
                    case PC: message.append(updatePC(pair.getValue(), pair.getKey()));
                        break;
                    case LAPTOP: message.append(updateLaptop(pair.getValue(), pair.getKey()));
                        break;
                    case PRINTER: message.append(updatePrinter(pair.getValue(), pair.getKey()));
                }
                message.append("\n");
            }
        } catch (SQLException e) {
            ExceptionHandler.log(e);
        }
        return new Result(message.toString());
    }

    private String updatePrinter(Product product, String model) throws SQLException {
        String result = null;
        Printer printer = (Printer) product;
        String sql = "UPDATE printer SET model=?, maker=?, price=?, type=?, color=? WHERE model=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, printer.getModel());
        statement.setString(2, printer.getMaker());
        statement.setInt(3, printer.getPrice());
        statement.setString(4, printer.getType());
        statement.setString(5, printer.getColor());
        statement.setString(6, model);
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            result = String.format("Данные модели принтера '%s' успешно обновлены!", printer.getModel());
        } else {
            result = String.format("Данные модели принтера '%s' не удалось обновить!", printer.getModel());
        }
        return result;
    }

    private String updateLaptop(Product product, String model) throws SQLException {
        String result = null;
        Laptop laptop = (Laptop) product;
        String sql = "UPDATE laptop SET model=?, maker=?, price=?, speed=?, hd=?, ram=?, screen=? WHERE model=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, laptop.getModel());
        statement.setString(2, laptop.getMaker());
        statement.setInt(3, laptop.getPrice());
        statement.setInt(4, laptop.getSpeed());
        statement.setInt(5, laptop.getHd());
        statement.setInt(6, laptop.getRam());
        statement.setInt(7, laptop.getScreen());
        statement.setString(8, model);
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            result = String.format("Данные модели ноутбука '%s' успешно обновлены!", laptop.getModel());
        } else {
            result = String.format("Данные модели ноутбука '%s' не удалось обновить!", laptop.getModel());
        }
        return result;
    }

    private String updatePC(Product product, String currModel) throws SQLException {
        String result = null;
        PC pc = (PC) product;
        String sql = "UPDATE pc SET model=?, maker=?, price=?, speed=?, hd=?, ram=?, cd=? WHERE model=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, pc.getModel());
        statement.setString(2, pc.getMaker());
        statement.setInt(3, pc.getPrice());
        statement.setInt(4, pc.getSpeed());
        statement.setInt(5, pc.getHd());
        statement.setInt(6, pc.getRam());
        statement.setString(7, pc.getCd());
        statement.setString(8, currModel);
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            result = String.format("Данные модели ПК '%s' успешно обновлены!", pc.getModel());
        } else {
            result = String.format("Данные модели ПК '%s' не удалось обновить!", pc.getModel());
        }
        return result;
    }

    // для запросов на удаление
    public Result deleteProduct(Order order) {
        String message = null;
        String sType = null;
        switch (order.getProductType()) {
            case PC: sType = "pc";
                break;
            case LAPTOP: sType = "laptop";
                break;
            case PRINTER: sType = "printer";
        }
        try {
            String sql = String.format("DELETE FROM %s WHERE model=?", sType);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, order.getModel());
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                message = String.format("Товар модель '%s' успешно удален из базы!", order.getModel());
            } else {
                message = String.format("Товар модель '%s' не был удален из базы. Возможно, его нет в базе.", order.getModel());
            }
        } catch (SQLException e) {
            ExceptionHandler.log(e);
        }
        return new Result(message);
    }

    public Result deleteProducts(Order order) {
        StringBuilder message = new StringBuilder("");
        String sType = null;
        switch (order.getProductType()) {
            case PC: sType = "pc";
                break;
            case LAPTOP: sType = "laptop";
                break;
            case PRINTER: sType = "printer";
        }
        try {
            String sql = String.format("DELETE FROM %s WHERE model=?", sType);
            PreparedStatement statement = connection.prepareStatement(sql);
            for (String model: order.getModels()) {
                statement.setString(1, model);
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    message.append(String.format("Товар модель '%s' успешно удален из базы!\n", model));
                } else {
                    message.append(String.format("Товар модель '%s' не был удален из базы. Возможно, его нет в базе.\n", model));
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.log(e);
        }
        return new Result(message.toString());
    }

    // проверка пользователя
    public Result checkUser(Order order) {
        Result result = null;
        String sql = String.format("SELECT * FROM users WHERE name='%s' AND password='%s'", order.getUser().getLogin(),
                order.getUser().getPassword());
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            UserMode mode = UserMode.NONE;
            if (rs.next()) {
                String sMode = rs.getString("mode");
                switch (sMode) {
                    case "admin":
                        mode = UserMode.ADMIN;
                        break;
                    case "user":
                        mode = UserMode.USER;
                }
                result = new Result(true, new User(order.getUser().getLogin(), order.getUser().getPassword(), mode));
            } else {
                result = new Result(false, null);
            }
        } catch (SQLException e) {
            ExceptionHandler.log(e);
        }
        return result;
    }
}
