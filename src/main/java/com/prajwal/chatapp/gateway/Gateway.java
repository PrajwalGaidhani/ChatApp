package com.prajwal.chatapp.service;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Gateway {

    private static final int PORT = 7777; // Port for this gateway
    public static final ConcurrentHashMap<String, Socket> connectedClients = new ConcurrentHashMap<>();
    public boolean sendMessageToUser(String targetUserId, String message) {
        Socket socket = connectedClients.get(targetUserId);
        if (socket != null && !socket.isClosed()) {
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(message);
                return true;
            } catch (Exception e) {
                System.out.println("Failed to send message to user: " + targetUserId);
                e.printStackTrace();
            }
        }
        return false;
    }
    public static void main(String[] args) {
        System.out.println("Gateway started on port " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                // Start a thread to handle this client
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class to handle each client
    static class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String userId;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // First message from client is its userId
                this.userId = in.readLine();
                connectedClients.put(userId, socket);
                System.out.println("User registered: " + userId);

                String msg;
                while ((msg = in.readLine()) != null) {
                    if (msg.equalsIgnoreCase("exit")) {
                        break;
                    }
                    System.out.println("Message from " + userId + ": " + msg);

                    // simulate send to SessionService
                    handleMessage(userId, msg);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                connectedClients.remove(userId);
                System.out.println("User disconnected: " + userId);
                try { socket.close(); } catch (IOException ignored) {}
            }
        }

        // Simulated message routing logic (replace with real SessionService later)
        private void handleMessage(String fromUserId, String fullMsg) {
            // Example message format: "user4|hello"
            String[] parts = fullMsg.split("\\|", 2);
            if (parts.length != 2) {
                out.println("Invalid format. Use: recipientId|message");
                return;
            }

            String recipientId = parts[0];
            String msg = parts[1];

            Socket recipientSocket = connectedClients.get(recipientId);
            if (recipientSocket != null) {
                try {
                    PrintWriter recipientOut = new PrintWriter(recipientSocket.getOutputStream(), true);
                    recipientOut.println("From " + fromUserId + ": " + msg);
                } catch (IOException e) {
                    out.println("Error sending message to " + recipientId);
                }
            } else {
                // simulate storing for offline delivery
                out.println("User " + recipientId + " is offline. Storing for later.");
                // Here, you would forward it to SessionService to store the message.
            }
        }

    }
}
