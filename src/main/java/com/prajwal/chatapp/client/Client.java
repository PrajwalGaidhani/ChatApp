// File: Client.java
package com.prajwal.chatapp.service;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public Client(String gatewayHost, int gatewayPort) {
        try {
            System.out.println("Connecting to Gateway...");
            socket = new Socket(gatewayHost, gatewayPort);
            System.out.println("Connected to Gateway.");

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            startReading();
            startWriting();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReading() {
        Thread readerThread = new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println("Received: " + line);
                }
            } catch (IOException e) {
                System.out.println("Connection closed.");
            }
        });
        readerThread.start();
    }

    private void startWriting() {
        Thread writerThread = new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while (true) {
                String msg = sc.nextLine();
                out.println(msg);
                if ("exit".equalsIgnoreCase(msg)) {
                    break;
                }
            }
        });
        writerThread.start();
    }

    public static void main(String[] args) {
        new Client("localhost", 9001); // Connects to Gateway 1
    }
}
