import java.io.*;
import java.net.Socket;

/**
 * this implements the airline interface
 * and creates an object for the Alaska Airline flight
 *
 * @author Joe Klug and Juilen Kofoid
 * @version December 2, 2019
 */
public class ReservationHandler implements Runnable {
    private static Airline[] airlines = {new Alaska(), new Delta(), new Southwest()};
    private Socket clientSocket;

    public ReservationHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {

        try {
            ObjectInputStream reader = new ObjectInputStream(clientSocket.getInputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            writer.flush();
            String message;
            Object o;
            while ((o = reader.readObject()) != null) {
                if (o instanceof String) {
                    message = (String) o;
                    String[] strings = message.split("//");
                    switch (strings[0]) {
                        case "getPassengers":
                            writer.write(getPassengers(strings[1]));
                            writer.newLine();
                            writer.flush();
                            writeReservations();
                            break;

                        case "checkFull":
                            writer.write(checkFull(strings[1]));
                            writer.newLine();
                            writer.flush();
                            break;

                        case "getGateNum":
                            switch (strings[1]) {
                                case "Alaska":
                                    writer.write(airlines[0].getGateNum());
                                    writer.newLine();
                                    writer.flush();
                                    break;
                                case "Delta":
                                    writer.write(airlines[1].getGateNum());
                                    writer.newLine();
                                    writer.flush();
                                    break;
                                case "Southwest":
                                    writer.write(airlines[2].getGateNum());
                                    writer.newLine();
                                    writer.flush();
                            }
                            break;
                        case "getBoardingPass":
                            switch (strings[1]) {
                                case "Alaska":
                                    Passenger p = airlines[0].getPassengers()[airlines[0].getNumPassengers() - 1];
                                    BoardingPass bp = new BoardingPass("Alaska", p.firstName,
                                            p.lastName, p.age, strings[2]);
                                    writer.write(bp.toString());
                                    writer.newLine();
                                    writer.flush();
                                    break;
                                case "Delta":
                                    p = airlines[1].getPassengers()[airlines[1].getNumPassengers() - 1];
                                    writer.write((new BoardingPass("Delta", p.firstName, p.lastName,
                                            p.age, strings[2]).toString()));
                                    writer.newLine();
                                    writer.flush();
                                    break;
                                case "Southwest":
                                    p = airlines[2].getPassengers()[airlines[2].getNumPassengers() - 1];
                                    writer.write((new BoardingPass("Southwest", p.firstName, p.lastName,
                                            p.age, strings[2]).toString()));
                                    writer.newLine();
                                    writer.flush();
                            }
                    }
                } else if (o instanceof Passenger) {
                    Passenger p = (Passenger) o;
                    String airline = p.getAirline();
                    switch (airline) {
                        case "Alaska":
                            airlines[0].addPassenger(p);
                            writeReservations();
                            break;
                        case "Delta":
                            airlines[1].addPassenger(p);
                            break;
                        case "Southwest":
                            airlines[2].addPassenger(p);
                            break;
                    }
                    writeReservations();
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    } //run

    private String getPassengers(String airline) throws IOException {
        StringBuilder passengerList = new StringBuilder();
        BufferedReader bfr = new BufferedReader(new FileReader(new File("reservations.txt")));
        String s;
        boolean found = false;
        boolean finished = false;
        boolean other = true;
        while (!finished) {
            s = bfr.readLine();
            if (found && s.equals("")) {
                finished = true;
            } else if (found) {
                if (other)
                    passengerList.append(s).append("//");
                other = !other;
            } else if (s.toLowerCase().equals(airline.toLowerCase())) {
                found = true;
            }
        }
        bfr.close();

        return passengerList.toString();
    }

    private String checkFull(String airline) throws IOException {
        int i = 0;
        BufferedReader bfr = new BufferedReader(new FileReader(new File(
                "reservations.txt")));
        String s;
        boolean found = false;
        boolean finished = false;
        boolean other = true;
        while (!finished) {
            s = bfr.readLine();
            if (found && s.equals("")) {
                finished = true;
            } else if (found) {
                if (other)
                    i++;
                other = !other;
            } else if (s.equals(airline + " passenger list")) {
                found = true;
            }
        }
        bfr.close();

        switch (airline) {
            case "Alaska":
                if (i >= airlines[0].getMaxPassengers()) {
                    return "true";
                } else {
                    return "false";
                }
            case "Delta":
                if (i >= airlines[1].getMaxPassengers()) {
                    return "true";
                } else {
                    return "false";
                }
            case "Southwest":
                if (i >= airlines[2].getMaxPassengers()) {
                    return "true";
                } else {
                    return "false";
                }
        }
        return "";
    }

    private void writeReservations() {
        try {
            FileWriter fw = new FileWriter("reservations.txt");
            BufferedWriter bw = new BufferedWriter(fw);

            for (int x = 0; x < 3; x++) {
                switch (x) {
                    case 0:
                        bw.write("ALASKA");
                        bw.newLine();
                        bw.write(airlines[0].getNumPassengers() + "/100");
                        bw.newLine();
                        bw.write("Alaska passenger list");
                        bw.newLine();
                        break;
                    case 1:
                        bw.write("DELTA");
                        bw.newLine();
                        bw.write(airlines[1].getNumPassengers() + "/200");
                        bw.newLine();
                        bw.write("Delta passenger list");
                        bw.newLine();
                        break;
                    case 2:
                        bw.write("SOUTHWEST");
                        bw.newLine();
                        bw.write(airlines[2].getNumPassengers() + "/100");
                        bw.newLine();
                        bw.write("Southwest passenger list");
                        bw.newLine();
                }
                for (int y = 0; y < airlines[x].getNumPassengers(); y++) {
                    bw.write(airlines[x].getPassengers()[y].toString());
                    bw.newLine();
                    switch (x) {
                        case 0:
                            bw.write("---------------------ALASKA");
                            break;
                        case 1:
                            bw.write("---------------------DELTA");
                            break;
                        case 2:
                            bw.write("---------------------SOUTHWEST");
                            break;
                    }
                    bw.newLine();
                }

                bw.newLine();
            }
            bw.write("EOF");
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}