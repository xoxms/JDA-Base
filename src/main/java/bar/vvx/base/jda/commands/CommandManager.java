package bar.vvx.base.jda.commands;

import bar.vvx.base.jda.Main;
import bar.vvx.base.jda.commands.impl.examples.command.CommandExample1;
import bar.vvx.base.jda.commands.impl.examples.subcommand.SubcommandExample1;
import bar.vvx.base.jda.commands.impl.games.minecraft.serverstatuschecker.ServerStatusChecker;
import bar.vvx.base.jda.commands.impl.util.prefix.Prefix;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static bar.vvx.base.jda.utilities.Mention.sendGuildPrefix;

public class CommandManager extends ListenerAdapter {
    private final List<Command> commands;

    public CommandManager() {
        this.commands = new ArrayList<>();
        this.addCommand();
    }

    private void addCommand() {
        // Example
        {
            // Normal Command
            addCommand(new CommandExample1());

            // Sub Command
            addCommand(new SubcommandExample1());
        }

        // Utilities
        {
            // Normal
            addCommand(new Prefix());
        }

        // Managers
        {
        }

        // Games
        {
            // Minecraft
            addCommand(new ServerStatusChecker());
        }
    }

    private void addCommand(Command command) {
        this.commands.add(command);
    }

    public boolean handleCommand(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (message.isEmpty()) {
            return false;
        } else {
            String prefix = Main.getPrefix(event.getGuild().getIdLong());
            this.getCommand(prefix, message).forEach((c) -> {
                try {
                    String[] args = null;
                    if (c.Type == 1) {
                        args = message.substring(message.split("\\s+")[0].length()).trim().split("\\s+");
                    } else if (c.Type == 2) {
                        String content = message;
                        do {
                            content = content.replaceAll(" {2}", " ");
                        } while (content.contains("  "));
                        String[] contentArgs = content.split("\\s+");
                        args = content.substring(contentArgs[0].length()).trim().substring(contentArgs[1].length()).trim().split("\\s+");
                    }

                    c.init(event);

                    if (c.globalCooldown != 0 && c.userCooldown != 0) {
                        if (!c.checkGlobalCooldown()) {
                            if (!c.checkUserCooldown()) {
                                c.setGlobalCurrentTime();
                                c.setUserCurrentTime();
                                if (c instanceof SubCommand) {
                                    c.init(event, args, event.getMessage().getContentRaw());
                                } else {
                                    c.init(event, args);
                                }
                            }
                        }
                    } else if (c.globalCooldown != 0) {
                        if (!c.checkGlobalCooldown()) {
                            c.setGlobalCurrentTime();
                            if (c instanceof SubCommand) {
                                c.init(event, args, event.getMessage().getContentRaw());
                            } else {
                                c.init(event, args);
                            }
                        }
                    } else if (c.userCooldown != 0) {
                        if (!c.checkUserCooldown()) {
                            c.setUserCurrentTime();
                            if (c instanceof SubCommand) {
                                c.init(event, args, event.getMessage().getContentRaw());
                            } else {
                                c.init(event, args);
                            }
                        }
                    } else {
                        if (c instanceof SubCommand) {
                            c.init(event, args, event.getMessage().getContentRaw());
                        } else {
                            c.init(event, args);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return true;
        }
    }

    public Stream<Command> getCommand(String prefix, String message) {
        return this.commands.stream().filter((c) -> {
            return c.is(prefix, message);
        });
    }

    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            // ADD ANY CODE
        } else if (Main.getUtil().checkMention(event.getMessage().getContentRaw())) {
            // ADD ANY CODE
            sendGuildPrefix(event);
        } else if (this.handleCommand(event)) {
            // ADD ANY CODE
        } else {
            // ADD ANY CODE
        }
    }
}
