package it.polimi.ingsw.network.socket;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient implements Closeable {


    private String ip;
    private int port;
    private Socket connection;
    private BufferedReader in;
    private PrintWriter out;


    public SocketClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void startClient() throws IOException {
        connection = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out = new PrintWriter(connection.getOutputStream(), true);
    }

    public String receive() throws IOException {
        return in.readLine();
    }

    public void send(String message) {
        out.println(message);
    }

    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        connection.close();
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Missing host::port");
            return;
        }

        String[] tokens = args[0].split("::");

        if (tokens.length < 2) {
            throw new IllegalArgumentException("Bad formatting: " + args[0]);
        }

        String host = tokens[0];
        int port = Integer.parseInt(tokens[1]);

        SocketClient2 socketClient2 = new SocketClient2(host, port);
        Scanner fromKeyboard = new Scanner(System.in);

        try{
            socketClient2.startClient();
            String msg = null;

            do {
                System.out.println(">>> ");
                String toSend = fromKeyboard.nextLine();
                socketClient2.send(toSend);
                msg = socketClient2.receive();
                System.out.println(msg);
            } while (msg != null);
        } catch(IOException e) {
            e.getMessage();
        } finally {
            try {
                socketClient2.close();
            } catch(IOException e) {
                e.getMessage();
            }
        }

    }
}
