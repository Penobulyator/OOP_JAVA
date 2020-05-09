package java;

import java.details.Accessory;
import java.details.Body;
import java.details.Engine;

public class Car {
    private Body body;
    private Engine engine;
    private Accessory accessory;

    public Car(Body body, Engine engine, Accessory accessory) {
        this.body = body;
        this.engine = engine;
        this.accessory = accessory;
    }

    public Body getBody() {
        return body;
    }

    public Engine getEngine() {
        return engine;
    }

    public Accessory getAccessory() {
        return accessory;
    }
}
