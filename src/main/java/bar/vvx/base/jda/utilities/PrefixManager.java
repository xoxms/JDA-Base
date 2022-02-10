package bar.vvx.base.jda.utilities;

import bar.vvx.base.jda.config;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class PrefixManager {
    private static JSONParser jsonParser;
    private static JSONObject jsonObject;
    private static FileReader fileReader;
    private static FileWriter fileWriter;
    private static File file;

    public static void load() {
        file = new File("./File/Prefix.json");
    }

    private static void loadReader() throws IOException, ParseException {
        fileReader = new FileReader(file);
        jsonParser = new JSONParser();
        jsonObject = (JSONObject) jsonParser.parse(fileReader);
    }

    private static void loadWriter() throws IOException {
        fileWriter = new FileWriter(file);
    }

    private static void closeReader() throws IOException {
        fileReader.close();
    }

    private static void close() throws IOException {
        fileWriter.flush();

        fileReader.close();
        fileWriter.close();
    }

    public static void checkEmpty() {
        try {
            load();

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                if (reader.readLine() == null) {
                    resetFile();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void checkExist() {
        load();

        File dir = new File("./File");
        if (!dir.exists()) {
            System.out.println("Creating directory: " + dir.getName());
            if (dir.mkdirs()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }

        if (!file.exists()) {
            System.out.println("Prefix file does not exist, creating...");
            try {
                if (file.createNewFile()) {
                    System.out.println("Prefix file created.");
                } else {
                    System.out.println("Prefix file created failed.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized void resetFile() {
        try {
            load();
            loadWriter();

            fileWriter.write("{}");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized String getPrefix(MessageReceivedEvent event) throws IOException, ParseException {
        return getPrefix(event.getGuild());
    }

    public static synchronized String getPrefix(Guild guild) {
        String prefix = null;
        try {
            load();
            loadReader();

            String guildID = guild.getId();

            Object prefixRaw = jsonObject.get(guildID);

            if (prefixRaw == null) {
                prefix = config.defaultPrefix;

                setPrefix(guild, prefix);
            } else {
                prefix = String.valueOf(prefixRaw);
            }

            closeReader();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            checkExist();
            checkEmpty();

            try {
                loadReader();

                return getPrefix(guild);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ParseException ex) {
                System.err.println("Error while parsing Prefix.json" + System.lineSeparator() + ex.getMessage());
            }
        }

        return prefix;
    }

    public static synchronized void setPrefix(MessageReceivedEvent event, String prefix) throws IOException, ParseException {
        setPrefix(event.getGuild(), prefix);
    }

    public static synchronized void setPrefix(Guild guild, String prefix) throws IOException, ParseException {
        load();
        loadReader();
        loadWriter();

        String guildID = guild.getId();

        jsonObject.remove(guildID);
        jsonObject.put(guildID, prefix);

        fileWriter.write(jsonObject.toJSONString());

        close();
    }
}
