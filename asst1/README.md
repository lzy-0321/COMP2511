You can view the assignment specification here: https://unswcse.atlassian.net/wiki/spaces/C2/pages/187793419/Assignment+I+Back+in+Blackout

First implementation

In the initial design, a superclass called FileHolder exists. The Device and Satellite classes are subclasses of FileHolder. Furthermore, different devices and satellites have their own specific subclasses under the Device and Satellite classes.

According to the problem requirements, every device and satellite should be a FileHolder. However, in this scenario, a relay satellite doesn't store files. This means it cannot be a FileHolder, thus breaking the Liskov Substitution Principle (LSP), as not every Satellite can replace the FileHolder class.

Second implementation

In the revised approach, the operation and storage of files are separated from the Device and Satellite classes. A new class named FileHandler is introduced to handle all file-related functionalities. If a type of Device or Satellite requires file storage, a FileHandler is included as a private variable.



BlackoutController 

1.0

The lists of Devices and Satellites are stored internally, and all the functions are implemented within. This leads to high dependency on the Device and Satellite classes, and there is too much logic inside the controller, such as handling the storage list, and determining how to communicate and send/receive data. To simplify this, two interfaces named Sender and Receiver are created. This makes it unnecessary to classify different methods when dealing with communication and sending. SatelliteHandlesFiles and Device classes should implement the Sender and Receiver interfaces, while file processing should be handled by the FileHandler. For simulation purposes, satellites are moved. The next position is calculated within the Satellite class. An abstract move() function is created and overridden in each subclass to achieve different movement methods for different satellites, and update their positions. File sending functionality isn't implemented at this point.

2.0

A class named WorldStorage is created, which is responsible for storing and processing the list of satellites and devices in the world, providing the necessary methods for managing the list. 

Communication and file sending logic are separated from the controller, with new classes named CommunicationService and SendingService handling these aspects. 
Inside CommunicationService, devices and satellites are handled separately in the communicableEntitiesInRange method, due to their different behaviors. Whether devices and satellites can communicate with each other is determined by their individual calculations and a returned Boolean value. For the specific implementation, a device needs to know if it can communicate with a satellite. For this calculation, the device needs to know the satellite's height, position, and type. Hence, only these variables (not the whole Satellite object) are passed to the device. The same approach is used for satellite-to-satellite and satellite-to-device communication. In the Device and Satellite classes, an isCommunicable function is used for the calculation. An abstract communicable function is created and overridden in all subclasses. In each subclass, isCommunicable and the type of the device or satellite are checked to ensure that two devices (or satellites) that aren't allowed to communicate can't establish communication.

In the case of relay satellites, recursion is used to find all connectable relay satellites. 

In the isCommunicableEntitiesInRange function, it works for the SendingService to verify whether the sender can still communicate with the receiver.

Inside SendingService, the only public function is sendingFiles. It is responsible for sending all the files that need to be sent by given the id. The special behavior of all satellites in transmitting and receiving interrupts will be implemented in the isSpecialSituation function. When adding special behavior for a satellite, the isSpecialSituation function is edited and a specific implementation is added. In this scenario, teleportation for the TeleportingSatellite is considered, so an isTeleport() function is added in SatelliteHandlesFiles, which returns false by default but is overridden in TeleportingSatellite to implement teleportation. As before, all file processing calls the interface of the sender and receiver, leaving the FileHandler to handle the implementation. 

For simulation, the sendingService.SendingFiles(id) function is called for all Devices and SatelliteHandlesFiles to send files. 

CommunicationServiceWorldStorage and SendingServiceWorldStorage are interfaces used to restrict the internal knowledge of CommunicationService and SendingService about WorldStorage.

2.1

An EntityInfo interface is created, which includes FileInfoResponse and EntityInfoResponse. The Device and Satellite classes implement this interface. The FileHandler implements the FileInfoResponse. 

An EquipmentInfo class is created, which includes height, position, and type. This is implemented within the Device and Satellite classes and is used by the communicableEntitiesInRange function. 

DeviceFactory and SatelliteFactory classes are created, which are used to create devices or satellites of different types. This allows the controller to focus solely on creation, while the factory handles type discernment. When different types of devices or satellites are added, there is no need to edit the controller. 

Future Improvement(maybe):

Create a superclass called Equipment. Device and Satellite would then serve as subclasses of this Equipment superclass.

Change all Lists to Maps, can optimise the code to reduce the time required for lookup a device or a satellite or a file.

For ElephantSatellite problems, i think we can create a new Map in Filehandler, it be used to store the files marked as transient and fromId. In sendingFiles function, we add operations to this new map, create a new send relation. It can be thinks as receiver asks files from sender.