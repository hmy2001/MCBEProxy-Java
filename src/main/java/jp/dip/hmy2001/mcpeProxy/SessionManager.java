package jp.dip.hmy2001.mcpeProxy;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.*;

public class SessionManager extends Thread{
    private static SessionManager instance = null;
    private boolean isRunning;
    private final ProxySocket proxySocket;
    private final Map<String, Session> sessions = new HashMap<>();
    private InetAddress serverAddress;
    private final int serverPort;

    public SessionManager(int bindPort, String serverAddress, int serverPort){
        isRunning = true;

        try {
            this.serverAddress = InetAddress.getByName(serverAddress);
        }catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("Proxy will connect to " + serverAddress + ":" + serverPort);

        this.serverPort = serverPort;

        instance = this;

        proxySocket = new ProxySocket(bindPort);
        proxySocket.start();
    }

    public static SessionManager getInstance(){
        return instance;
    }

    public void run(){
        while (isRunning){
            try {
                Thread.sleep(500);
            }catch (Exception ignored){

            }

            if(!sessions.isEmpty()){
                for (Session session : new ArrayList<>(sessions.values())) {
                    if(!session.isConnected()){
                        session.close();
                    }
                }
            }
        }
    }

    public void sendServer(DatagramPacket packet, String ip, int port){
        packet.setAddress(serverAddress);
        packet.setPort(serverPort);
        getSession(ip, port).sendServer(packet);
    }

    public void sendClient(DatagramPacket packet){
        proxySocket.sendClient(packet);
    }

    public Session getSession(String ip, int port) {
        String id = ip + ":" + port;
        if (!this.sessions.containsKey(id)) {
            Session session = new Session(ip, port);
            session.connect(serverAddress, serverPort);
            session.start();

            this.sessions.put(id, session);

            return session;
        }

        return this.sessions.get(id);
    }


    public void shutdown(){
        isRunning = false;

        for (Session session : new ArrayList<>(this.sessions.values())) {
            session.close();
        }

        proxySocket.close();
    }

}
