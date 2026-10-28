import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * Experiment 10: A Java Bean for addition of two numbers.
 *
 * A JavaBean must: be public, be Serializable, have a public no-argument constructor,
 * and expose its properties through getXxx()/setXxx() methods.
 */
public class AddBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private double number1;
    private double number2;
    private double sum;

    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public AddBean() { }                       // public no-arg constructor

    public double getNumber1() { return number1; }

    public void setNumber1(double number1) {
        this.number1 = number1;
        recalculate();
    }

    public double getNumber2() { return number2; }

    public void setNumber2(double number2) {
        this.number2 = number2;
        recalculate();
    }

    /** Read-only (derived) property. */
    public double getSum() { return sum; }

    private void recalculate() {
        double old = sum;
        sum = number1 + number2;
        changes.firePropertyChange("sum", old, sum);   // bound property
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }
}
