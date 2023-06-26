package unsw.equipment.device;

import unsw.utils.Angle;

public class DesktopDevice extends Device {
    public DesktopDevice(String deviceId, String type, Angle position) {
        super(deviceId, type, position, 200000.0);
    }

    @Override
    public boolean communicable(Double satelliteHigh, Angle satellitePosition, String satelliteType) {
        return isCommunicable(satelliteHigh, satellitePosition);
    }
}
