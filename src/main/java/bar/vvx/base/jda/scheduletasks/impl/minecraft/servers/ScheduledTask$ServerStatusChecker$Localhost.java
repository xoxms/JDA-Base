package bar.vvx.base.jda.scheduletasks.impl.minecraft.servers;

import bar.vvx.base.jda.scheduletasks.ScheduledTaskInfo;
import bar.vvx.base.jda.utilities.Colours;
import bar.vvx.base.jda.Main;
import bar.vvx.base.jda.scheduletasks.ScheduledTask;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ScheduledTaskInfo(
        delay = 5,
        timeUnit = TimeUnit.MINUTES
)
public class ScheduledTask$ServerStatusChecker$Localhost extends ScheduledTask {
    boolean mention;
    String mentionId;
    String mentionChannelId;

    List<String> ign;
    Map<String, String> servers;

    @Override
    protected void init() throws Exception {
        this.ign = new ArrayList<>();
        this.servers = new HashMap<>();
        this.readFile();
    }

    @Override
    protected Runnable start() {
        return () -> {
            for (Map.Entry<String, String> entry : servers.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                String ip = key;
                String channelId = value;

                String found = "";
                List<String> found_ign = new ArrayList<>();

                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("https://api.mcsrvstat.us/2/" + ip).openConnection();

                    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
                        String image = "https://api.mcsrvstat.us/icon/" + ip;

                        JSONObject jsonObject = (JSONObject) new JSONParser().parse(bufferedReader);

                        String status = String.valueOf(jsonObject.get("online"));

                        if (status.equals("true")) {
                            status = "Online";

                            String motd = String.valueOf(((JSONObject) jsonObject.get("motd")).get("clean"));

                            JSONObject players = (JSONObject) jsonObject.get("players");
                            String online = String.valueOf(players.get("online"));
                            String max = String.valueOf(players.get("max"));
                            JSONArray jsonArray = (JSONArray) players.get("list");
                            String playersList = "";
                            if (jsonArray != null) {
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    for (String ign : this.ign) {
                                        if (String.valueOf(jsonArray.get(i)).equals(ign)) {
                                            found_ign.add(ign);
                                        }
                                    }
                                    playersList += (String) jsonArray.get(i);
                                    if (i != jsonArray.size() - 1) {
                                        playersList += ", ";
                                    }
                                }
                                for (int i = 0; i < found_ign.size(); i++) {
                                    found += found_ign.get(i);
                                    if (i != found_ign.size() - 1) {
                                        found += ", ";
                                    }
                                }
                            }

                            String version = String.valueOf(jsonObject.get("version"));

                            String protocol = String.valueOf(jsonObject.get("protocol"));

                            String software = String.valueOf(jsonObject.get("software"));

                            JSONObject jsonplugins = (JSONObject) jsonObject.get("plugins");

                            String pluginsList = "";
                            if (jsonplugins != null) {
                                String[] plugins = jsonplugins.get("raw").toString().split(",");
                                pluginsList = "";
                                for (int i = 0; i < plugins.length; i++) {
                                    pluginsList += plugins[i];
                                    if (i != plugins.length - 1) {
                                        pluginsList += ", ";
                                    }
                                }
                            }

                            EmbedBuilder embedBuilder = new EmbedBuilder();

                            embedBuilder.setTitle("Server Status");
                            embedBuilder.setColor(Colours.COMPLETED);
                            embedBuilder.setThumbnail(image);
                            embedBuilder.addField("IP Address", "`" + ip + "`", true);
                            embedBuilder.addField("Status", "`" + status + "`", true);
                            embedBuilder.addField("Version", "`" + version + "`", true);
                            embedBuilder.addField("Protocol", "`" + protocol + "`", true);
                            embedBuilder.addField("Software", "`" + software + "`", true);
                            if (online != null && max != null) {
                                embedBuilder.addField("Online Players", "`" + online + "/" + max + "`", true);
                            }
                            if (!playersList.isEmpty()) {
                                embedBuilder.addField("Players", "```\n" + playersList + "```", false);
                            }
                            if (motd != null) {
                                embedBuilder.addField("MOTD", "```\n" + motd + "```", false);
                            }
                            if (!pluginsList.isEmpty()) {
                                embedBuilder.addField("Plugins", "```\n" + pluginsList + "```", false);
                            }

                            TextChannel textChannel = Main.getJda().getTextChannelById(channelId);
                            textChannel.sendTyping().queue();
                            textChannel.sendMessageEmbeds(embedBuilder.build()).queue();

                            embedBuilder.clear();

                            String message;
                            if (found_ign.size() > 0) {
                                if (mention) {
                                    message = Main.getJda().retrieveUserById(this.mentionId).complete().getAsMention() + " found `" + found + "` in `" + ip + "`";
                                } else {
                                    message = "Found `" + found + "` in `" + ip + "`";
                                }
                                TextChannel textChannel2 = Main.getJda().getTextChannelById(this.mentionChannelId);
                                textChannel2.sendTyping().queue();
                                textChannel2.sendMessage(message).queue();
                            }
                        } else {
                            status = "Offline";

                            EmbedBuilder embedBuilder = new EmbedBuilder();

                            embedBuilder.setTitle("Server Status");
                            embedBuilder.setColor(Colours.FAILED);
                            embedBuilder.setThumbnail(image);
                            embedBuilder.addField("IP Address", "`" + ip + "`", true);
                            embedBuilder.addField("Status", "`" + status + "`", true);

                            TextChannel textChannel = Main.getJda().getTextChannelById(channelId);
                            textChannel.sendTyping().queue();
                            textChannel.sendMessageEmbeds(embedBuilder.build()).queue();

                            embedBuilder.clear();
                        }

                        httpURLConnection.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (ParseException | IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void readFile() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("./File/serverchecker/localhost.txt"))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                } else if (line.startsWith("! CONFIG")) {
                    line = line.replace("! CONFIG", "").trim();
                    String[] config = line.split(":");
                    switch (config[0]) {
                        case "MENTION" :
                            if (config[1].equalsIgnoreCase("true")) {
                                this.mention = true;
                            } else if (config[1].equalsIgnoreCase("false")) {
                                this.mention = false;
                            } else {
                                throw new IllegalArgumentException("Invalid value for MENTION");
                            }
                            break;
                            case "MENTIONID" :
                                this.mentionId = config[1];
                        case "MENTIONCHANNELID" :
                            this.mentionChannelId = config[1];
                            break;
                        default :
                            throw new IllegalArgumentException("Invalid config");
                    }
                } else if (line.startsWith("-")){
                    line = line.replaceFirst("-", "").trim();
                    String[] split = line.split(">");
                    servers.put(split[0], split[1]);
                } else if (line.startsWith(".")) {
                    line = line.replaceFirst(".", "").trim();
                    String[] split = line.split(">", 2);
                    switch (split[0]) {
                        case "ADDNAME" :
                            this.ign.add(split[1]);
                            break;
                        default :
                            throw new IllegalArgumentException("Invalid config");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
