package bar.vvx.base.jda.utilities;

import bar.vvx.base.jda.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Mention {
    public static void sendGuildPrefix(MessageReceivedEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription("This guild's prefix is `" + Main.getPrefix(event.getGuild().getIdLong()) + "`.");
        builder.setColor(0x1AE6C);

        event.getMessage().replyEmbeds(builder.build()).queue();

        builder.clear();
    }
}
