import java.io.*;
import java.net.*;

public class ServerHill {
    // Function to find modular inverse of a number mod 26
    static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++)
            if ((a * x) % m == 1)
                return x;
        return -1;
    }

    // Function to get determinant of a matrix
    static int determinant(int[][] mat, int n) {
        if (n == 2)
            return (mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0]);
        else if (n == 3) {
            return mat[0][0] * (mat[1][1] * mat[2][2] - mat[1][2] * mat[2][1])
                 - mat[0][1] * (mat[1][0] * mat[2][2] - mat[1][2] * mat[2][0])
                 + mat[0][2] * (mat[1][0] * mat[2][1] - mat[1][1] * mat[2][0]);
        }
        return 0;
    }

    // Function to get adjoint matrix
    static int[][] adjoint(int[][] mat, int size) {
        int[][] adj = new int[size][size];
        if (size == 2) {
            adj[0][0] = mat[1][1];
            adj[1][1] = mat[0][0];
            adj[0][1] = -mat[0][1];
            adj[1][0] = -mat[1][0];
        } else if (size == 3) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int[][] temp = new int[2][2];
                    int r = 0;
                    for (int k = 0; k < 3; k++) {
                        if (k == i) continue;
                        int c = 0;
                        for (int l = 0; l < 3; l++) {
                            if (l == j) continue;
                            temp[r][c] = mat[k][l];
                            c++;
                        }
                        r++;
                    }
                    int val = temp[0][0] * temp[1][1] - temp[0][1] * temp[1][0];
                    adj[j][i] = ((i + j) % 2 == 0) ? val : -val;
                }
            }
        }
        return adj;
    }

    // Multiply matrix with vector mod 26
    static String hillDecrypt(String cipher, int[][] key, int size) {
        int det = determinant(key, size);
        det = ((det % 26) + 26) % 26;
        int detInv = modInverse(det, 26);

        if (detInv == -1) {
            return "Key matrix is not invertible (no mod inverse exists)";
        }

        int[][] adj = adjoint(key, size);
        int[][] invKey = new int[size][size];

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                invKey[i][j] = ((adj[i][j] * detInv) % 26 + 26) % 26;

        StringBuilder plain = new StringBuilder();
        for (int i = 0; i < cipher.length(); i += size) {
            int[] vec = new int[size];
            for (int j = 0; j < size; j++)
                vec[j] = cipher.charAt(i + j) - 'A';

            for (int row = 0; row < size; row++) {
                int sum = 0;
                for (int col = 0; col < size; col++) {
                    sum += invKey[row][col] * vec[col];
                }
                sum = ((sum % 26) + 26) % 26;
                plain.append((char) ('a' + sum));
            }
        }
        return plain.toString();
    }

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8020);
        System.out.println("Server running...");
        Socket socket = ss.accept();
        System.out.println("Client connected.");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String data = in.readLine(); // Format: cipher::size::keyMatrixCSV

        String[] parts = data.split("::");
        String cipher = parts[0];
        int size = Integer.parseInt(parts[1]);

        int[][] key = new int[size][size];
        String[] keyVals = parts[2].split(",");
        int idx = 0;
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                key[i][j] = Integer.parseInt(keyVals[idx++]);

        String plain = hillDecrypt(cipher, key, size);
        System.out.println("[Cipher Text]: " + cipher);
        System.out.println("[Decrypted Text]: " + plain);

        socket.close();
        ss.close();
    }
}