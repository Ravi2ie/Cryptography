import java.io.*;
import java.net.*;

public class RailServer {

    // Decrypt function
    private static String decryptRailFence(String cipher, int key) {
        char[][] rail = new char[key][cipher.length()];
        for (int i = 0; i < key; i++)
            for (int j = 0; j < cipher.length(); j++)
                rail[i][j] = '\n';

        boolean dir_down = true;
        int row = 0, col = 0;

        for (int i = 0; i < cipher.length(); i++) {
            if (row == 0) dir_down = true;
            if (row == key - 1) dir_down = false;
            rail[row][col++] = '*';
            row += dir_down ? 1 : -1;
        }

        int index = 0;
        for (int i = 0; i < key; i++)
            for (int j = 0; j < cipher.length(); j++)
                if (rail[i][j] == '*' && index < cipher.length())
                    rail[i][j] = cipher.charAt(index++);

        StringBuilder result = new StringBuilder();
        row = 0; col = 0;
        for (int i = 0; i < cipher.length(); i++) {
            if (row == 0) dir_down = true;
            if (row == key - 1) dir_down = false;
            if (rail[row][col] != '\n')
                result.append(rail[row][col++]);
            row += dir_down ? 1 : -1;
        }
        return result.toString();
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server listening on port 5000");

            Socket socket = serverSocket.accept();
            System.out.println("Client connected");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String encryptedText = in.readLine();
            int rails = Integer.parseInt(in.readLine());

            System.out.println("Encrypted text received: " + encryptedText);
            System.out.println("Rails: " + rails);

            String decrypted = decryptRailFence(encryptedText, rails);
            System.out.println("Decrypted text: " + decrypted);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
