/**
 * runs reservation server
 *
 * @author Julien Kofoid and Joe Klug
 * @version December 2, 2019
 */

public class ReservationRunner {
    public static void main(String[] args) {
        ReservationServer server;

        try {
            server = new ReservationServer();
        } catch (Exception e) {
            e.printStackTrace();

            return;
        } //end try catch

        server.serveClients();
    } //main
}
