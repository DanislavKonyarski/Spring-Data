package L1DBAppsIntroduction.Exercise;

import java.lang.ref.PhantomReference;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class T3GetMinionNames {
    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Please insert ID on villain: ");
        String vId = scanner.nextLine();

        Properties properties = new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","1221");

        Connection connection = DriverManager.
                getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        PreparedStatement query = connection.prepareStatement(
                "SELECT v.name AS vName, m.name AS mName, m.age AS mAge\n" +
                        "FROM minions m\n" +
                        "         JOIN minions_villains mv on m.id = mv.minion_id\n" +
                        "         JOIN villains v on v.id = mv.villain_id\n" +
                        "where v.id = ?");
        query.setString(1,vId);

        ResultSet result = query.executeQuery();

        int count = 1;
        while (result.next()) {

            if (count==1) {
                System.out.printf("Villain: %s%n", result.getString("vName"));
            }
            System.out.printf("%d. %s %d%n",count,result.getString("mName"),result.getInt("mAge"));
            count++;

        }
    }
}
