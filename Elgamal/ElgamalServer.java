import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ElgamalServer {

    // global parameters (can be agreed on beforehand)
    private static final long q = 353;   // large prime
    private static final long g = 3;     // primitive root

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

    private static long modInverse(long a, long m) {
        // Extended Euclidean algorithm
        long m0 = m, t, q;
        long x0 = 0, x1 = 1;
        if (m == 1) return 0;
        while (a > 1) {
            q = a / m;
            t = m; m = a % m; a = t;
            t = x0; x0 = x1 - q * x0; x1 = t;
        }
        if (x1 < 0) x1 += m0;
        return x1;
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("User B (receiver) private key x_b (<" + q + "): ");
        long xb = sc.nextLong();
        sc.close();

        long yb = modExp(g, xb, q);
        System.out.println("Public key y_b: " + yb);

        ServerSocket server = new ServerSocket(6000);
        System.out.println("Waiting for client...");

        Socket socket = server.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // Send public parameters and public key to client
        out.println(q + "," + g + "," + yb);

        // Receive ciphertext
        String[] ct = in.readLine().split(",");
        long c1 = Long.parseLong(ct[0]);
        long c2 = Long.parseLong(ct[1]);
        System.out.println("Received ciphertext: c1=" + c1 + ", c2=" + c2);

        // Decrypt
        long s = modExp(c1, xb, q);
        long m = (c2 * modInverse(s, q)) % q;
        System.out.println("Decrypted message m: " + m);

        socket.close();
        server.close();
    }
}
