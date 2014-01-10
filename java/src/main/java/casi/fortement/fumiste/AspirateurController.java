package casi.fortement.fumiste;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;

import org.apache.commons.dbcp.BasicDataSource;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import casi.fortement.pojo.JeuSteam;
import casi.fortement.pojo.SteamUser;
import casi.fortement.utils.JsonReader;

@Controller
public class AspirateurController {

	@Autowired
	BasicDataSource datasource;

	private final String apiKey = "3365F0BE987ABFBDA9635BBD58058C99";

	@RequestMapping("/analyser")
	public ModelAndView recupererListeJeux(@ModelAttribute("command") SteamUser user)
			throws MalformedURLException, IOException {

		StringBuilder url = new StringBuilder(
				"http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=");
		url.append(this.apiKey);
		url.append("&steamid=");
		url.append(user.getId());
		url.append("&format=json");

		long start = System.currentTimeMillis();
		JSONObject jsonObject = JsonReader.readJsonFromUrl(url.toString(), true)
				.getJSONObject("response");
		JSONArray tmp = jsonObject.getJSONArray("games");

		StringBuilder requestFabricator = new StringBuilder(
				"BEGIN TRANSACTION;\n");
		for (int j = 0; j < tmp.length(); j++) {
			JSONObject jeuTmp = tmp.getJSONObject(j);
			JeuSteam jeuSteamTmp = new JeuSteam();
			jeuSteamTmp.setProprioId(user.getId());
			jeuSteamTmp.setGameId(Integer.valueOf(jeuTmp.get("appid")
					.toString()));
			jeuSteamTmp.setPlaytimeForever(Integer.valueOf(jeuTmp.get(
					"playtime_forever").toString()));
			requestFabricator.append(prepareQuery(jeuSteamTmp));
		}
		long end = System.currentTimeMillis() - start;
		System.out.println("Indicateur 2 : " + String.valueOf(end));
		
		requestFabricator.append("COMMIT;");
		majBase(requestFabricator);
		ModelAndView result = new ModelAndView("resultat");
		result.addObject("id", user.getId());
		return result;
	}

	private void majBase(StringBuilder donnees) {
		try {
			String fileName = DateFormat.getDateTimeInstance(DateFormat.LONG,
					DateFormat.LONG).format(new Date())
					+ "tmp.sql";
			File file = new File(fileName);
			file.createNewFile();
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(donnees.toString());
			bw.close();

			ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
			populator.addScript(new FileSystemResource(file));
			try {
				populator.populate(datasource.getConnection());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			file.delete();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String prepareQuery(JeuSteam jeuSteamTmp) {
		String result = "insert into users values ("
				+ String.valueOf(jeuSteamTmp.getProprioId()) + ","
				+ String.valueOf(jeuSteamTmp.getGameId()) + ","
				+ String.valueOf(jeuSteamTmp.getPlaytimeForever()) + ");\n";

		return result;
	}
}
