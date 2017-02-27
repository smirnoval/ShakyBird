package smirnovalexander.shakybird;

import smirnovalexander.framework.Music;
import smirnovalexander.framework.Sound;
import smirnovalexander.framework.gl.Font;
import smirnovalexander.framework.gl.Texture;
import smirnovalexander.framework.gl.TextureRegion;
import smirnovalexander.framework.impl.GLGame;

public class Assets {
    public static Texture background;
    public static TextureRegion backgroundRegion;

    public static Texture items;
    public static Texture itemsBad;
    public static TextureRegion logo;
    public static TextureRegion logoBird;
    public static TextureRegion start;
    public static TextureRegion highScores;
    public static TextureRegion settings;
    public static TextureRegion highScoresRegion;
    public static TextureRegion SettingsRegion;
    public static TextureRegion soundOn;
    public static TextureRegion soundOff;
    public static TextureRegion arrow;

    public static Font font;

    public static Music music;
    public static Sound clickSound;


    public static void load(GLGame game) {
        background = new Texture(game, "background.png");
        backgroundRegion = new TextureRegion(background, 0, 0, 640, 960);

        items = new Texture(game, "atlas.png");
        itemsBad = new Texture(game, "items.png");
        logo = new TextureRegion(items, 706, 180, 190, 50);
        logoBird = new TextureRegion(items, 226, 760, 38, 28);
        start = new TextureRegion(items, 706, 234, 108, 66);
        highScores = new TextureRegion(Assets.items, 827, 234, 108, 66);
        settings = new TextureRegion(Assets.items, 902, 170, 108, 66);
        highScoresRegion = new TextureRegion(itemsBad , 0, 257, 300, 110 / 3);
        SettingsRegion = new TextureRegion(items , 587, 382, 300, 50);
        soundOff = new TextureRegion(itemsBad, 0, 0, 64, 64);
        soundOn = new TextureRegion(itemsBad, 64, 0, 64, 64);
        arrow = new TextureRegion(itemsBad, 0, 64, 64, 64);

        font = new Font(itemsBad, 224, 0, 16, 16, 20);

        music = game.getAudio().newMusic("music.mp3");
        music.setLooping(true);
        music.setVolume(0.5f);
        if(Settings.soundEnabled)
            music.play();
        clickSound = game.getAudio().newSound("click.ogg");
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
