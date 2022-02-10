package bar.vvx.base.jda.utilities;

import bar.vvx.base.jda.utilities.status.StatusManager;
import bar.vvx.base.jda.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

public class Storage {
    public static Runnable getStatusRunnable() {
        return () -> {
            JDA jda = Main.getJda();
            StatusManager statusManager = StatusManager.getNextStatus();
            switch (statusManager.getType()) {
                case PLAYING :
                    jda.getPresence().setActivity(Activity.playing(statusManager.getCurrent()));
                    break;
                case WATCHING :
                    jda.getPresence().setActivity(Activity.watching(statusManager.getCurrent()));
                    break;
                case COMPETING :
                    jda.getPresence().setActivity(Activity.playing(statusManager.getCurrent()));
                    break;
                case LISTENING :
                    jda.getPresence().setActivity(Activity.listening(statusManager.getCurrent()));
                    break;
                case STREAMING :
                    jda.getPresence().setActivity(Activity.streaming(statusManager.getCurrent(), statusManager.getUrl()));
                    break;
                case CUSTOM_STATUS :
                    jda.getPresence().setActivity(Activity.playing(statusManager.getCurrent()));
                    break;
                default :
                    System.err.println("ERROR : Status not found!");
            }
        };
    }
}
