import product.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Map;

public class DBManager {

    private Connection connection;
    private Encryptor encryptor;

    public DBManager(Connection connection) {
        this.connection = connection;
        this.encryptor = new Encryptor(connection);
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
        String sql = String.format("SELECT * FROM users WHERE name='%s'", order.getUser().getLogin());
        UserMode mode = UserMode.NONE;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String sMode = rs.getString("mode");
                switch (sMode) {
                    case "admin":
                        mode = UserMode.ADMIN;
                        break;
                    case "user":
                        mode = UserMode.USER;
                }
                int pass_id = rs.getInt("password_id");
                String original = getOriginalPassword(pass_id);
                if (original.equals(order.getUser().getPassword())) {
                    result = new Result(true, new User(order.getUser().getLogin(), original, mode));
                } else {
                    result = new Result(false, null);
                }
            } else {
                result = new Result(false, null);
            }
        } catch (SQLException e) {
            ExceptionHandler.log(e);
        }
        return result;
    }

    private String getOriginalPassword(int pass_id) throws SQLException {
        String sql = String.format("SELECT * FROM passwords WHERE id=%d", pass_id);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        byte[] encoded = new byte[16];
        int i = 0;
        while (rs.next()) {
            encoded[i] = (byte) rs.getInt("val");
            i++;
        }
        return encryptor.decode(encoded);
    }

    public Result addUser(Order order) {
        Result result = null;
        String sql = "INSERT INTO users (name, mode, password_id) " +
                "VALUES (?, ?, ?)";
        String sMode = "";
        switch (order.getUser().getMode()) {
            case USER: sMode = "user";
                break;
            case ADMIN: sMode = "admin";
        }
        int rowsInserted = 0;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, order.getUser().getLogin());
            statement.setString(2, sMode);
            statement.setInt(3, getMaxPasswordId() + 1);
            rowsInserted = statement.executeUpdate();
            if ((rowsInserted > 0) && (addPassword(order.getUser().getPassword(), -1))) {
                result = new Result(String.format("Пользователь с логином '%s' успешно добавлен в базу!",
                        order.getUser().getLogin()));
            } else {
                result = new Result(String.format("Пользователя с логином '%s' не удалось добавить в базу!",
                        order.getUser().getLogin()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private boolean addPassword(String password, int pass_id) throws SQLException {
        String sql = "INSERT INTO passwords (id, val) VALUES (?,?)";
        byte[] encoded = encryptor.code(password);
        int count = 0;
        for (int i = 0; i < encoded.length; i++) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, (pass_id == -1) ? getMaxPasswordId() : pass_id);
            statement.setInt(2, encoded[i]);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                count++;
            }
        }
        if(count == 16) {
            System.out.println("Пароль успешно сохранен");
        }
        return (count == 16);
    }


    private int getMaxPasswordId() throws SQLException {
        int result = 0;
        String sql = "SELECT MAX(password_id) AS max_id FROM users";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            result = rs.getInt("max_id");
        }
        return result;
    }

    public Result extractAllUsers() {
        Object[][] data = Constants.EMPTY_USER_TABLE;
        String sql = "SELECT COUNT(*) AS count FROM users";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int rowCount = rs.getInt("count");
            sql = "SELECT * FROM users";
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            if(rowCount > 0) {
                data = new Object[rowCount][Constants.USER_COLUMNS.length];
                int i = 0;
                while (rs.next()) {
                    data[i][0] = rs.getString("name");
                    data[i][1] = rs.getString("mode");
                    int pass_id = rs.getInt("password_id");
                    data[i][2] = getOriginalPassword(pass_id);
                    i++;
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.log(e);
        }
        return new Result(data);
    }

    public Result updateUsers(Order order) {
        StringBuilder message = new StringBuilder("");
        String sql = "UPDATE users SET name=?, mode=? WHERE name=?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            for (Map.Entry<String, User> pair: order.getUsers().entrySet()) {
                statement.setString(1, pair.getValue().getLogin());
                statement.setString(2, pair.getValue().getMode().name().toLowerCase());
                updatePassword(pair.getValue().getPassword(), getOldPasswordId(pair.getValue().getLogin()));
                statement.setString(3, pair.getKey());
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    message.append(String.format("Параметры пользователя '%s' успешно обновлены!", pair.getKey()));
                } else {
                    message.append(String.format("Параметры пользователя '%s' не удалось обновить!", pair.getKey()));
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.log(e);
        }
        return new Result(message.toString());
    }

    private void updatePassword(String password, int old_pass_id) throws SQLException {
        deletePassword(old_pass_id);
        addPassword(password, old_pass_id);
    }

    private void deletePassword(int password_id) throws SQLException {
        String sql = String.format("DELETE FROM passwords WHERE id=%d",password_id);
        Statement stmt = connection.createStatement();
        int deletedRows = stmt.executeUpdate(sql);
        if(deletedRows > 0) {
            System.out.println("Пароль успешно удален");
        } else {
            System.out.println("Не удалось удалить старый пароль");
        }
    }

    private int getOldPasswordId(String name) throws SQLException {
        int id = 0;
        String sql = String.format("SELECT password_id FROM users WHERE name='%s'", name);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(rs.next()) {
            id = rs.getInt("password_id");
        }
        return id;
    }

    public Result deleteUsers(Order order) {
        StringBuilder message = new StringBuilder("");
        try {
            String sql = "DELETE FROM users WHERE name=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            String[] logins = order.getLogins();
            for (int i = 1; i < logins.length; i++) {
                deletePassword(getOldPasswordId(logins[i]));
                statement.setString(1, logins[i]);
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    message.append(String.format("Пользователь '%s' успешно удален из базы!\n", logins[i]));
                } else {
                    message.append(String.format("Пользователь '%s' не был удален из базы. Возможно, его нет в базе.\n",
                            logins[i]));
                }
            }
        } catch (SQLException e) {
            ExceptionHandler.log(e);
        }
        return new Result(message.toString());
    }
}
