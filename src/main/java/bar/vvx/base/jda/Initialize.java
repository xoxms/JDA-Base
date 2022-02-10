package bar.vvx.base.jda;

import bar.vvx.base.jda.utilities.Util;
import bar.vvx.base.jda.utilities.PrefixManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class Initialize {
    protected static String setToken() {
        return config.token;
    }

    protected static String setPrefix() {
        return config.defaultPrefix;
    }

    protected static JDA createJDA() {
        try {
            return JDABuilder.createDefault(Main.getToken()).build();
        } catch (LoginException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static Util createUtil() {
        return new Util();
    }

    protected static void Initialize() {
        PrefixManager.checkExist();
        PrefixManager.checkEmpty();
    }
}
