import java.io.*;
import java.util.*;

public class primeNo{
    public static void main(String args[]){
        Scanner s = new Scanner(System.in);
        System.out.println("Enter a Number");
        int result = s.nextInt();
        boolean valid = true;
        for(int i=2; i<result; i++){
            if(result%i==0) {
                valid = false;
                System.out.println("It is not a Prime Number");
                break;
                }
        }
        if(valid) System.out.println("It is a Prime Number");
    }
}