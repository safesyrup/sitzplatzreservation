import de.vandermeer.asciitable.AsciiTable;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

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

    //guest stuff
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

        System.out.println("Gast wurde hinzugefügt");

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
        while (resultSet.next()) {
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
        String getGuestIdsQuery = "select guestId from seatreservationlesi.guest";
        String deleteGuestQuery, idInputString;

        int idInputInteger;
        ArrayList<Integer> indexes = new ArrayList<>();
        ResultSet resultSet;

        printGuests();

        resultSet = statement.executeQuery(getGuestIdsQuery);

        while (resultSet.next()) {
            indexes.add(resultSet.getInt("guestId"));
        }

        System.out.println("Id des zu löschenden Gastes:");
        idInputString = controller.readline();
        idInputInteger = idTryParseInt(idInputString);
        idInputInteger = idIsInRange(indexes, idInputInteger);

        deleteGuestQuery = "delete from seatreservationlesi.guest where guestId = " + idInputInteger;
        statement.executeUpdate(deleteGuestQuery);

        conn.close();
        System.out.println("Gast mit id " + idInputInteger + " wurde gelöscht");
        controller.deleteMenu();
    }

    //team stuff
    public void addTeam() throws SQLException, IOException {
        controller = new Controller();
        conn = DriverManager.getConnection(dbURL, username, password);
        String sqlStatement = "insert into seatreservationlesi.foreignteam (name) values (?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sqlStatement);

        System.out.println("Name");
        preparedStatement.setString(1, controller.readline());

        preparedStatement.execute();

        System.out.println("Team wurde hinzugefügt");

        conn.close();
        controller.addMenu();
    }

    public void printTeams() throws SQLException {
        controller = new Controller();
        conn = DriverManager.getConnection(dbURL, username, password);
        String sqlQuery = "select * from seatreservationlesi.foreignteam";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlQuery);

        AsciiTable asciiTable = new AsciiTable();
        asciiTable.getContext().setWidth(250);
        asciiTable.addRule();
        asciiTable.addRow("id", "name");

        while (resultSet.next()) {
            asciiTable.addRule();
            asciiTable.addRow(resultSet.getInt("foreignteamId"),
                    resultSet.getString("name"));
        }
        String table = asciiTable.render();
        System.out.println(table);
        conn.close();

    }

    //reservation stuff
    public void addReservation() throws SQLException {
        controller = new Controller();
        conn = DriverManager.getConnection(dbURL, username, password);
        String sqlInsertStatement;


    }

    //seat stuff
    public void printSeats() throws SQLException, IOException {
        controller = new Controller();
        conn = DriverManager.getConnection(dbURL, username, password);
        String getGameRowcoutQuery = "select count(*) from seatreservationlesi.game";
        Statement statement = conn.createStatement();
        ResultSet resultSet;
        int gameRowcount;

        resultSet = statement.executeQuery(getGameRowcoutQuery);
        resultSet.next();
        gameRowcount = resultSet.getInt("count(*)");
        if (gameRowcount > 1) {
            ArrayList<Integer> gameIds = new ArrayList<>();
            ArrayList<String> seats = new ArrayList<>();
            String getGameIdsQuery = "select gameId from seatreservationlesi.game";
            String input;
            String[] row1 = new String[11], row2 = new String[11], row3 = new String[11], row4 = new String[11], row5 = new String[11];
            int idInput;
            AsciiTable asciiTable = new AsciiTable();

            asciiTable.getContext().setWidth(250);

            //fill arraylist with numbers to 50
            for (int i = 0; i <= 50; i++) {
                seats.add(i, ""+i);
            }

            row1[0] = "A";
            row2[0] = "A";
            row3[0] = "B";
            row4[0] = "B";
            row5[0] = "C";

            resultSet = statement.executeQuery(getGameIdsQuery);
            while (resultSet.next()) {
                gameIds.add(resultSet.getInt("gameId"));
            }

            System.out.println("id des Spiels:");
            printGames();
            input = controller.readline();
            idInput = idTryParseInt(input);
            idInput = idIsInRange(gameIds, idInput);

            String getSeatsFromGameQuery = "select seat.seatId from seatreservationlesi.game inner join seatreservationlesi.reservation on game.gameId = reservation.game_gameId inner join seatreservationlesi.seat on reservation.seat_seatId = seat.seatId inner join seatreservationlesi.category on seat.category_categoryId = category.categoryId where game.gameId = "+ idInput;
            resultSet = statement.executeQuery(getSeatsFromGameQuery);
            resultSet.next();
            for (int i = 1; i <= 50; i++) {
                if (i <= 10) {
                    if (resultSet.next() && resultSet.getInt("seatId") == i) {
                        row1[i] = "x";
                    } else {
                        row1[i] = ""+i;
                    }
                } else if (i <= 20) {
                    if (resultSet.next() && resultSet.getInt("seatId") == i) {
                        row2[i-10] = "x";
                    } else {
                        row2[i-10] = ""+i;
                    }
                } else if (i <= 30) {
                    if (resultSet.next() && resultSet.getInt("seatId") == i) {
                        row3[i-20] = "x";
                    } else {
                        row3[i-20] = ""+i;
                    }
                } else if (i <= 40) {
                    if (resultSet.next() && resultSet.getInt("seatId") == i) {
                        row4[i-30] = "x";
                    } else {
                        row4[i-30] = ""+i;
                    }
                } else {
                    if (resultSet.next() && resultSet.getInt("seatId") == i) {
                        row5[i-40] = "x";
                    } else {
                        row5[i-40] = ""+i;
                    }
                }
                if (resultSet.next()) {
                    resultSet.next();
                }
            }

            for (int i = 1; i <= 50; i++) {
                if (resultSet.next()) {
                    resultSet.next();
                }

                if (resultSet.next() && resultSet.getInt("seatId") == i) {
                    seats.set(i, "x");
                }
            }

            asciiTable.addRule();

            for (int i = 1; i <= 5; i++) {
                for (int j = 10; j <= 10; j++) {

                }
            }

        } else {
            System.out.println("es gibt noch keine spiele");
        }
        controller.printMenu();
    }

    //game stuff
    public void addGame() throws SQLException, IOException {
        controller = new Controller();
        conn = DriverManager.getConnection(dbURL, username, password);
        ResultSet resultSet;
        String getTeamsRowcountQuery = "select count(*) from seatreservationlesi.foreignteam";
        Statement statement = conn.createStatement();
        int teamRowCount;

        resultSet = statement.executeQuery(getTeamsRowcountQuery);
        resultSet.next();
        teamRowCount = resultSet.getInt("count(*)");
        if (teamRowCount > 1) {
            String input;
            int idInput;
            ArrayList<Integer> teamIds = new ArrayList<>();
            String getTeamIdsQuery = "select foreignteamId from seatreservationlesi.foreignteam";
            String insertGameQuery = "insert into seatreservationlesi.game (time, hometeam, foreignteam_foreignteamId) values(?, ?, ?)";
            resultSet = statement.executeQuery(getTeamIdsQuery);
            PreparedStatement preparedStatement = conn.prepareStatement(insertGameQuery);

            while (resultSet.next()) {
                teamIds.add(resultSet.getInt("foreignteamId"));
            }

            printTeams();
            System.out.println("Id des Teams:");

            input = controller.readline();
            idInput = idTryParseInt(input);
            idInput = idIsInRange(teamIds, idInput);

            System.out.println("Datum und Uhrzeit:");
            preparedStatement.setString(1, controller.readline());
            preparedStatement.setString(2, "FC Winner");
            preparedStatement.setInt(3, idInput);

            preparedStatement.execute();

            System.out.println("Spiel wurde hinzugefügt");
        } else {
            System.out.println("es gibt noch keien Teams");
        }
        conn.close();

        controller.addMenu();
    }

    public void printGames() throws SQLException {
        controller = new Controller();
        conn = DriverManager.getConnection(dbURL, username, password);
        String getGameQuery = "select * from seatreservationlesi.game inner join seatreservationlesi.foreignteam on foreignteamId = game.foreignteam_foreignteamId";
        Statement statement = conn.createStatement();
        ResultSet resultSet;
        AsciiTable asciiTable = new AsciiTable();

        resultSet = statement.executeQuery(getGameQuery);

        asciiTable.getContext().setWidth(250);
        asciiTable.addRule();
        asciiTable.addRow("id",
                "Zeit",
                "Heimteam",
                "Auswärtsteam");
        while (resultSet.next()) {
            asciiTable.addRule();
            asciiTable.addRow(resultSet.getInt("gameId"),
                    resultSet.getString("time"),
                    resultSet.getString("hometeam"),
                    resultSet.getString("name"));
        }

        String table = asciiTable.render();
        System.out.println(table);
    }

    //tryparse and checking
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
