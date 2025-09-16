import java.util.Scanner;

public class MultiplicativeInverse {

    // Extended Euclidean Algorithm
    public static int[] extendedGCD(int a, int b) {
        if (b == 0)
            return new int[]{a, 1, 0};
        int[] vals = extendedGCD(b, a % b);
        int d = vals[0];
        int x1 = vals[2];
        int y1 = vals[1] - (a / b) * vals[2];
        return new int[]{d, x1, y1};
    }

    // Compute multiplicative inverse of a modulo m
    public static int modInverse(int a, int m) {
        int[] vals = extendedGCD(a, m);
        int gcd = vals[0];
        int x = vals[1];
        if (gcd != 1) {
            throw new ArithmeticException("Inverse does not exist since gcd(" + a + ", " + m + ") != 1");
        } else {
            // Make sure inverse is positive
            return (x % m + m) % m;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number a: ");
        int a = scanner.nextInt();

        System.out.print("Enter modulus m: ");
        int m = scanner.nextInt();

        try {
            int inverse = modInverse(a, m);
            System.out.println("Multiplicative inverse of " + a + " modulo " + m + " is: " + inverse);
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }
}
