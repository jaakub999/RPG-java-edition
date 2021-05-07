package API;

import java.util.HashMap;
import java.util.Map;

public class PlayerContainer {
    private Map<String, Player> players_list;

    public PlayerContainer() {
        players_list = new HashMap<String, Player>();
    }

    public void addPlayer(Player player) {
        players_list.put(player.getNickname(), player);
    }

    public void removePlayer(String nickname) {
        players_list.remove(nickname);
    }

    public Map<String, Player> getPlayers_list() {
        return players_list;
    }

    public void setPlayers_list(Map<String, Player> players_list) {
        this.players_list = players_list;
    }
}