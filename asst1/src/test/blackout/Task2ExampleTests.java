package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.blackout.FileTransferException;
import unsw.response.models.FileInfoResponse;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.Arrays;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task2ExampleTests {
    @Test
    public void testEntitiesInRange() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to
        // download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createSatellite("Satellite2", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(315));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));
        controller.createDevice("DeviceD", "HandheldDevice", Angle.fromDegrees(180));
        controller.createSatellite("Satellite3", "StandardSatellite", 2000 + RADIUS_OF_JUPITER, Angle.fromDegrees(175));

        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceC", "Satellite2"),
                controller.communicableEntitiesInRange("Satellite1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "Satellite1"),
                controller.communicableEntitiesInRange("Satellite2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite2"), controller.communicableEntitiesInRange("DeviceB"));

        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceD"), controller.communicableEntitiesInRange("Satellite3"));

        // add relay satellite
        controller.createSatellite("Satellite4", "RelaySatellite", 10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(210));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceD", "Satellite4"),
                controller.communicableEntitiesInRange("Satellite3"));

        controller.createSatellite("Satellite5", "RelaySatellite", 30000 + RADIUS_OF_JUPITER, Angle.fromDegrees(270));
        assertListAreEqualIgnoringOrder(
                Arrays.asList("Satellite1", "Satellite2", "Satellite3", "Satellite4", "Satellite5"),
                controller.communicableEntitiesInRange("DeviceB"));
        assertListAreEqualIgnoringOrder(
                Arrays.asList("DeviceB", "DeviceD", "Satellite1", "Satellite2", "Satellite4", "Satellite5"),
                controller.communicableEntitiesInRange("Satellite3"));

        controller.removeSatellite("Satellite5");
        controller.createSatellite("Satellite5", "RelaySatellite", 20000 + RADIUS_OF_JUPITER, Angle.fromDegrees(270));
        controller.createSatellite("Satellite6", "RelaySatellite", 20000 + RADIUS_OF_JUPITER, Angle.fromDegrees(300));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "DeviceD", "Satellite1", "Satellite2",
                "Satellite4", "Satellite5", "Satellite6"), controller.communicableEntitiesInRange("Satellite3"));
        controller.createSatellite("Satellite7", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(330));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "Satellite1", "Satellite2", "Satellite3",
                "Satellite4", "Satellite5", "Satellite6"), controller.communicableEntitiesInRange("Satellite7"));
    }

    @Test
    public void testSomeExceptionsForSend() {
        // just some of them... you'll have to test the rest
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to
        // download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertThrows(FileTransferException.VirtualFileNotFoundException.class,
                () -> controller.sendFile("NonExistentFile", "DeviceC", "Satellite1"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false),
                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
        controller.simulate(msg.length() * 2);

        assertThrows(FileTransferException.VirtualFileAlreadyExistsException.class,
                () -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));

        controller.addFileToDevice("DeviceC", "FileBeta", msg);
        assertDoesNotThrow(() -> controller.sendFile("FileBeta", "DeviceC", "Satellite1"));
        controller.simulate(2);
        assertThrows(FileTransferException.VirtualFileNotFoundException.class,
                () -> controller.sendFile("FileBeta", "Satellite1", "DeviceB"));
        controller.addFileToDevice("DeviceC", "FileCat", msg);
        assertThrows(FileTransferException.VirtualFileNoBandwidthException.class,
                () -> controller.sendFile("FileCat", "DeviceC", "Satellite1"));
        controller.simulate(msg.length() - 2);
        assertEquals(new FileInfoResponse("FileBeta", msg, msg.length(), true),
                controller.getInfo("Satellite1").getFiles().get("FileBeta"));

        controller.simulate(180);

        assertDoesNotThrow(() -> controller.sendFile("FileCat", "DeviceC", "Satellite1"));
        controller.simulate(msg.length());
        assertEquals(new FileInfoResponse("FileCat", msg, msg.length(), true),
                controller.getInfo("Satellite1").getFiles().get("FileCat"));

        controller.addFileToDevice("DeviceC", "FileDog", msg);
        assertThrows(FileTransferException.VirtualFileNoStorageSpaceException.class,
                () -> controller.sendFile("FileDog", "DeviceC", "Satellite1"));

    }

    @Test
    public void testMovement() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to
        // download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(340), 100 + RADIUS_OF_JUPITER,
                "StandardSatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(337.95), 100 + RADIUS_OF_JUPITER,
                "StandardSatellite"), controller.getInfo("Satellite1"));
    }

    @Test
    public void testExample() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to
        // download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 10000 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false),
                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        controller.simulate(msg.length() * 2);
        assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Satellite1", "DeviceB"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false),
                controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

        controller.simulate(msg.length());
        assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
                controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

        controller.addFileToDevice("DeviceB", "FileBate", msg);
        assertDoesNotThrow(() -> controller.sendFile("FileBate", "DeviceB", "Satellite1"));
        controller.simulate(msg.length() * 2);
        assertEquals(new FileInfoResponse("FileBate", msg, msg.length(), true),
                controller.getInfo("Satellite1").getFiles().get("FileBate"));
        // Hints for further testing:
        // - What about checking about the progress of the message half way through?
        // - Device/s get out of range of satellite
        // ... and so on.

        // Satellite1 lose connection to DeviceC in 2 minute later, so the last byte of
        // file will go through relay satellite
        controller.createSatellite("Satellite2", "RelaySatellite", 10000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        assertDoesNotThrow(() -> controller.sendFile("FileBate", "Satellite1", "DeviceC"));
        controller.simulate(msg.length() * 2);
        assertEquals(new FileInfoResponse("FileBate", msg, msg.length(), true),
                controller.getInfo("DeviceC").getFiles().get("FileBate"));

        // teleporting when receiving file
        controller.removeDevice("DeviceB");
        controller.removeDevice("DeviceC");
        controller.removeSatellite("Satellite1");
        controller.removeSatellite("Satellite2");
        String msgWithT = "HeytImteleportingCanyouseemeImhereHeyt";
        String msgWithoutT = "HeyImeleporingCanyouseemeImhereHey";

        controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(179));
        controller.createDevice("DeviceB", "HandheldDevice", Angle.fromDegrees(180));

        controller.addFileToDevice("DeviceB", "FileAlpha", msgWithT);
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceB", "Satellite1"));

        controller.simulate(msgWithT.length());

        assertEquals(new FileInfoResponse("FileAlpha", msgWithoutT, msgWithoutT.length(), true),
                controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

        // teleporting when sending file
        controller.removeDevice("DeviceB");
        controller.removeSatellite("Satellite1");
        controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(176));
        controller.createDevice("DeviceB", "HandheldDevice", Angle.fromDegrees(180));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(180));

        controller.addFileToDevice("DeviceB", "FileAlpha", msgWithT);
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceB", "Satellite1"));

        controller.simulate(3);
        assertEquals(new FileInfoResponse("FileAlpha", msgWithT, msgWithT.length(), true),
                controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Satellite1", "DeviceC"));

        controller.simulate(msgWithT.length());
        // 2 minutes after sending file, teleporting
        String msgWithSomeT = "HeytImteleportingCanyouseemeImhereHey";

        assertEquals(new FileInfoResponse("FileAlpha", msgWithSomeT, msgWithSomeT.length(), true),
                controller.getInfo("DeviceC").getFiles().get("FileAlpha"));
    }

    @Test
    public void testRelayMovement() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to
        // download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "RelaySatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(180));

        // moves in negative direction
        assertEquals(
                new EntityInfoResponse("Satellite1", Angle.fromDegrees(180), 100 + RADIUS_OF_JUPITER, "RelaySatellite"),
                controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(178.77), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(177.54), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(176.31), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));

        controller.simulate(5);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(170.18), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(24);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(140.72), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        // edge case
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(139.49), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        // coming back
        controller.simulate(1);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(140.72), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(5);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(146.85), 100 + RADIUS_OF_JUPITER,
                "RelaySatellite"), controller.getInfo("Satellite1"));
    }

    @Test
    public void testTeleportingMovement() {
        // Test for expected teleportation movement behaviour
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                Angle.fromDegrees(0));

        controller.simulate();
        Angle clockwiseOnFirstMovement = controller.getInfo("Satellite1").getPosition();
        controller.simulate();
        Angle clockwiseOnSecondMovement = controller.getInfo("Satellite1").getPosition();
        assertTrue(clockwiseOnSecondMovement.compareTo(clockwiseOnFirstMovement) == 1);

        // It should take 250 simulations to reach theta = 180.
        // Simulate until Satellite1 reaches theta=180
        controller.simulate(250);

        // Verify that Satellite1 is now at theta=0
        assertTrue(controller.getInfo("Satellite1").getPosition().toDegrees() % 360 == 0);
    }
}
