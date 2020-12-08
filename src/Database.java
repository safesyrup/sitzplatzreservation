import de.vandermeer.asciitable.AsciiTable;

import java.io.File;
import java.io.IOException;
import  java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Database {
    Controller controller;
    Connection conn;

    final String dbURL = "jdbc:mysql://localhost/seatreservationLeSi?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    final String username = "root";
    final String password = "root";

    public Database() {
    }

    public void initializeConn() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    public void addGuest() throws SQLException, IOException {
        controller = new Controller();
        conn = DriverManager.getConnection(dbURL, username, password);

        String sqlstatement = "insert into seatreservationlesi.guest (foreName, surName, " +
                "address, " +
                "dateOfBirth, " +
                "phone, mobilephone, mail) " +
                "values(?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = conn.prepareStatement(sqlstatement);
        System.out.println("Vorname:");
        preparedStatement.setString(1, controller.readline());
        System.out.println("Nachname:");
        preparedStatement.setString(2, controller.readline());
        System.out.println("Addresse:");
        preparedStatement.setString(3, controller.readline());
        System.out.println("Geburtsdatum:");
        preparedStatement.setString(4, controller.readline());
        System.out.println("Telefon:");
        preparedStatement.setString(5, controller.readline());
        System.out.println("Mobilnummer:");
        preparedStatement.setString(6, controller.readline());
        System.out.println("Email:");
        preparedStatement.setString(7, controller.readline());

        preparedStatement.execute();

        conn.close();
        controller.addMenu();
    }

    public void printGuests() throws SQLException {
        conn = DriverManager.getConnection(dbURL, username, password);
        Statement statement = conn.createStatement();
        String sqlstatement = "select * from seatreservationlesi.guest";
        ResultSet resultSet = statement.executeQuery(sqlstatement);

        AsciiTable asciitable = new AsciiTable();
        asciitable.getContext().setWidth(250);
        asciitable.addRule();
        asciitable.addRow("id",
                "Vorname",
                "Nachname",
                "Addresse",
                "Geburtsdatum",
                "Telefon",
                "Mobiltelefon",
                "Email"
        );
        while(resultSet.next()) {
            asciitable.addRule();
            asciitable.addRow(resultSet.getInt("guestId"),
                    resultSet.getString("foreName"),
                    resultSet.getString("surName"),
                    resultSet.getString("address"),
                    resultSet.getString("dateOfBirth"),
                    resultSet.getString("phone"),
                    resultSet.getString("mobilePhone"),
                    resultSet.getString("mail")
                    );
        }

        String table = asciitable.render();
        System.out.println(table);

        conn.close();
    }

    public void deleteGuest() throws SQLException, IOException {
        controller = new Controller();

        conn = DriverManager.getConnection(dbURL, username, password);
        Statement statement = conn.createStatement();

        //queries
        String getGuestRowCountQuery = "select count(*) from seatreservationlesi.guest";
        String getGuestIdsQuery = "select guestId from seatreservationlesi.guest";
        String deleteGuestQuery, idInputString;

        int idInputInteger;
        ArrayList<Integer> indexes = new ArrayList<>();
        ResultSet resultSet;

        printGuests();

        resultSet = statement.executeQuery(getGuestRowCountQuery);
        resultSet.next();

        resultSet = statement.executeQuery(getGuestIdsQuery);

        while (resultSet.next()){
            indexes.add(resultSet.getInt("guestId"));
        }

        System.out.println("Id des zu löschenden Gastes:");
        idInputString = controller.readline();
        idInputInteger = idTryParseInt(idInputString);
        idInputInteger = idIsInRange(indexes, idInputInteger);

        deleteGuestQuery = "delete from seatreservationlesi.guest where guestId = "+idInputInteger;
        statement.executeUpdate(deleteGuestQuery);

        conn.close();
        System.out.println("Gast mit id "+idInputInteger+" wurde gelöscht");
        controller.deleteMenu();
    }

    public boolean tryParseInt(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int idTryParseInt(String input) throws IOException {
        while (!tryParseInt(input)) {
            System.out.println("fehler, bitte gültige Zahl eingeben:");
            input = controller.readline();
        }
        return Integer.parseInt(input);
    }

    public int idIsInRange(ArrayList indexes, int input) throws IOException {
        while (!indexes.contains(input)) {
            System.out.println("Fehler, bitte gültige Zahl eigeben");
            String stringInput = controller.readline();
            input = idTryParseInt(stringInput);
        }
        return input;
    }
}
