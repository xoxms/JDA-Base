package bar.vvx.base.jda.commands;

import bar.vvx.base.jda.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.lang3.Validate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public abstract class Command {
    public final String name;
    public final ArrayList<String> alias;

    protected final HashMap<Long, Long> userCooldowns;
    protected final HashMap<Long, Long> _userCooldowns;

    protected long userCooldown = 0;

    protected long globalCurrentTime = 0;
    protected long _globalCooldown = 0;
    protected long globalCooldown = 0;

    protected short Type = 1;

    protected MessageReceivedEvent event;
    protected String[] args;
    protected String message;

    public Command() {
        CommandInfo commandInfo = this.getClass().getAnnotation(CommandInfo.class);
        Validate.notNull(commandInfo, "CommandInfo annotation not found on class " + this.getClass().getName());
        this.name = commandInfo.name();
        this.alias = new ArrayList<>();
        Collections.addAll(this.alias, commandInfo.alias());
        this.alias.add(this.name);
        this.userCooldowns = new HashMap<>();
        this._userCooldowns = new HashMap<>();
        this.userCooldown = Long.parseLong(String.valueOf(commandInfo.userDelay())) * 1000;
        this.globalCooldown = Long.parseLong(String.valueOf(commandInfo.globalDelay())) * 1000;
        this.globalCurrentTime = System.currentTimeMillis() - this.globalCooldown;
    }

    public abstract void execute() throws Exception;

    public void init(MessageReceivedEvent event) {
        this.event = event;
    }

    /**
     * @param event is the event where the message was sent
     * @param args  are the arguments passed to the command
     */
    public void init(MessageReceivedEvent event, String[] args) throws Exception {
        this.event = event;
        this.args = args;

        this.execute();
    }

    public void init(String[] args, String message) throws Exception {
        this.args = args;
        this.message = message;

        this.execute();
    }

    public void init(MessageReceivedEvent event, String[] args, String message) throws Exception {
        this.message = message;
        this.init(event, args);
    }

    protected void sendMessage(String message) {
        this.event.getChannel().sendTyping().queue();
        this.event.getChannel().sendMessage(message).queue();
    }

    protected void replyMessage(String message) {
        this.event.getChannel().sendTyping().queue();
        this.event.getMessage().reply(message).queue();
    }

    public void replyWarning(MessageReceivedEvent event, String message) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(message);
        builder.setColor(0xFF0000);
        event.getMessage().replyEmbeds(builder.build()).queue();
        builder.clear();
    }

    public void replyWarning(String message) {
        this.replyWarning(this.event, message);
    }

    /**
     * @return true if there the url is Invalid ; cannot use URL
     */
    public boolean isInvalidURL(MessageReceivedEvent event, String[] args) {
        if (args.length == 0) {
            this.replyWarning(event, "Please provide a URL.");
            return true;
        } else if (args.length > 1) {
            this.replyWarning(event, "You can only provide 1 URL at a time.");
            return true;
        }
        try {
            new URL(args[0]);
        } catch (MalformedURLException e) {
            this.replyWarning(event, "Please enter a correct URL.");
            return true;
        }
        return false;
    }

    public boolean isInvalidURL(String[] args) {
        return this.isInvalidURL(this.event, args);
    }

    public boolean isInvalidURL() {
        return this.isInvalidURL(this.args);
    }

    public void sendUserCooldownWarning(MessageReceivedEvent event, boolean sendRestTime) {
        EmbedBuilder builder = new EmbedBuilder();
        if (sendRestTime) {
            builder.setDescription("This command's cooldown is " + Integer.parseInt(String.valueOf(this.userCooldown / 1000)) + " second(s).\nYou must wait " + Integer.parseInt(String.valueOf((this.userCooldown - (System.currentTimeMillis() - this.userCooldowns.get(this.event.getAuthor().getIdLong()))) / 1000)) + " more second(s) to use this command again.");
        }
        builder.setTitle("Please slow down.");
        builder.setColor(0xFF0000);
        event.getMessage().replyEmbeds(builder.build()).queue();
        builder.clear();
    }

    public void sendGlobalCooldownWarning(MessageReceivedEvent event, boolean sendRestTime) {
        EmbedBuilder builder = new EmbedBuilder();
        if (sendRestTime) {
            builder.setDescription("This command's global cooldown is " + Integer.parseInt(String.valueOf(this.globalCooldown / 1000)) + " second(s).\nYou must wait " + Integer.parseInt(String.valueOf((this.globalCooldown - (System.currentTimeMillis() - this.globalCurrentTime)) / 1000)) + " more second(s) to use this command again.");
        }
        builder.setTitle("Please slow down.");
        builder.setColor(0xFF0000);
        event.getMessage().replyEmbeds(builder.build()).queue();
        builder.clear();
    }

    public boolean checkUserCooldown() {
        if (this.isOnUserCooldown()) {
            this.sendUserCooldownWarning(this.event, true);
            return true;
        }
        return false;
    }

    /**
     * @return true if you should add `return;`
     */
    public boolean checkGlobalCooldown() {
        if (this.isOnGlobalCooldown()) {
            this.sendGlobalCooldownWarning(this.event, true);
            return true;
        }
        return false;
    }

    public boolean isOnUserCooldown() {
        Long uCoolDown = null;
        try {
            uCoolDown = this.userCooldowns.get(this.event.getAuthor().getIdLong());
        } catch (NullPointerException e) {
            return false;
        }

        if (uCoolDown == null) {
            return false;
        }

        return System.currentTimeMillis() - uCoolDown < this.userCooldown;
    }

    /**
     * @return true if there is still a cooldown ; cannot run command
     */
    public boolean isOnGlobalCooldown() {
        return System.currentTimeMillis() - this.globalCurrentTime < this.globalCooldown;
    }

    public void setUserCurrentTime() {
        try {
            this._userCooldowns.put(this.event.getAuthor().getIdLong(), this.userCooldowns.get(this.event.getAuthor().getIdLong()));
        } catch (Exception e) {
        }
        this.userCooldowns.put(
                this.event.getAuthor().getIdLong(),
                System.currentTimeMillis());
    }

    public void setGlobalCurrentTime() {
        this._globalCooldown = this.globalCurrentTime;
        this.globalCurrentTime = System.currentTimeMillis();
    }

    public void revertUserCooldown() {
        this.userCooldowns.put(this.event.getAuthor().getIdLong(), this._userCooldowns.get(this.event.getAuthor().getIdLong()));
    }

    public void revertGlobalCooldown() {
        if (this._globalCooldown != 0) {
            this.globalCurrentTime = this._globalCooldown;
        }
    }

    /**
     * @param prefix is the prefix of the guild
     * @param raw    is the raw message
     * @return check that it is this command or not
     */
    public boolean is(String prefix, String raw) {
        String content = raw;
        if (!(content.startsWith(Main.getUtil().getSelfMention() + " ")) && !(content.replaceFirst(Main.getUtil().getSelfMention(), Main.getUtil().getSelfMention() + " ").equalsIgnoreCase(content)))
            content = content.replaceFirst(Main.getUtil().getSelfMention(), Main.getUtil().getSelfMention() + " ");
        String[] args = content.split("\\s+");
        for (String s : this.alias) {
            try {
                if (args[0].equalsIgnoreCase(prefix + s)) {
                    this.Type = 1;
                    return true;
                } else if ((args[0] + args[1]).equalsIgnoreCase("<@!" + Main.getJda().getSelfUser().getIdLong() + ">" + s)) {
                    this.Type = 2;
                    return true;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        return false;
    }
}
