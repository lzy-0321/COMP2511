package unsw.equipment.satellite;

import unsw.utils.Angle;

public class SatelliteFactory {
    public static Satellite createSatellite(String satelliteId, String type, double height, Angle position) {
        if (type.equals("StandardSatellite")) {
            return new StandardSatellite(satelliteId, type, height, position);
        } else if (type.equals("TeleportingSatellite")) {
            return new TeleportingSatellite(satelliteId, type, height, position);
        } else if (type.equals("RelaySatellite")) {
            return new RelaySatellite(satelliteId, type, height, position);
        } else if (type.equals("ElephantSatellite")) {
            return new ElephantSatellite(satelliteId, type, height, position);
        }
        throw new IllegalArgumentException("Invalid satellite type: " + type);
    }
}
