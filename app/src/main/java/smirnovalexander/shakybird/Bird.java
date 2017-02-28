package smirnovalexander.shakybird;

import smirnovalexander.framework.DynamicGameObject;

public class Bird extends DynamicGameObject{
    public static final int BIRD_STATE_JUMP = 0;
    public static final int BIRD_STATE_FALL = 1;
    public static final int BIRD_STATE_HIT = 2;
    public static final float BIRD_JUMP_VELOCITY = 400;
    public static final float BIRD_WIDTH = 70;
    public static final float BIRD_HEIGHT = 70;

    int state;
    float stateTime;

    public Bird(float x, float y) {
        super(x, y, BIRD_WIDTH, BIRD_HEIGHT);
        state = BIRD_STATE_FALL;
        stateTime = 0;
    }

    public void update(float deltaTime) {
        velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);

        if(velocity.y > 0 && state != BIRD_STATE_HIT) {
            if(state != BIRD_STATE_JUMP) {
                state = BIRD_STATE_JUMP;
                stateTime = 0;
            }
        }

        if(velocity.y < 0 && state != BIRD_STATE_HIT) {
            if(state != BIRD_STATE_FALL) {
                state = BIRD_STATE_FALL;
                stateTime = 0;
            }
        }

        stateTime += deltaTime;
    }

    public void hit() {
        velocity.set(0,0);
        state = BIRD_STATE_HIT;
        stateTime = 0;
    }

    public void birdJump() {
        velocity.y = BIRD_JUMP_VELOCITY;
        state = BIRD_STATE_JUMP;
        stateTime = 0;
    }
}
