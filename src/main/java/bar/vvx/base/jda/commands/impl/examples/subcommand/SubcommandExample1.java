package bar.vvx.base.jda.commands.impl.examples.subcommand;

import bar.vvx.base.jda.commands.CommandInfo;
import bar.vvx.base.jda.commands.SubCommand;

/* add `@CommandInfo` annotation */
@CommandInfo(
        // specify its name which is used to call the command
        name = "sub1",
        // (OPTIONAL) add alias(es) for the command
        alias = {"subcommand1"},
        // (OPTIONAL) add userDelay & globalDelay to specify the delay between each command call
        userDelay = 60 /* seconds */,
        globalDelay = 1 /* second */
)
public class SubcommandExample1 /* first, extends `SubCommand` class */ extends SubCommand {
    // implement `execute` method
    @Override
    public void execute() throws Exception {
        /*

        this method will execute every time the command is called.
        even the subcommand is called.

        */

        /*

        but you can cancel the subcommand execution by using `cancelSubCommand()` method

        */

        /*

        provided variables :
            MessageReceivedEvent event
            String[] args
            String message

        */

        /*

        args is command arguments (from discord)

        Example :
            in discord -> ,sub1 test hello world               space

            args[0] = test
            args[1] = hello
            args[2] = world
            args[3] = space

        */

        /*

        message is the message which is sent by the user

        Example :
            in discord -> ,sub1 test hello world               space

            message = test hello world               space

        */

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("cancel")) {
                cancelSubCommand();
            }
        }
    }

    // implement `addCommand` method
    @Override
    protected void addCommand() {
        /*

        this method is used to add its SubCommand

        SubCommand can be either extends a SubCommand or a Command

        */

        addCommand(new SubCommandExaple1$ping());
    }
}
