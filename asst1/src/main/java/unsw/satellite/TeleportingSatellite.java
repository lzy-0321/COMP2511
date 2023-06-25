package unsw.satellite;

import unsw.device.Device;
import unsw.utils.Angle;

public class TeleportingSatellite extends Satellite {
    public TeleportingSatellite(String satelliteId, String type, Double height, Angle position) {
        // -1 for maxFiles means unlimited
        super(satelliteId, type, height, position, 1000.0, 1, 200000.0, -1, 200, 15, 10);
    }

    @Override
    public void move() {
        Double newPosition = calculateNewPosition();
        // depend on the speed of this satellite, calculate the increase angle in this
        // move
        Double angle = (this.getSpeed() * 180) / (this.getHeight() * Math.PI);
        // When the position of the satellite reaches θ = 180, the satellite teleports
        // to θ = 0 and changes direction
        // start by moving anticlockwise
        if ((newPosition - angle >= 180 && newPosition <= 180) || (newPosition - angle <= 180 && newPosition >= 180)) {
            this.setDirection(this.getDirection() * -1);
            newPosition = 0.0;
        }
        this.setPosition(Angle.fromDegrees(newPosition));
    }

    @Override
    // Supports all devices
    public boolean communicable(Device device) {
        return isCommunicable(device);
    }

    @Override
    public boolean communicable(Satellite satellite) {
        return isCommunicable(satellite);
    }
}
