package com.dupeclient;

import java.net.*;

public class Proxy {
    private DatagramSocket local;
    private DatagramSocket remote;
    private InetAddress target;
    private int targetPort;
    private volatile boolean running;
    private long guid = (long)(Math.random() * Long.MAX_VALUE);

    public void start(String host, int port, int localPort) throws Exception {
        target = InetAddress.getByName(host);
        targetPort = port;
        local = new DatagramSocket(localPort);
        remote = new DatagramSocket();
        running = true;

        // handshake inicial al server
        byte[] req = RakNet.connectionRequest(guid, System.currentTimeMillis());
        remote.send(new DatagramPacket(req, req.length, target, targetPort));

        new Thread(this::pumpDown).start(); // juego -> server
        new Thread(this::pumpUp).start();   // server -> juego
    }

    private void pumpDown() {
        byte[] buf = new byte[2048];
        try {
            while (running) {
                DatagramPacket p = new DatagramPacket(buf, buf.length);
                local.receive(p);
                // aquí luego interceptamos umm copilot nada raro e nada raro umm NoPacket
                remote.send(new DatagramPacket(p.getData(), p.getLength(), target, targetPort));
            }
        } catch (Exception e) { }
    }

    private void pumpUp() {
        byte[] buf = new byte[2048];
        try {
            while (running) {
                DatagramPacket p = new DatagramPacket(buf, buf.length);
                remote.receive(p);
                local.send(new DatagramPacket(p.getData(), p.getLength(),
                        p.getAddress(), p.getPort()));
            }
        } catch (Exception e) { }
    }

    public void stop() {
        running = false;
        if (local != null) local.close();
        if (remote != null) remote.close();
    }
                                       }
