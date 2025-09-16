import java.io.*;
import java.net.*;
import java.util.Scanner;

public class RailClient {

    // Encrypt function
    private static String encryptRailFence(String text, int key) {
        char[][] rail = new char[key][text.length()];
        for (int i = 0; i < key; i++)
            for (int j = 0; j < text.length(); j++)
                rail[i][j] = '\n';

        boolean dir_down = false;
        int row = 0, col = 0;

        for (int i = 0; i < text.length(); i++) {
            if (row == 0 || row == key - 1)
                dir_down = !dir_down;
            rail[row][col++] = text.charAt(i);
            row += dir_down ? 1 : -1;
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < key; i++)
            for (int j = 0; j < text.length(); j++)
                if (rail[i][j] != '\n') result.append(rail[i][j]);

        return result.toString();
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            Scanner sc = new Scanner(System.in);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            System.out.print("Enter plaintext: ");
            String plaintext = sc.nextLine().replaceAll("\\s+", ""); // remove spaces

            System.out.print("Enter number of rails: ");
            int rails = sc.nextInt();

            String encrypted = encryptRailFence(plaintext, rails);
            System.out.println("Encrypted Text: " + encrypted);

            out.println(encrypted);
            out.println(rails);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
