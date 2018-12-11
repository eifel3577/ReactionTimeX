package com.argo_entertainment.reactiontime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import java.util.Random;

public class Assets {
    public static boolean soundEnabled = false;
    public static Music music;
    public static Music music1;
    public static Music music2;


    public static Sound clickSound;

    public static Sound inAppOk;

    public static Sound freezeSound;
    public static Sound boombSound;

    public static Sound moneySound;
    public static Sound wallSound;

    public static Sound newBlaclHole;

    public static Music introSound;
    public static Sound gameOver;
    public static Sound niceWork;
    public static Sound flash_1, flash_2;
    public static Sound[] rndVoices = new Sound[6];

    public static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static void load () {

        //TODO реализовать воспроизведение треков в ранодомном порядке
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/main.mp3"));
        music1 = Gdx.audio.newMusic(Gdx.files.internal("sounds/main2.mp3"));
        music2 = Gdx.audio.newMusic(Gdx.files.internal("sounds/main3.mp3"));

        Music[] arrayMusic = {music,music1,music2};
        shuffleArray(arrayMusic);

        if(soundEnabled) {
            for(int i=0;i<arrayMusic.length;i++){
                arrayMusic[i].setVolume(0.8f);
                arrayMusic[i].play();
            }
            //Music randomMusic = arrayMusic[MathUtils.random(0,2)];
            //randomMusic.setVolume(0.8f);
            //randomMusic.play();
        }




        flash_1 = Gdx.audio.newSound(Gdx.files.internal("sounds/Thunder1.mp3"));
        flash_2 = Gdx.audio.newSound(Gdx.files.internal("sounds/Thunder2.mp3"));

        introSound = Gdx.audio.newMusic(Gdx.files.internal("sounds/intro.mp3"));
        clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/click.mp3"));
        inAppOk = Gdx.audio.newSound(Gdx.files.internal("sounds/in-app_purchase.mp3"));
        freezeSound = Gdx.audio.newSound(Gdx.files.internal("sounds/freeze.mp3"));
        boombSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bomb.mp3"));
        newBlaclHole = Gdx.audio.newSound(Gdx.files.internal("sounds/new_bh.mp3"));

        moneySound = Gdx.audio.newSound(Gdx.files.internal("sounds/money.mp3"));
        wallSound = Gdx.audio.newSound(Gdx.files.internal("sounds/wall.mp3"));

        gameOver = Gdx.audio.newSound(Gdx.files.internal("sounds/voices/game_over.mp3"));
        niceWork = Gdx.audio.newSound(Gdx.files.internal("sounds/voices/nice_work.mp3"));

        rndVoices[0] = Gdx.audio.newSound(Gdx.files.internal("sounds/voices/great.mp3"));
        rndVoices[1] = Gdx.audio.newSound(Gdx.files.internal("sounds/voices/awesome.mp3"));
        rndVoices[2] = Gdx.audio.newSound(Gdx.files.internal("sounds/voices/insane.mp3"));
        rndVoices[3] = Gdx.audio.newSound(Gdx.files.internal("sounds/voices/perfect.mp3"));
        rndVoices[4] = Gdx.audio.newSound(Gdx.files.internal("sounds/voices/incredable.mp3"));
        rndVoices[5] = Gdx.audio.newSound(Gdx.files.internal("sounds/voices/amazing_job.mp3"));

    }


    public static void shuffleArray(Music[]musics){
        Random random = new Random();
        for(int i = musics.length - 1;i > 0;i--){
            int index = random.nextInt(i+1);
            Music randomMusic = musics[index];
            musics[index] = musics[i];
            musics[i] = randomMusic;
        }
    }

    public static void playMusic (Music sound) {
        if(soundEnabled) { sound.setVolume(0.9f);  if(sound.isPlaying()) sound.stop(); else sound.play(); }
    }
    public static void playSound (Sound sound) {
        if(soundEnabled) sound.play(0.9f);
    }

    public static void playVoice (Sound sound) {
        if(soundEnabled) sound.play(0.5f);
    }

    //TODO использовать для таска рандомного воспроизведения
    public static void playVoiceRandom () {
        if(soundEnabled){
            int random_number1 = (int) (Math.random() * 5);
            rndVoices[random_number1].play(0.5f);
        }
    }

    public static void setSound (boolean enable) {
        soundEnabled = enable;
    }

    public static void setMusic (boolean enable) {
        if(!enable) music.stop(); else music.play();
    }
}
