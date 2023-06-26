package unsw.equipment.device;

import unsw.utils.Angle;

public class DeviceFactory {
    public static Device createDevice(String deviceId, String type, Angle position) {
        if (type.equals("HandheldDevice")) {
            return new HandheldDevice(deviceId, type, position);
        } else if (type.equals("LaptopDevice")) {
            return new LaptopDevice(deviceId, type, position);
        } else if (type.equals("DesktopDevice")) {
            return new DesktopDevice(deviceId, type, position);
        }
        throw new IllegalArgumentException("Invalid device type: " + type);
    }

}
