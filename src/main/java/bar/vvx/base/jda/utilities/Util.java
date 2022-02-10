package bar.vvx.base.jda.utilities;

import bar.vvx.base.jda.Main;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Objects;

public class Util {
    public String getSelfMention() {
        return "<@!" + Main.getJda().getSelfUser().getIdLong() + ">";
    }

    public boolean checkMention(String message) {
        return message.equals(Main.getJda().getSelfUser().getAsMention()) || message.equals("<@" + Main.getJda().getSelfUser().getAsMention().split("@")[1].split(">")[0] + ">") || message.equals("<@!" + Main.getJda().getSelfUser().getAsMention().split("@")[1].split(">")[0] + ">");
    }

    public Member authorToGuildMember(MessageReceivedEvent event) {
        return Objects.requireNonNull(Main.getJda().getGuildById(event.getGuild().getId())).getMemberById(event.getAuthor().getId());
    }
}
