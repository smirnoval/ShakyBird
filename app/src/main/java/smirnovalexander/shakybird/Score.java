package smirnovalexander.shakybird;

import smirnovalexander.framework.DynamicGameObject;

public class Score extends DynamicGameObject {
    public static final float SCORE_WIDTH = 100;
    public static final float SCORE_HEIGHT = 200;
    public static final float SCORE_VELOCITY = 200;

    float stateTime;

    public Score(float x, float y) {
        super(x, y, SCORE_WIDTH, SCORE_HEIGHT);
        this.stateTime = 0;
        velocity.x = -SCORE_VELOCITY;
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, 0);
        bounds.lowerLeft.set(position).sub(SCORE_WIDTH / 2, SCORE_HEIGHT / 2);
        stateTime += deltaTime;
    }
}
