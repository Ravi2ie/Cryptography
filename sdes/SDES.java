package sdes;

import java.util.Arrays;

public class SDES {

    private static final int[] P10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    private static final int[] P8 = {6, 3, 7, 4, 8, 5, 10, 9};
    private static final int[] IP = {2, 6, 3, 1, 4, 8, 5, 7};
    private static final int[] IP_INV = {4, 1, 3, 5, 7, 2, 8, 6};
    private static final int[] EP = {4, 1, 2, 3, 2, 3, 4, 1};
    private static final int[] P4 = {2, 4, 3, 1};

    private static final int[][] S0 = {
        {1, 0, 3, 2},
        {3, 2, 1, 0},
        {0, 2, 1, 3},
        {3, 1, 3, 2}
    };

    private static final int[][] S1 = {
        {0, 1, 2, 3},
        {2, 0, 1, 3},
        {3, 0, 1, 0},
        {2, 1, 0, 3}
    };

    public static String[] generateSubkeys(String key10bit) {
        int[] key = toIntArray(key10bit);
        int[] p10 = permute(key, P10);
        int[] left = Arrays.copyOfRange(p10, 0, 5);
        int[] right = Arrays.copyOfRange(p10, 5, 10);

        left = leftShift(left, 1);
        right = leftShift(right, 1);
        int[] k1 = permute(concat(left, right), P8);

        left = leftShift(left, 1);
        right = leftShift(right, 1);
        int[] k2 = permute(concat(left, right), P8);

        return new String[] { toBitString(k1), toBitString(k2) };
    }

    public static String encrypt(String plaintext, String k1, String k2) {
        return sdes(plaintext, k1, k2);
    }

    public static String decrypt(String ciphertext, String k1, String k2) {
        // Decryption is same as encryption but subkeys reversed
        return sdes(ciphertext, k2, k1);
    }

    private static String sdes(String input, String key1, String key2) {
        int[] bits = toIntArray(input);
        bits = permute(bits, IP);
        bits = fk(bits, toIntArray(key1));
        bits = swap(bits);
        bits = fk(bits, toIntArray(key2));
        bits = permute(bits, IP_INV);
        return toBitString(bits);
    }

    private static int[] fk(int[] bits, int[] subkey) {
        int[] left = Arrays.copyOfRange(bits, 0, 4);
        int[] right = Arrays.copyOfRange(bits, 4, 8);

        int[] ep = permute(right, EP);
        int[] xor = xor(ep, subkey);
        int[] sbox = sboxOutput(xor);
        int[] p4 = permute(sbox, P4);
        int[] result = xor(left, p4);

        return concat(result, right);
    }

    private static int[] sboxOutput(int[] bits) {
        int[] left = Arrays.copyOfRange(bits, 0, 4);
        int[] right = Arrays.copyOfRange(bits, 4, 8);

        int row1 = (left[0] << 1) | left[3];
        int col1 = (left[1] << 1) | left[2];
        int row2 = (right[0] << 1) | right[3];
        int col2 = (right[1] << 1) | right[2];

        int[] output = new int[4];
        int val1 = S0[row1][col1];
        int val2 = S1[row2][col2];

        output[0] = (val1 >> 1) & 1;
        output[1] = val1 & 1;
        output[2] = (val2 >> 1) & 1;
        output[3] = val2 & 1;

        return output;
    }

    // Utility functions

    private static int[] permute(int[] bits, int[] sequence) {
        int[] output = new int[sequence.length];
        for (int i = 0; i < sequence.length; i++) {
            output[i] = bits[sequence[i] - 1];
        }
        return output;
    }

    private static int[] leftShift(int[] bits, int count) {
        int[] shifted = new int[bits.length];
        for (int i = 0; i < bits.length; i++) {
            shifted[i] = bits[(i + count) % bits.length];
        }
        return shifted;
    }

    private static int[] concat(int[] a, int[] b) {
        int[] result = new int[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    private static int[] xor(int[] a, int[] b) {
        int[] result = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] ^ b[i];
        }
        return result;
    }

    private static int[] swap(int[] bits) {
        int[] swapped = new int[bits.length];
        System.arraycopy(bits, 4, swapped, 0, 4);
        System.arraycopy(bits, 0, swapped, 4, 4);
        return swapped;
    }

    private static int[] toIntArray(String bitString) {
        int[] result = new int[bitString.length()];
        for (int i = 0; i < bitString.length(); i++) {
            result[i] = bitString.charAt(i) - '0';
        }
        return result;
    }

    private static String toBitString(int[] bits) {
        StringBuilder sb = new StringBuilder();
        for (int bit : bits) {
            sb.append(bit);
        }
        return sb.toString();
    }
}
