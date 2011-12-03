package multitallented.plugins.herograpevine;

import java.util.ArrayList;
import java.util.List;
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
    private FakeListener fakeListener;
    private List<String> ignoredPlayers = new ArrayList<String>();
    private Tip lastTip;
    
    
    @Override
    public void onDisable() {
        Logger log = Logger.getLogger("Minecraft");
        String message = "[HeroGrapevine] has been disabled!";
        log.info(message);
        
        //TODO stop cooldown events?
    }

    @Override
    public void onEnable() {
        config = getConfig();
        new Thread(new Runnable() {

            @Override
            public void run() {
                processConfig();
            }
        }).start();
        
        pluginListener = new PluginListener();
        
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvent(Type.PLUGIN_ENABLE, pluginListener, Priority.Normal, this);
        pm.registerEvent(Type.PLUGIN_DISABLE, pluginListener, Priority.Normal, this);
        
        entityListener = new EntityDamageListener(this);
        pm.registerEvent(Type.ENTITY_DAMAGE, entityListener, Priority.Low, this);
        
        playerListener = new PlayerInteractListener(this);
        pm.registerEvent(Type.PLAYER_INTERACT, playerListener, Priority.Low, this);
        
        fakeListener = new FakeListener(this);
        //TODO use method of callback?
        
        
        //TODO add ignored players from config file
        
        
        MessageSender theSender = new MessageSender(this);
        long someInterval = config.getLong("cooldown");
        getServer().getScheduler().scheduleSyncRepeatingTask(this, theSender, someInterval, someInterval);
        
        Logger log = Logger.getLogger("Minecraft");
        String message = "[HeroGrapevine] has been enabled!";
        log.info(message);
        
        
    }
    
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if (!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("herograpevine")) {
            if (args.length >= 1 && args[1].equalsIgnoreCase("toggle") && player.hasPermission("herograpevine.toggle")) {
                if (ignoredPlayers.contains(player.getName())) {
                    ignoredPlayers.remove(player.getName());
                    player.sendMessage("[HeroGrapevine] You will now recieve incoming tips.");
                } else {
                    ignoredPlayers.add(player.getName());
                    player.sendMessage("[HeroGrapevine] You will no longer recieve incoming tips.");
                }
                return true;
            }
        } else if (config.getBoolean("commands",true) && (config.getStringList("ignored-commands") == null
                || !config.getStringList("ignored-commands").contains(cmd.getName()))) {
            String command = cmd.getName();
            for (String param : args) {
                command += " " + param;
            }
            lastTip = new Tip(player, TipType.COMMAND, command);
        }
        return false;
    }
    
    public boolean hasIgnoredPlayer(String name) {
        return ignoredPlayers.contains(name);
    }
    
    private void processConfig() {
        if (config.getString("commands") == null) {
            config.set("commands", true);
        }
        if (config.get("cooldown") == null) {
            config.set("cooldown", 60000);
        }
        if (config.getStringList("ignored-players") == null) {
            config.set("ignored-players", new ArrayList<String>());
        } else {
            for (String currentPlayer : (List<String>) config.getStringList("ignored-players")) {
                ignoredPlayers.add(currentPlayer);
            }
        }
        
        if (config.getStringList("ignored-commands") == null) {
            config.set("ignored-players", new ArrayList<String>());
        }
        //TODO add more config options here
    }
}
