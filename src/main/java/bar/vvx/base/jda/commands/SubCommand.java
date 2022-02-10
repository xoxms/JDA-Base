package bar.vvx.base.jda.commands;

import bar.vvx.base.jda.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class SubCommand extends Command {
    private boolean runSubCommand;

    protected final List<Command> subCommands;

    public SubCommand() {
        super();
        subCommands = new ArrayList<>();
        addCommand();
    }

    protected abstract void addCommand();

    protected void addCommand(Command command) {
        subCommands.add(command);
    }

    public void init(MessageReceivedEvent event, String[] args, String message) throws Exception {
        this.runSubCommand = true;
        this.event = event;
        this.args = args;

        this.execute();

// TODO : Add support for guild cooldowns
        if (this.runSubCommand) {
            this.getSubCommand(message).forEach((c) -> {
                try {
                    String[] _args = new String[args.length - 1];
                    System.arraycopy(args, 1, _args, 0, args.length - 1);
                    String _message = message.substring(message.split("\\s+")[0].length() + 1);
                    if (c.globalCooldown != 0 && c.userCooldown != 0) {
                        if (!c.checkGlobalCooldown()) {
                            if (!c.checkUserCooldown()) {
                                c.init(event);
                                c.setGlobalCurrentTime();
                                c.setUserCurrentTime();
                                c.init(_args, _message);
                            }
                        }
                    } else if (c.globalCooldown != 0) {
                        if (!c.checkGlobalCooldown()) {
                            c.setGlobalCurrentTime();
                            c.init(event, _args, _message);
                        }
                    } else if (c.userCooldown != 0) {
                        if (!c.checkUserCooldown()) {
                            c.init(event);
                            c.setUserCurrentTime();
                            c.init(_args, _message);
                        }
                    } else {
                        c.init(event, _args);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    protected void cancelSubCommand() {
        runSubCommand = false;
    }

    public boolean is(String raw) {
        String content = raw;
        if (!(content.startsWith(Main.getUtil().getSelfMention() + " ")) && !(content.replaceFirst(Main.getUtil().getSelfMention(), Main.getUtil().getSelfMention() + " ").equalsIgnoreCase(content))) content = content.replaceFirst(Main.getUtil().getSelfMention(), Main.getUtil().getSelfMention() + " ");
        String[] args = content.split("\\s+");
        for (String s : this.alias) {
            try {
                if (args[0].equalsIgnoreCase(s)) {
                    this.Type = 1;
                    return true;
                } else if ((args[0] + args[1]).equalsIgnoreCase("<@!" + Main.getJda().getSelfUser().getIdLong() + ">" + s)) {
                    this.Type = 2;
                    return true;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        return false;
    }

    public Stream<Command> getSubCommand(String message) {
        String _message;
        try {
            _message = message.substring(message.split("\\s+")[0].length() + 1);
        } catch (StringIndexOutOfBoundsException e) {
            _message = "";
        }
        String final_message = _message;
        return this.subCommands.stream().filter((c) -> {
            if (c instanceof SubCommand) {
                SubCommand sc = (SubCommand) c;
                return sc.is(final_message);
            } else if (c != null) {
                return c.is("", final_message);
            } else {
                return false;
            }
        });
    }
}
