import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateCommand implements Command {
    @Override
    public void execute() {
        try (Connection connection = DriverManager.getConnection(s.getDbURL(), s.getUsername(), s.getPassword())) {
            if (connection == null) return;
            ConsoleHelper.writeMessage("Соединение с базой данных установлено");
            boolean isExit = false;
            do {
                ConsoleHelper.writeMessage("Укажите, какой тип товара необходимо добавить:");
                ConsoleHelper.writeMessage("1 ПК");
                ConsoleHelper.writeMessage("2 Ноутбук");
                ConsoleHelper.writeMessage("3 Принтер");
                ConsoleHelper.writeMessage("0 Выйти в главное меню");
                int prod_type = ConsoleHelper.readInt();
                switch (prod_type) {
                    case 1:
                        addPC(connection);
                        break;
                    case 2:
                        addLaptop(connection);
                        break;
                    case 3:
                        addPrinter(connection);
                    default:
                        isExit = true;
                }
            } while (!isExit);
        } catch (SQLException ex) {
            ExceptionHandler.log(ex);
        }
    }

    private void addPrinter(Connection connection) throws SQLException {
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
        String sql = "INSERT INTO printer (model, maker, price, type, color) " +
                "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, model);
        statement.setString(2, maker);
        statement.setInt(3, price);
        statement.setString(4, type);
        statement.setString(5, color);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            ConsoleHelper.writeMessage(String.format("Модель притера '%s' успешно добавлена в базу!", model));
        }
    }

    private void addLaptop(Connection connection) throws SQLException {
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
        String sql = "INSERT INTO laptop (model, maker, price, speed, hd, ram, screen) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, model);
        statement.setString(2, maker);
        statement.setInt(3, price);
        statement.setInt(4, speed);
        statement.setInt(5, hd);
        statement.setInt(6, ram);
        statement.setInt(7, screen);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            ConsoleHelper.writeMessage(String.format("Модель ноутбука '%s' успешно добавлена в базу!", model));
        }
    }

    private void addPC(Connection connection) throws SQLException {
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
        String sql = "INSERT INTO pc (model, maker, price, speed, hd, ram, cd) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, model);
        statement.setString(2, maker);
        statement.setInt(3, price);
        statement.setInt(4, speed);
        statement.setInt(5, hd);
        statement.setInt(6, ram);
        statement.setString(7, cd);

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            ConsoleHelper.writeMessage(String.format("Модель ПК '%s' успешно добавлена в базу!", model));
        }
    }
}
