package satellite;

public class Satellite {
    private String name;
    private int height;
    private double position;
    private double velocity;

    /**
     * Constructor for Satellite
     * @param name
     * @param height
     * @param velocity
     */
    public Satellite(String name, int height, double position, double velocity) {
        this.name = name;
        this.height = height;
        this.position = position;
        this.velocity = velocity;
    }

    /**
     * Getter for name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Getter for position (degrees)
     */
    public double getPositionDegrees() {
        return position;
    }

    /**
     * Getter for position (radians)
     */
    public double getPositionRadians() {
        return position * Math.PI / 180;
    }

    /**
     * Returns the linear velocity (metres per second) of the satellite
     */
    public double getLinearVelocity() {
        return velocity;
    }

    /**
     * Returns the angular velocity (radians per second) of the satellite
     */
    public double getAngularVelocity() {
        return velocity / height;
    }

    /**
     * Setter for name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for height
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Setter for velocity
     * @param velocity
     */
    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    /**
     * Setter for position
     * @param position
     */
    public void setPosition(double position) {
        this.position = position;
    }

    /**
     * Calculates the distance travelled by the satellite in the given time
     * @param time (seconds)
     * @return distance in metres
     */
    public double distance(double time) {
        return velocity * time;
    }

    public static void main(String[] args) {
        // Add your code
        // create an instance of the Satellite class
        Satellite satelliteA = new Satellite("A", 10000, 122, 55);
        Satellite satelliteB = new Satellite("B", 5438, 0, 234);
        Satellite satelliteC = new Satellite("C", 9029, 284, 0);

        // Print out the information of Satellite A
        System.out.println("I am Satellite " + satelliteA.getName() + " at position " + satelliteA.getPositionDegrees()
                + " degrees, " + satelliteA.getHeight()
                + " km above the centre of the earth and moving at a velocity of " + satelliteA.getLinearVelocity()
                + " metres per second");

        // Change Satellite A's height to 9999, by use setHeight method
        satelliteA.setHeight(9999);

        // Change Satellite B's angle to 45
        satelliteB.setPosition(45);

        // Change Satellite C's velocity to 36.5 mps
        satelliteC.setVelocity(36.5);

        // Print out Satellite A's radians velocity
        System.out.println(satelliteA.getPositionRadians());
        // Print out Satellite B's angular velocity
        System.out.println(satelliteB.getAngularVelocity());
        // Print out the distance Satellite C travels after 2 minutes.
        System.out.println(satelliteC.distance(120));
    }

}
