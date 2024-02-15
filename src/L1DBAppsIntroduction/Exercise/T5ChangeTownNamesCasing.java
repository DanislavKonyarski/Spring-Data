package L1DBAppsIntroduction.Exercise;

import jdk.jshell.spi.SPIResolutionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Collectors;

public class T5ChangeTownNamesCasing {
    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);
        String country = scanner.nextLine();

        Properties properties = new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","1221");

        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/minions_db", properties);

        PreparedStatement updateQuery = connection.prepareStatement(
                "update towns set  name = upper(name) where country=?;");
        updateQuery.setString(1,country);
        updateQuery.executeUpdate();

        PreparedStatement selectQuery = connection.prepareStatement(
                "select name from towns where country = ?");
        selectQuery.setString(1, country);
        ResultSet resultTowns = selectQuery.executeQuery();
        if (!resultTowns.next()){
            System.out.println("No town names were affected.");
        }else {
            List<String> towns = new ArrayList<>();
            towns.add(resultTowns.getString("name"));
            while (resultTowns.next()){
                towns.add(resultTowns.getString("name"));
            }
            System.out.printf("%d town names were affected.\n",towns.size());
            System.out.println(towns);
        }
    }
}
