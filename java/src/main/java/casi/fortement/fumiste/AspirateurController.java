package casi.fortement.fumiste;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import casi.fortement.pojo.JeuSteam;
import casi.fortement.pojo.SteamUser;
import casi.fortement.utils.JsonReader;

@Controller
public class AspirateurController {

	private final String apiKey = "3365F0BE987ABFBDA9635BBD58058C99";

	@RequestMapping("/analyser")
	public Model recupererListeJeux(@ModelAttribute("command") SteamUser user)
			throws MalformedURLException, IOException {
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
			
			jeuTmp.
			System.out.println(jeuSteamTmp.toString());
		}
		System.out.println();
		return null;
	}
}
