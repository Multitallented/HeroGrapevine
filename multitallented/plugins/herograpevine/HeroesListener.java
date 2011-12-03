package multitallented.plugins.herograpevine;

import com.herocraftonline.dev.heroes.api.HeroesEventListener;
import com.herocraftonline.dev.heroes.api.SkillUseEvent;
import java.util.Date;

/**
 *
 * @author Multitallented
 */
class HeroesListener extends HeroesEventListener {
    private final HeroGrapevine plugin;
    public HeroesListener(HeroGrapevine plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public void onSkillUse(SkillUseEvent event) {
        if (event.isCancelled())
            return;
        plugin.putTip(TipType.HERO, new Tip(event.getPlayer(), event.getSkill().getName(), new Date()));
    }
}
