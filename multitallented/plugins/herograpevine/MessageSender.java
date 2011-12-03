package multitallented.plugins.herograpevine;

import org.bukkit.entity.Player;

/**
 *
 * @author Multitallented
 */
class MessageSender implements Runnable {

    HeroGrapevine myPlugin;
    MessageSender(HeroGrapevine myPlugin) {
        this.myPlugin = myPlugin;
    }
    
    @Override
    public void run() {
        for (Player p : myPlugin.getServer().getOnlinePlayers()) {
            if (p.hasPermission("herograpevine.notify") && !myPlugin.hasIgnoredPlayer(p.getName())) {
                //TODO grab a tip and send it as a message to a player
            }
                
        }
    }
    
}
