package smirnovalexander.shakybird;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import smirnovalexander.framework.Game;
import smirnovalexander.framework.Input.TouchEvent;
import smirnovalexander.framework.gl.Camera2D;
import smirnovalexander.framework.gl.SpriteBatcher;
import smirnovalexander.framework.impl.GLScreen;
import smirnovalexander.framework.math.OverlapTester;
import smirnovalexander.framework.math.Rectangle;
import smirnovalexander.framework.math.Vector2;
import smirnovalexander.shakybird.World.WorldListener;

public class GameScreen extends GLScreen {
    static final int GAME_READY = 0;
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_OVER = 3;

    int state;
    Camera2D guiCam;
    Vector2 touchPoint;
    SpriteBatcher batcher;
    World world;
    WorldListener worldListener;
    WorldRenderer renderer;
    Rectangle pauseBounds;
    Rectangle resumeBounds;
    Rectangle quitBounds;
    int lastScore;
    String scoreString;

    public GameScreen(Game game) {
        super(game);
        state = GAME_READY;
        guiCam = new Camera2D(glGraphics, 640, 960);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 1000);
        worldListener = new WorldListener() {
            public void jump() {}

            public void hit() {
                Assets.playSound(Assets.hitSound);
            }
        };
        world = new World(worldListener);
        renderer = new WorldRenderer(glGraphics, batcher, world);
        pauseBounds = new Rectangle(0, 820, 128, 128);
        resumeBounds = new Rectangle(120, 450, 400, 100);
        quitBounds = new Rectangle(120, 350, 400, 100);
        lastScore = 0;
        scoreString = "Score: 0";
    }

    @Override
    public void update(float deltaTime) {
        if(deltaTime > 0.1f)
            deltaTime = 0.1f;

        switch(state) {
            case GAME_READY:
                updateReady();
                break;
            case GAME_RUNNING:
                updateRunning(deltaTime);
                break;
            case GAME_PAUSED:
                updatePaused();
                break;
            case GAME_OVER:
                updateGameOver();
                break;
        }
    }

    private void updateReady() {
        if(game.getInput().getTouchEvents().size() > 0) {
            state = GAME_RUNNING;
        }
    }

    private void updateRunning(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type != TouchEvent.TOUCH_UP)
                continue;

            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);

            if(OverlapTester.pointInRectangle(pauseBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                state = GAME_PAUSED;
                return;
            }
        }

        world.update(deltaTime, touchEvents);
        if(world.score != lastScore) {
            lastScore = world.score;
            scoreString = "Score: " + lastScore;
        }
        if(world.state == World.WORLD_STATE_GAME_OVER) {
            state = GAME_OVER;
            if(lastScore > Settings.highscores[4])
                scoreString = "new highscore: " + lastScore;
            else
                scoreString = "score: " + lastScore;
            Settings.addScore(lastScore);
            Settings.save(game.getFileIO());
        }
    }

    private void updatePaused() {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type != TouchEvent.TOUCH_UP)
                continue;

            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);

            if(OverlapTester.pointInRectangle(resumeBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                state = GAME_RUNNING;
                return;
            }

            if(OverlapTester.pointInRectangle(quitBounds, touchPoint)) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new MainMenuScreen(game));
                return;

            }
        }
    }

    private void updateGameOver() {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type != TouchEvent.TOUCH_UP)
                continue;
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glEnable(GL10.GL_TEXTURE_2D);

        renderer.render();

        guiCam.setViewportAndMatrices();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        batcher.beginBatch(Assets.items);
        switch(state) {
            case GAME_READY:
                presentReady();
                break;
            case GAME_RUNNING:
                presentRunning();
                break;
            case GAME_PAUSED:
                presentPaused();
                break;
            case GAME_OVER:
                presentGameOver();
                break;
        }
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }

    private void presentReady() {
        batcher.drawSprite(320, 740, 400, 120, Assets.ready);
        batcher.drawSprite(330, 420, 360, 420, Assets.readyHelpInfo);
    }

    private void presentRunning() {
        batcher.drawSprite(50, 900, 40, 50, Assets.pause);
        Assets.font.drawText(batcher, scoreString, 230, 900, 2);
    }

    private void presentPaused() {
        batcher.drawSprite(320, 450, 400, 200, Assets.pauseMenu);
        Assets.font.drawText(batcher, scoreString, 230, 900, 2);
    }

    private void presentGameOver() {
        batcher.drawSprite(325, 740, 550, 150, Assets.gameOver);
        if(lastScore > Settings.highscores[4])
            Assets.font.drawText(batcher, scoreString, 80, 500, 2);
        else
            Assets.font.drawText(batcher, scoreString, 230, 500, 2);
    }

    @Override
    public void pause() {
        if(state == GAME_RUNNING)
            state = GAME_PAUSED;
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
