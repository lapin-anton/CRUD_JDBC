import java.sql.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("init");
        String dbURL = "jdbc:mysql://localhost:3306/pc_shop?useUnicode=true&serverTimezone=UTC";
        System.out.println("Введите имя пользователя:");
        String username = ConsoleHelper.readString();
        System.out.println("Введите пароль:");
        String password = ConsoleHelper.readString();

        try (Connection connection = DriverManager.getConnection(dbURL, username, password)) {
            if (connection != null) {
                System.out.println("Connected");
            }
            String sql = "SELECT * FROM product order by maker_id";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int count = 0;
            while (rs.next()) {
                String model = rs.getString("model");
                int type_id = rs.getInt("type_id");
                int maker_id = rs.getInt("maker_id");
                System.out.println(String.format("#%d| %s | %d | %d", ++count, model, type_id, maker_id));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
