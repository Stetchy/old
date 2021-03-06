package core.hcrg.bob;

import java.util.ArrayList;
import java.util.HashMap;

public class BobTeam {
	private final String colour;
	private HashMap<String, BobPlayer> playerList = new HashMap<String, BobPlayer>();

	public BobTeam(String colour) {
		this.colour = colour;
	}

	private final int maxLives = 5;

	private String[] getPlayersByScore() {
		ArrayList<String> str = new ArrayList<String>();
		for (int i = maxLives; i > 0; i++) {
			for (BobPlayer bp : playerList.values()) {
				if (bp.getLivesRemaining() == i)
					str.add(bp.getName());
			}
		}
		return str.toArray(new String[str.size()]);
	}
}
