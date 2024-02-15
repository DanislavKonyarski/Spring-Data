package L1DBAppsIntroduction.Exercise;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class T4AddMinion {
    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);

        String[] minionInformation = scanner.nextLine().split(" ");

        String minionName = minionInformation[1];
        int minionAge = Integer.parseInt(minionInformation[2]);
        String minionTown = minionInformation[3];


        String villainName = scanner.nextLine().split(" ")[1];

        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1221");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", properties);

        String selectQueryForTowns = "select * from towns\n" +
                "where name = ?;";
        String insertQueryForTowns = "insert into towns(name)\n" +
                "value (?);";
        String outputForInsertTown = "Town %s was added to the database.\n";

        insertInformationForTownOrVillain(connection,
                minionTown,
                selectQueryForTowns,
                insertQueryForTowns,
                outputForInsertTown);

        String selectQueryForVillains = "select * from villains\n" +
                "where name = ?;";
        String insertQueryForVillains = "insert into villains(name,evilness_factor)\n" +
                "value (?, 'evil');";
        String outputForInsertVillains = "Villain %s was added to the database.\n";

        insertInformationForTownOrVillain(connection,
                villainName,
                selectQueryForVillains,
                insertQueryForVillains,
                outputForInsertVillains);

        String selectQueryForMinions = "select * from minions\n" +
                "where name = ?;\n";
        String insertQueryForMinions = "insert into minions(name,age,town_id)\n" +
                "value (?,?,?);";
        String outputForInsertMinions = "Successfully added %s to be minion of %s";
        insertMinion(connection,
                selectQueryForMinions,
                insertQueryForMinions,
                minionName,
                minionAge,
                minionTown,
                villainName,
                outputForInsertMinions);

    }
    private static void insertMinion(Connection connection,
                                     String selectQuery,
                                     String insertQuery,
                                     String minionName,
                                     int minionAge,
                                     String nameTown,
                                     String villainName,
                                     String stringForOutput) throws SQLException {
        PreparedStatement selectQueryForMinion = connection.prepareStatement(selectQuery);
        selectQueryForMinion.setString(1,minionName);
        if (!selectQueryForMinion.executeQuery().next()){

            int minionTownID = getId(connection, "towns", nameTown);

            PreparedStatement insertMinionQuery = connection.prepareStatement(insertQuery);
            insertMinionQuery.setString(1,minionName);
            insertMinionQuery.setInt(2,minionAge);
            insertMinionQuery.setInt(3,minionTownID);
            insertMinionQuery.executeUpdate();

            int minionId = getId(connection,"minions", minionName);
            int villainId = getId(connection, "villains", villainName);

            PreparedStatement insertMapingTable = connection.prepareStatement(
                    "insert into minions_villains(minion_id, villain_id)\n" +
                            "value (?,?);");
            insertMapingTable.setInt(1,minionId);
            insertMapingTable.setInt(2,villainId);
            insertMapingTable.executeUpdate();

            System.out.printf(stringForOutput,minionName,villainName);

        }
    }
    private static void insertInformationForTownOrVillain(Connection connection,
                                                          String parameter,
                                                          String selectQuery,
                                                          String insertQuery,
                                                          String stringForOutput) throws SQLException {

        PreparedStatement getInformationFromSelectQuery = connection.prepareStatement(selectQuery);
        getInformationFromSelectQuery.setString(1, parameter);

        if (!getInformationFromSelectQuery.executeQuery().next()) {
            PreparedStatement setNewInformation = connection.prepareStatement(insertQuery);
            setNewInformation.setString(1, parameter);

            setNewInformation.executeUpdate();

            System.out.printf(stringForOutput, parameter);
        }
    }
    private static int getId(Connection connection,String tableName, String name) throws SQLException {
        String query = String.format("select id from %s where name = ?;",tableName);
        PreparedStatement getIdQuery = connection.prepareStatement(
                query);
        getIdQuery.setString(1, name);
        ResultSet resultTownID = getIdQuery.executeQuery();
        resultTownID.next();
        return  resultTownID.getInt("id");
    }
}
