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
			socket.setSoTimeout(0);
			
			byte[] buf = new byte[1024];
			byte[] data;
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			bListening = true;
			System.out.println("Socket created.");
			while (true) {
				if (bListening) {
					if(socket.isClosed())
					{
						socket = new DatagramSocket(port);
						socket.setSoTimeout(0);
					}
					
					try {
						System.out.println("Listening for incoming data...");
						socket.receive(packet);
						data = packet.getData();
						String request = new String(data, 0, packet.getLength());
						System.out.println("Server got msg: " + packet.getAddress().getHostAddress() + " : " + packet.getPort() + " - " + request);
						setSendIP(packet.getAddress());
						handleMsg(request);
					} catch (Exception e) {
						System.out.println("Exception in run(): " + e.getMessage());
					}
					
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void handleMsg(String s) {
		if (s.equals("NEW GAME") && !ingame) {
			sendIt("ACK NEW GAME");
			ingame = true;
			GameFrame.Instance();
		}
		else if (s.equals("ACK NEW GAME")) {
			ingame = true;
			GameFrame.Instance();
		}
//		-------------------------------------------
		else if (s.startsWith("MOVE", 4)) {
			System.out.println("he want's to place " + s.charAt(s.length()-1));
			GameFrame.Instance().move(Integer.parseInt(s.substring(s.length()-1)));
			sendIt("ACK MOVE " + s.charAt(s.length()-1));
		}
		else if (s.startsWith("ACK MOVE", 8)) {
			System.out.println("It got placed");
		}
//		-------------------------------------------
		else if (s.equals("END GAME") && ingame) {
			sendIt("ACK END GAME");
			System.exit(0);
		}
		else if (s.equals("ACK END GAME")) {
			System.exit(0);
		}
	}
	
	public static void sendMsg(MESSAGE msg, String msgString)
	{
		switch(msg)
		{
		case NEWGAME: sendIt("NEW GAME");
		case ENDGAME: sendIt("ACK END GAME");
		case MOVECOLUMN: sendIt("MOVE " + msgString);
		default:
			break;
		
		}
	}

	private static void sendIt(String s) {
		socket.close();
		bListening = false;
		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(listentimeout_ms);
			byte[] buf = s.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, sendIP, port);
			System.out.println("Sending message (" + s + ")...");
			socket.send(packet);

			byte[] buffer = new byte[1024];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			System.out.println("Waiting for reply on message (" + s + ")...");
			socket.receive(reply);

			byte[] data = reply.getData();
			s = new String(data, 0, reply.getLength());
			handleMsg(s);
			System.out.println("Client received: " + reply.getAddress().getHostAddress() + " : " + reply.getPort() + " - " + s);
		} catch (IOException e) {
			System.out.println("Exception in sendIt(String s): " + e.getMessage());
		}
		socket.close();
		bListening = true;
	}

	public static void setSendIP(InetAddress _sendIP) {
		sendIP = _sendIP;
		System.out.println("Setting new sendIP: " + sendIP.getHostAddress());
	}
}
