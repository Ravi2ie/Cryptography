import java.util.Scanner;

public class EulerTheorem {

    // Compute gcd using Euclidean algorithm
    public static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Euler's Totient function φ(n)
    public static long phi(long n) {
        long result = n;
        for (long p = 2; p * p <= n; p++) {
            if (n % p == 0) {
                while (n % p == 0)
                    n /= p;
                result -= result / p;
            }
        }
        if (n > 1) {
            result -= result / n;
        }
        return result;
    }

    // Modular exponentiation (a^b mod m)
    public static long modPow(long base, long exponent, long modulus) {
        long result = 1;
        base = base % modulus;
        while (exponent > 0) {
            if ((exponent & 1) == 1)
                result = (result * base) % modulus;
            base = (base * base) % modulus;
            exponent >>= 1;
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter integer a (base): ");
        long a = scanner.nextLong();

        System.out.print("Enter integer n (modulus): ");
        long n = scanner.nextLong();

        System.out.print("Enter the exponent: ");
        long exp = scanner.nextLong();

        if (gcd(a, n) != 1) {
            System.out.println("a and n are not coprime; Euler's theorem doesn't apply.");
            scanner.close();
            return;
        }

        long phiN = phi(n);
        System.out.println("phi(" + n + ") = " + phiN);

        
        long result = modPow(a, exp%phiN, n);
        System.out.println(a + "^" + exp + " mod " + n + " = " + result);

        long result1=modPow(a, phiN, n);

        System.out.println(a + "^" + phiN + " mod " + n + " = " + result1);
        System.out.println("Since exponent = phi(" + n + "), Euler's theorem predicts result = 1.");
        

        scanner.close();
    }
}
