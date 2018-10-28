package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**A server socket to recieve messages from the C++ side
 * @author Edwin Fennell
 */

public class RServerSocket {

    public static void main(String args[]) throws Exception{
        RServerSocket SERVER = new RServerSocket();
        SERVER.run();
    }

    public void run() throws Exception {

        ServerSocket SRVSOCK = new ServerSocket(1337);
        Socket SOCK = SRVSOCK.accept();

        InputStreamReader IR = new InputStreamReader(SOCK.getInputStream());
        BufferedReader BR = new BufferedReader(IR);

        String MESSAGE = BR.readLine();
        System.out.println(MESSAGE);

                if(MESSAGE != null){
                    PrintStream PS = new PrintStream(SOCK.getOutputStream());
                    PS.println("MESSAGE RECIEVED");
                }
    }

}
