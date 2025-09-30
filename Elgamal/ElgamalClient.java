import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ElgamalClient {

    private static long modExp(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = (result * base) % mod;
            exp >>= 1;
            base = (base * base) % mod;
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        // Sender's own private key (not strictly needed for basic encryption,
        // but read for completeness)
        System.out.print("User A (sender) private key x_a (<353): ");
        long xa = sc.nextLong();

        System.out.print("Plaintext message m (<353): ");
        long m = sc.nextLong();
        System.out.print("Random k (1..q-2): ");
        long k = sc.nextLong();
        sc.close();

        Socket socket = new Socket("localhost", 6000);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Receive public parameters from server
        String[] params = in.readLine().split(",");
        long q = Long.parseLong(params[0]);
        long g = Long.parseLong(params[1]);
        long yb = Long.parseLong(params[2]);

        System.out.println("Received q=" + q + ", g=" + g + ", y_b=" + yb);

        // Encrypt
        long c1 = modExp(g, k, q);
        long c2 = (m * modExp(yb, k, q)) % q;
        System.out.println("Sending ciphertext: c1=" + c1 + ", c2=" + c2);

        // Send ciphertext to server
        out.println(c1 + "," + c2);

        socket.close();
    }
}
