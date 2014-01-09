package casi.fortement.fumiste;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import casi.fortement.pojo.JeuSteam;
import casi.fortement.pojo.SteamUser;
import casi.fortement.utils.JsonReader;

@Controller
public class AspirateurController {

	@Autowired
	BasicDataSource datasource;

	private final String apiKey = "3365F0BE987ABFBDA9635BBD58058C99";

	@RequestMapping("/analyser")
	public String recupererListeJeux(@ModelAttribute("command") SteamUser user)
			throws MalformedURLException, IOException {

		StringBuilder result = new StringBuilder();
		StringBuilder url = new StringBuilder(
				"http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=");
		url.append(this.apiKey);
		url.append("&steamid=");
		url.append(user.getId());
		url.append("&format=json");

		JSONObject jsonObject = JsonReader.readJsonFromUrl(url.toString())
				.getJSONObject("response");
		JSONArray tmp = jsonObject.getJSONArray("games");
		for (int j = 0; j < tmp.length(); j++) {
			JSONObject jeuTmp = tmp.getJSONObject(j);
			JeuSteam jeuSteamTmp = new JeuSteam();
			jeuSteamTmp.setGameId(Integer.valueOf(jeuTmp.get("appid")
					.toString()));
			jeuSteamTmp.setPlaytimeForever(Integer.valueOf(jeuTmp.get(
					"playtime_forever").toString()));
			updateDataBase(user.getId(), jeuSteamTmp);
			result.append(jeuSteamTmp.toString());
		}
		return "/";
	}

	private void updateDataBase(String user_id, JeuSteam jeuSteamTmp) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(datasource);
		jdbcTemplate.execute("insert into users values (" + user_id + ","
				+ String.valueOf(jeuSteamTmp.getGameId()) + ","
				+ String.valueOf(jeuSteamTmp.getPlaytimeForever()) + ")");
	}
}
