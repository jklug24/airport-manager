import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * this implements the airline interface
 * and creates an object for the Alaska Airline flight
 *
 * @author Julien Kofoid and Joe Klug
 * @version December 2, 2019
 */
public class Alaska implements Airline {

    private int maxPassengers;
    private int numPassengers;
    private Passenger[] passengers;
    private String gateNum;

    public Alaska() {
        this.maxPassengers = 100;
        this.numPassengers = 0;
        this.passengers = new Passenger[this.maxPassengers];
        this.gateNum = (new Gate()).gateNum;

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
                        addPassenger(new Passenger("Alaska", info[0], lastName, Integer.parseInt(info[2])));
                    }
                    other = !other;
                } else if (s.equals("Alaska passenger list")) {
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