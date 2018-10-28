package util;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.io.PrintStream;
import java.io.BufferedReader;

/**A client socket to send messages to the C++ side
 * @author Edwin Fennell
 */

public class RClientSocket {


    public static void main(String[] args) throws Exception{
        RClientSocket CLIENT = new RClientSocket();
        CLIENT.run(args);
    }

    public void run(String[] messages) throws Exception{
        Socket SOCK = new Socket("localhost",1337);
        PrintStream PS = new PrintStream(SOCK.getOutputStream());
            PS.println(messages[0]);

        InputStreamReader IR = new InputStreamReader(SOCK.getInputStream());
        BufferedReader BR = new BufferedReader(IR);

        String MESSAGE = BR.readLine();
        System.out.println(MESSAGE);
    }
}
