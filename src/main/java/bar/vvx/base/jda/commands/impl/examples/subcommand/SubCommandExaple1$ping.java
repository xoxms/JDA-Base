package bar.vvx.base.jda.commands.impl.examples.subcommand;

import bar.vvx.base.jda.commands.Command;
import bar.vvx.base.jda.commands.CommandInfo;

/*
don't forget to add `@CommandInfo` annotation
*/
@CommandInfo(
        name = "ping"
)
// you can name the class name whatever you want but I prefer to use the same name as its command + $ + subcommand name
public class SubCommandExaple1$ping /* you can even extends another `SubCommand` class but you can extends `Command` class too */ extends Command {
    // implement the `execute` method
    @Override
    public void execute() throws Exception {
        // only send message to the channel
        {
            /* sendPlainMessage(); */
        }

        // reply with mention
        {
            /* replyWithMention(); */
        }

        // reply without mention
        {
            /* replyWithoutMention(); */
        }
    }

    private void sendPlainMessage() {
        sendMessage("pong");
        /* or */
        event.getChannel().sendMessage("pong").queue();
    }

    private void replyWithMention() {
        replyMessage("pong");
        /* or */
        event.getMessage().reply("pong").queue();
        /* or */
        event.getMessage().reply("pong").mentionRepliedUser(true).queue();
        /* or */
        event.getChannel().sendMessage("pong").reference(event.getMessage()).queue();
        /* or */
        event.getChannel().sendMessage("pong").reference(event.getMessage()).mentionRepliedUser(true).queue();
    }

    private void replyWithoutMention() {
        event.getMessage().reply("pong").mentionRepliedUser(false).queue();
        /* or */
        event.getChannel().sendMessage("pong").reference(event.getMessage()).mentionRepliedUser(false).queue();
    }
}
