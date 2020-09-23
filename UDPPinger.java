import java.io.*;
import java.net.*;
public class UDPPinger {
	public static void sendPing(PingMessage ping, DatagramSocket socket) {
		DatagramPacket packet = new DatagramPacket(ping.getPayload().getBytes(), ping.getPayload().length(), ping.getIP(), ping.getPort());
		try {
			socket.send(packet);
		} catch (IOException IOE) {
			System.out.println(IOE + ":IO Exception Occurred");
		}
	}
	public static PingMessage receivePing(DatagramSocket socket, boolean check) {
		try {
			byte[] buffer = new byte[512];
			DatagramPacket packet = new DatagramPacket(buffer, 512);
			socket.receive(packet);
			String payload = new String(packet.getData(), packet.getOffset(), packet.getLength());
			PingMessage msg = new PingMessage(packet.getAddress(), packet.getPort(), payload);
			return msg;
		}catch(java.net.SocketTimeoutException STE) {
			if(check) {
				System.out.println(STE);
				return null;
			}else {
				return null;
			}
		}catch(java.net.PortUnreachableException PUE) {
			System.out.println(PUE);
			return new PingMessage(null, 0, "false");
		}catch(IOException IOE) {
			System.out.println(IOE);
			return null;
		}
	}
}
