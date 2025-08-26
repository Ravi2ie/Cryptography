import java.util.Scanner;

public class ExtendedEuclidean {

    // Iterative Extended Euclidean Algorithm
    static int[] extendedGCD(int a, int b) {
        int s1 = 1, s2 = 0;
        int t1 = 0, t2 = 1;
        int r1 = a, r2 = b;

        while (r2 != 0) {
            int q = r1 / r2;

            int r = r1 % r2;
            int s = s1 - s2 * q;
            int t = t1 - t2 * q;

            // Update for next iteration
            r1 = r2;
            r2 = r;
            s1 = s2;
            s2 = s;
            t1 = t2;
            t2 = t;
        }

        // At this point, r1 = gcd, and (s1, t1) are the coefficients
        return new int[] {r1, s1, t1};
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first number a: ");
        int a = scanner.nextInt();

        System.out.print("Enter second number b: ");
        int b = scanner.nextInt();

        int[] result = extendedGCD(a, b);
        if (result[1]<0){
            System.out.println("The Inverse is " + (b+result[1]));
        }
        else{
            System.out.println("The Inverse is " + result[1]);
        }
        //System.out.println("GCD = " + result[0]);
        
        //System.out.println("Coefficients: x = " + result[1] + ", y = " + result[2]);
        //System.out.println("Check: " + a + "*" + result[1] + " + " + b + "*" + result[2] + " = " + result[0]);

        scanner.close();
    }
}