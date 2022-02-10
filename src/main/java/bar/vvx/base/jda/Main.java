package bar.vvx.base.jda;

import bar.vvx.base.jda.commands.CommandManager;
import bar.vvx.base.jda.scheduletasks.ScheduledTaskManager;
import bar.vvx.base.jda.utilities.PrefixManager;
import bar.vvx.base.jda.utilities.Storage;
import bar.vvx.base.jda.utilities.Util;
import net.dv8tion.jda.api.JDA;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static String token;
    private static String prefix;
    private static JDA jda;
    private static Util util;

    public static void main(String[] args) {
        token = Initialize.setToken();
        prefix = Initialize.setPrefix();
        jda = Initialize.createJDA();
        util = Initialize.createUtil();

        Initialize.Initialize();

        new ScheduledTaskManager().ScheduledTaskInitialize();

        setActivity();
        addEventListener();
    }

    private static void setActivity() {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(Storage.getStatusRunnable(), 0, 20, TimeUnit.SECONDS);
    }

    public static void addEventListener() {
        jda.addEventListener(new CommandManager());
    }

    public static String getToken() {
        return token;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static String getPrefix(long guildId) {
        return PrefixManager.getPrefix(Objects.requireNonNull(jda.getGuildById(guildId)));
    }

    public static JDA getJda() {
        return jda;
    }

    public static Util getUtil() {
        return util;
    }
}
