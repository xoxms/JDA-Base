package bar.vvx.base.jda.commands.impl.games.minecraft.serverstatuschecker;

import bar.vvx.base.jda.commands.Command;
import bar.vvx.base.jda.commands.CommandInfo;
import bar.vvx.base.jda.utilities.Colours;
import net.dv8tion.jda.api.EmbedBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@CommandInfo(
        name = "singleport",
        alias = {"single", "sp", "s"}
)
public class ServerStatusChecker$SinglePort extends Command {
    @Override
    public void execute() throws Exception {
        if (args.length < 1) {
            replyWarning("Usage: `serverstatuschecker singleport <host> <port>`");
            return;
        }

        String host = args[0];
        int port = 25565;
        try {
            if (args.length > 1) {
                port = Integer.parseInt(args[1]);
            }
            if (port < 1 || port > 65535) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            replyWarning("`" + args[1] + "` is not a valid port number.");
        }

        String ip = host + ":" + port;

        String[] proxy = ServerStatusChecker$Proxy.getProxy();
        System.out.println(proxy[0] + ":" + proxy[1]);
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("https://api.mcsrvstat.us/2/" + host).openConnection(/*new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxy[0], Integer.parseInt(proxy[1])))*/);

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
            String image = "https://api.mcsrvstat.us/icon/" + host;

            JSONObject jsonObject = (JSONObject) new JSONParser().parse(bufferedReader);

            String status = String.valueOf(jsonObject.get("online"));

            if (status.equals("true")) {
                status = "Online";

                String motd = String.valueOf(((JSONObject) jsonObject.get("motd")).get("clean"));

                JSONObject players = (JSONObject) jsonObject.get("players");
                String online = String.valueOf(players.get("online"));
                String max = String.valueOf(players.get("max"));
                JSONArray jsonArray = (JSONArray) players.get("list");
                StringBuilder playersList = new StringBuilder();
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        playersList.append((String) jsonArray.get(i));
                        if (i != jsonArray.size() - 1) {
                            playersList.append(", ");
                        }
                    }
                }

                String version = String.valueOf(jsonObject.get("version"));

                String protocol = String.valueOf(jsonObject.get("protocol"));

                String software = String.valueOf(jsonObject.get("software"));

                JSONObject jsonplugins = (JSONObject) jsonObject.get("plugins");

                StringBuilder pluginsList = new StringBuilder();
                if (jsonplugins != null) {
                    String[] plugins = jsonplugins.get("raw").toString().split(",");
                    pluginsList = new StringBuilder();
                    for (int i = 0; i < plugins.length; i++) {
                        pluginsList.append(plugins[i]);
                        if (i != plugins.length - 1) {
                            pluginsList.append(", ");
                        }
                    }
                }

                if (args.length < 2) {
                    ip = ip.substring(0, ip.length() - ":25565".length());
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
                if (playersList.length() > 0) {
                    embedBuilder.addField("Players", "```\n" + playersList + "```", false);
                }
                if (motd != null) {
                    embedBuilder.addField("MOTD", "```\n" + motd + "```", false);
                }
                if (pluginsList.length() > 0) {
                    embedBuilder.addField("Plugins", "```\n" + pluginsList + "```", false);
                }

                event.getChannel().sendTyping().queue();
                event.getMessage().replyEmbeds(embedBuilder.build()).queue();

                embedBuilder.clear();
            } else {
                status = "Offline";

                EmbedBuilder embedBuilder = new EmbedBuilder();

                if (args.length < 2) {
                    ip = ip.substring(0, ip.length() - ":25565".length());
                }

                embedBuilder.setTitle("Server Status");
                embedBuilder.setColor(Colours.FAILED);
                embedBuilder.setThumbnail(image);
                embedBuilder.addField("IP Address", "`" + ip + "`", true);
                embedBuilder.addField("Status", "`" + status + "`", true);

                event.getChannel().sendTyping().queue();
                event.getMessage().replyEmbeds(embedBuilder.build()).queue();

                embedBuilder.clear();
            }

            httpURLConnection.disconnect();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
