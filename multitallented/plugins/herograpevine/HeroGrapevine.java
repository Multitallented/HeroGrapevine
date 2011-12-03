package multitallented.plugins.herograpevine;

import com.Acrobot.ChestShop.ChestShop;
import com.herocraftonline.dev.heroes.Heroes;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HeroGrapevine extends JavaPlugin {
    private PluginListener pluginListener;
    private EntityDamageListener entityListener;
    private PlayerInteractListener playerListener;
    protected FileConfiguration config;
    private List<String> ignoredPlayers = new ArrayList<String>();
    private Map<TipType, Tip> lastTip = new HashMap<TipType, Tip>();
    private HeroesListener customListener;
    private File primaryConfig;
    
    
    @Override
    public void onDisable() {
        Logger log = Logger.getLogger("Minecraft");
        String message = "[HeroGrapevine] has been disabled!";
        log.info(message);
        
        //TODO stop cooldown events?
    }

    @Override
    public void onEnable() {
        Logger log = Logger.getLogger("Minecraft");
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
        
        pluginListener = new PluginListener();
        
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvent(Type.PLUGIN_ENABLE, pluginListener, Priority.Normal, this);
        pm.registerEvent(Type.PLUGIN_DISABLE, pluginListener, Priority.Normal, this);
        
        if (config.getBoolean("pvp")) {
            entityListener = new EntityDamageListener(this);
            pm.registerEvent(Type.ENTITY_DAMAGE, entityListener, Priority.Low, this);
        }
        
        if (config.getBoolean("chest") || config.getBoolean("chestshop")) {
            playerListener = new PlayerInteractListener(this);
            pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Low, this);
        }
        
        if (config.getBoolean("heroes") && getHeroes() != null) {
            customListener = new HeroesListener(this);
            pm.registerEvent(Type.CUSTOM_EVENT, customListener, Priority.Low, this);
        }
        
        
        MessageSender theSender = new MessageSender(this);
        long someInterval = config.getLong("cooldown");
        getServer().getScheduler().scheduleSyncRepeatingTask(this, theSender, someInterval, someInterval);
        
        String message = "[HeroGrapevine] has been enabled!";
        log.info(message);
        
        
    }
    
    public Heroes getHeroes() {
        return pluginListener.getHeroes();
    }
    
    public ChestShop getChestShop() {
        return pluginListener.getChestShop();
    }
    
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if (!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("herograpevine")) {
            if (args.length >= 1 && args[1].equalsIgnoreCase("toggle") && player.hasPermission("herograpevine.toggle") &&
                    !player.hasPermission("herograpevine.bypass")) {
                if (ignoredPlayers.contains(player.getName())) {
                    ignoredPlayers.remove(player.getName());
                    player.sendMessage("[HeroGrapevine] You will now recieve incoming tips.");
                } else {
                    ignoredPlayers.add(player.getName());
                    player.sendMessage("[HeroGrapevine] You will no longer recieve incoming tips.");
                }
                return true;
            }
        } else if (config.getBoolean("command",true) && (config.getStringList("ignored-commands") == null
                || !config.getStringList("ignored-commands").contains(cmd.getName()))) {
            String command = cmd.getName();
            for (String param : args) {
                command += " " + param;
            }
            lastTip.put(TipType.COMMAND, new Tip(player, command, new Date()));
        }
        return false;
    }
    
    public void putTip(TipType tipType, Tip tip) {
        lastTip.put(tipType, tip);
    }
    
    public Tip getTip(TipType tipType) {
        return lastTip.get(tipType);
    }
    
    public boolean hasIgnoredPlayer(String name) {
        return ignoredPlayers.contains(name);
    }
}
