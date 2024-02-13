package L1DBAppsIntroduction.Exercise;

import java.sql.*;
import java.util.Properties;

public class T2GetVillainsNames {

    public static void main(String[] args) throws SQLException {

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password","1221");

        Connection connection = DriverManager.
                getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        PreparedStatement query = connection.prepareStatement(
                "SELECT v.name, COUNT(distinct mv.minion_id) AS count_minion\n" +
                "FROM minions m\n" +
                "         JOIN minions_villains mv on m.id = mv.minion_id\n" +
                "         JOIN villains v on v.id = mv.villain_id\n" +
                "GROUP BY v.name\n" +
                "HAVING count_minion > 15\n" +
                "ORDER BY count_minion DESC;");

        ResultSet result = query.executeQuery();


        while (result.next()){
            System.out.printf("%s %d",result.getString("name"),
                    result.getInt("count_minion"));
        }

    }
}
