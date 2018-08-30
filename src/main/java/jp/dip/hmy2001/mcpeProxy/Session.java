package jp.dip.hmy2001.mcpeProxy;

import jp.dip.hmy2001.mcpeProxy.utils.CommandReader;

import java.net.*;

public class Session extends Thread{
    private boolean isRunning = false;
    private DatagramSocket serverSocket;
    private InetAddress ip;
    private int port;

    public Session(String ip, int port){
        isRunning = true;

        try {
            this.ip = InetAddress.getByName(ip);
        }catch (Exception e){
            e.printStackTrace();
        }

        this.port = port;

        try {
            serverSocket = new DatagramSocket(null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isConnected(){//TODO: Raknet
        return serverSocket.isConnected();
    }

    public void connect(InetAddress ip, int port){
        try {
            CommandReader.getInstance().stashLine();

            System.out.println("Connected to " + ip.getHostName() + ":" + port);
            CommandReader.getInstance().unstashLine();

            serverSocket.connect(ip, port);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run() {
        while (isRunning){
            byte buffer[] = new byte[65507];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            try {
                serverSocket.receive(packet);

                DatagramPacket sendPacket = new DatagramPacket(buffer, packet.getLength());

                sendPacket.setAddress(ip);
                sendPacket.setPort(port);

                SessionManager.getInstance().sendClient(sendPacket);
            }catch (Exception e) {
                //e.printStackTrace();//TODO
            }
        }
    }

    public void sendServer(DatagramPacket packet){
        try {
            serverSocket.send(packet);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void close(){
        try{
            isRunning = false;

            CommandReader.getInstance().stashLine();
            System.out.println("Closed Session");
            CommandReader.getInstance().unstashLine();

            serverSocket.close();
        }catch (Exception e){

        }
    }

}
