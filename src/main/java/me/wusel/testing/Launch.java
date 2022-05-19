package me.wusel.testing;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.wusel.testing.controllers.DashboardController;
import me.wusel.testing.discordbot.DiscordBot;

import java.io.IOException;

public class Launch extends Application {

    private Stage stage;

    private static Launch instance;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        instance = this;
    }

    @Override
    public void stop() throws Exception {

        if (Main.getDiscordBot() != null)
        Main.getDiscordBot().disonnect();

        Main.getInstance().onDisable();
    }

    public boolean handleLogin(String token) {
        DiscordBot discordBot = new DiscordBot(token);

        final Boolean[] b = {false};

        discordBot.setOnConnect(new Runnable() {
            @Override
            public void run() {
                try {
                    b[0] = true;
                    Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
                    Scene scene = new Scene(root);
                    DashboardController.setInstance();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stage.hide();
                            stage.setScene(scene);
                            stage.setResizable(false);
                            stage.show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        discordBot.setup();

        return b[0];
    }


    public static Launch getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        launch();
    }
}