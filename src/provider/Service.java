package provider;

import java.util.Random;

/**
 * The Service has a Value and the Client wants to know it.
 */
public class Service {
    public int getValue() {
        return new Random().nextInt(6)+1;
    }
}
