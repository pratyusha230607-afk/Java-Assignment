import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/** Remote object: runs on the server; client calls reach these methods over RMI. */
public class MathServiceImpl extends UnicastRemoteObject implements MathService {

    public MathServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public double multiply(double a, double b) throws RemoteException {
        System.out.println("multiply(" + a + ", " + b + ") called");
        return a * b;
    }

    @Override
    public double add(double a, double b) throws RemoteException {
        System.out.println("add(" + a + ", " + b + ") called");
        return a + b;
    }

    @Override
    public double subtract(double a, double b) throws RemoteException {
        System.out.println("subtract(" + a + ", " + b + ") called");
        return a - b;
    }
}
