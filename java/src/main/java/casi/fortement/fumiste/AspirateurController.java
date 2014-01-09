package casi.fortement.fumiste;

import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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
		System.out.println(jsonObject.getJSONObject(
				JSONObject.getNames(jsonObject)[0]).toString());
		return null;
	}
}
