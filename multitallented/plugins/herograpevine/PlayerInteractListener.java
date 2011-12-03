package multitallented.plugins.herograpevine;

import com.Acrobot.ChestShop.Config.Config;
import com.Acrobot.ChestShop.Config.Language;
import com.Acrobot.ChestShop.Config.Property;
import com.Acrobot.ChestShop.Signs.restrictedSign;
import com.Acrobot.ChestShop.Utils.uLongName;
import com.Acrobot.ChestShop.Utils.uSign;
import java.util.Date;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Multitallented
 */
class PlayerInteractListener extends PlayerListener {
    private final HeroGrapevine plugin;

    public PlayerInteractListener(HeroGrapevine plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.isCancelled() || !event.hasBlock() || event.getPlayer().hasPermission("herograpevine.bypass"))
            return;
        BlockState block = event.getClickedBlock().getState();
        if (block instanceof Chest) {
            Chest chest = (Chest) block;
            String iSName = null;
            event.getPlayer().sendMessage("Chest size: " + chest.getInventory().getContents().length);
            outer: for (ItemStack is : chest.getInventory().getContents()) {
                if (is != null) {
                    event.getPlayer().sendMessage(is.getType().name() + " : " + is.getAmount());
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
                
            } 
            if (iSName != null) {
                iSName += " at x:" + (int) chest.getX() + ", y:" + (int) chest.getY() + " and z:" + (int) chest.getZ();
                event.getPlayer().sendMessage(iSName);
                plugin.putTip(TipType.CHEST, new Tip(event.getPlayer(), iSName, new Date()));
            }
        } else if (block instanceof Sign) {
            Action action = event.getAction();
            if (action != Action.LEFT_CLICK_BLOCK && action != Action.RIGHT_CLICK_BLOCK) return;

            Player player = event.getPlayer();

            Sign sign = (Sign) block;

            if (!uSign.isValid(sign) || player.isSneaking()) return;

            if (action == Action.RIGHT_CLICK_BLOCK) event.setCancelled(true);

            if (uLongName.stripName(player.getName()).equals(sign.getLine(0))) {
                if (action != Action.LEFT_CLICK_BLOCK || !Config.getBoolean(Property.ALLOW_LEFT_CLICK_DESTROYING))
                return;
            }


            if (restrictedSign.isRestrictedShop(sign) && !restrictedSign.canAccess(sign, player)) {
                player.sendMessage(Config.getLocal(Language.ACCESS_DENIED));
                return;
            }

            Action buy = (Config.getBoolean(Property.REVERSE_BUTTONS) ? Action.LEFT_CLICK_BLOCK : Action.RIGHT_CLICK_BLOCK);
            
            String message = "";
            if (action == buy) {
                message += "bought ";
            } else {
                message += "sold ";
            }
            
            message = sign.getLine(1) + " " + sign.getLine(3);
            plugin.putTip(TipType.CHEST_SHOP, new Tip(player, message, new Date()));
        }
    }
    
    @Override
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled() || !plugin.config.getBoolean("command", true) || event.getPlayer().hasPermission("herograpevine.bypass"))
            return;
        String[] message = event.getMessage().split(" ");
        if (message.length > 0 && !plugin.containsIgnoredCommand(message[0].substring(1))) {
            plugin.putTip(TipType.COMMAND, new Tip(event.getPlayer(), event.getMessage(), new Date()));
        }
    }
    
}
