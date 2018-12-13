package com.argo_entertainment.reactiontime;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

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
    public static Music[] musics = new Music[3];

    public static Texture loadTexture (String file) {
        return new Texture(Gdx.files.internal(file));
    }

    public static void load () {
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/main.mp3"));
        music1 = Gdx.audio.newMusic(Gdx.files.internal("sounds/main2.mp3"));
        music2 = Gdx.audio.newMusic(Gdx.files.internal("sounds/main3.mp3"));

        musics[0] = music;
        musics[1] = music1;
        musics[2] = music2;

        playingBackgroundLoop(false);

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

    public static void playMusic (Music sound) {
        if(soundEnabled) { sound.setVolume(0.9f);  if(sound.isPlaying()) sound.stop(); else sound.play(); }
    }
    public static void playSound (Sound sound) {
        if(soundEnabled) sound.play(0.9f);
    }

    public static void playVoice (Sound sound) {
        if(soundEnabled) sound.play(0.5f);
    }

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
        if(!enable) stopBackgroundMusic(); else playBackgroundMusic(enable);
    }

    public static void playMusicAfterEnable(){
        playingBackgroundLoop(true);
    }


    public static void playBackgroundMusic(boolean soundEnabled){
        if(soundEnabled){
            playingBackgroundLoop(false);
        }
    }

    public static void disableMusic(){
        Preferences prefs = Gdx.app.getPreferences("settings.prefs");
        if(musics[0].isPlaying()){
            prefs.putInteger("saved_track", 1);
            prefs.flush();
        }
        if(musics[1].isPlaying()){
            prefs.putInteger("saved_track", 2);
            prefs.flush();
        }
        if(musics[2].isPlaying()){
            prefs.putInteger("saved_track", 3);
            prefs.flush();
        }
        Assets.setMusic(false);
    }

    public static void resumeMusic(){
        Assets.playMusicAfterEnable();
    }

    public static boolean playingBackgroundLoop(boolean afterEnable){
        boolean isPlay = false;

        if(afterEnable){
            Preferences prefs = Gdx.app.getPreferences("settings.prefs");
            int trackNumber = prefs.getInteger("saved_track", 0);

            if(trackNumber==1){
                musics[0].setVolume(0.8f);
                musics[0].play();
                isPlay = true;
                musics[0].setOnCompletionListener(new Music.OnCompletionListener() {
                    @Override
                    public void onCompletion(Music music) {
                        playingBackgroundLoop(false);
                    }
                });

            }
            if(trackNumber==2){
                musics[1].setVolume(0.8f);
                musics[1].play();
                isPlay = true;
                musics[1].setOnCompletionListener(new Music.OnCompletionListener() {
                    @Override
                    public void onCompletion(Music music) {
                        playingBackgroundLoop(false);
                    }
                });
            }
            if(trackNumber==3){
                musics[2].setVolume(0.8f);
                musics[2].play();
                isPlay = true;
                musics[2].setOnCompletionListener(new Music.OnCompletionListener() {
                    @Override
                    public void onCompletion(Music music) {
                        playingBackgroundLoop(false);
                    }
                });
            }

        }

        else {

            if (musics[0].isPlaying()||musics[1].isPlaying()||musics[2].isPlaying()) {
                return true;
            }

            int rand = MathUtils.random(0, 2);

            if (rand != 1 && rand != 2) {
                musics[0].setVolume(0.8f);
                musics[0].play();
                isPlay = true;
                musics[0].setOnCompletionListener(new Music.OnCompletionListener() {
                    @Override
                    public void onCompletion(Music music) {
                        playingBackgroundLoop(false);
                    }
                });
            }

            if (rand != 0 && rand != 2) {
                musics[1].setVolume(0.8f);
                musics[1].play();
                isPlay = true;
                musics[1].setOnCompletionListener(new Music.OnCompletionListener() {
                    @Override
                    public void onCompletion(Music music) {
                        playingBackgroundLoop(false);
                    }
                });
            }

            if (rand != 0 && rand != 1) {
                musics[2].setVolume(0.8f);
                musics[2].play();
                isPlay = true;
                musics[2].setOnCompletionListener(new Music.OnCompletionListener() {
                    @Override
                    public void onCompletion(Music music) {
                        playingBackgroundLoop(false);
                    }
                });
            }
        }

        return isPlay;
    }

    public static void stopBackgroundMusic(){
        if(playingBackgroundLoop(false)){
            if(musics[0].isPlaying()) musics[0].stop();
            if(musics[1].isPlaying()) musics[1].stop();
            if(musics[2].isPlaying()) musics[2].stop();
        }
    }




}
