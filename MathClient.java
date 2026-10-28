import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/** Experiment 6 (Client): invokes multiply / add / subtract on the remote system. */
public class MathClient {

    public static void main(String[] args) {
        String host = (args.length > 0) ? args[0] : "localhost";

        try (Scanner sc = new Scanner(System.in)) {
            Registry registry = LocateRegistry.getRegistry(host, MathServer.PORT);
            MathService service = (MathService) registry.lookup(MathServer.NAME);
            System.out.println("Connected to remote MathService at " + host);

            System.out.print("Enter first number : ");
            double a = sc.nextDouble();
            System.out.print("Enter second number: ");
            double b = sc.nextDouble();

            System.out.println("\nResults computed on the remote system:");
            System.out.println(a + " + " + b + " = " + service.add(a, b));
            System.out.println(a + " - " + b + " = " + service.subtract(a, b));
            System.out.println(a + " x " + b + " = " + service.multiply(a, b));
        } catch (Exception e) {
            System.out.println("Client error: " + e);
        }
    }
}
