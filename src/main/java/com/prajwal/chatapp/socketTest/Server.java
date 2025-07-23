package com.prajwal.chatapp.socketTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    public Server(){
        try{
            server =new ServerSocket(7777);
            System.out.println("Server is running at port 7777");
            System.out.println("wating");
            socket = server.accept();
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream()); // auto flush ?
            StartReader();
            StartWriter();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void StartReader() {
        Runnable run1 = () -> {
            try{
                while(true){
                    String msg=br.readLine();
                    System.out.println("user2 > " + msg);
                    if(msg.equals("exit")){
                        System.out.println("user2 >  Terminated the chat" );

                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        };
        new Thread(run1).start();
    }

    private void StartWriter() {
        Scanner sc=new Scanner(System.in);
        Runnable run2=()->{
            try{
                while(true){
                   String msg=sc.nextLine();
                    System.out.println("user1 > " + msg);
                    out.println(msg);
                    out.flush();
                    if(msg.equals("exit")){
                        System.out.println("user1 >  Terminated the chat" );
                        break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        };
        new Thread(run2).start();
    }


    public static void main(String[] args) {
        System.out.println("Server Started");
        new Server();
    }
}
