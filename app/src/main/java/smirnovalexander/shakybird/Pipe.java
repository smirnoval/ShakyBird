package smirnovalexander.shakybird;

import smirnovalexander.framework.DynamicGameObject;

public class Pipe extends DynamicGameObject {
    public static final float PIPE_WIDTH = 100;
    public static final float PIPE_HEIGHT = 700;
    public static final float PIPE_VELOCITY = 200;

    float stateTime;

    public Pipe(float x, float y) {
        super(x, y, PIPE_WIDTH, PIPE_HEIGHT);
        this.stateTime = 0;
        velocity.x = -PIPE_VELOCITY;
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, 0);
        bounds.lowerLeft.set(position).sub(PIPE_WIDTH / 2, PIPE_HEIGHT / 2);
        stateTime += deltaTime;
    }
}