package bar.vvx.base.jda.utilities.status;

import bar.vvx.base.jda.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

import java.util.HashMap;
import java.util.Map;

public class StatusManager {
    private Activity.ActivityType type;
    private String current;
    private String url;

    private static HashMap<String, Activity.ActivityType> status;
    private static HashMap<Short, String> helper;
    private static HashMap<String, String> URLList;
    private static HashMap<String, IStatus> dynamic;
    private static Short order;

    private static JDA jda;

    public StatusManager(Activity.ActivityType type, String current, String url) {
        this.type = type;
        this.current = current;
        this.url = url;
    }

    static {
        status = new HashMap<>();
        helper = new HashMap<>();
        URLList = new HashMap<>();
        dynamic = new HashMap<>();
        order = 0;
        Short i = 0;
        addStatus();
        addURL();
        addDynamic();
        for (Map.Entry<String, Activity.ActivityType> m : status.entrySet()) {
            helper.put(i, m.getKey());
            i++;
        }
        jda = Main.getJda();
    }

    private static void addStatus() {
        /*
         * status.put("status", Activity.ActivityType.???);
         */
        status.put("watching <count> servers", Activity.ActivityType.WATCHING);
        status.put("something~", Activity.ActivityType.WATCHING);
        status.put("created by Monmcgt", Activity.ActivityType.CUSTOM_STATUS);
        status.put("Almost Home (Mariah Carey)", Activity.ActivityType.LISTENING);
        status.put("<streaming> on YouTube!", Activity.ActivityType.STREAMING);
    }

    private static void addURL() {
        URLList.put("<streaming> on YouTube!", "https://www.youtube.com/watch?v=dQw4w9WgXcQ");
    }

    private static void addDynamic() {
        /*
         * if you use this method, to change the status use dynamic.put and `manager.setCurrent("STATUS");`
         */
        dynamic.put("watching <count> servers", (manager) -> {
            manager.setCurrent(jda.getGuilds().size() + " servers");
        });
        dynamic.put("<streaming> on YouTube!", (manager) -> {
           manager.setCurrent("on YouTube!");
        });
    }

    private static String getCurrentURL(String message) {
        return URLList.get(message);
    }

    public static StatusManager getNextStatus() {
        if (order == helper.size()) {
            order = 0;
        }
        StatusManager statusManager = new StatusManager(status.get(helper.get(order)), helper.get(order), getCurrentURL(helper.get(order)));
        IStatus dym = dynamic.get(helper.get(order));
        if (dym != null) {
            dym.run(statusManager);
        }
        try {
            return statusManager;
        } finally {
            order++;
        }
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public Activity.ActivityType getType() {
        return this.type;
    }

    public String getCurrent() {
        return this.current;
    }

    public String getUrl() {
        return this.url;
    }
}