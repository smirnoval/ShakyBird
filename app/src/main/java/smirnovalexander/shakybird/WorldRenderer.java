package smirnovalexander.shakybird;

import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

import smirnovalexander.framework.gl.Animation;
import smirnovalexander.framework.gl.Camera2D;
import smirnovalexander.framework.gl.SpriteBatcher;
import smirnovalexander.framework.gl.TextureRegion;
import smirnovalexander.framework.impl.GLGraphics;

public class WorldRenderer {
    GLGraphics glGraphics;
    World world;
    Camera2D cam;
    SpriteBatcher batcher;
    public int birdAngle;

    public WorldRenderer(GLGraphics glGraphics, SpriteBatcher batcher, World world) {
        this.glGraphics = glGraphics;
        this.world = world;
        this.cam = new Camera2D(glGraphics, 640, 960);
        this.batcher = batcher;
        birdAngle = 0;
    }

    public void render() {
        if(world.bird.position.x > cam.position.x )
            cam.position.x = world.bird.position.x;
        cam.setViewportAndMatrices();
        renderBackground();
        renderObjects();
    }

    public void renderBackground() {
        batcher.beginBatch(Assets.background);
        batcher.drawSprite(320, 480, 640, 960, Assets.backgroundRegion);
        batcher.endBatch();
    }

    public void renderObjects() {
        GL10 gl = glGraphics.getGL();
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        batcher.beginBatch(Assets.items);
        renderPipes();
        renderBird();
        renderPlatforms();
        batcher.endBatch();
        gl.glDisable(GL10.GL_BLEND);
    }

    private void renderBird() {
        TextureRegion keyFrame;
        keyFrame = Assets.birdJump.getKeyFrame(world.bird.stateTime, Animation.ANIMATION_LOOPING);
        switch(world.bird.state) {
            case Bird.BIRD_STATE_FALL:
                if (birdAngle > - 30)
                    birdAngle -= 3;
                batcher.drawSprite(world.bird.position.x, world.bird.position.y, 70, 70, birdAngle, Assets.birdStatic);
                break;
            case Bird.BIRD_STATE_JUMP:
                birdAngle = 30;
                batcher.drawSprite(world.bird.position.x, world.bird.position.y, 70, 70, birdAngle, keyFrame);
                break;
            case Bird.BIRD_STATE_HIT:
                if (birdAngle > - 45)
                    birdAngle -= 3;
                batcher.drawSprite(world.bird.position.x, world.bird.position.y, 70, 70, birdAngle, Assets.birdDead);
                break;
            default:
                batcher.drawSprite(world.bird.position.x, world.bird.position.y, 70, 70, keyFrame);
                break;
        }
    }

    private void renderPipes() {
        int len = world.pipes.size();
        for(int i = 0; i < len; i+=2) {
            Pipe pipeHigh = world.pipes.get(i);
            Pipe pipeLow = world.pipes.get(i+1);
            batcher.drawSprite(pipeHigh.position.x, pipeHigh.position.y, 100, 700, Assets.pipeHigh);
            batcher.drawSprite(pipeLow.position.x, pipeLow.position.y, 100, 700, Assets.pipeLow);
        }
    }

    private void renderPlatforms() {
        for (Iterator<Platform> iterator = world.platforms.iterator(); iterator.hasNext();) {
            Platform platform = iterator.next();
            batcher.drawSprite(platform.position.x, platform.position.y, 302, 100, Assets.platform);
        }
    }

}
