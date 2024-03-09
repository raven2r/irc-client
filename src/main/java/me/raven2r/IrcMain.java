package me.raven2r;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class IrcMain {
    private static String address;
    private static String nick;
    private static String username;
    private static String realname;
    private static PrintWriter output;
    private static Scanner input;

    public static void main(String[] args) {
        try {
            var stdin = new Scanner(System.in);

            System.out.print("Enter server address: ");
            address = stdin.nextLine();
            System.out.print("Enter nickname: ");
            nick = stdin.nextLine();
            System.out.print("Enter username: ");
            username = stdin.nextLine();
            System.out.print("Enter realname: ");
            realname = stdin.nextLine();

            var socket = new Socket(address, 6667);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new Scanner(socket.getInputStream());

            writeMessage("NICK", nick);
            writeMessage("USER", username + " 0 * :" + realname);
            writeMessage("JOIN", "#main");

            while(input.hasNext()) {
                var serverMessage = input.nextLine();
                System.out.println("<<<" + serverMessage);

                if(serverMessage.startsWith("PING")) {
                    String pingContents = serverMessage.split(" ", 2)[1];
                    writeMessage("PONG", pingContents);
                    writeMessage("hello all", " every one");
                }
            }

            output.close();
            input.close();
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeMessage(String command, String message) {
        var fullMessage = command + " " + message;
        System.out.println(">>>" + fullMessage);
        output.print(fullMessage + "\r\n");
        output.flush();
    }
}