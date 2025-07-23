package com.prajwal.chatapp.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Client() {
        try {
            System.out.println("Connecting to server...");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connected to server.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true); // autoFlush = true

            StartReader();
            StartWriter();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void StartReader() {
        Runnable reader = () -> {
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg == null || msg.equals("exit")) {
                        System.out.println("Server terminated the chat.");
                        break;
                    }
                    System.out.println("user1 > " + msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(reader).start();
    }

    private void StartWriter() {
        Scanner sc = new Scanner(System.in);
        Runnable writer = () -> {
            try {
                while (true) {
                    String msg = sc.nextLine();
                    out.println(msg);
                    if (msg.equals("exit")) {
                        System.out.println("You terminated the chat.");
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        new Thread(writer).start();
    }

    public static void main(String[] args) {
        new Client();
    }
}
