package multitallented.plugins.herograpevine;

import com.herocraftonline.heroes.api.events.SkillUseEvent;
import java.util.Date;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 *
 * @author Multitallented
 */
class HeroesListener implements Listener {
    private final HeroGrapevine plugin;
    public HeroesListener(HeroGrapevine plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onCustomEvent(SkillUseEvent event) {
        if (plugin.getHeroes() == null || event.isCancelled()) {
            return;
        }
        Player player = event.getPlayer();
        if (HeroGrapevine.permission != null && HeroGrapevine.permission.has(player.getWorld(), player.getName(), "herograpevine.bypass"))
            return;
        String message = event.getSkill().getName() + " and has " + event.getHero().getMana() + " left ";
        plugin.putTip(TipType.HERO, new Tip(event.getPlayer(), message, new Date()));
    }
}
