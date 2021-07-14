import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * this implements the airline interface
 * and creates an object for the Delta Airline flight
 *
 * @author Julien Kofoid and Joe Klug
 * @version December 2, 2019
 */

public class Delta implements Airline {

    private int maxPassengers;
    private int numPassengers;
    private Passenger[] passengers;
    private String gateNum;

    public Delta() {
        maxPassengers = 100;
        numPassengers = 0;
        passengers = new Passenger[this.maxPassengers];
        gateNum = (new Gate()).gateNum;

        try {
            StringBuilder passengerList = new StringBuilder();
            BufferedReader bfr = new BufferedReader(new FileReader(new File("reservations.txt")));
            String s;
            boolean found = false;
            boolean other = true;
            while (true) {
                s = bfr.readLine();
                if (found && s.equals("")) {
                    break;
                } else if (found) {
                    if (other) {
                        String[] info = s.split(" ");
                        String lastName = info[1].substring(0, info[1].length() - 1);
                        addPassenger(new Passenger("Delta", info[0], lastName, Integer.parseInt(info[2])));
                    }
                    other = !other;
                } else if (s.equals("Delta passenger list")) {
                    found = true;
                }
            }
            bfr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addPassenger(Passenger passenger) {
        passengers[numPassengers] = passenger;
        numPassengers++;
    }

    @Override
    public int getNumPassengers() {
        return numPassengers;
    }

    @Override
    public int getMaxPassengers() {
        return maxPassengers;
    }

    @Override
    public String getGateNum() {
        return gateNum;
    }

    @Override
    public Passenger[] getPassengers() {
        return passengers;
    }
}