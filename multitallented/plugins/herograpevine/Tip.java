package multitallented.plugins.herograpevine;

import java.util.Date;
import org.bukkit.entity.Player;

/**
 *
 * @author Multitallented
 */
class Tip {
    private final String data;
    private final Player player;
    private final Date date;
    public Tip(Player player, String data, Date date) {
        this.player = player;
        this.data = data;
        this.date = date;
    }
    
    public String getData() {
        return data;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public Date getDate() {
        return date;
    }
}
