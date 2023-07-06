package metrics;

import java.util.Observable;

public class Emitter extends Observable {
    public void emitMetric(double xValue) {
        Double metric = Math.sin(Math.toRadians(xValue));
        setChanged();
        notifyObservers(metric);
    }
}
