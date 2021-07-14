import java.io.Serializable;

/**
 * is implemented by all the different airlines
 *
 * @author Julien Kofoid
 * @version December 2, 2019
 */
public interface Airline extends Serializable {

    void addPassenger(Passenger passenger);

    int getNumPassengers();

    int getMaxPassengers();

    String getGateNum();

    Passenger[] getPassengers();

}