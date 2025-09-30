import java.io.*;
import java.net.*;
import java.util.Scanner;

public class DHServer {

    // ----- Global parameters -----
    private static final long q = 353;    // large prime
    private static final long a = 3;      // primitive root of q

    // fast modular exponentiation
    private static long modExp(long base, long exp, long mod) {
        long result = 1;
        base = base % mod;
        while (exp > 0) {
            if ((exp & 1) == 1) {
                result = (result * base) % mod;
            }
            exp >>= 1;
            base = (base * base) % mod;
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter User A's private key (xa < " + q + "): ");
        long xa = sc.nextLong();
        sc.close();

        long ya = modExp(a, xa, q);   // Server's public key

        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Server ready on port 5000");
        System.out.println("Public parameters: q=" + q + ", a=" + a);
        System.out.println("Server public key (ya): " + ya);

        Socket socket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // send q, a, ya to client
        out.println(q + "," + a + "," + ya);

        // receive client's public key
        long yb = Long.parseLong(in.readLine());
        System.out.println("Received client public key (yb): " + yb);

        // compute shared secret
        long kServer = modExp(yb, xa, q);
        System.out.println("Server computed secret key: " + kServer);

        socket.close();
        serverSocket.close();
    }
}
