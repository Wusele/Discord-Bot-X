package me.wusel.testing;

import me.wusel.testing.discordbot.DiscordBot;

public class Main {

    private static Main instance;

    private static DiscordBot discordBot;

    public void onEnable(DiscordBot bot) {
        instance = this;

        discordBot = bot;



    }

    public void onDisable() {
        System.out.println("Disabling!");
    }


    public static DiscordBot getDiscordBot() {
        return discordBot;
    }

    public static Main getInstance() {
        return instance;
    }
}
