import java.sql.*;

public class ReadCommand implements Command {
    @Override
    public void execute() {
        String dbURL = "jdbc:mysql://localhost:3306/pc_shop?useUnicode=true&serverTimezone=UTC";
        ConsoleHelper.writeMessage("Введите имя пользователя:");
        String username = ConsoleHelper.readString();
        ConsoleHelper.writeMessage("Введите пароль:");
        String password = ConsoleHelper.readString();

        try (Connection connection = DriverManager.getConnection(dbURL, username, password)) {
            if (connection == null) return;
            ConsoleHelper.writeMessage("Соединение с базой данных установлено");

            ConsoleHelper.writeMessage("Информацию по какому товару вы хотите найти? (выберите номер типа товара)");
            ConsoleHelper.writeMessage("1. ПК");
            ConsoleHelper.writeMessage("2. Ноутбуки");
            ConsoleHelper.writeMessage("3. Принтеры");
            int prod_type = ConsoleHelper.readInt();
            String prod_view = null;
            switch (prod_type) {
                case 1: extractAllAboutPC(connection);
                    break;
                case 2: extractAllAboutLaptop();
                    break;
                case 3: extractAllAboutPrinter();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void extractAllAboutPC(Connection connection) throws SQLException {
        String sql = String.format("SELECT * FROM common_pc");
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        int count = 0;
        ConsoleHelper.writeMessage("+-------+---------------+-------+-------+-------+-------+-------+-------+");
        ConsoleHelper.writeMessage("| id    | Maker         | Model | Speed | hd    | ram   | cd    | price |");
        while (rs.next()) {
            String maker = rs.getString("maker");
            String model = rs.getString("model");
            int speed = rs.getInt("speed");
            int hd = rs.getInt("hd");
            int ram = rs.getInt("ram");
            String cd = rs.getString("cd");
            int price = rs.getInt("price");
            ConsoleHelper.writeMessage("+-------+---------------+-------+-------+-------+-------+-------+-------+");
            String out = "| #%-4d | %-13s | %-5s | %-5d | %-5d | %-5d | %-5s | %-5d |";
            ConsoleHelper.writeMessage(String.format(out, ++count, maker, model, speed, hd, ram, cd, price));
        }
        ConsoleHelper.writeMessage("+-------+---------------+-------+-------+-------+-------+-------+-------+");
    }

    private void extractAllAboutLaptop() {

    }

    private void extractAllAboutPrinter() {

    }
}
