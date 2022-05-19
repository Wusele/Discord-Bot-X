package me.wusel.testing.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextFlow;
import me.wusel.testing.Main;
import me.wusel.testing.discordbot.audio.AudioManager;
import me.wusel.testing.discordbot.audio.AudioSourceType;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.channel.VoiceChannel;
import org.javacord.api.entity.server.Server;

import java.io.Console;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

public class DashboardController {

    public Pane audioPane;
    public Pane homePane;
    public Pane consolePane;
    public Pane audioInterfacePane;

    public Button homeButton;
    public Button audioButton;
    public Button consoleButton;

    public TextArea consoleArea;

    public Pane currentPane;

    private static DashboardController instance;
    public ChoiceBox serverChoice;
    public ChoiceBox channelChoice;
    public TextField linkField;
    public Button playButton;
    public Button interfaceButton;


    public static void setInstance() {
        instance = new DashboardController();
    }

    private void setConsoleArea() {

        return;
        /*

        TextArea textArea = new TextArea();

        textArea.skinProperty().addListener(new ChangeListener<Skin<?>>() {
            @Override
            public void changed(ObservableValue<? extends Skin<?>> observableValue, Skin<?> skin, Skin<?> t1) {
                Region r = (Region) t1.getNode();
                r.setBackground(Background.EMPTY);

                r.getChildrenUnmodifiable().stream()
                        .filter(n -> n instanceof Region)
                        .map(n -> (Region) n)
                        .forEach(n -> n.setBackground(Background.EMPTY));


                r.getChildrenUnmodifiable().stream().
                        filter(n -> n instanceof Control).
                        map(n -> (Control) n).
                        forEach(c -> c.skinProperty().addListener(this)); // *
            }
        });

        textArea.setText("Test Text!");

        homePane.getChildren().add(textArea);
         */
    }

    public void homeButtonPressed(ActionEvent actionEvent) {
        if (getCurrentPane() == homePane) return;
        setCurrentPane(homePane);
    }

    public void audioButtonPressed(ActionEvent actionEvent) {
        if (getCurrentPane() == audioPane) return;

        setCurrentPane(audioPane);
        setAudioPane();
    }

    public void consoleButtonPressed(ActionEvent actionEvent) {
        if (getCurrentPane() == consolePane) return;
        setCurrentPane(consolePane);
        setConsoleArea();
    }

    public void setCurrentPane(Pane pane) {
        if (currentPane != null) {
            currentPane.setVisible(false);
            currentPane.setDisable(true);
        }
        currentPane = pane;
        currentPane.setDisable(false);
        currentPane.setVisible(true);
    }

    public Pane getCurrentPane() {
        if (currentPane == null)
            currentPane = homePane;

        return currentPane;
    }

    public static DashboardController getInstance() {
        return instance;
    }



    private HashMap<String, Long> servers;
    private HashMap<String, Long> channels;

    private void setAudioPane() {
        servers = new HashMap<>();
        Collection<Server> server =  Main.getDiscordBot().getApi().getServers();

        server.forEach(new Consumer<Server>() {
            @Override
            public void accept(Server server) {
                String name = server.getName();
                Long id = server.getId();
                servers.put(name, id);
                if (!serverChoice.getItems().contains(name))
                serverChoice.getItems().add(name);
            }
        });
    }




    public void serverInputSet() {
        if (serverChoice.getSelectionModel().getSelectedItem() == null) return;
        channels = new HashMap<>();
        String serverName = serverChoice.getSelectionModel().getSelectedItem().toString();
        Long serverId = servers.get(serverName);
        Server server = Main.getDiscordBot().getApi().getServerById(serverId).get();

        if (Main.getDiscordBot().getAudioManager().isPlaying(server)) {
            System.out.println("Is playing music on that server!");
            interfaceButton.setVisible(true);
            interfaceButton.setDisable(false);
            return;
        }


        server.getVoiceChannels().forEach(serverVoiceChannel -> {
            String channelName = serverVoiceChannel.getName();
            Long channelId = serverVoiceChannel.getId();
            channels.put(channelName, channelId);
            channelChoice.getItems().add(channelName);
        });

    }

    private Server getServer() {
        if (serverChoice.getSelectionModel().getSelectedItem() == null) return null;
        Server server;
        String serverName = serverChoice.getSelectionModel().getSelectedItem().toString();
        Long serverId = servers.get(serverName);

        server = Main.getDiscordBot().getApi().getServerById(serverId).get();

        return server;
    }

    private ServerVoiceChannel getVoiceChannel() {
        ServerVoiceChannel voiceChannel;
        String channelName = channelChoice.getSelectionModel().getSelectedItem().toString();
        if (channelName == null) return null;
        Long channelId = channels.get(channelName);


        voiceChannel = getServer().getVoiceChannelById(channelId).get();


        return voiceChannel;
    }

    public void playButtonPressed(ActionEvent actionEvent) {
        Server server = getServer();
        if (server == null) return;
        ServerVoiceChannel voiceChannel = getVoiceChannel();
        if (voiceChannel == null) return;
        String link = linkField.getText();
        if (link == null) return;

        AudioManager audioManager = Main.getDiscordBot().getAudioManager();

        if (audioManager.isPlaying(server)) audioManager.stopAudio(server);

        voiceChannel.connect().thenAccept(audioConnection -> {
           audioManager.playAudio(link, audioConnection, server, AudioSourceType.YOUTUBE, 100);
        });

    }

    public void interfaceButtonPressed(ActionEvent actionEvent) {
        setCurrentPane(audioInterfacePane);
    }

}
