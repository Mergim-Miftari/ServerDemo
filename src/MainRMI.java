import providerremote.ClientRMI;
import providerremote.ServerRMI;

public class MainRMI {
    public static void main(String[] args) {
        try {
            new ServerRMI().foreverAcceptAndDelegate();
            new ClientRMI();
        } catch (Exception e) {
            System.out.println("Unexpected Exception");
        }
    }
}
