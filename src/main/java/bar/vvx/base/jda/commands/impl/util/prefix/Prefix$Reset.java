package bar.vvx.base.jda.commands.impl.util.prefix;

import bar.vvx.base.jda.utilities.Colours;
import bar.vvx.base.jda.Main;
import bar.vvx.base.jda.commands.Command;
import bar.vvx.base.jda.commands.CommandInfo;
import bar.vvx.base.jda.config;
import bar.vvx.base.jda.utilities.PrefixManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;

@CommandInfo(
        name = "reset",
        userDelay = 30
)
public class Prefix$Reset extends Command {
    @Override
    public void execute() throws Exception {
        if (!Main.getUtil().authorToGuildMember(event).hasPermission(Permission.ADMINISTRATOR)) {
            replyWarning(event, "You do not have permission to use this command!");
            revertUserCooldown();
        } else {
            PrefixManager.setPrefix(event, config.defaultPrefix);
            EmbedBuilder builder = new EmbedBuilder();
            builder.setDescription("This guild's prefix has been set to `" + config.defaultPrefix + "`.");
            builder.setColor(Colours.COMPLETED);
            event.getChannel().sendTyping().queue();
            event.getMessage().replyEmbeds(builder.build()).queue();
            builder.clear();
        }
    }
}
