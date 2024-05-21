package grafitely.regionblocker;

import org.bukkit.plugin.java.JavaPlugin;

public final class RegionBlocker extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
