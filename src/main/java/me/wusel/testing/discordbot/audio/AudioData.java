package me.wusel.testing.discordbot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;

public class AudioData {

    private AudioPlayer audioPlayer;
    private AudioPlayerManager playerManager;
    private AudioSource audioSource;
    private AudioConnection audioConnection;

    public AudioData(AudioPlayer audioPlayer, AudioPlayerManager playerManager, AudioSource audioSource, AudioConnection audioConnection) {
        this.audioPlayer = audioPlayer;
        this.playerManager = playerManager;
        this.audioSource = audioSource;
        this.audioConnection = audioConnection;
    }

    public void setAudioPlayer(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    public void setPlayerManager(AudioPlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    public void setAudioSource(AudioSource audioSource) {
        this.audioSource = audioSource;
    }

    public void setAudioConnection(AudioConnection audioConnection) {
        this.audioConnection = audioConnection;
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public AudioPlayerManager getPlayerManager() {
        return playerManager;
    }

    public AudioSource getAudioSource() {
        return audioSource;
    }

    public AudioConnection getAudioConnection() {
        return audioConnection;
    }
}
