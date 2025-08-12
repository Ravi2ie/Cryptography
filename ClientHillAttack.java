import java.util.Scanner;

public class ClientHillAttack {

    static final int MOD = 26;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Hill Cipher Key Recovery (2x2 matrix)");

        // Input plaintext and ciphertext pairs (4 letters each)
        System.out.println("Enter 4 letters of plaintext (no spaces, letters only):");
        String plaintext = sc.nextLine().toUpperCase().replaceAll("[^A-Z]", "");
        if (plaintext.length() != 4) {
            System.out.println("Plaintext length must be exactly 4 letters.");
            sc.close();
            return;
        }

        System.out.println("Enter 4 letters of corresponding ciphertext (no spaces, letters only):");
        String ciphertext = sc.nextLine().toUpperCase().replaceAll("[^A-Z]", "");
        if (ciphertext.length() != 4) {
            System.out.println("Ciphertext length must be exactly 4 letters.");
            sc.close();
            return;
        }

        // Construct matrices column-wise!
        int[][] P = new int[2][2];
        int[][] C = new int[2][2];

        for (int i = 0; i < 4; i++) {
            int row = i % 2;
            int col = i / 2;
            P[row][col] = plaintext.charAt(i) - 'A';
            C[row][col] = ciphertext.charAt(i) - 'A';
        }

        // Compute determinant of plaintext matrix
        int det = determinant(P);
        det = mod26(det);
        int detInv = modInverse(det, MOD);

        if (detInv == -1) {
            System.out.println("Plaintext matrix is not invertible modulo 26. Cannot recover key.");
            sc.close();
            return;
        }

        int[][] P_inv = invertMatrix(P, detInv);

        // Compute key matrix K = C * P_inv mod 26
        int[][] K = multiplyMatrices(C, P_inv);

        System.out.println("Recovered key matrix (2x2):");
        printMatrix(K);

        sc.close();
    }

    // Calculate determinant of 2x2 matrix
    static int determinant(int[][] m) {
        return m[0][0]*m[1][1] - m[0][1]*m[1][0];
    }

    // Modular inverse of a mod m
    static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1)
                return x;
        }
        return -1;
    }

    // Modulo 26 helper
    static int mod26(int x) {
        x %= MOD;
        if (x < 0) x += MOD;
        return x;
    }

    // Invert 2x2 matrix mod 26 using determinant inverse
    static int[][] invertMatrix(int[][] m, int detInv) {
        int[][] inv = new int[2][2];
        inv[0][0] = mod26(m[1][1] * detInv);
        inv[0][1] = mod26(-m[0][1] * detInv);
        inv[1][0] = mod26(-m[1][0] * detInv);
        inv[1][1] = mod26(m[0][0] * detInv);
        return inv;
    }

    // Multiply 2x2 matrices mod 26
    static int[][] multiplyMatrices(int[][] a, int[][] b) {
        int[][] res = new int[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                res[i][j] = 0;
                for (int k = 0; k < 2; k++) {
                    res[i][j] += a[i][k] * b[k][j];
                }
                res[i][j] = mod26(res[i][j]);
            }
        }
        return res;
    }

    // Print matrix nicely
    static void printMatrix(int[][] m) {
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
    }
}
