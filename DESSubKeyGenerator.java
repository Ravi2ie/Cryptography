import java.util.Scanner;

public class DESSubKeyGenerator {

    // PC-1: 64 → 56 bits (drops parity bits)
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

    // Left shifts per round
    private static final int[] SHIFTS = {
        1,1,2,2,2,2,2,2,
        1,2,2,2,2,2,2,1
    };

    // PC-2: 56 → 48 bits (subkey selection)
    private static final int[] PC2 = {
        14,17,11,24,1,5,3,28,
        15,6,21,10,23,19,12,4,
        26,8,16,7,27,20,13,2,
        41,52,31,37,47,55,30,40,
        51,45,33,48,44,49,39,56,
        34,53,46,42,50,36,29,32
    };

    // Get bit from 64-bit key (MSB=1)
    private static int getBit64(long v, int pos) {
        return (int)((v >>> (64 - pos)) & 1L);
    }

    // Get bit from 56-bit key
    private static int getBit56(long v, int pos) {
        return (int)((v >>> (56 - pos)) & 1L);
    }

    // Set bit in 56-bit value
    private static long setBit56(long v, int pos, int bit) {
        if (bit == 1) v |= (1L << (56 - pos));
        return v;
    }

    // Apply PC-1
    private static long applyPC1(long key64) {
        long out56 = 0L;
        for (int i = 0; i < PC1.length; i++) {
            int b = getBit64(key64, PC1[i]);
            out56 = setBit56(out56, i + 1, b);
        }
        return out56;
    }

    // Left rotate 28-bit half
    private static int rot28(int v, int shifts) {
        v &= 0x0FFFFFFF;
        return ((v << shifts) | (v >>> (28 - shifts))) & 0x0FFFFFFF;
    }

    // Combine C and D halves into 56 bits
    private static long combineCD(int C, int D) {
        return ((long)C << 28) | (long)D;
    }

    // Apply PC-2
    private static long applyPC2(long cd56) {
        long out48 = 0L;
        for (int i = 0; i < PC2.length; i++) {
            int b = getBit56(cd56, PC2[i]);
            if (b == 1) out48 |= (1L << (48 - (i + 1)));
        }
        return out48;
    }

    // Format 48-bit subkey as 12 hex digits
    private static String toHex48(long v48) {
        return String.format("%012X", v48 & 0xFFFFFFFFFFFFL);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== DES Subkey Generator ===");
        System.out.print("Enter 64-bit key in hex (16 hex chars): ");
        String hex = sc.next().trim();

        // Clean input
        hex = hex.replaceAll("[^0-9A-Fa-f]", "");
        while (hex.length() < 16) hex = "0" + hex;
        if (hex.length() > 16) hex = hex.substring(hex.length() - 16);

        // Convert hex → 64-bit number
        long key64 = Long.parseUnsignedLong(hex, 16);

        // Apply PC-1 → 56-bit
        long key56 = applyPC1(key64);

        // Split into C and D halves (28 bits each)
        int C = (int)((key56 >>> 28) & 0x0FFFFFFF);
        int D = (int)(key56 & 0x0FFFFFFF);

        // Generate 16 subkeys
        System.out.println("\nRound Subkeys:");
        for (int round = 0; round < 16; round++) {
            C = rot28(C, SHIFTS[round]);   // shift C
            D = rot28(D, SHIFTS[round]);   // shift D
            long cd = combineCD(C, D);     // merge
            long subkey = applyPC2(cd);    // apply PC-2
            System.out.printf("K%-2d = %s%n", round + 1, toHex48(subkey));
        }

        System.out.println("\nEncrypt: K1..K16, Decrypt: K16..K1");
    }
}
