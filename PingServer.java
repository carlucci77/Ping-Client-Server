import java.io.*;
import java.net.*;
import java.util.*;
public class PingServer {
	public static void main(String[] args) {
		PingServer server = new PingServer();
		server.run();
	}
	public void run() {
		int port = 5538;
		double lossRate = 0.3;
		long avgDelay = 100;
		try {
			DatagramSocket udpSocket = new DatagramSocket(port);
			System.out.println("Ping Server running....");
			byte[] buffer = new byte[512];
			while(true) {
				DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
				System.out.println("Waiting for UDP packet....");
				udpSocket.receive(inPacket);
				String payload = new String(inPacket.getData(), inPacket.getOffset(), inPacket.getLength());
				System.out.println("Received from: " + inPacket.getAddress() + " " + payload.trim());
				Random random = new Random(new Date().getTime());
				double num = random.nextDouble();
				if(num > lossRate) {
					DatagramPacket outPacket = new DatagramPacket(inPacket.getData(), inPacket.getData().length, inPacket.getAddress(), inPacket.getPort());
					num = random.nextDouble();
					int temp = (int) (num * 2 * avgDelay);
					Thread.sleep(temp);
					udpSocket.send(outPacket);
					System.out.println("Reply sent.");
				}else {
					System.out.println("Packet loss.... reply not sent.");
				}
			}
		}catch(SocketException SE) {
			System.out.println(SE);
			System.out.println("Shutting Server");
		}catch(IOException IOE) {
			System.out.println(IOE);
			System.out.println("Shutting Server");
		}catch(InterruptedException IE) {
			System.out.println(IE);
			System.out.println("Shutting Server");
		}
	}
}
