/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multitallented.plugins.herograpevine;

import com.Acrobot.ChestShop.ChestShop;
import com.herocraftonline.heroes.Heroes;
import java.util.logging.Logger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 *
 * @author Multitallented
 */
public class PluginListener implements Listener {
    private Heroes heroes;
    private ChestShop chestShop;
    private final HeroGrapevine plugin;
    
    public PluginListener(HeroGrapevine plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        Plugin currentPlugin = event.getPlugin();
        String name = currentPlugin.getDescription().getName();
        if (name.equals("ChestShop")) {
            chestShop = (ChestShop) currentPlugin;
            Logger log = Logger.getLogger("Minecraft");
            String message = "[HeroGrapevine] will start using " + name + " because it has been enabled!";
            log.info(message);
        } else if (name.equals("Heroes")) {
            heroes = (Heroes) currentPlugin;
            Logger log = Logger.getLogger("Minecraft");
            String message = "[HeroGrapevine] will start using " + name + " because it has been enabled!";
            log.info(message);
        } else if ((name.equals("Vault") || name.equals("Permissions")) && HeroGrapevine.permission == null) {
            plugin.setupPermissions();
        }
    }
    
    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        Plugin currentPlugin = event.getPlugin();
        String name = currentPlugin.getDescription().getName();

        if (name.equals("ChestShop")) {
            chestShop = null;
            Logger log = Logger.getLogger("Minecraft");
            String message = "[HeroGrapevine] will stop using " + name + " because it has been disabled!";
            log.info(message);
        } else if (name.equals("Heroes")) {
            heroes = null;
            Logger log = Logger.getLogger("Minecraft");
            String message = "[HeroGrapevine] will stop using " + name + " because it has been disabled!";
            log.info(message);
        }
    }
    
    public Heroes getHeroes() {
        if (heroes == null) {
            PluginManager pm = plugin.getServer().getPluginManager();
            if (pm.isPluginEnabled("Heroes")) {
                heroes = (Heroes) pm.getPlugin("Heroes");
            }
        }
        return heroes;
    }
    
    public ChestShop getChestShop() {
        return chestShop;
    }
}
