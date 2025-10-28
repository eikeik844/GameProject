package main;

import javax.sound.sampled.*;
import java.io.File;

public class Sound {
    private Clip[] clips;    
    private String[] soundFiles;  

    public Sound() {
        soundFiles = new String[] {
            "Res/Sounds/gamewin.wav",     // 0
            "Res/Sounds/gameover.wav",    // 1
            "Res/Sounds/hitmonster.wav",  // 2
            "Res/Sounds/Items.wav",  	  // 3
            "Res/Sounds/playState.wav",   // 4
            "Res/Sounds/waitingState.wav"  // 5
        };

        clips = new Clip[soundFiles.length];
        loadAllSounds();
    }

    private void loadAllSounds() {
        for (int i = 0; i < soundFiles.length; i++) {
            try {
                File file = new File(soundFiles[i]);
                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                clips[i] = AudioSystem.getClip();
                clips[i].open(ais);
            } catch (Exception e) {
                System.out.println("Cannot load sound: " + soundFiles[i]);
            }
        }
    }

    public void play(int index) {
        if (clips[index] != null) {
            clips[index].setFramePosition(0);
            clips[index].start();
        }
    }

    public void loop(int index) {
        if (clips[index] != null) {
            clips[index].loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop(int index) {
        if (clips[index] != null && clips[index].isRunning()) {
            clips[index].stop();
        }
    }
}
