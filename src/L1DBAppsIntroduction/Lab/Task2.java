package L1DBAppsIntroduction.Lab;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Task2 {
    public static void main(String[] args) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1221");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/diablo", properties);

        System.out.print("Enter ussername: ");
        String username = scanner.nextLine();

        PreparedStatement query = connection.prepareStatement(
                "SELECT user_name, first_name, last_name, COUNT(ug.id) as games "+
                "FROM users "+
                "JOIN users_games as ug ON users.id = ug.user_id "+
                "WHERE user_name = ? "+
                "GROUP BY user_id;");
        query.setString(1, username);

        ResultSet resultSet = query.executeQuery();

        if (resultSet.next()) {
            System.out.printf("Users: %s%n%s %s has played %d games",
                    resultSet.getString("user_name"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getInt("games")
                    );

        } else {
            System.out.println("No such user exists");
        }
    }
}
