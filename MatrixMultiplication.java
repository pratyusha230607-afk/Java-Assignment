import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Experiment 1: Matrix multiplication using InputStreamReader and BufferedReader.
 *
 * Compile: javac MatrixMultiplication.java
 * Run    : java MatrixMultiplication
 */
public class MatrixMultiplication {

    private static final BufferedReader br =
            new BufferedReader(new InputStreamReader(System.in));

    // Reads a positive integer, re-prompting until the input is valid.
    private static int readPositiveInt(String prompt) throws IOException {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(br.readLine().trim());
                if (value > 0) return value;
                System.out.println("  Please enter a number greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("  Invalid number, try again.");
            }
        }
    }

    private static double readDouble(String prompt) throws IOException {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(br.readLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  Invalid number, try again.");
            }
        }
    }

    private static double[][] readMatrix(String name, int rows, int cols) throws IOException {
        double[][] m = new double[rows][cols];
        System.out.println("Enter elements of matrix " + name + ":");
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                m[i][j] = readDouble("  " + name + "[" + (i + 1) + "][" + (j + 1) + "] = ");
        return m;
    }

    private static void printMatrix(String title, double[][] m) {
        System.out.println(title);
        for (double[] row : m) {
            for (double v : row) {
                // print whole numbers without ".0"
                System.out.printf("%10s", (v == Math.rint(v)) ? String.valueOf((long) v)
                                                              : String.format("%.2f", v));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("=== Matrix Multiplication ===");
        int r1 = readPositiveInt("Rows of matrix A    : ");
        int c1 = readPositiveInt("Columns of matrix A : ");
        int r2 = readPositiveInt("Rows of matrix B    : ");
        int c2 = readPositiveInt("Columns of matrix B : ");

        if (c1 != r2) {
            System.out.println("\nMultiplication not possible: columns of A (" + c1
                    + ") must equal rows of B (" + r2 + ").");
            return;
        }

        double[][] a = readMatrix("A", r1, c1);
        double[][] b = readMatrix("B", r2, c2);
        double[][] c = new double[r1][c2];

        for (int i = 0; i < r1; i++)
            for (int j = 0; j < c2; j++)
                for (int k = 0; k < c1; k++)
                    c[i][j] += a[i][k] * b[k][j];

        System.out.println();
        printMatrix("Matrix A:", a);
        printMatrix("Matrix B:", b);
        printMatrix("Result (A x B):", c);
    }
}
