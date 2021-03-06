package multitallented.plugins.herograpevine;

import com.Acrobot.ChestShop.ChestShop;
import com.herocraftonline.heroes.Heroes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class HeroGrapevine extends JavaPlugin {
    private PluginListener pluginListener;
    private EntityDamageListener entityListener;
    private PlayerInteractListener playerListener;
    protected FileConfiguration config;
    private List<String> ignoredPlayers = new ArrayList<String>();
    private Map<TipType, Tip> lastTip = new HashMap<TipType, Tip>();
    private HeroesListener customListener;
    public static Permission permission;
    
    
    @Override
    public void onDisable() {
        Logger log = Logger.getLogger("Minecraft");
        log.info("[HeroGrapevine] has been disabled!");
        
        //TODO stop MessageSender
    }

    @Override
    public void onEnable() {
        Logger log = Logger.getLogger("Minecraft");
        config = getConfig();
        config.options().copyDefaults(true);
        saveConfig();
        
        setupPermissions();
        
        pluginListener = new PluginListener(this);
        
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(pluginListener, this);
        
        
        
        if (config.getBoolean("pvp")) {
            entityListener = new EntityDamageListener(this);
            pm.registerEvents(entityListener, this);
        }
        
        if (config.getBoolean("chest") || config.getBoolean("chestshop") || config.getBoolean("command")) {
            playerListener = new PlayerInteractListener(this);
            pm.registerEvents(playerListener, this);
        }
        
        if (config.getBoolean("heroes") && getHeroes() != null) {
            customListener = new HeroesListener(this);
            pm.registerEvents(customListener, this);
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
        if (cmd.getName().equalsIgnoreCase("herograpevine")) {
            if (args.length >= 1 && args[0].equalsIgnoreCase("toggle")) {
                if (permission == null || permission.has(player.getWorld(), player.getName(), "herograpevine.toggle")) {
                    if (permission == null || !permission.has(player.getWorld(), player.getName(), "herograpevine.bypass")) {
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
    
    public boolean containsIgnoredCommand(String cmd) {
        List<String> ignoredCommands = config.getStringList("ignored-commands");
        if (ignoredCommands != null) {
            return ignoredCommands.contains(cmd);
        } else {
            return false;
        }
    }
    public boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
            if (permission != null)
                System.out.println("[HeroGrapevine] Hooked into " + permission.getName());
        }
        return (permission != null);
    }
}
