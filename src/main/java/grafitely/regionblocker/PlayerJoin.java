package grafitely.regionblocker;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;


public class PlayerJoin implements Listener {

    @EventHandler
    public void OnPlayerJoin(PlayerJoinEvent event) throws IOException, ParseException {
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

            if (Objects.equals(data.get("countryCode").toString(), "FR")){
                event.getPlayer().banIp("French. disgusting.",  (Date) null,"Balls", true);
            }
        }
    }
}
