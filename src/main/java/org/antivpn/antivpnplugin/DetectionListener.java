package org.antivpn.antivpnplugin;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Scanner;
import org.json.*;

public class DetectionListener implements Listener {
    FileConfiguration config = AntiVPN.getPlugin(AntiVPN.class).getConfig();

    @EventHandler
    public void VPNKick(AsyncPlayerPreLoginEvent event) throws IOException {
        String ip = event.getAddress().toString().substring(1);
        URL url = new URL("https://api.incolumitas.com/datacenter?ip=" + ip);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("GET");

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        Scanner s = new Scanner(responseStream).useDelimiter("\\A");
        String response = s.hasNext() ? s.next() : "";
        JSONObject obj = new JSONObject(response);
        boolean dc = obj.getBoolean("is_datacenter");
        boolean tor = obj.getBoolean("is_tor");
        boolean proxy = obj.getBoolean("is_proxy");
        boolean abuser = obj.getBoolean("is_abuser");

        String finalMessage = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("message")));
        if (dc || tor || proxy || abuser) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, finalMessage);

        }
    }
}
