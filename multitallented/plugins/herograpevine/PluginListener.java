/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package multitallented.plugins.herograpevine;

import com.Acrobot.ChestShop.ChestShop;
import com.herocraftonline.dev.heroes.Heroes;
import java.util.logging.Logger;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Multitallented
 */
public class PluginListener extends ServerListener {
    private Heroes heroes;
    private ChestShop chestShop;
    
    @Override
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
        }
    }
    
    @Override
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
        return heroes;
    }
    
    public ChestShop getChestShop() {
        return chestShop;
    }
}
