package bar.vvx.base.jda.commands.impl.games.minecraft.serverstatuschecker;

import bar.vvx.base.jda.commands.Command;
import bar.vvx.base.jda.commands.CommandInfo;
import bar.vvx.base.jda.commands.SubCommand;

@CommandInfo(
        name = "ServerStatusChecker",
        alias = {"ssc", "status", "server"},
        userDelay = 1
)
public class ServerStatusChecker extends SubCommand {
    @Override
    public void execute() throws Exception {
        if (args.length < 1) {
            replyWarning("Please specify a sub-command.");
            return;
        }
        break1 : {
            for (Command command : subCommands) {
                if (command.alias.contains(args[0])) {
                    break break1;
                }
            }
            replyWarning("Unknown sub-command.");
        }
    }

    @Override
    protected void addCommand() {
        addCommand(new ServerStatusChecker$SinglePort());
        addCommand(new ServerStatusChecker$MultiplePorts());
    }
}
