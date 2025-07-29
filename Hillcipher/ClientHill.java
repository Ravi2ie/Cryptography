import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHill {
    // Accepts text and key, performs Hill encryption
    public static String hillEncrypt(String plain, int[][] key, int size) {
        // Pad with 'x' if needed
        while (plain.length() % size != 0)
            plain += 'x';

        StringBuilder cipher = new StringBuilder();
        for (int i = 0; i < plain.length(); i += size) {
            int[] vec = new int[size];
            for (int j = 0; j < size; j++)
                vec[j] = plain.charAt(i + j) - 'a';

            for (int row = 0; row < size; row++) {
                int sum = 0;
                for (int col = 0; col < size; col++) {
                    sum += key[row][col] * vec[col];
                }
                cipher.append((char) ('A' + (sum % 26)));
            }
        }
        return cipher.toString();
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter lowercase plaintext: ");
        String plain = sc.nextLine();

        System.out.print("Enter matrix size (2 or 3): ");
        int size = sc.nextInt();

        int[][] key = new int[size][size];
        System.out.println("Enter the " + size + "x" + size + " key matrix (integers 0–25):");
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                key[i][j] = sc.nextInt();

        String cipher = hillEncrypt(plain, key, size);
        System.out.println("[Encrypted]: " + cipher);

        // Send to server
        Socket socket = new Socket("localhost", 8020);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // Serialize key as comma-separated string
        StringBuilder keyBuilder = new StringBuilder();
        for (int[] row : key)
            for (int val : row)
                keyBuilder.append(val).append(",");

        out.write(cipher + "::" + size + "::" + keyBuilder.toString() + "\n");
        out.flush();
        socket.close();
    }
}