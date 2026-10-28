import java.util.Scanner;

/**
 * Test client for AddBean.
 * Compile: javac AddBean.java AddBeanTest.java
 * Run    : java AddBeanTest
 */
public class AddBeanTest {

    public static void main(String[] args) {
        AddBean bean = new AddBean();

        // Listener is notified whenever the bound property "sum" changes.
        bean.addPropertyChangeListener(evt ->
                System.out.println("  [event] " + evt.getPropertyName()
                        + " changed: " + evt.getOldValue() + " -> " + evt.getNewValue()));

        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter first number : ");
            bean.setNumber1(sc.nextDouble());
            System.out.print("Enter second number: ");
            bean.setNumber2(sc.nextDouble());
        }

        System.out.println("Sum = " + bean.getSum());
    }
}
