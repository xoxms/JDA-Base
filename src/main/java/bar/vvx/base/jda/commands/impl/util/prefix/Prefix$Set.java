package bar.vvx.base.jda.commands.impl.util.prefix;

import bar.vvx.base.jda.Main;
import bar.vvx.base.jda.commands.Command;
import bar.vvx.base.jda.commands.CommandInfo;
import bar.vvx.base.jda.utilities.Colours;
import bar.vvx.base.jda.utilities.PrefixManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

@CommandInfo(
        name = "set",
        userDelay = 30
)
public class Prefix$Set extends Command {
    @Override
    public void execute() throws Exception {
        if (!Main.getJda().getGuildById(event.getGuild().getId()).getMemberById(event.getAuthor().getId()).hasPermission(Permission.ADMINISTRATOR)) {
          replyWarning(event, "You do not have permission to use this command!");
          revertUserCooldown();
        } else if (args.length == 1) {
            PrefixManager.setPrefix(event.getGuild(), args[0]);
            EmbedBuilder builder = new EmbedBuilder();
            builder.setDescription("This guild's prefix has been set to `" + args[0] + "`.");
            builder.setColor(Colours.COMPLETED);
            event.getChannel().sendTyping().queue();
            event.getMessage().replyEmbeds(builder.build()).queue();
            builder.clear();
        } else if (args.length > 1) {
            replyWarning(event, "Prefix cannot contain space(s).");
            revertUserCooldown();
        } else {
            replyWarning(event, "You must specify a prefix.");
            revertUserCooldown();
        }
    }
}
