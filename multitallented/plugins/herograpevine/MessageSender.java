package multitallented.plugins.herograpevine;

import java.util.Date;
import java.util.Random;
import org.bukkit.ChatColor;
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
                String message = null;
                do {
                    switch (new Random().nextInt(6)) {
                        case 0:
                            Tip tip = myPlugin.getTip(TipType.COMMAND);
                            if (tip != null) {
                                message = ChatColor.GRAY + "[HeroGrapevine] " + ChatColor.WHITE + tip.getPlayer().getDisplayName() + " used the command /"
                                        + tip.getData() + " " + ((new Date().getTime() - tip.getDate().getTime()) / 1000) + " seconds ago";
                            }
                        case 1:
                            //INVENTORY
                        case 2:
                            //CHEST
                        case 3:
                            //LOCATION
                        case 4:
                            //PVP
                        case 5:
                            //Heroes
                        case 6:
                            //ChestShop
                    }
                } while(message == null);
                
                //TODO send string to the p
            }
        }
                
    }
}
