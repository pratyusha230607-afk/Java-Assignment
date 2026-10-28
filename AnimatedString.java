import java.applet.Applet;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/*
 * Experiment 2: Animate a string in an Applet using multithreading.
 *
 * Compile : javac AnimatedString.java
 * Run     : java AnimatedString          (main() hosts the applet in a Frame)
 *   or    : appletviewer AnimatedString.java   (uses the <applet> tag below, JDK 8-style)
 *
 * <applet code="AnimatedString" width="500" height="150"></applet>
 */
@SuppressWarnings("removal")
public class AnimatedString extends Applet implements Runnable {

    private final String message = "Welcome to Java Multithreading!";
    private int x;
    private boolean positioned = false;
    private Thread animator;
    private volatile boolean running;

    private Image buffer;   // off-screen image to avoid flicker
    private Graphics bufferG;

    @Override
    public void init() {
        setBackground(Color.BLACK);
        setForeground(Color.YELLOW);
        setFont(new Font("Serif", Font.BOLD, 30));
    }

    @Override
    public void start() {
        running = true;
        animator = new Thread(this);   // the animation runs in its own thread
        animator.start();
    }

    @Override
    public void stop() {
        running = false;
        if (animator != null) animator.interrupt();
    }

    @Override
    public void run() {
        while (running) {
            int textWidth = getFontMetrics(getFont()).stringWidth(message);
            if (!positioned) {
                x = getWidth();
                positioned = true;
            }
            x -= 4;                       // move left
            if (x < -textWidth) x = getWidth();   // wrap around
            repaint();
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    // Double buffering: draw off-screen first, then copy to the screen.
    @Override
    public void update(Graphics g) {
        Dimension d = getSize();
        if (d.width <= 0 || d.height <= 0) return;
        if (buffer == null || buffer.getWidth(null) != d.width || buffer.getHeight(null) != d.height) {
            buffer = createImage(d.width, d.height);
            bufferG = buffer.getGraphics();
        }
        bufferG.setColor(getBackground());
        bufferG.fillRect(0, 0, d.width, d.height);
        paint(bufferG);
        g.drawImage(buffer, 0, 0, this);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(getForeground());
        g.setFont(getFont());
        g.drawString(message, x, getHeight() / 2 + 10);
    }

    // Lets the applet run as a normal application (Applet API is deprecated).
    public static void main(String[] args) {
        Frame frame = new Frame("Animated String Applet");
        AnimatedString applet = new AnimatedString();
        frame.add(applet);
        frame.setSize(500, 150);
        frame.setLocationRelativeTo(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                applet.stop();
                applet.destroy();
                frame.dispose();
                System.exit(0);
            }
        });
        frame.setVisible(true);
        applet.init();
        applet.start();
    }
}
