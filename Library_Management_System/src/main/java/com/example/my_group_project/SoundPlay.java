package com.example.my_group_project;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundPlay {
    public static void playSound(String soundFilePath) {
        try {
            URL resource = SoundPlay.class.getResource(soundFilePath);
            if (resource == null) {
                System.err.println("Resource not found: " + soundFilePath);
                return;
            }
            String path = resource.toExternalForm();
            Media sound = new Media(path);
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
