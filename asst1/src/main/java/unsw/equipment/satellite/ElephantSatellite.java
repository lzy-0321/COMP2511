package unsw.equipment.satellite;

import unsw.utils.Angle;

public class ElephantSatellite extends SatelliteHandlesFiles {
    public ElephantSatellite(String satelliteId, String type, Double height, Angle position) {
        // 1 for clockwise, -1 for anticlockwise
        // -1 for maxFiles means unlimited
        super(satelliteId, type, height, position, 2500.0, -1, 400000.0, -1, 90, 20, 20);
    }

    @Override
    public void move() {
        Double newPosition = calculateNewPosition();
        this.setPosition(Angle.fromDegrees(newPosition));
    }

    @Override
    // standard satellites can communicate with handhelds and laptops
    public boolean communicable(Angle devicePosition, String deviceType) {
        if (isCommunicable(devicePosition)
                && (deviceType.equals("DesktopDevice") || deviceType.equals("LaptopDevice"))) {
            return true;
        }
        return false;
    }

    @Override
    // along with other satellites, except for teleporting satellites
    public boolean communicable(Double otherheight, Angle otherposition, String otherType) {
        if (otherType.equals("TeleportingSatellite")) {
            return false;
        }
        return isCommunicable(otherheight, otherposition);
    }

}
