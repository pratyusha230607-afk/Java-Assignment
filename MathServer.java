import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Experiment 6 (Server): registers the remote object in the RMI registry.
 *
 * Compile: javac *.java
 * Run    : 1) java MathServer                 (terminal 1)
 *          2) java MathClient [serverHost]    (terminal 2)
 */
public class MathServer {

    public static final int PORT = 1099;
    public static final String NAME = "MathService";

    public static void main(String[] args) {
        try {
            MathService service = new MathServiceImpl();
            Registry registry = LocateRegistry.createRegistry(PORT);  // built-in registry
            registry.rebind(NAME, service);
            System.out.println("MathService is ready on port " + PORT + ". Waiting for clients...");
        } catch (Exception e) {
            System.out.println("Server error: " + e);
            e.printStackTrace();
        }
    }
}
