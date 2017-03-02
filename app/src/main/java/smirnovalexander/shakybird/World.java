package smirnovalexander.shakybird;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import smirnovalexander.framework.Input;
import smirnovalexander.framework.math.OverlapTester;
import smirnovalexander.framework.math.Vector2;

public class World {
    public interface WorldListener {
        public void jump();
        public void hit();
    }

    public static final int WORLD_STATE_RUNNING = 0;
    public static final int WORLD_STATE_GAME_OVER = 1;
    public static final Vector2 gravity = new Vector2(0, -1500);

    public final Bird bird;
    public final List<Pipe> pipes;
    public final List<Score> scores;
    public final List<Platform> platforms;
    public final WorldListener listener;
    public final Random rand;

    public float heightSoFar;
    public int score;
    public int state;
    public double generatorTime;
    public double jumpTime;

    public World(WorldListener listener) {
        this.bird = new Bird(320, 575);
        this.pipes = new ArrayList<Pipe>();
        this.scores = new ArrayList<Score>();
        this.platforms = new ArrayList<Platform>();
        this.listener = listener;
        rand = new Random();
        generatorTime=System.nanoTime();
        jumpTime = System.nanoTime();

        this.heightSoFar = 0;
        this.score = 0;
        this.state = WORLD_STATE_RUNNING;

        generateStartPlatforms();
    }

    public void generatePipes() {
        int y = rand.nextInt(1200 - 700 + 1) + 700;
        int x = 1200;
        Pipe pipe = new Pipe(x, y);
        pipes.add(pipe);
        pipe = new Pipe(x, y-900);
        pipes.add(pipe);
        Score scoreObject = new Score(x, y-200);
        scores.add(scoreObject);
    }

    public void generateStartPlatforms() {
        int x = -260;
        int y = 0;
        for(int i = 0; i <= 4; i++, x+=300) {
            Platform platform = new Platform(x, y);
            platforms.add(platform);
        }
    }

    public void update(float deltaTime, List<Input.TouchEvent> touchEvents) {
        if (System.nanoTime() - generatorTime >= 2000000000) {
            generatePipes();
            generatorTime = System.nanoTime();
        }
        updateBird(deltaTime, touchEvents);
        updatePipe(deltaTime);
        updateScores(deltaTime);
        updatePlatforms(deltaTime);
        if (bird.state != Bird.BIRD_STATE_HIT)
            checkPipeCollisions();
        checkGameOver();
    }

    private void updateBird(float deltaTime, List<Input.TouchEvent> touchEvents) {
        if (touchEvents.size() > 0 && bird.state != Bird.BIRD_STATE_HIT) {
            if (System.nanoTime() - jumpTime >= 250000000) {
                bird.jump();
                Assets.playSound(Assets.jumpSound);
                jumpTime = System.nanoTime();
            }
            else
                bird.jumpWithoutAnimation();
        }
        bird.update(deltaTime);
        heightSoFar = Math.max(bird.position.y, heightSoFar);
    }

    private void updatePipe(float deltaTime) {
        for (Iterator<Pipe> iterator = pipes.iterator(); iterator.hasNext();) {
            Pipe pipe = iterator.next();
            if (pipe.position.x < -100)
                iterator.remove();
            else
                pipe.update(deltaTime);
        }
    }

    private void updateScores(float deltaTime) {
        for (Iterator<Score> iterator = scores.iterator(); iterator.hasNext();) {
            Score scoreObject = iterator.next();
            scoreObject.update(deltaTime);
            if(scoreObject.bounds.lowerLeft.x < bird.bounds.lowerLeft.x && bird.state != Bird.BIRD_STATE_HIT) {
                score += 1;
                iterator.remove();
                break;
            }
        }
    }

    private void updatePlatforms(float deltaTime) {
        boolean addPlatform = false;
        int gen = 0;
        for (Iterator<Platform> iterator = platforms.iterator(); iterator.hasNext();) {
            Platform platform = iterator.next();
            platform.update(deltaTime);
            if(platform.position.x <= - 560) {
                gen = (int)platform.position.x;
                iterator.remove();
                addPlatform = true;
            }
        }
        if (addPlatform) {
            Platform newPlatform = new Platform(939-Math.abs(560+gen), 0);
            platforms.add(newPlatform);
        }

    }

    private void checkPipeCollisions() {
        int len = pipes.size();
        for (int i = 0; i < len; i++) {
            Pipe pipe = pipes.get(i);
            if (OverlapTester.overlapRectangles(pipe.bounds, bird.bounds)) {
                Assets.playSound(Assets.hitSound);
                bird.hit();
                listener.hit();
            }
        }
    }

    private void checkGameOver() {
        if (bird.position.y <= 80 || bird.position.y >= 960) {
            if (bird.state != Bird.BIRD_STATE_HIT) {
                bird.hit();
                Assets.playSound(Assets.hitSound);
            }
            Assets.playSound(Assets.gameOverSound);
            state = WORLD_STATE_GAME_OVER;
        }
    }
}
