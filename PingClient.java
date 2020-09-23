import java.net.*;
import java.util.*;
import java.text.*;
public class PingClient extends UDPPinger{
	public static void main(String[] args) {
		run();
	}
	public static void run(){
		try {
			DatagramSocket socket = new DatagramSocket();
			socket.setSoTimeout(1000);
			//InetAddress addr = InetAddress.getByName("constance.cs.rutgers.edu");
			InetAddress addr = InetAddress.getLocalHost();
			int port = 5538;
			socket.connect(addr, port);
			System.out.println("Contacting host: " + addr + " at port " + port);
			long[] start = new long[10];
			long[] end = new long[10];
			long sum = 0;
			long min = 0;
			long max = 0;
			int count = 0;
			for(int k = 0; k < 10; k++) {
				Date date = new Date();
				long time = date.getTime();
				String payload = "PING " + k + " " + time;
				PingMessage msg = new PingMessage(socket.getInetAddress(), socket.getPort(), payload);
				sendPing(msg, socket);
				PingMessage response = receivePing(socket, true);
				date = new Date();
				long time2 = date.getTime();
				start[k] = time;
				if(response != null) {
					if(response.getPayload().equals("false")) {
						System.out.println("Closing Client");
						return;
					}
					int num = Integer.parseInt(response.getPayload().substring(5, 6));
					end[num] = time2;
					SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zz yyyy");
					String dateString = format.format(date);
					System.out.println("Received packet from " + response.getIP() + " " + response.getPort() + " " + dateString);
					count++;
				}else {
					end[k] = 0;
				}
			}
			socket.setSoTimeout(5000);
			while(count != 10) {
				try {
					PingMessage response = receivePing(socket, false);
					if(response == null) {
						break;
					}
					Date date = new Date();
					long time = date.getTime();
					int num = Integer.parseInt(response.getPayload().substring(5, 6));
					end[num] = time;
					System.out.println("Received packet from " + response.getIP() + " " + response.getPort() + " " + time);
					count++;
				}catch(Exception e) {
					break;
				}
			}
			for(int k = 0; k < 10; k++) {
				String check = "true";
				long RTT = 0;
				if(end[k] == 0) {
					check = "false";
					RTT = 1000;
				}else {
					RTT = end[k] - start[k];
				}
				if(RTT > max) {
					max = RTT;
				}
				if(min == 0) {
					min = RTT;
				}else if(min > RTT) {
					min = RTT;
				}
				sum += RTT;
				System.out.println("PING " + k + ": " + check + " RTT: " + RTT);
			}
			double avg = sum/10;
			System.out.println("Minimum = " + min + "ms, Maximum = " + max + "ms, Average = " + avg + "ms.");
			socket.close();
		}catch(java.net.UnknownHostException UHE) {
			System.out.println(UHE);
		}catch(java.net.PortUnreachableException PUE) {
			System.out.println(PUE);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
