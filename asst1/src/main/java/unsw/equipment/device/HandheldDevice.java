package unsw.equipment.device;

import unsw.utils.Angle;

public class HandheldDevice extends Device {
    public HandheldDevice(String deviceId, String type, Angle position) {
        super(deviceId, type, position, 50000.0);
    }

    @Override
    public boolean communicable(Double satelliteHigh, Angle satellitePosition, String satelliteType) {
        return isCommunicable(satelliteHigh, satellitePosition);
    }
}
