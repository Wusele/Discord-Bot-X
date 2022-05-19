package me.wusel.testing.discordbot.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.http.HttpAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.twitch.TwitchStreamAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.vimeo.VimeoAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.wusel.testing.Main;
import org.javacord.api.audio.AudioConnection;
import org.javacord.api.audio.AudioSource;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;

import java.util.HashMap;

public class AudioManager {

    private HashMap<Server, AudioData> connections;

    public AudioManager() {
        connections = new HashMap<>();
    }

    public void playAudio(String audioLink, AudioConnection audioConnection, Server server, AudioSourceType audioSourceType, int volume) {
        AudioPlayerManager playerManager =  new DefaultAudioPlayerManager();
        switch (audioSourceType) {
            case YOUTUBE -> playerManager.registerSourceManager(new YoutubeAudioSourceManager());
            case TWITCH -> playerManager.registerSourceManager(new TwitchStreamAudioSourceManager());
            case HTTP -> playerManager.registerSourceManager(new HttpAudioSourceManager());
        }

        playerManager.registerSourceManager(new VimeoAudioSourceManager());

        AudioPlayer player = playerManager.createPlayer();
        System.out.println("Created player");

        player.setVolume(volume);

        AudioSource source = new LavaplayerAudioSource(Main.getDiscordBot().getApi(), player);
        audioConnection.setAudioSource(source);

        playerManager.loadItem(audioLink, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                player.playTrack(audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                for (AudioTrack track : audioPlaylist.getTracks()) {
                    player.playTrack(track);
                }
            }

            @Override
            public void noMatches() {
                //channel.sendMessage("Video not found!");
            }

            @Override
            public void loadFailed(FriendlyException e) {
                //channel.sendMessage("Error loading video!");
            }
        });
        connections.put(server, new AudioData(player, playerManager, source, audioConnection));
    }

    public boolean isPlaying(Server server) {
        return connections.containsKey(server);
    }

    public void stopAudio(Server server) {
        if (isPlaying(server)) {
            AudioConnection connection = connections.get(server).getAudioConnection();
            connection.close();
            connections.remove(server);
        }
    }

    public void setAudioSource(Server server, AudioSource source) {
        if (isPlaying(server)) {
            AudioConnection connection = connections.get(server).getAudioConnection();
            connection.setAudioSource(source);
        }
    }

    public AudioData getAudioData(Server server) {
        if (isPlaying(server))
            return connections.get(server);
        return null;
    }
}
