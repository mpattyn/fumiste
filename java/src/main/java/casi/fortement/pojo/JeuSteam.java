package casi.fortement.pojo;

public class JeuSteam {
	private int playtimeForever = 0;
	private int playtime2Weeks = 0;
	private int gameId = 0;
	
	public int getPlaytimeForever() {
		return playtimeForever;
	}
	public void setPlaytimeForever(int playtimeForever) {
		this.playtimeForever = playtimeForever;
	}
	public int getPlaytime2Weeks() {
		return playtime2Weeks;
	}
	public void setPlaytime2Weeks(int playtime2Weeks) {
		this.playtime2Weeks = playtime2Weeks;
	}
	public int getGameId() {
		return gameId;
	}
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("Duree de jeu : ");
		sb.append(String.valueOf(this.playtimeForever));
		sb.append("\nDuree de jeu depuis deux semaines : ");
		sb.append(String.valueOf(this.playtime2Weeks));
		sb.append("\nId du jeu : ");
		sb.append(String.valueOf(this.gameId));
		
		return sb.toString();
	}
}
