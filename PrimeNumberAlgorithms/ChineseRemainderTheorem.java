import java.util.Scanner;

public class ChineseRemainderTheorem {

    // Extended Euclidean Algorithm
    public static long[] extendedGCD(long a, long b) {
        if (b == 0)
            return new long[]{a, 1, 0};
        long[] vals = extendedGCD(b, a % b);
        long d = vals[0];
        long x1 = vals[2];
        long y1 = vals[1] - (a / b) * vals[2];
        return new long[]{d, x1, y1};
    }

    // Modular inverse of a mod m
    public static long modInverse(long a, long m) {
        long[] vals = extendedGCD(a, m);
        long gcd = vals[0];
        long x = vals[1];
        if (gcd != 1) {
            throw new IllegalArgumentException("Inverse doesn't exist");
        } else {
            return (x % m + m) % m;
        }
    }

    // Chinese Remainder Theorem solver
    public static long chineseRemainder(long[] a, long[] m) {
        long product = 1;
        for (long val : m) {
            product *= val;
        }

        long result = 0;
        for (int i = 0; i < a.length; i++) {
            long partialProduct = product / m[i];
            long inverse = modInverse(partialProduct, m[i]);
            result += a[i] * inverse * partialProduct;
        }

        return (result % product + product) % product;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of congruences: ");
        int k = scanner.nextInt();

        long[] a = new long[k];
        long[] m = new long[k];

        System.out.println("Enter remainders (a_i) and moduli (m_i) for each congruence:");
        for (int i = 0; i < k; i++) {
            System.out.print("a[" + i + "] = ");
            a[i] = scanner.nextLong();
            System.out.print("m[" + i + "] = ");
            m[i] = scanner.nextLong();
        }

        try {
            long x = chineseRemainder(a, m);
            System.out.println("Solution x = " + x);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}
