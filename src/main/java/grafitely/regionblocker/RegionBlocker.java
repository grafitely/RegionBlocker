package grafitely.regionblocker;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public final class RegionBlocker extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(this, this);

        Bukkit.getLogger().info("RegionBlocker enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) throws IOException, ParseException {
        List<String> countries = (List<String>) getConfig().getList("Countries");

        String PlayerIP = Objects.requireNonNull(event.getPlayer().getAddress()).getHostString();
        URL url = new URL("http://ip-api.com/json/" + PlayerIP + "?fields=countryCode");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();

        int responseCode = con.getResponseCode();

        if (responseCode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        } else {

            String inline = "";
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                inline += scanner.nextLine();
            }

            scanner.close();
            JSONParser parse = new JSONParser();
            JSONObject data = (JSONObject) parse.parse(inline);

            if (countries.contains(data.get("countryCode").toString())){
                event.getPlayer().banIp("You are region blocked",  (Date) null,"Balls", true);
            }
        }
    }
}
