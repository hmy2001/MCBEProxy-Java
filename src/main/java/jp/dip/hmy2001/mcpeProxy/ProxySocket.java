package jp.dip.hmy2001.mcpeProxy;

import jp.dip.hmy2001.mcpeProxy.utils.CommandReader;

import java.net.*;

public class ProxySocket extends Thread{
    private DatagramSocket proxySocket;
    private boolean isRunning;

    public ProxySocket(int bindPort){
        try {
            proxySocket = new DatagramSocket(bindPort);

            isRunning = true;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void run() {
        while (isRunning){
            byte[] buffer = new byte[65507];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            try {
                proxySocket.receive(packet);

                DatagramPacket sendPacket = new DatagramPacket(buffer, packet.getLength());
                SessionManager.getInstance().sendServer(sendPacket, packet.getAddress().getHostAddress(), packet.getPort());
            }catch (Exception e) {
                //e.printStackTrace();//TODO
            }
        }
    }

    public void sendClient(DatagramPacket packet){
        try {
            proxySocket.send(packet);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void close(){
        isRunning = false;

        try {
            proxySocket.close();

            CommandReader.getInstance().stashLine();
            System.out.println("Closed Socket.");
            CommandReader.getInstance().unstashLine();
        }catch (Exception ignored){

        }
    }

}
