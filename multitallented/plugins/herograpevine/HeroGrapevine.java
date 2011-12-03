package multitallented.plugins.herograpevine;

import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class HeroGrapevine extends JavaPlugin {
    private PluginListener pluginListener;
    private EntityDamageListener entityListener;
    private PlayerInteractListener playerListener;
    private FakeListener fakeListener;
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
        
        
        
        
        Logger log = Logger.getLogger("Minecraft");
        String message = "[HeroGrapevine] has been enabled!";
        log.info(message);
        
        
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
        if (!(sender instanceof Player))
            return false;
        if (cmd.getName().equalsIgnoreCase("herograpevine")) {
            //TODO handle commands here
            return true;
        } else {
            Tip tip = new Tip((Player) sender, TipType.COMMAND, cmd.getName());
        }
        return false;
    }
}
