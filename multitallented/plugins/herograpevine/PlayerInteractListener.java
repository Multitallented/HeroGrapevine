package multitallented.plugins.herograpevine;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

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
        if (block.getType() != Material.CHEST)
            return;
        
        //TODO add tip
    }
    
}
