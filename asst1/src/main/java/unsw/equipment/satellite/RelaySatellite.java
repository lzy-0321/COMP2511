package unsw.equipment.satellite;

import unsw.utils.Angle;

public class RelaySatellite extends SatelliteDoesNotHandleFiles {
    public RelaySatellite(String satelliteId, String type, Double height, Angle position) {
        // -1 for maxSend and maxReceive means unlimited
        super(satelliteId, type, height, position, 1500.0, -1, 300000.0);
    }

    @Override
    public void move() {
        // Only travels in the region between 140° and 190°
        // In the case that the satellite doesn't start in the region [140°, 190°], it
        // should choose whatever direction gets it to the region [140°, 190°] in the
        // shortest amount of time.
        Double position = this.getPosition().toDegrees();
        if (position < 140 || position > 190) {
            if (position < 140 || position >= 345) {
                this.setDirection(1);
            } else {
                this.setDirection(-1);
            }
        }
        Double newPosition = calculateNewPosition();
        this.setPosition(Angle.fromDegrees(newPosition));
        if (newPosition <= 140 || newPosition >= 190) {
            this.setDirection(this.getDirection() * -1);
        }
    }

    @Override
    // Supports all devices
    public boolean communicable(Angle devicePosition, String deviceType) {
        return isCommunicable(devicePosition);
    }

    @Override
    public boolean communicable(Double otherheight, Angle otherposition, String otherType) {
        return isCommunicable(otherheight, otherposition);
    }
}
