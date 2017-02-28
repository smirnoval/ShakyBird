package smirnovalexander.shakybird;

import smirnovalexander.framework.DynamicGameObject;

public class Platform extends DynamicGameObject {
    public static final float PLATFORM_WIDTH = 300;
    public static final float PLATFORM_HEIGHT = 30;
    public static final float PLATFORM_VELOCITY = 200;

    float stateTime;

    public Platform(float x, float y) {
        super(x, y, PLATFORM_WIDTH, PLATFORM_HEIGHT);
        this.stateTime = 0;
        velocity.x = -PLATFORM_VELOCITY;
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, 0);
        stateTime += deltaTime;
    }
}
