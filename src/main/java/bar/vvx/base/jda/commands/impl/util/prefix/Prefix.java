package bar.vvx.base.jda.commands.impl.util.prefix;

import bar.vvx.base.jda.commands.Command;
import bar.vvx.base.jda.utilities.Colours;
import bar.vvx.base.jda.commands.CommandInfo;
import bar.vvx.base.jda.commands.SubCommand;
import net.dv8tion.jda.api.EmbedBuilder;

@CommandInfo(
        name = "prefix"
)
public class Prefix extends SubCommand {
    @Override
    public void execute() throws Exception {
        if (args.length == 0 || args[0].isEmpty()) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Prefix Commands");
            builder.setDescription("Prefix commands are used to change the prefix of the bot.\n" +
                    "Usage: `prefix <command>`");
            builder.addField("Get", "Get guild's prefix.", false);
            builder.addField("Set", "Set guild's prefix.\n(require administrator permission)", false);
            builder.addField("Reset", "Reset guild's prefix to default.\n(require administrator permission)", false);
            builder.setColor(Colours.INFO);
            event.getChannel().sendTyping().queue();
            event.getMessage().replyEmbeds(builder.build()).queue();
            builder.clear();

            return;
        }

        for (Command c : subCommands) {
            if (args[0].equalsIgnoreCase(c.name)) {
                return;
            }
        }

        replyWarning(event, "Unknown command.");
    }

    @Override
    protected void addCommand() {
        addCommand(new Prefix$Get());
        addCommand(new Prefix$Set());
        addCommand(new Prefix$Reset());
    }
}
