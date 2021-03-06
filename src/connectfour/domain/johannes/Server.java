package connectfour.domain.johannes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;

import sun.nio.cs.ext.ISCII91;

import connectfour.gui.johannes.GameFrame;

public class Server implements Runnable
{
//	private HashMap<String, String> protocol = new HashMap<String, String>();
	
	public static InetAddress sendIP;
	private static int receivePort;

	private static boolean ingame = false;

	public Server(int receive)
	{
//		protocol.put("NEW GAME", "ACK NEW GAME");
//		protocol.put("END GAME", "ACK END GAME");
//		protocol.put("NEW GAME", "ACK NEW GAME");
		Server.receivePort = receive;
		this.run();
	}

	public static String getLocIp()
	{
		String ip = "";
		try
		{
			ip = InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e)
		{
			System.out.println("Could not receive local ip");
			e.printStackTrace();
		}
		return ip;
	}

	@Override
	public void run()
	{
		DatagramSocket socketListen = null;
		try
		{
			socketListen = new DatagramSocket(receivePort);

			byte[] buf = new byte[1024];
			byte[] data;
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			System.out
					.println("Server socket created. Waiting for incoming data...");
			while (true)
			{
				socketListen.receive(packet);
				data = packet.getData();
				String request = new String(data, 0, packet.getLength());
				System.out.println("Server got msg: "
						+ packet.getAddress().getHostAddress() + " : "
						+ packet.getPort() + " - " + request);
				request = handleMsg(request);
				DatagramPacket dp = new DatagramPacket(request.getBytes(),
						request.getBytes().length, packet.getAddress(),
						packet.getPort());
				socketListen.send(dp);
				if(request.equals("ACK END GAME")) System.exit(0);
			}

		} catch (SocketException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	private String handleMsg(String req)
	{
		if (req.equals("NEW GAME") && !ingame)
		{
			ingame = true;
			GameFrame.Instance();
			return "ACK GAME";
		}
		if (req.startsWith("PLACE", 4))
		{
			System.out.println("he want's to place");
			return "you can place";
		}
		if (req.equals("END GAME") && ingame)
		{
			return "ACK END GAME";
		} else
			return "";

	}

}
