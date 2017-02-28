package smirnovalexander.shakybird;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import smirnovalexander.framework.Game;
import smirnovalexander.framework.Input.TouchEvent;
import smirnovalexander.framework.gl.Animation;
import smirnovalexander.framework.gl.Camera2D;
import smirnovalexander.framework.gl.SpriteBatcher;
import smirnovalexander.framework.gl.TextureRegion;
import smirnovalexander.framework.impl.GLScreen;
import smirnovalexander.framework.math.OverlapTester;
import smirnovalexander.framework.math.Rectangle;
import smirnovalexander.framework.math.Vector2;

public class MainMenuScreen extends GLScreen {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle soundBounds;
    Rectangle playBounds;
    Rectangle highscoresBounds;
    Vector2 touchPoint;
    public final Bird bird;

    public MainMenuScreen(Game game) {
        super(game);
        this.bird = new Bird(320, 575);
        guiCam = new Camera2D(glGraphics, 640, 960);
        batcher = new SpriteBatcher(glGraphics, 100);
        playBounds = new Rectangle(200, 320, 200, 150);
        highscoresBounds = new Rectangle(30, 110, 200, 120);
        soundBounds = new Rectangle(400, 110, 200, 120);
        touchPoint = new Vector2();
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        if (!Assets.music.isPlaying() && Settings.soundEnabled) {
            Assets.music.play();
        }

        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            if(event.type == TouchEvent.TOUCH_UP) {
                touchPoint.set(event.x, event.y);
                guiCam.touchToWorld(touchPoint);

                if(OverlapTester.pointInRectangle(playBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    Assets.music.reset();
                    game.setScreen(new GameScreen(game));
                    return;
                }

                if(OverlapTester.pointInRectangle(highscoresBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    game.setScreen(new HighscoreScreen(game));
                    return;
                }
                if (OverlapTester.pointInRectangle(soundBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    Settings.soundEnabled = !Settings.soundEnabled;
                    if (Settings.soundEnabled)
                        Assets.music.play();
                    else
                        Assets.music.pause();
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        guiCam.setViewportAndMatrices();

        gl.glEnable(GL10.GL_TEXTURE_2D);

        batcher.beginBatch(Assets.background);
        batcher.drawSprite(320, 480, 640, 960, Assets.backgroundRegion);
        batcher.endBatch();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(Assets.items);

        TextureRegion keyFrame;
        keyFrame = Assets.birdJump.getKeyFrame(bird.stateTime, Animation.ANIMATION_LOOPING);
        batcher.drawSprite(320, 600, 100, 100, keyFrame);
        bird.update(deltaTime);

        batcher.drawSprite(340, 820, 600, 200, Assets.logo);
        batcher.drawSprite(320, 400, 200, 132, Assets.start);
        batcher.drawSprite(150, 180, 200, 132, Assets.highScores);
        if (Settings.soundEnabled)
            batcher.drawSprite(500, 180, 200, 132, Assets.soundOn);
        else
            batcher.drawSprite(500, 180, 200, 132, Assets.soundOff);

        batcher.endBatch();

        gl.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void pause() {
        Settings.save(game.getFileIO());
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
