package smirnovalexander.shakybird;

import smirnovalexander.framework.Music;
import smirnovalexander.framework.Sound;
import smirnovalexander.framework.gl.Font;
import smirnovalexander.framework.gl.Texture;
import smirnovalexander.framework.gl.TextureRegion;
import smirnovalexander.framework.impl.GLGame;
import smirnovalexander.framework.gl.Animation;
import java.util.Random;


public class Assets {
    public static Texture background;
    public static TextureRegion backgroundRegion;

    public static Texture items;
    public static TextureRegion logo;
    public static TextureRegion logoBird;
    public static TextureRegion start;
    public static TextureRegion highScores;
    public static TextureRegion highScoresRegion;
    public static TextureRegion soundOn;
    public static TextureRegion soundOff;
    public static TextureRegion arrow;
    public static TextureRegion pipeHigh;
    public static TextureRegion pipeLow;
    public static TextureRegion ready;
    public static TextureRegion readyHelpInfo;
    public static TextureRegion platform;

    public static TextureRegion pauseMenu;
    public static TextureRegion gameOver;
    public static TextureRegion pause;

    public static Animation birdJump;
    public static TextureRegion birdDead;
    public static TextureRegion birdStatic;

    public static Font font;

    public static Music music;
    public static Sound clickSound;
    public static Sound jumpSound;
    public static Sound hitSound;
    public static Sound gameOverSound;

    public static Random rand;

    public static void load(GLGame game) {
        rand = new Random();
        int randBackground = rand.nextInt(2 - 1 + 1) + 1;
        int randPipe = rand.nextInt(2 - 1 + 1) + 1;
        int randBird = rand.nextInt(3 - 1 + 1) + 1;
        background = new Texture(game, "background.png");
        if (randBackground == 1)
            backgroundRegion = new TextureRegion(background, 0, 0, 288, 511);
        else
            backgroundRegion = new TextureRegion(background, 291, 0, 288, 511);

        items = new Texture(game, "atlas.png");
        logo = new TextureRegion(items, 706, 180, 190, 50);
        logoBird = new TextureRegion(items, 226, 760, 38, 28);
        start = new TextureRegion(items, 706, 234, 108, 66);
        highScores = new TextureRegion(Assets.items, 827, 234, 108, 66);
        highScoresRegion = new TextureRegion(items , 510, 795, 290, 35);
        soundOff = new TextureRegion(Assets.items, 902, 170, 108, 66);
        soundOn = new TextureRegion(items, 907, 318, 108, 66);
        arrow = new TextureRegion(items, 510, 608, 64, 40);
        ready = new TextureRegion(items, 588, 117, 186, 56);
        readyHelpInfo = new TextureRegion(items, 581, 176, 124, 137);
        platform = new TextureRegion(items , 583, 0, 336, 100);

        if (randPipe == 1) {
            pipeHigh = new TextureRegion(items , 0, 645, 53, 321);
            pipeLow = new TextureRegion(items , 55, 645, 53, 321);
        }
        else {
            pipeHigh = new TextureRegion(items , 112, 645, 53, 321);
            pipeLow = new TextureRegion(items , 167, 645, 53, 321);
        }

        pauseMenu = new TextureRegion(items, 741, 673, 178, 75);
        gameOver = new TextureRegion(items, 789, 117, 196, 48);
        pause = new TextureRegion(items, 580, 606, 40, 40);

        if (randBird == 1) {
            birdJump = new Animation(0.08f,
                    new TextureRegion(items, 229, 760, 35, 30),
                    new TextureRegion(items, 229, 793, 35, 30),
                    new TextureRegion(items, 229, 824, 35, 30));
            birdStatic =  new TextureRegion(items, 229, 760, 35, 30);
            birdDead =  new TextureRegion(items, 229, 852, 35, 30);
        }
        else if (randBird == 2) {
            birdJump = new Animation(0.08f,
                    new TextureRegion(items, 271, 760, 35, 30),
                    new TextureRegion(items, 271, 793, 35, 30),
                    new TextureRegion(items, 271, 824, 35, 30));
            birdStatic =  new TextureRegion(items, 271, 760, 35, 30);
            birdDead =  new TextureRegion(items, 271, 852, 35, 30);
        }
        else {
            birdJump = new Animation(0.08f,
                    new TextureRegion(items, 315, 760, 35, 30),
                    new TextureRegion(items, 315, 793, 35, 30),
                    new TextureRegion(items, 315, 824, 35, 30));
            birdStatic =  new TextureRegion(items, 315, 760, 35, 30);
            birdDead =  new TextureRegion(items, 315, 852, 35, 30);
        }

        font = new Font(items, 733, 529, 16, 16, 20);

        music = game.getAudio().newMusic("music.mp3");
        music.setLooping(true);
        music.setVolume(0.5f);
        if(Settings.soundEnabled)
            music.play();
        clickSound = game.getAudio().newSound("click.ogg");
        jumpSound = game.getAudio().newSound("jump.ogg");
        hitSound = game.getAudio().newSound("hit.ogg");
        gameOverSound = game.getAudio().newSound("gameover.ogg");
    }

    public static void reload() {
        background.reload();
        items.reload();
        if(Settings.soundEnabled)
            music.play();
    }

    public static void playSound(Sound sound) {
        if(Settings.soundEnabled)
            sound.play(1);
    }
}
