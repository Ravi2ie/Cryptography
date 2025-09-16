import java.io.*;
import java.net.*;

public class ServerVignere{
    public static String vignereDecrypt(String cipher,String key){
        StringBuilder res=new StringBuilder();
        int n=cipher.length();

        for(int i=0;i<n;i++){
            char c=cipher.charAt(i);
            char k=key.charAt(i%key.length());
            int decryptedChar=(c-'A'-(k-'a')+26)%26;
            res.append((char)('a'+decryptedChar));
        }
        return res.toString();
    }
    public static void main(String[] args)throws IOException{
        ServerSocket ss=new ServerSocket(8080);
        System.out.println("Server started, waiting for connection...");
        Socket socket=ss.accept();
        System.out.println("Client connected.");
        BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String received=br.readLine();
        String[] parts=received.split("::");
        String cipher=parts[0];
        String key=parts[1];

        System.out.println("[Cipher Text]:"+cipher);
        String plain=vignereDecrypt(cipher,key);
        System.out.println("[Plain Text]:"+plain);
        socket.close();
        ss.close();
    }
}
