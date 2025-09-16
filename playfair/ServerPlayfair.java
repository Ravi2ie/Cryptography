import java.io.*;
import java.net.*;
import java.util.*;

public class ServerPlayfair {
    static char[][] matrix = new char[5][5];

    public static void generateMatrix(String key) {
        key = key.toLowerCase().replaceAll("[^a-z]", "").replace("z", "x");
        Set<Character> seen = new LinkedHashSet<>();
        for (char c : key.toCharArray()) seen.add(c);
        for (char c = 'a'; c <= 'z'; c++) {
            if (c == 'z') continue;
            seen.add(c);
        }
        Iterator<Character> it = seen.iterator();
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                matrix[i][j] = it.next();
    }

    public static int[] findPosition(char c) {
        c = Character.toLowerCase(c);
        if (c == 'z') c = 'x';
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                if (matrix[i][j] == c)
                    return new int[]{i, j};
        return null;
    }

    public static String playfairDecrypt(String cipher) {
        StringBuilder plain = new StringBuilder();
        cipher = cipher.toLowerCase().replaceAll("[^a-z]", "").replace("z", "x");
        for (int i = 0; i < cipher.length(); i += 2) {
            char a = cipher.charAt(i);
            char b = cipher.charAt(i + 1);
            int[] posA = findPosition(a);
            int[] posB = findPosition(b);
            if (posA[0] == posB[0]) {
                plain.append(matrix[posA[0]][(posA[1] + 4) % 5]);
                plain.append(matrix[posB[0]][(posB[1] + 4) % 5]);
            } else if (posA[1] == posB[1]) {
                plain.append(matrix[(posA[0] + 4) % 5][posA[1]]);
                plain.append(matrix[(posB[0] + 4) % 5][posB[1]]);
            } else {
                plain.append(matrix[posA[0]][posB[1]]);
                plain.append(matrix[posB[0]][posA[1]]);
            }
        }
        return plain.toString();
    }

    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(8090);
        System.out.println("Waiting for connection...");
        Socket socket = ss.accept();
        System.out.println("Client connected.");

        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String received = br.readLine();
        String[] parts = received.split("::");
        String cipher = parts[0];
        String key = parts[1];

        generateMatrix(key);
        String plain = playfairDecrypt(cipher);

        System.out.println("[Cipher Text]: " + cipher);
        System.out.println("[Decrypted]: " + plain);

        socket.close();
        ss.close();
    }
}
