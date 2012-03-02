package multitallented.plugins.herograpevine;

import java.util.Date;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 *
 * @author Multitallented
 */
class EntityDamageListener implements Listener {
    private final HeroGrapevine plugin;
    public EntityDamageListener(HeroGrapevine plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled() || !(event.getEntity() instanceof Player) || !(event instanceof EntityDamageByEntityEvent))
            return;
        Player player = (Player) event.getEntity();
        if (HeroGrapevine.permission != null && HeroGrapevine.permission.has(player.getWorld(), player.getName(), "herograpevine.bypass"))
            return;
        EntityDamageByEntityEvent edBy = (EntityDamageByEntityEvent) event;
        if (!(edBy.getDamager() instanceof Player))
            return;
        plugin.putTip(TipType.PVP, new Tip((Player) event.getEntity(), ((Player) edBy.getDamager()).getDisplayName(), new Date()));
    }
    
}
