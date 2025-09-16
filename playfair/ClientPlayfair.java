import java.io.*;
import java.net.*;
import java.util.*;

public class ClientPlayfair {
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

    public static String formatPlain(String plain) {
        plain = plain.toLowerCase().replaceAll("[^a-z]", "").replace("z", "x");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < plain.length(); i++) {
            char a = plain.charAt(i);
            sb.append(a);
            if (i + 1 < plain.length() && plain.charAt(i + 1) == a) {
                sb.append('x');
            }
        }
        if (sb.length() % 2 != 0) sb.append('x');
        return sb.toString();
    }

    public static String playfairEncrypt(String plain) {
        StringBuilder cipher = new StringBuilder();
        plain = formatPlain(plain);
        for (int i = 0; i < plain.length(); i += 2) {
            char a = plain.charAt(i);
            char b = plain.charAt(i + 1);
            int[] posA = findPosition(a);
            int[] posB = findPosition(b);
            if (posA[0] == posB[0]) {
                cipher.append(matrix[posA[0]][(posA[1] + 1) % 5]);
                cipher.append(matrix[posB[0]][(posB[1] + 1) % 5]);
            } else if (posA[1] == posB[1]) {
                cipher.append(matrix[(posA[0] + 1) % 5][posA[1]]);
                cipher.append(matrix[(posB[0] + 1) % 5][posB[1]]);
            } else {
                cipher.append(matrix[posA[0]][posB[1]]);
                cipher.append(matrix[posB[0]][posA[1]]);
            }
        }
        return cipher.toString().toUpperCase();
    }

    public static int[] findPosition(char c) {
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                if (matrix[i][j] == c)
                    return new int[]{i, j};
        return null;
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter plaintext: ");
        String plain = sc.nextLine();
        System.out.print("Enter key: ");
        String key = sc.nextLine();

        generateMatrix(key);
        String cipher = playfairEncrypt(plain);
        System.out.println("[Encrypted]: " + cipher);

        Socket socket = new Socket("localhost", 8090);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        out.write(cipher + "::" + key + "\n");
        out.flush();
        socket.close();
    }
}
