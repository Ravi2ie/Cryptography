<<<<<<< HEAD
import java.util.Scanner;

public class SimpleDESSubKeyGeneratorMinimal {

    private static final int[] PC1 = {
        57,49,41,33,25,17,9,
        1,58,50,42,34,26,18,
        10,2,59,51,43,35,27,
        19,11,3,60,52,44,36,
        63,55,47,39,31,23,15,
        7,62,54,46,38,30,22,
        14,6,61,53,45,37,29,
        21,13,5,28,20,12,4
    };

    private static final int[] PC2 = {
        14,17,11,24,1,5,3,28,
        15,6,21,10,23,19,12,4,
        26,8,16,7,27,20,13,2,
        41,52,31,37,47,55,30,40,
        51,45,33,48,44,49,39,56,
        34,53,46,42,50,36,29,32
    };

    private static final int[] SHIFTS = {
        1,1,2,2,2,2,2,2,
        1,2,2,2,2,2,2,1
    };

    // Permutation function
    private static String permute(String input, int[] table) {
        StringBuilder sb = new StringBuilder();
        for (int pos : table) {
            sb.append(input.charAt(pos - 1)); // 1-based indexing
        }
        return sb.toString();
    }

    // Left circular shift
    private static String leftShift(String bits, int n) {
        return bits.substring(n) + bits.substring(0, n);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== DES Subkey Generator (Minimal, binary only) ===");
        System.out.print("Enter 64-bit key as binary string (64 chars 0/1): ");
        String key64 = sc.next().trim();

        if (key64.length() != 64 || !key64.matches("[01]+")) {
            System.out.println("Invalid input. Please enter exactly 64 bits.");
            return;
        }

        // Apply PC-1: 64 → 56 bits
        String key56 = permute(key64, PC1);

        // Split into C and D halves
        String C = key56.substring(0, 28);
        String D = key56.substring(28);

        System.out.println("\nRound Subkeys (binary):");
        for (int i = 0; i < 16; i++) {
            C = leftShift(C, SHIFTS[i]);
            D = leftShift(D, SHIFTS[i]);

            String CD = C + D;
            String subkey48 = permute(CD, PC2);

            System.out.printf("K%-2d = %s%n", i + 1, subkey48);
        }

        System.out.println("\nEncrypt: K1..K16, Decrypt: K16..K1");
    }
}
=======
import java.util.Scanner;

public class SimpleDESSubKeyGeneratorMinimal {

    private static final int[] PC1 = {
        57,49,41,33,25,17,9,
        1,58,50,42,34,26,18,
        10,2,59,51,43,35,27,
        19,11,3,60,52,44,36,
        63,55,47,39,31,23,15,
        7,62,54,46,38,30,22,
        14,6,61,53,45,37,29,
        21,13,5,28,20,12,4
    };

    private static final int[] PC2 = {
        14,17,11,24,1,5,3,28,
        15,6,21,10,23,19,12,4,
        26,8,16,7,27,20,13,2,
        41,52,31,37,47,55,30,40,
        51,45,33,48,44,49,39,56,
        34,53,46,42,50,36,29,32
    };

    private static final int[] SHIFTS = {
        1,1,2,2,2,2,2,2,
        1,2,2,2,2,2,2,1
    };

    // Permutation function
    private static String permute(String input, int[] table) {
        StringBuilder sb = new StringBuilder();
        for (int pos : table) {
            sb.append(input.charAt(pos - 1)); // 1-based indexing
        }
        return sb.toString();
    }

    // Left circular shift
    private static String leftShift(String bits, int n) {
        return bits.substring(n) + bits.substring(0, n);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== DES Subkey Generator (Minimal, binary only) ===");
        System.out.print("Enter 64-bit key as binary string (64 chars 0/1): ");
        String key64 = sc.next().trim();

        if (key64.length() != 64 || !key64.matches("[01]+")) {
            System.out.println("Invalid input. Please enter exactly 64 bits.");
            return;
        }

        // Apply PC-1: 64 → 56 bits
        String key56 = permute(key64, PC1);

        // Split into C and D halves
        String C = key56.substring(0, 28);
        String D = key56.substring(28);

        System.out.println("\nRound Subkeys (binary):");
        for (int i = 0; i < 16; i++) {
            C = leftShift(C, SHIFTS[i]);
            D = leftShift(D, SHIFTS[i]);

            String CD = C + D;
            String subkey48 = permute(CD, PC2);

            System.out.printf("K%-2d = %s%n", i + 1, subkey48);
        }

        System.out.println("\nEncrypt: K1..K16, Decrypt: K16..K1");
    }
}
>>>>>>> dcd33ee789c1c0541a1bc723fe017a3b95c90250
