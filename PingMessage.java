import java.net.*;
public class PingMessage {
	private InetAddress addr;
	private int port;
	private String payload;
	public PingMessage(InetAddress addr, int port, String payload) {
		this.addr = addr;
		this.port = port;
		this.payload = payload;
	}
	public InetAddress getIP() {
		return addr;
	}
	public int getPort() {
		return port;
	}
	public String getPayload() {
		return payload;
	}
}
