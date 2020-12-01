import de.vandermeer.asciitable.AsciiTable;

import java.io.File;
import  java.sql.*;

public class Database {
    final String dbURL = "jdbc:mysql://localhost/seatreservationLeSi";
    final String username = "root";
    final String password = "root";

    Connection conn = DriverManager.getConnection(dbURL, username, password);

    public Database() throws SQLException {
    }

    public void printGuests() throws SQLException {
        Statement statement = conn.createStatement();
        String sqlstatement = "select from * guests";
        ResultSet resultSet = statement.executeQuery(sqlstatement);

        AsciiTable asciitable = new AsciiTable();
        asciitable.getContext().setWidth(300);
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
        for (int i = 0; resultSet.next(); i++) {
            asciitable.addRule();
            asciitable.addRow(resultSet.getString("guestId"),
                    resultSet.getString("name"),
                    resultSet.getString("surname"),
                    resultSet.getString("address"),
                    resultSet.getDate("dateOfBirth").toString(),
                    resultSet.getString("phone"),
                    resultSet.getString("mobilePhone"),
                    resultSet.getString("mail")
                    );
        }
    }
}
