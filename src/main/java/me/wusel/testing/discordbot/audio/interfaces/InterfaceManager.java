package me.wusel.testing.discordbot.audio.interfaces;

import javafx.scene.layout.Pane;
import me.wusel.testing.discordbot.audio.AudioData;
import me.wusel.testing.discordbot.audio.AudioManager;
import org.javacord.api.entity.server.Server;

import java.util.HashMap;

public class InterfaceManager {

    private HashMap<Server, Interface> interfaces;

    public InterfaceManager() {
        interfaces = new HashMap<>();
    }

    public boolean hasInterface(Server server) {
        return interfaces.containsKey(server);
    }

    public Interface getInterface(Server server) {
        if (!hasInterface(server)) return null;

        return interfaces.get(server);
    }

    public Interface createInterface(Server server, Pane pane, AudioData audioData) {
        Interface interFace = null;

        if (hasInterface(server)) return null;

        interFace = new Interface() {
            @Override
            public Pane getInterfacePane() {
                return pane;
            }
            @Override
            public AudioData getAudioData() {
                return audioData;
            }
            @Override
            public Server getServer() {
                return server;
            }
        };

        interfaces.put(server, interFace);
        return interFace;
    }
}
