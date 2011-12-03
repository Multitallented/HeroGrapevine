package multitallented.plugins.herograpevine;

import com.herocraftonline.dev.heroes.Heroes;
import java.util.Date;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
        Player[] onlinePlayers = myPlugin.getServer().getOnlinePlayers();
        Random rand = new Random();
        if (onlinePlayers.length < 2)
            return;
        for (Player p : onlinePlayers) {
            if (p.hasPermission("herograpevine.notify") && !p.hasPermission("herograpevine.bypass") &&
                !myPlugin.hasIgnoredPlayer(p.getName())) {
                String message = null;
                Tip tip;
                int i = 1;
                do {
                    switch (rand.nextInt(7)) {
                        case 0:
                            if (!myPlugin.config.getBoolean("command"))
                                break;
                            tip = myPlugin.getTip(TipType.COMMAND);
                            if (tip != null && !tip.getPlayer().equals(p)) {
                                message = ChatColor.GRAY + "[HeroGrapevine] " + ChatColor.WHITE + tip.getPlayer().getDisplayName() + " used the command /"
                                        + tip.getData() + " " + ((new Date().getTime() - tip.getDate().getTime()) / 1000) + " seconds ago";
                            }
                            break;
                        case 1:
                            if (!myPlugin.config.getBoolean("inventory"))
                                break;
                            Player currentPlayer = onlinePlayers[rand.nextInt(onlinePlayers.length)];
                            if (!currentPlayer.equals(p)) {
                                String iSName = null;
                                outer: for (ItemStack is : currentPlayer.getInventory().getContents()) {
                                    switch (is.getTypeId()) {
                                        case 264:
                                            if (is.getAmount() > 3) {
                                                iSName = "a lot of Diamonds";
                                                break outer;
                                            }
                                        case 265:
                                            if (is.getAmount() > 9) {
                                                iSName = "a lot of Gold";
                                                break outer;
                                            }
                                        case 266:
                                            if (is.getAmount() > 19) {
                                                iSName = "a lot of Iron";
                                                break outer;
                                            }
                                        case 46:
                                            if (is.getAmount() > 6) {
                                                iSName = "a lot of TNT";
                                                break outer;
                                            }
                                        case 49:
                                            if (is.getAmount() > 7) {
                                                iSName = "a lot of Obsidian";
                                                break outer;
                                            }
                                        case 368:
                                            if (is.getAmount() > 4) {
                                                iSName = "a lot of EnderPearls";
                                                break outer;
                                            }
                                    }
                                }
                                if (iSName != null)
                                    message = ChatColor.GRAY + "[HeroGrapevine] " + ChatColor.WHITE + currentPlayer.getDisplayName() + " has " + iSName;
                            }
                            break;
                        case 2:
                            if (!myPlugin.config.getBoolean("chest"))
                                break;
                            tip = myPlugin.getTip(TipType.CHEST);
                            if (tip == null || tip.getPlayer().equals(p))
                                break;
                            message = ChatColor.GRAY + "[HeroGrapevine] " + ChatColor.WHITE + tip.getPlayer().getDisplayName() + " has a chest with " + tip.getData() + " as of " +
                                    ((new Date().getTime() - tip.getDate().getTime()) /1000) + " seconds ago";
                            break;
                        case 3:
                            if (!myPlugin.config.getBoolean("location"))
                                break;
                            Player player = onlinePlayers[rand.nextInt(onlinePlayers.length)];
                            if (!player.equals(p)) {
                                Location loc = player.getLocation();
                                message = ChatColor.GRAY + "[HeroGrapevine] " + ChatColor.WHITE + player.getDisplayName() + " is at x:" + (int) loc.getX() +
                                        ", y:" + (int) loc.getY() + " and z:" + (int) loc.getZ();
                            }
                            break;
                        case 4:
                            if (!myPlugin.config.getBoolean("pvp"))
                                break;
                            tip = myPlugin.getTip(TipType.PVP);
                            if (tip == null)
                                break;
                            if (tip.getPlayer().equals(p))
                                break;
                            message = ChatColor.GRAY + "[HeroGrapevine] " + ChatColor.WHITE + tip.getPlayer().getDisplayName() + " hit " + tip.getData() + " " +
                                    ((new Date().getTime() - tip.getDate().getTime()) /1000) + " seconds ago";
                            break;
                        case 5:
                            if (!myPlugin.config.getBoolean("heroes") || myPlugin.getHeroes() == null)
                                break;
                            tip = myPlugin.getTip(TipType.HERO);
                            if (tip == null || tip.getPlayer().equals(p))
                                break;
                            message = ChatColor.GRAY + "[HeroGrapevine] " + ChatColor.WHITE + tip.getPlayer().getDisplayName() + " used " + tip.getData() + " " +
                                    ((new Date().getTime() - tip.getDate().getTime()) /1000) + " seconds ago";
                            break;
                        case 6:
                            if (!myPlugin.config.getBoolean("chestshop") || myPlugin.getChestShop() == null)
                                break;
                            tip = myPlugin.getTip(TipType.CHEST_SHOP);
                            if (tip == null || tip.getPlayer().equals(p))
                                break;
                            message = ChatColor.GRAY + "[HeroGrapevine] " + ChatColor.WHITE + tip.getPlayer().getDisplayName() + " bought " + tip.getData() + " " +
                                    ((new Date().getTime() - tip.getDate().getTime()) /1000) + " seconds ago";
                            break;
                        case 7:
                            if (!myPlugin.config.getBoolean("health"))
                                break;
                            Player aPlayer = onlinePlayers[rand.nextInt(onlinePlayers.length)];
                            if (!aPlayer.equals(p) && aPlayer.getHealth() < 11) {
                                message = ChatColor.GRAY + "[HeroGrapevine] " + ChatColor.WHITE + aPlayer.getDisplayName() + "s health is " + aPlayer.getHealth();
                            }
                            break;
                    }
                    i++;
                } while(message == null && i < 6);
                if (message == null)
                    return;
                p.sendMessage(message);
            }
        }
                
    }
}
