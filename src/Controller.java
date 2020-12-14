import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class Controller {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    Database database = new Database();

    public String readline() throws IOException {
        return reader.readLine();
    }

    public void chooseMainMenu() throws IOException, SQLException {
        switch (readline()) {
            case "1":
                addMenu();
                break;
            case "2":
                printMenu();
                break;
            case "3":
                deleteMenu();
                break;
            default:
                mainMenu();
        }
    }

    public void chooseAddMenu() throws IOException, SQLException {
        switch (readline()) {
            case "1":
                database.addGuest();
                break;
            case "2":
                database.addTeam();
                break;
            case "3":
                database.addReservation();
                break;
            case "4":
                database.addGame();
                break;
            case "0":
                mainMenu();
                break;
            default:
                addMenu();
        }
    }

    public void choosePrintMenu() throws IOException, SQLException {
        switch (readline()) {
            case "1":
                database.printGuests();
                printMenu();
                break;
            case "2":
                database.printSeats(null);
                printMenu();
                break;
            case "0":
                mainMenu();
                break;
            default:
                printMenu();
                break;
        }
    }

    public void chooseDeleteMenu() throws IOException, SQLException {
        switch (readline()) {
            case "1":
                database.deleteGuest();
                break;
            case "0":
                mainMenu();
                break;
            default:
                deleteMenu();
                break;
        }
    }

    public void mainMenu() throws IOException, SQLException {
        System.out.println("-----------------------------------");
        System.out.println("Willkommen zur Sitzplatzreservation");
        System.out.println("-----------------------------------");
        System.out.println("Wählen Sie eine Option aus:");
        System.out.println("1 Erfassen");
        System.out.println("2 Anzeigen");
        System.out.println("3 Löschen");
        System.out.println("4 Ändern");
        System.out.println("-----------------------------------");

        chooseMainMenu();
    }

    public void addMenu() throws IOException, SQLException {
        System.out.println("1 Vip-Gast erfassen");
        System.out.println("2 Team hinzufügen");
        System.out.println("3 Reservation hinzufügen");
        System.out.println("4 Spiel hinzufügen");
        System.out.println("0 zurück");

        chooseAddMenu();
    }

    public void printMenu() throws IOException, SQLException {
        System.out.println("1 Gäste anzeigen");
        System.out.println("2 Sitze anzeigen");
        System.out.println("0 zurück");

        choosePrintMenu();
    }

    public void deleteMenu() throws IOException, SQLException {
        System.out.println("1 Gäste löschen");
        System.out.println("0 zurück");

        chooseDeleteMenu();
    }
}
