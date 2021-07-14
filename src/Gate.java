import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Gate generator
 *
 * @author Julien Kofoid and Joe Klug
 * @version December 2, 2019
 */

public class Gate implements Serializable {
    public String gateNum;
    private ArrayList<String> possibleGateNum;


    public Gate() {
        Random random = new Random();
        possibleGateNum = new ArrayList<>();
        setPossibleGateNum();
        int newRandom = random.nextInt(this.possibleGateNum.size());
        this.gateNum = possibleGateNum.get(newRandom);
        this.possibleGateNum.remove(newRandom);
    }

    private void setPossibleGateNum() {
        if (possibleGateNum.size() == 0) {
            for (int x = 0; x < 3; x++) {
                for (int i = 1; i < 19; i++) {
                    switch (x) {
                        case 0:
                            this.possibleGateNum.add("A" + i);
                            break;
                        case 1:
                            this.possibleGateNum.add("B" + i);
                            break;
                        case 2:
                            this.possibleGateNum.add("C" + i);
                            break;
                    }
                }
            }
        }
    }
}