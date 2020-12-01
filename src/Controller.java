import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

public class Controller {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    Database database = new Database();

    public Controller() throws SQLException {
    }

    public String readline() throws IOException {
        return reader.readLine();
    }

    public void chooseMainMenu() throws IOException {
        switch (readline()) {
            case "1":
                addMenu();
                break;
            case "2":
                //code
                break;
            default:
                mainMenu();
        }
    }

    public void chooseAddMenu() throws IOException {
        switch (readline()) {
            case "1":

                break;
            case "2":
                //code
                break;
            default:
                addMenu();
        }
    }

    public void mainMenu() throws IOException {
        System.out.println("-----------------------------------");
        System.out.println("Willkommen zur Sitzplatzreservation");
        System.out.println("-----------------------------------");
        System.out.println("Wählen Sie eine Option aus:");
        System.out.println("1 Erfassen");
        System.out.println("2 Anzeigen");
        System.out.println("-----------------------------------");

        chooseMainMenu();
    }

    public void addMenu() throws IOException {
        System.out.println("1 Vip-Gast erfassen");
        System.out.println("2 ");
        System.out.println("3 ");
        System.out.println("4 ");

        chooseAddMenu();
    }
}
