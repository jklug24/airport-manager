import java.io.Serializable;

/**
 * creates a passenger object
 *
 * @author Julien Kofoid and Joe Klug
 * @version December 2, 2019
 */
public class Passenger implements Serializable {
    String firstName;
    String lastName;
    int age;
    BoardingPass boardingPass;
    String boardingGate;
    String airline;

    public Passenger(String airline, String firstName, String lastName, int age) {
        this.airline = airline;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getBoardingGate() {
        return this.boardingGate;
    }

    public void setBoardingGate(String boardingGate) {
        this.boardingGate = boardingGate;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public int getAge() {
        return this.age;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String toString() {
        return this.firstName.charAt(0) + ". " + this.lastName +
                ", " + this.age;
    }
}