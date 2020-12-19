import java.sql.*;

public class UpdateCommand implements Command {
    @Override
    public void execute(Connector connector) {
        try (Connection connection = DriverManager.getConnection(s.getDbURL(), s.getUsername(), s.getPassword())) {
            if (connection == null) return;
            ConsoleHelper.writeMessage("Соединение с базой данных установлено");
            boolean isExit = false;
            do {
                ConsoleHelper.writeMessage("Укажите, данные товара какого типа необходимо изменить:");
                ConsoleHelper.writeMessage("1 ПК");
                ConsoleHelper.writeMessage("2 Ноутбук");
                ConsoleHelper.writeMessage("3 Принтер");
                ConsoleHelper.writeMessage("0 Выйти в главное меню");
                int prod_type = ConsoleHelper.readInt();
                switch (prod_type) {
                    case 1:
                        updatePC(connection);
                        break;
                    case 2:
                        updateLaptop(connection);
                        break;
                    case 3:
                        updatePrinter(connection);
                    default:
                        isExit = true;
                }
            } while (!isExit);
        } catch (SQLException ex) {
            ExceptionHandler.log(ex);
        }
    }

    private void updatePrinter(Connection connection) throws SQLException {
        ConsoleHelper.writeMessage("Укажите номер модели принтера, данные которой необходимо изменить:");
        String currModel = ConsoleHelper.readString();
        String sql = String.format("SELECT * FROM printer WHERE model='%s'", currModel);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(!rs.next()) {
            ConsoleHelper.writeMessage("К сожалению, такая модель принтера не нашлась:(");
            return;
        }
        ConsoleHelper.writeMessage(String.format("Изменить модель '%s' на:", rs.getString("model")));
        String model = ConsoleHelper.readString();
        ConsoleHelper.writeMessage(String.format("Изменить производителя '%s' на:", rs.getString("maker")));
        String maker = ConsoleHelper.readString();
        ConsoleHelper.writeMessage(String.format("Изменить значение цены '%d' на:", rs.getInt("price")));
        int price = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage(String.format("Изменить тип печати '%s' на:", rs.getString("type")));
        String type = ConsoleHelper.readString();
        ConsoleHelper.writeMessage(String.format("Изменить статус цветной печати '%s' на:", rs.getString("color")));
        String color = ConsoleHelper.readString();
        sql = "UPDATE printer SET model=?, maker=?, price=?, type=?, color=? WHERE model=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, model);
        statement.setString(2, maker);
        statement.setInt(3, price);
        statement.setString(4, type);
        statement.setString(5, color);
        statement.setString(6, currModel);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            ConsoleHelper.writeMessage(String.format("Данные модели принтера '%s' успешно обновлены!", model));
        }
    }

    private void updateLaptop(Connection connection) throws SQLException {
        ConsoleHelper.writeMessage("Укажите номер модели ноутбука, данные которой необходимо изменить:");
        String currModel = ConsoleHelper.readString();
        String sql = String.format("SELECT * FROM laptop WHERE model='%s'", currModel);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(!rs.next()) {
            ConsoleHelper.writeMessage("К сожалению, такая модель ноутбука не нашлась:(");
            return;
        }
        ConsoleHelper.writeMessage(String.format("Изменить модель '%s' на:", rs.getString("model")));
        String model = ConsoleHelper.readString();
        ConsoleHelper.writeMessage(String.format("Изменить производителя '%s' на:", rs.getString("maker")));
        String maker = ConsoleHelper.readString();
        ConsoleHelper.writeMessage(String.format("Изменить значение цены '%d' на:", rs.getInt("price")));
        int price = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage(String.format("Изменить значение скорости процессора '%d' на:", rs.getInt("speed")));
        int speed = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage(String.format("Изменить объем памяти жесткого диска '%d' на:", rs.getInt("hd")));
        int hd = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage(String.format("Изменить объем оперативной памяти '%d' на:", rs.getInt("ram")));
        int ram = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage(String.format("Изменить размер экрана монитора '%d' на:", rs.getInt("screen")));
        int screen = ConsoleHelper.readInt();
        sql = "UPDATE laptop SET model=?, maker=?, price=?, speed=?, hd=?, ram=?, screen=? WHERE model=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, model);
        statement.setString(2, maker);
        statement.setInt(3, price);
        statement.setInt(4, speed);
        statement.setInt(5, hd);
        statement.setInt(6, ram);
        statement.setInt(7, screen);
        statement.setString(8, currModel);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            ConsoleHelper.writeMessage(String.format("Данные модели ноутбука '%s' успешно обновлены!", model));
        }
    }

    private void updatePC(Connection connection) throws SQLException {
        ConsoleHelper.writeMessage("Укажите номер модели ПК, данные которой необходимо изменить:");
        String currModel = ConsoleHelper.readString();
        String sql = String.format("SELECT * FROM pc WHERE model='%s'", currModel);
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(!rs.next()) {
            ConsoleHelper.writeMessage("К сожалению, такая модель ПК не нашлась:(");
            return;
        }
        ConsoleHelper.writeMessage(String.format("Изменить модель '%s' на:", rs.getString("model")));
        String model = ConsoleHelper.readString();
        ConsoleHelper.writeMessage(String.format("Изменить производителя '%s' на:", rs.getString("maker")));
        String maker = ConsoleHelper.readString();
        ConsoleHelper.writeMessage(String.format("Изменить значение цены '%d' на:", rs.getInt("price")));
        int price = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage(String.format("Изменить значение скорости процессора '%d' на:", rs.getInt("speed")));
        int speed = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage(String.format("Изменить объем памяти жесткого диска '%d' на:", rs.getInt("hd")));
        int hd = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage(String.format("Изменить объем оперативной памяти '%d' на:", rs.getInt("ram")));
        int ram = ConsoleHelper.readInt();
        ConsoleHelper.writeMessage(String.format("Изменить скорость привода CD-дисков '%s' на:", rs.getString("cd")));
        String cd = ConsoleHelper.readString();
        sql = "UPDATE pc SET model=?, maker=?, price=?, speed=?, hd=?, ram=?, cd=? WHERE model=?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, model);
        statement.setString(2, maker);
        statement.setInt(3, price);
        statement.setInt(4, speed);
        statement.setInt(5, hd);
        statement.setInt(6, ram);
        statement.setString(7, cd);
        statement.setString(8, currModel);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            ConsoleHelper.writeMessage(String.format("Данные модели ПК '%s' успешно обновлены!", model));
        }
    }
}
