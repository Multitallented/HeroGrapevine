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
import org.bukkit.ChatColor;
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
        log.info("[HeroGrapevine] has been disabled!");
        
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
        //Fix this later
        long someInterval = config.getLong("cooldown", 60000);
        if (someInterval < 5000 || someInterval % 1000 != 0) {
            log.info("[HeroGrapevine] cooldown set improperly, reverting to default.");
            someInterval = 60000;
        }
        String message = "[HeroGrapevine] cooldown set to " + someInterval;
        log.info(message);
        someInterval = someInterval / 50;
        getServer().getScheduler().scheduleSyncRepeatingTask(this, theSender, someInterval, someInterval);
        
        log.info("[HeroGrapevine] has been enabled!");
        
        
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
        player.sendMessage((config.getShortList("ignored-commands") == null) + " : "
                + (config.getStringList("ignored-commands") != null) + (!config.getStringList("ignored-commands").contains(cmd.getName()))
                + " : " + !player.hasPermission("herograpevine.bypass"));
        if (cmd.getName().equalsIgnoreCase("herograpevine")) {
            if (args.length >= 1 && args[0].equalsIgnoreCase("toggle")) {
                if (player.hasPermission("herograpevine.toggle")) {
                    if (!player.hasPermission("herograpevine.bypass")) {
                        if (ignoredPlayers.contains(player.getName())) {
                            ignoredPlayers.remove(player.getName());
                            player.sendMessage("[HeroGrapevine] You will now recieve incoming tips.");
                        } else {
                            ignoredPlayers.add(player.getName());
                            player.sendMessage("[HeroGrapevine] You will no longer recieve incoming tips.");
                        }
                    } else {
                        player.sendMessage(ChatColor.RED + "You have herograpevine.bypass and won't recieve tips.");
                    }
                    
                } else {
                    player.sendMessage(ChatColor.RED + "You don't have permission to use /herograpevine");
                }
                return true;
            }
        } else if (config.getBoolean("command",true) && (config.getStringList("ignored-commands") == null
                || !config.getStringList("ignored-commands").contains(cmd.getName())) && !player.hasPermission("herograpevine.bypass")) {
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
