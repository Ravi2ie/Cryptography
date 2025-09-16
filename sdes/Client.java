package sdes;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (
            Socket socket = new Socket("localhost", 5000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner scanner = new Scanner(System.in)
        ) {
            String key10bit;
            while (true) {
                System.out.print("Enter 10-bit key: ");
                key10bit = scanner.nextLine();
                if (key10bit.matches("[01]{10}")) break;
                System.out.println("Invalid key. Must be 10-bit binary.");
            }

            String plaintext;
            while (true) {
                System.out.print("Enter 8-bit plaintext: ");
                plaintext = scanner.nextLine();
                if (plaintext.matches("[01]{8}")) break;
                System.out.println("Invalid plaintext. Must be 8-bit binary.");
            }

            // Send key and plaintext
            out.println(key10bit);
            out.println(plaintext);

            // Receive encrypted text and subkeys
            String encrypted = in.readLine();
            String k1 = in.readLine();
            String k2 = in.readLine();

            System.out.println("Encrypted text from server: " + encrypted);
            System.out.println("Subkey K1: " + k1);
            System.out.println("Subkey K2: " + k2);

            // Decrypt using subkeys in reverse order
            String decrypted = SDES.decrypt(encrypted, k1, k2);
            System.out.println("Decrypted text: " + decrypted);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
