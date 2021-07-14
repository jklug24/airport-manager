import java.io.Serializable;

/**
 * creates the string of the boarding pass
 *
 * @author Julien Kofoid and Joe Klug
 * @version December 2, 2019
 */
public class BoardingPass implements Serializable {
    private String boardingPass;

    public BoardingPass(String airline, String firstName, String lastName, int age, String boardingGate) {
        boardingPass = "------------------------------------------------------------------------------" +
                "//BOARDING PASS FOR FLIGHT 18000 WITH " + airline +
                "//PASSENGER FIRST NAME: " + firstName.toUpperCase() +
                "//PASSENGER LAST NAME: " + lastName.toUpperCase() +
                "//PASSENGER AGE: " + age +
                "//You can begin boarding at gate " + boardingGate +
                "//------------------------------------------------------------------------------";

    }

    public String toString() {
        return boardingPass;
    }
}