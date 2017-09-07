package process;

public class Device {
	public DeviceType type;
	public boolean isAvailable=false;
	public Device(DeviceType type) {
		// TODO Auto-generated constructor stub
		this.type=type;
	}
	public DeviceType getType() {
		return type;
	}
	public void setType(DeviceType type) {
		this.type = type;
	}
	public boolean isAvailable() {
		return isAvailable;
	}
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}
	
}
