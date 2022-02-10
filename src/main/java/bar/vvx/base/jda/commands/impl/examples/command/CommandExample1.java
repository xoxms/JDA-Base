package bar.vvx.base.jda.commands.impl.examples.command;

import bar.vvx.base.jda.commands.Command;
import bar.vvx.base.jda.commands.CommandInfo;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

/* add `@CommandInfo` annotation */
@CommandInfo(
        name = "example1",
        alias = {"ex1", "e1"}
)
public class CommandExample1 extends Command {
    @Override
    public void execute() throws Exception {
        /* create `EmbedBuilder` object */
        EmbedBuilder embedBuilder = new EmbedBuilder();

        /* set title */
        embedBuilder.setTitle("Command Example 1");

        /* set description */
        embedBuilder.setDescription("This is an example of a command.");

        /* set colour */
        /* in () put colour you want */
        /* you can use any of these methods: */
        embedBuilder.setColor(0xFF0000);
        embedBuilder.setColor(16711680);
        embedBuilder.setColor(Color.RED);
        embedBuilder.setColor(new Color(0xFF0000));
        embedBuilder.setColor(new Color(255, 0, 0));

        /* send embed */
        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        /* or */
        event.getMessage().replyEmbeds(embedBuilder.build()).queue();

        /* clear `embedBuilder` (OPTIONAL) */
        embedBuilder.clear();
    }
}
