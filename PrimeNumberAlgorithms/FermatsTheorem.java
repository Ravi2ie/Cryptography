import java.util.Scanner;

public class FermatsTheorem {

    // Compute gcd using Euclidean algorithm
    public static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Modular exponentiation (a^b mod m) using fast exponentiation
    public static long modPow(long base, long exponent, long modulus) {
        long result = 1;
        base = base % modulus;
        while (exponent > 0) {
            if ((exponent & 1) == 1) { // if exponent is odd
                result = (result * base) % modulus;
            }
            base = (base * base) % modulus;
            exponent >>= 1; // divide exponent by 2
        }
        return result;
    }

    // Check primality
    public static boolean isPrime(long n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        for (long i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter integer a (base): ");
        long a = scanner.nextLong();

        System.out.print("Enter prime number p (modulus): ");
        long p = scanner.nextLong();

        if (!isPrime(p)) {
            System.out.println("Error: p must be a prime number for Fermat's theorem to apply.");
            scanner.close();
            return;
        }

        if (gcd(a, p) != 1) {
            System.out.println("Error: a and p must be coprime (gcd(a,p) = 1).");
            scanner.close();
            return;
        }

        System.out.print("Enter the exponent: ");
        long exp = scanner.nextLong();

        long result = modPow(a, exp, p);
        System.out.println(a + "^" + exp + " mod " + p + " = " + result);

        long result1=modPow(a,p-1,p);
        System.out.println(a+"^"+(p-1)+" mod "+p+" = "+result1);
        // Extra check: Fermat’s theorem case
        
        System.out.println("According to Fermat's Little Theorem, this should be 1.");
        

        scanner.close();
    }
}
