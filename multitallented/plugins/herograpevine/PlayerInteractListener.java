package multitallented.plugins.herograpevine;

import com.Acrobot.ChestShop.Config.Config;
import com.Acrobot.ChestShop.Config.Language;
import com.Acrobot.ChestShop.Config.Property;
import com.Acrobot.ChestShop.Protection.Plugins.Default;
import com.Acrobot.ChestShop.Shop.ShopManagement;
import com.Acrobot.ChestShop.Signs.restrictedSign;
import com.Acrobot.ChestShop.Utils.uLongName;
import com.Acrobot.ChestShop.Utils.uSign;
import java.util.Date;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
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
        if (event.isCancelled() || !event.hasBlock())
            return;
        Block block = event.getClickedBlock();
        if (block instanceof Chest) {
            Chest chest = (Chest) block;
            String iSName = null;
            outer: for (ItemStack is : chest.getInventory().getContents()) {
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
                if (iSName != null) {
                    iSName += " at x:" + (int) chest.getX() + ", y:" + (int) chest.getY() + " and z:" + (int) chest.getZ();
                    plugin.putTip(TipType.CHEST, new Tip(event.getPlayer(), iSName, new Date()));
                }
            } 
        } else if (block instanceof Sign) {
            Action action = event.getAction();
            if (action != Action.LEFT_CLICK_BLOCK && action != Action.RIGHT_CLICK_BLOCK) return;

            Player player = event.getPlayer();

            if (!uSign.isSign(block)) return;
            Sign sign = (Sign) block.getState();

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
    
}
