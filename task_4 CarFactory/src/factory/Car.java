package factory;

import factory.details.Accessory;
import factory.details.Body;
import factory.details.Engine;

public class Car {
    private final int id;
    private final Body body;
    private final Engine engine;
    private final Accessory accessory;

    public Car(int id, Body body, Engine engine, Accessory accessory) {
        this.body = body;
        this.engine = engine;
        this.accessory = accessory;
        this.id = id;
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

    public Integer getId() {
        return id;
    }
}
