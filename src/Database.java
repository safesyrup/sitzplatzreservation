import de.vandermeer.asciitable.AsciiTable;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

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

    //guest methods
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

    public void editGuest() throws SQLException, IOException {
        controller = new Controller();
        conn = DriverManager.getConnection(dbURL, username, password);
        Statement statement = conn.createStatement();
        ResultSet resultSet;
        String updateSqlStatement = "update seatreservationlesi.guest set foreName = ?, " +
                "surName = ?, " +
                "address = ?, " +
                "dateOfBirth = ?, " +
                "phone = ?, " +
                "mobilePhone = ?, " +
                "mail = ?" +
                "where guestId = ?";
        String getGuestIdsQuery = "select guestId from seatreservationlesi.guest";
        ArrayList<Integer> guestIds = new ArrayList();
        PreparedStatement preparedStatement;
        String stringGuestInput;
        int intGuestInput;

        resultSet = statement.executeQuery(getGuestIdsQuery);

        while (resultSet.next()) {
            guestIds.add(resultSet.getInt("guestId"));
        }

        if (!guestIds.isEmpty()) {
            printGuests();

            if (conn.isClosed()) {
                conn = DriverManager.getConnection(dbURL, username, password);
            }

            System.out.println("Id des zu editierenden Gastes:");
            stringGuestInput = controller.readline();
            intGuestInput = idTryParseInt(stringGuestInput);
            intGuestInput = idIsInRange(guestIds, intGuestInput);

            preparedStatement = conn.prepareStatement(updateSqlStatement);

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
            System.out.println("Email");
            preparedStatement.setString(7, controller.readline());
            preparedStatement.setInt(8, intGuestInput);

            preparedStatement.execute();
        }
        controller.editMenu();
        conn.close();
    }

    //team methods
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

    //reservation methods
    public void addReservation() throws SQLException, IOException {
        controller = new Controller();
        conn = DriverManager.getConnection(dbURL, username, password);
        Statement statement = conn.createStatement();
        ResultSet resultSet;
        ArrayList<Integer> guestIds = new ArrayList<>(), gameIds = new ArrayList<>(), allSeats = new ArrayList<>(), takenSeats = new ArrayList<>(), availableSeats = new ArrayList<>();
        String sqlInsertStatement, getGuestIdsQuery, getGamesIdsQuery, getSeatIdsTakenQuery;
        String stringGuestInput, stringGameInput, stringSeatInput;
        int intGuestInput, intGameInput, intSeatInput;

        getGuestIdsQuery = "select guestId from seatreservationlesi.guest";
        resultSet = statement.executeQuery(getGuestIdsQuery);
        while (resultSet.next()) {
            guestIds.add(resultSet.getInt("guestId"));
        }
        if (!guestIds.isEmpty()) {
            printGuests();
            System.out.println("id des Gastes:");
            stringGuestInput = controller.readline();
            intGuestInput = idTryParseInt(stringGuestInput);
            intGuestInput = idIsInRange(guestIds, intGuestInput);

            getGamesIdsQuery = "select gameId from seatreservationlesi.game";
            resultSet = statement.executeQuery(getGamesIdsQuery);
            while (resultSet.next()) {
                gameIds.add(resultSet.getInt("gameId"));
            }

            if (!gameIds.isEmpty()) {
                printGames();
                System.out.println("id des Spiels:");
                stringGameInput = controller.readline();
                intGameInput = idTryParseInt(stringGameInput);
                intGameInput = idIsInRange(gameIds, intGameInput);

                getSeatIdsTakenQuery = "select seat.seatId from seatreservationlesi.game " +
                        "inner join seatreservationlesi.reservation on game.gameId = reservation.game_gameId " +
                        "inner join seatreservationlesi.seat on reservation.seat_seatId = seat.seatId " +
                        "where game.gameId = " + intGameInput;
                resultSet = statement.executeQuery(getSeatIdsTakenQuery);
                //fill arraylist takenseats with all taken seats
                while (resultSet.next()) {
                    takenSeats.add(resultSet.getInt("seatId"));
                }

                //fill arraylist allseats with all seats
                for (int i = 1; i <= 50; i++) {
                    allSeats.add(i);
                }

                allSeats.removeAll(takenSeats);
                availableSeats = allSeats;

                if (!availableSeats.isEmpty()) {
                    printSeats(intGameInput);
                    System.out.println("id des Sitzes:");
                    stringSeatInput = controller.readline();
                    intSeatInput = idTryParseInt(stringSeatInput);
                    intSeatInput = idIsInRange(availableSeats, intSeatInput);

                    sqlInsertStatement = "insert into seatreservationlesi.reservation (game_gameId, seat_seatId, guest_guestId) values (?, ?, ?)";
                    PreparedStatement preparedStatement = conn.prepareStatement(sqlInsertStatement);
                    preparedStatement.setInt(1, intGameInput);
                    preparedStatement.setInt(2, intSeatInput);
                    preparedStatement.setInt(3, intGuestInput);

                    preparedStatement.execute();

                    System.out.println("Reservaiton wurde hinzugefüt");
                } else {
                    System.out.println("für dieses Spiel sind keine Sitzplätze mehr verfügbar");
                }
            } else {
                System.out.println("es gibt noch keine Spiele");
            }
        } else {
            System.out.println("es gibt noch keine Gäste");
        }
        controller.addMenu();
    }

    public void printReservation() throws SQLException, IOException {
        controller = new Controller();
        conn = DriverManager.getConnection(dbURL, username, password);
        String sqlQuery = "select reservationId, seatId, price, foreName, surName, mail, seatRow, time from seatreservationlesi.reservation " +
                "inner join seat on reservation.seat_seatId = seat.seatId " +
                "inner join category on seat.category_categoryId = category.categoryId " +
                "inner join guest on guest.guestId = reservation.guest_guestId " +
                "inner join game on game.gameId = reservation.game_gameId";
        Statement statement = conn.createStatement();
        ResultSet resultSet;
        AsciiTable asciiTable = new AsciiTable();

        asciiTable.getContext().setWidth(200);

        asciiTable.addRule();
        asciiTable.addRow("id",
                "Sitznummer",
                "Sitzreihe",
                "Preis",
                "Vorname",
                "Nachname",
                "Email",
                "Zeit");

        resultSet = statement.executeQuery(sqlQuery);

        while (resultSet.next()) {
            asciiTable.addRule();
            asciiTable.addRow(resultSet.getInt("reservationId"),
                    resultSet.getString("seatId"),
                    resultSet.getString("seatRow"),
                    resultSet.getString("price"),
                    resultSet.getString("foreName"),
                    resultSet.getString("surName"),
                    resultSet.getString("mail"),
                    resultSet.getString("time"));
        }

        System.out.println(asciiTable.render());

        controller.printMenu();
    }

    //seat methods
    public void printSeats(Integer gameId) throws SQLException, IOException {
        controller = new Controller();
        conn = DriverManager.getConnection(dbURL, username, password);
        String getGameRowcoutQuery = "select count(*) from seatreservationlesi.game";
        Statement statement = conn.createStatement();
        ResultSet resultSet;
        int gameRowcount;

        resultSet = statement.executeQuery(getGameRowcoutQuery);
        resultSet.next();
        gameRowcount = resultSet.getInt("count(*)");
        if (gameRowcount > 0) {
            ArrayList<Integer> gameIds = new ArrayList<>();
            String getGameIdsQuery = "select gameId from seatreservationlesi.game";
            String input;
            String[] row1 = new String[12], row2 = new String[12], row3 = new String[12], row4 = new String[12], row5 = new String[12];
            int idInput;
            AsciiTable asciiTable = new AsciiTable();

            asciiTable.getContext().setWidth(75);

            //initialize first index of every row
            row1[0] = "1";
            row2[0] = "2";
            row3[0] = "3";
            row4[0] = "4";
            row5[0] = "5";

            //initialize last index of every row
            row1[11] = "100.-";
            row2[11] = "100.-";
            row3[11] = "80.-";
            row4[11] = "80.-";
            row5[11] = "50.-";

            //fill arraylist with ids
            resultSet = statement.executeQuery(getGameIdsQuery);
            while (resultSet.next()) {
                gameIds.add(resultSet.getInt("gameId"));
            }

            if (gameId == null) {
                System.out.println("id des Spiels:");
                printGames();
                input = controller.readline();
                idInput = idTryParseInt(input);
                idInput = idIsInRange(gameIds, idInput);
            } else {
                idInput = (int) gameId;
            }

            String getSeatsFromGameQuery = "select seatId from seatreservationlesi.game " +
                    "inner join seatreservationlesi.reservation on game.gameId = reservation.game_gameId " +
                    "inner join seatreservationlesi.seat on reservation.seat_seatId = seat.seatId " +
                    "inner join seatreservationlesi.category on seat.category_categoryId = category.categoryId " +
                    "where game.gameId = " + idInput + " " +
                    "order by seatId asc";
            resultSet = statement.executeQuery(getSeatsFromGameQuery);

            //this ugly code sets an x everywhere where the id in the row is allocated to a reservation
            boolean hasnext = resultSet.next();

            for (int i = 1; i <= 50; i++) {
                if (!hasnext) {
                    hasnext = resultSet.next();
                }
                if (i <= 10) {
                    if (hasnext && resultSet.getInt("seatId") == i) {
                        row1[i] = "x";
                        hasnext = false;
                    } else {
                        row1[i] = "" + i;
                    }
                } else if (i <= 20) {
                    if (hasnext && resultSet.getInt("seatId") == i) {
                        row2[i - 10] = "x";
                        hasnext = false;
                    } else {
                        row2[i - 10] = "" + i;
                    }
                } else if (i <= 30) {
                    if (hasnext && resultSet.getInt("seatId") == i) {
                        row3[i - 20] = "x";
                        hasnext = false;
                    } else {
                        row3[i - 20] = "" + i;
                    }
                } else if (i <= 40) {
                    if (hasnext && resultSet.getInt("seatId") == i) {
                        System.out.println("x");
                        hasnext = false;
                        row4[i - 30] = "x";
                    } else {
                        row4[i - 30] = "" + i;
                    }
                } else {
                    if (hasnext && resultSet.getInt("seatId") == i) {
                        row5[i - 40] = "x";
                        hasnext = false;
                    } else {
                        row5[i - 40] = "" + i;
                    }
                }
            }

            asciiTable.addRule();
            asciiTable.addRow(row1);
            asciiTable.addRule();
            asciiTable.addRow(row2);
            asciiTable.addRule();
            asciiTable.addRow(row3);
            asciiTable.addRule();
            asciiTable.addRow(row4);
            asciiTable.addRule();
            asciiTable.addRow(row5);

            String table = asciiTable.render();
            System.out.println(table);
            System.out.println("Sitzplätze mit 'x' sind vergeben");
            System.out.println("");
        } else {
            System.out.println("es gibt noch keine spiele");
        }
    }

    //game methods
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
        String getGameQuery = "select * from seatreservationlesi.game " +
                "inner join seatreservationlesi.foreignteam on foreignteamId = game.foreignteam_foreignteamId";
        Statement statement = conn.createStatement();
        ResultSet resultSet;
        AsciiTable asciiTable = new AsciiTable();

        resultSet = statement.executeQuery(getGameQuery);

        asciiTable.getContext().setWidth(150);
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

    //tryparse and checking methods
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

    public int idIsInRange(ArrayList<Integer> indexes, int input) throws IOException {
        while (!indexes.contains(input)) {
            System.out.println("Fehler, bitte gültige Zahl eigeben");
            String stringInput = controller.readline();
            input = idTryParseInt(stringInput);
        }
        return input;
    }
}
