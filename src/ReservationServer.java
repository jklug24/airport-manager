import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * allows multiple users to connect to the servers
 *
 * @author Julien Kofoid and Joe Klug
 * @version December 2, 2019
 */

public class ReservationServer {
    private ServerSocket serverSocket;

    public ReservationServer() throws NullPointerException, IOException {
        this.serverSocket = new ServerSocket(0);
    } //CensoringServer

    public void serveClients() {
        Socket clientSocket;
        Thread handlerThread;
        int clientCount = 0;

        System.out.printf("<Now serving clients on port %d...>%n", this.serverSocket.getLocalPort());

        while (true) {
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();

                return;
            } //end try catch

            handlerThread = new Thread(new ReservationHandler(clientSocket));

            handlerThread.start();

            System.out.printf("<Client %d connected...>%n", clientCount);

            clientCount++;
        } //end while
    } //serveClients
}
