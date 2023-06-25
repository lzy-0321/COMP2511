package unsw.satellite;

import unsw.device.Device;
import unsw.utils.Angle;

public class StandardSatellite extends Satellite {
    public StandardSatellite(String satelliteId, String type, Double height, Angle position) {
        super(satelliteId, type, height, position, 2500.0, -1, 150000.0, 3, 80, 1, 1);
    }

    @Override
    public void move() {
        Double newPosition = calculateNewPosition();
        this.setPosition(Angle.fromDegrees(newPosition));
    }

    @Override
    // standard satellites can communicate with handhelds and laptops
    public boolean communicable(Device device) {
        if (isCommunicable(device)
                && (device.getType().equals("HandheldDevice") || device.getType().equals("LaptopDevice"))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean communicable(Satellite satellite) {
        return isCommunicable(satellite);
    }

}
