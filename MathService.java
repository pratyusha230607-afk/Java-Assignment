import java.rmi.Remote;
import java.rmi.RemoteException;

/** Remote interface: every method must declare RemoteException. */
public interface MathService extends Remote {
    double multiply(double a, double b) throws RemoteException;
    double add(double a, double b) throws RemoteException;
    double subtract(double a, double b) throws RemoteException;
}
