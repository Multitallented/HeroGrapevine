package multitallented.plugins.herograpevine;

import org.bukkit.entity.Player;

/**
 *
 * @author Multitallented
 */
class Tip {
    private final TipType type;
    private final String data;
    private final Player player;
    public Tip(Player player, TipType type, String data) {
        this.player = player;
        this.type = type;
        this.data = data;
    }
    
    public Enum getType() {
        return type;
    }
    
    public String getData() {
        return data;
    }
    
    public Player getPlayer() {
        return player;
    }
}
