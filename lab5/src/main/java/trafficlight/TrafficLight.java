// public class TrafficLight {
// private String state = "Red light";
// private String id;

// private int count = 0;

// public TrafficLight(String state, String id) {
// this.state = state;
// this.id = id;
// if (state.equals("Red light")) {
// this.count = 6;
// } else if (state.equals("Green light")) {
// this.count = 4;
// } else if (state.equals("Yellow light")) {
// this.count = 1;
// }
// }

// public void change(int numOfCars, int numOfPedestrians) {
// if (count > 0) {
// count -= 1;
// return;
// }
// int trafficDemand = numOfCars + numOfPedestrians;
// if (state.equals("Red light")) {
// state = "Green light";
// count = trafficDemand > 100 ? 6 : 4;
// } else if (state.equals("Green light")) {
// state = "Yellow light";
// count = 1;
// } else if (state.equals("Yellow light")) {
// state = "Red light";
// count = trafficDemand < 10 ? 10 : 6;
// }
// }

// public int timeRemaining() {
// return count;
// }

// public String reportState() {
// if (state.equals("Red light")) {
// return "Red light";
// } else if (state.equals("Green light")) {
// return "Green light";
// } else if (state.equals("Yellow light")) {
// return "Yellow light";
// }
// return "Oops, unknown light!";
// }
// }

package trafficlight;

public class TrafficLight {
    private TrafficLightState state;
    private String id;
    private int count;

    public TrafficLight(String state, String id) {
        this.state = getStateFromString(state);
        this.id = id;
        this.count = this.state instanceof RedLightState ? 6
                : this.state instanceof GreenLightState ? 4 : this.state instanceof PedestrianLightState ? 2 : 1;
    }

    public void change(int numOfCars, int numOfPedestrians) {
        if (count > 0) {
            count -= 1;
        } else {
            state.change(this, numOfCars, numOfPedestrians);
        }
    }

    public int timeRemaining() {
        return count;
    }

    public String reportState() {
        return state.reportState();
    }

    public void setState(TrafficLightState state) {
        this.state = state;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private TrafficLightState getStateFromString(String stateStr) {
        switch (stateStr) {
        case "Red light":
            return new RedLightState();
        case "Green light":
            return new GreenLightState();
        case "Yellow light":
            return new YellowLightState();
        case "Pedestrian light":
            return new PedestrianLightState();
        default:
            throw new IllegalArgumentException("Invalid state: " + stateStr);
        }
    }
}

interface TrafficLightState {
    void change(TrafficLight trafficLight, int numOfCars, int numOfPedestrians);

    String reportState();
}

class RedLightState implements TrafficLightState {
    public void change(TrafficLight trafficLight, int numOfCars, int numOfPedestrians) {
        if (numOfPedestrians > 0) {
            trafficLight.setState(new PedestrianLightState());
            trafficLight.setCount(2);
        } else {
            trafficLight.setState(new GreenLightState());
            trafficLight.setCount((numOfCars + numOfPedestrians) > 100 ? 6 : 4);
        }
    }

    public String reportState() {
        return "Red light";
    }
}

class GreenLightState implements TrafficLightState {
    public void change(TrafficLight trafficLight, int numOfCars, int numOfPedestrians) {
        trafficLight.setState(new YellowLightState());
        trafficLight.setCount(1);
    }

    public String reportState() {
        return "Green light";
    }
}

class YellowLightState implements TrafficLightState {
    public void change(TrafficLight trafficLight, int numOfCars, int numOfPedestrians) {
        trafficLight.setState(new RedLightState());
        trafficLight.setCount((numOfCars + numOfPedestrians) < 10 ? 10 : 6);
    }

    public String reportState() {
        return "Yellow light";
    }
}

class PedestrianLightState implements TrafficLightState {
    public void change(TrafficLight trafficLight, int numOfCars, int numOfPedestrians) {
        trafficLight.setState(new GreenLightState());
        trafficLight.setCount((numOfCars + numOfPedestrians) > 100 ? 6 : 4);
    }

    public String reportState() {
        return "Pedestrian light";
    }
}
