package me.wusel.testing.discordbot.audio.interfaces;

import javafx.scene.layout.Pane;
import me.wusel.testing.discordbot.audio.AudioData;
import org.javacord.api.entity.server.Server;

public interface Interface {

    public Pane getInterfacePane();

    public AudioData getAudioData();

    public Server getServer();


}
