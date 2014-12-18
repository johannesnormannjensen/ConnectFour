package connectfour.domain.johannes;

import java.awt.Color;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

import jdk.internal.jfr.events.SocketReadEvent;
import sun.nio.cs.ext.ISCII91;
import connectfour.gui.johannes.GameFrame;

public class Network implements Runnable {
	// private HashMap<String, String> protocol = new HashMap<String, String>();

	public static InetAddress sendIP;
	private static final int port = 1337;

	private static boolean ingame = false;
	private static boolean bListening = false;
	
	private static int listentimeout_ms = 5000;

	public Network() {
		this.run();
	}

	public static String getLocIp() {
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			System.out.println("Could not receive local ip");
			e.printStackTrace();
		}
		return ip;
	}

	@Override
	public void run() {
		bListening = true;
		System.out.println("Socket created.");
		while (true) {
			if (bListening) {
//					if(socket.isClosed())
//					{
//						socket = new DatagramSocket(null);
//						socket.setReuseAddress(true);
//						socket.setSoTimeout(listentimeout_ms);
//					}
				
				try {
					DatagramSocket socket = new DatagramSocket();
					socket.setSoTimeout(listentimeout_ms);
					
					byte[] buf = new byte[1024];
					byte[] data;
					DatagramPacket packet = new DatagramPacket(buf, buf.length, sendIP, port);
					System.out.println("Listening for incoming data...");
					socket.receive(packet);
					Thread.currentThread().sleep(1000);
					data = packet.getData();
					String request = new String(data, 0, packet.getLength());
					System.out.println("Server got msg: " + packet.getAddress().getHostAddress() + " : " + packet.getPort() + " - " + request);
					setSendIP(packet.getAddress().getHostAddress());
					socket.close();
					handleMsg(request);
				} catch (Exception e) {
					System.out.println("Exception in run(): " + e.getMessage());
				}
				
			}
		}

	}

	private static void handleMsg(String s) {
		if (s.equals("NEW GAME") && !ingame) {
			sendIt("ACK NEW GAME", false);
			ingame = true;
			GameFrame.Instance().initialize("X", Color.BLUE, "O", Color.RED);
		}
		if (s.equals("ACK NEW GAME")) {
			ingame = true;
			GameFrame.Instance().initialize("O", Color.RED, "X", Color.BLUE);
		}
//		-------------------------------------------
		if (s.startsWith("MOVE")) {
			System.out.println("he want's to place " + s.substring(s.length()-2).trim());
			GameFrame.Instance().move(Integer.parseInt(s.substring(s.length()-2).trim()), false);
			sendIt("ACK MOVE " + s.substring(s.length()-2).trim(), false);
		}
		if (s.startsWith("ACK MOVE")) {
			System.out.println("It got placed");
		}
//		-------------------------------------------
		if (s.equals("END GAME") && ingame) {
			sendIt("ACK END GAME", false);
			System.exit(0);
		}
		if (s.equals("ACK END GAME")) {
			System.exit(0);
		}
	}
	
	public static void sendMsg(MESSAGE msg, String msgString)
	{
		switch(msg)
		{
		case NEWGAME: sendIt("NEW GAME", true); break;
		case ENDGAME: sendIt("END GAME", true); break;
		case MOVE: sendIt("MOVE " + msgString, true); break;
		default:
			break;
		
		}
	}

	private static void sendIt(String s, boolean waitforreply) {
//		socket.close();
		bListening = false;
		try {
			DatagramSocket socket = new DatagramSocket();
			socket.setSoTimeout(listentimeout_ms);
			byte[] buf = s.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, sendIP, port);
			System.out.println("Sending message (" + s + ")..." + " to " + sendIP + " " + port);
			socket.send(packet);
			socket.close();
			bListening = true;
			
		} catch (IOException e) {
			System.out.println("Exception in sendIt(String s): " + e.getMessage());
		}
		
	}

	public static void setSendIP(String _sendIP) {
		try {
			sendIP = InetAddress.getByName(_sendIP);
			System.out.println("Setting new sendIP: " + sendIP + " (from " + _sendIP + ")");
		} catch (UnknownHostException e) {
			System.out.println("Could not connect to IP: " + _sendIP);
		}
	}
}
