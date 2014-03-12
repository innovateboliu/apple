package location;

public class WifiAP {
	private char[] mac;
	private int rssi;
	private int rssiOneMeter;
	Location location;
	
	public char[] getMac() {
		return mac;
	}
	public void setMac(char[] mac) {
		this.mac = mac;
	}
	public int getRssi() {
		return rssi;
	}
	public void setRssi(int rssi) {
		this.rssi = rssi;
	}
	public int getRssiOneMeter() {
		return rssiOneMeter;
	}
	public void setRssiOneMeter(int rssiOneMeter) {
		this.rssiOneMeter = rssiOneMeter;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	} 
}
