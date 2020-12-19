import java.sql.*;

public class DeleteCommand implements Command {
    @Override
    public void execute(Connector connector) {
        try (Connection connection = DriverManager.getConnection(s.getDbURL(), s.getUsername(), s.getPassword())) {
            if (connection == null) return;
            ConsoleHelper.writeMessage("Соединение с базой данных установлено");
            do {
                ConsoleHelper.writeMessage("Укажите, товар какого типа необходимо удалить:");
                ConsoleHelper.writeMessage("1 ПК");
                ConsoleHelper.writeMessage("2 Ноутбук");
                ConsoleHelper.writeMessage("3 Принтер");
                ConsoleHelper.writeMessage("0 Выйти в главное меню");
                int prod_type = ConsoleHelper.readInt();
                if(prod_type == 0) break;
                deleteProduct(connection, prod_type);
            } while (true);
        } catch (SQLException ex) {
            ExceptionHandler.log(ex);
        }
    }

    private void deleteProduct(Connection connection, int type) throws SQLException {
        String sType;
        switch (type) {
            case 1: sType = "pc";
                break;
            case 2: sType = "laptop";
                break;
            case 3: sType = "printer";
                break;
            default: return;
        }
        ConsoleHelper.writeMessage("Введите модель, которую нужно удалить:");
        String model = ConsoleHelper.readString();
        String sql = String.format("SELECT * FROM %s where model=?", sType);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, model);
        ResultSet rs = statement.executeQuery();
        if(!rs.next()) {
            ConsoleHelper.writeMessage("Нельзя удалить несуществующий товар. Проверьте правильность ввода названия модели.");
            deleteProduct(connection, type);
        }
        sql = String.format("DELETE FROM %s WHERE model=?", sType);
        statement = connection.prepareStatement(sql);
        statement.setString(1, model);
        int rowsDeleted = statement.executeUpdate();
        if (rowsDeleted > 0) {
            ConsoleHelper.writeMessage(String.format("Товар модель '%s' успешно удален из базы!", model));
        }
    }
}
