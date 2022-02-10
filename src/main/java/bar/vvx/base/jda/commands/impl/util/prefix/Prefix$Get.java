package bar.vvx.base.jda.commands.impl.util.prefix;

import bar.vvx.base.jda.commands.Command;
import bar.vvx.base.jda.commands.CommandInfo;

import static bar.vvx.base.jda.utilities.Mention.sendGuildPrefix;

@CommandInfo(
        name = "get",
        userDelay = 10
)
public class Prefix$Get extends Command {
    @Override
    public void execute() throws Exception {
        sendGuildPrefix(event);
    }
}
