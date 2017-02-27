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

public class SettingsScreen extends GLScreen {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle backBounds;
    Rectangle soundBounds;
    Vector2 touchPoint;

    public SettingsScreen(Game game) {
        super(game);

        guiCam = new Camera2D(glGraphics, 640, 960);
        backBounds = new Rectangle(0, 0, 64, 64);
        soundBounds = new Rectangle(280, 400, 128, 128);
        touchPoint = new Vector2();
        batcher = new SpriteBatcher(glGraphics, 100);
    }

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);
            touchPoint.set(event.x, event.y);
            guiCam.touchToWorld(touchPoint);

            if(event.type == TouchEvent.TOUCH_UP) {
                if (OverlapTester.pointInRectangle(backBounds, touchPoint)) {
                    game.setScreen(new MainMenuScreen(game));
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

        batcher.beginBatch(Assets.itemsBad);
        batcher.drawSprite(330, 440, 128, 128, Settings.soundEnabled?Assets.soundOn:Assets.soundOff);
        batcher.drawSprite(32, 32, 64, 64, Assets.arrow);
        batcher.endBatch();

        batcher.beginBatch(Assets.items);
        batcher.drawSprite(460, 760, 600, 100, Assets.SettingsRegion);
        batcher.endBatch();

        gl.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void dispose() {
    }
}
