package casi.fortement.pojo;

public class JeuSteam {
	private String proprioId = "";
	private int playtimeForever = 0;
	private int gameId = 0;

	public String getProprioId() {
		return proprioId;
	}

	public void setProprioId(String proprioId) {
		this.proprioId = proprioId;
	}

	public int getPlaytimeForever() {
		return playtimeForever;
	}

	public void setPlaytimeForever(int playtimeForever) {
		this.playtimeForever = playtimeForever;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("Duree de jeu : ");
		sb.append(String.valueOf(this.playtimeForever));
		sb.append("\nId du jeu : ");
		sb.append(String.valueOf(this.gameId));

		return sb.toString();
	}
}
