package connectfour.domain.johannes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import connectfour.gui.johannes.GameFrame;

public class Client
{
	public static InetAddress sendIP;
	private static int sendPort;

	public Client(int send)
	{
		Client.sendPort = send;
	}

	public static void sendIt(String s)
	{
		DatagramSocket socketSend = null;
		try
		{
			socketSend = new DatagramSocket();
			byte[] buf = s.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length,
					InetAddress.getByName(sendIP.getHostAddress()), sendPort);
			socketSend.send(packet);

			byte[] buffer = new byte[1024];
			DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
			socketSend.receive(reply);

			byte[] data = reply.getData();
			s = new String(data, 0, reply.getLength());
			handleRep(s);
			System.out.println("Client received: "
					+ reply.getAddress().getHostAddress() + " : "
					+ reply.getPort() + " - " + s);
		} catch (IOException e)
		{
			System.out.println("Couldn't send packet");
			e.printStackTrace();
		}
	}

	public static void setSendIP(String sendIP)
	{
		try
		{
			Client.sendIP = InetAddress.getByName(sendIP);
		} catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	}

	public static void handleRep(String s)
	{
		if (s.equals("ACK GAME"))
		{
			GameFrame.Instance();
		}
		if (s.equals("ACK END GAME"))
		{
			System.exit(0);
		}

	}
}
