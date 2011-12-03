package multitallented.plugins.herograpevine;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

/**
 *
 * @author Multitallented
 */
class EntityDamageListener extends EntityListener {
    private final HeroGrapevine plugin;

    public EntityDamageListener(HeroGrapevine plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.isCancelled() || !(event.getEntity() instanceof Player) || !(event instanceof EntityDamageByEntityEvent))
            return;
        EntityDamageByEntityEvent edBy = (EntityDamageByEntityEvent) event;
        if (!(edBy.getDamager() instanceof Player))
            return;
        //TODO add tip
    }
    
}
