package edu.ncepu.yutao.Network;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Created by AUTOY on 2016/4/30.
 */
public class Connection {
    public static final String SHUT_DOWN = "ShutDownTheThreadDoYouHearMe!!!!!";
    private static InetAddress address = null;
    private static Vector<InetAddress> ipAddresses = new Vector<>();

    public static boolean initializeNetwork() {
        try {
            Enumeration allNetInterfaces = java.net.NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                java.net.NetworkInterface netInterface = (java.net.NetworkInterface) allNetInterfaces.nextElement();
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = (InetAddress) addresses.nextElement();
                    ipAddresses.add(ip);
                    if (ip != null && ip instanceof Inet4Address && !ip.isLoopbackAddress()) {
                        address = ip;
                        System.out.println("本机的IP = " + ip.getHostAddress());
                    }
                }
            }
            if (address == null) {
                address = Inet4Address.getLoopbackAddress();
                System.out.println("系统处于本机工作状态");
                return false;
            }
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static InetAddress getAddress() {
        return address;
    }

    public static void sendMulticastMessage(String message, MulticastSocket socket, InetAddress group, int port) throws IOException {
        byte[] bytes = message.getBytes("UTF8");
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length, group, port);
        socket.send(dp);
    }

    public static void sendDatagramMessage(String message, DatagramSocket socket, InetAddress address, int port) throws IOException {
        byte[] bytes = message.getBytes("UTF8");
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length, address, port);
        socket.send(dp);
    }
}


