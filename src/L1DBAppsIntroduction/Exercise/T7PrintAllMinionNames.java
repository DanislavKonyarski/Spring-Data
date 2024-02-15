package L1DBAppsIntroduction.Exercise;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class T7PrintAllMinionNames {
    public static void main(String[] args) throws SQLException {

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1221");

        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/minions_db", properties);

        PreparedStatement query = connection.prepareStatement(
                "select name from minions;");

        ResultSet resultQuery = query.executeQuery();

        List<String> minionNames = new ArrayList<>();

        while (resultQuery.next()) {
            minionNames.add(resultQuery.getString("name"));
        }
        if (minionNames.size() % 2 == 0) {
            for (int i = 0; i < minionNames.size() / 2; i++) {
                System.out.println(minionNames.get(i));
                System.out.println(minionNames.get(minionNames.size() - i - 1));
            }
        } else {
            for (int i = 0; i <= minionNames.size() / 2; i++) {
                System.out.println(minionNames.get(i));
                if (i < minionNames.size() / 2) {
                    System.out.println(minionNames.get(minionNames.size() - i - 1));
                }
            }
        }
    }
}