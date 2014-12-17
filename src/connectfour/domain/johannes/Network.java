package connectfour.domain.johannes;

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
	private static int port;

	private static boolean ingame = false;
	private static boolean bListening = false;
	
	static DatagramSocket socket = null;
	
	private static int listentimeout_ms = 2000;

	public Network(int receive) {
		// protocol.put("NEW GAME", "ACK NEW GAME");
		// protocol.put("END GAME", "ACK END GAME");
		// protocol.put("NEW GAME", "ACK NEW GAME");
		Network.port = receive;
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
		try {
			socket = new DatagramSocket(port);
			socket.setSoTimeout(listentimeout_ms);
			
			byte[] buf = new byte[1024];
			byte[] data;
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			bListening = true;
			System.out.println("Server socket created.");
			while (true) {
				if (bListening) {
					if(socket.isClosed())
					{
						socket = new DatagramSocket(port);
						socket.setSoTimeout(listentimeout_ms);
					}
					
					if(socket.isConnected())
					{
						System.out.println("Waiting for incoming data...");
						socket.receive(packet);
						data = packet.getData();
						String request = new String(data, 0, packet.getLength());
						System.out.println("Server got msg: " + packet.getAddress().getHostAddress() + " : " + packet.getPort() + " - " + request);
						request = handleMsg(request);
						DatagramPacket dp = new DatagramPacket(request.getBytes(), request.getBytes().length, packet.getAddress(), packet.getPort());
						socket.send(dp);
						if (request.equals("ACK END GAME"))
							System.exit(0);
					}
				}
			}

		} catch (SocketException e) {
			if(!bListening)
			{
				System.out.println("Error when waiting for incoming data.");
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String handleMsg(String req) {
		if (req.equals("NEW GAME") && !ingame) {
			ingame = true;
			GameFrame.Instance();
			return "ACK GAME";
		}
		if (req.startsWith("PLACE", 4)) {
			System.out.println("he want's to place");
			return "you can place";
		}
		if (req.equals("END GAME") && ingame) {
			return "ACK END GAME";
		} else
			return "";
	}

	public static void sendIt(String s) {
		socket.close();
		bListening = false;
		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(listentimeout_ms);
			byte[] buf = s.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(sendIP.getHostAddress()), port);
			System.out.println("Sending...");
			socket.send(packet);

			byte[] buffer = new byte[1024];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			System.out.println("Waiting for reply...");
			socket.receive(reply);

			byte[] data = reply.getData();
			s = new String(data, 0, reply.getLength());
			handleRep(s);
			System.out.println("Client received: " + reply.getAddress().getHostAddress() + " : " + reply.getPort() + " - " + s);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		socket.close();
		bListening = true;
	}

	public static void setSendIP(String _sendIP) {
		try {
			sendIP = InetAddress.getByName(_sendIP);
		} catch (UnknownHostException e) {
			System.out.println("Could not connect to IP: " + _sendIP);
		}
	}

	public static void handleRep(String s) {
		if (s.equals("ACK GAME")) {
			GameFrame.Instance();
		}
		if (s.equals("ACK END GAME")) {
			System.exit(0);
		}
	}

}
